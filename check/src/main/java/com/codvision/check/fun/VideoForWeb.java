package com.codvision.check.fun;

import android.app.Activity;
import android.content.Intent;

import com.codvision.check.bean.UploadFile;
import com.codvision.check.data.DataType;
import com.codvision.check.video.RecorderConfig;
import com.codvision.check.video.VideoRecordActivity;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import me.xujichang.util.tool.LogTool;
import me.xujichang.web.WebConst;

/**
 * Des:
 * Web 视频录制接口
 * flag：video_record
 * data:min max famerate height;
 *
 * @author xujichang
 * <p>
 * created by 2018/9/19-下午4:22
 */
public class VideoForWeb {
    private CallBackFunction function;
    public static final int                REQUEST_VIDEO_RECORD = 20;
    private             FileUploadCallBack fileUpload;

    public static VideoForWeb getInstance(CallBackFunction function) {
        VideoForWeb videoForWeb = getInstance();
        videoForWeb.withFunction(function);
        return videoForWeb;
    }

    private VideoForWeb withFunction(CallBackFunction function) {
        this.function = function;
        return this;
    }

    public static VideoForWeb getInstance() {
        return Holder.instance;
    }

    public VideoForWeb withFileUploadCallBack(FileUploadCallBack callBack) {
        this.fileUpload = callBack;
        return Holder.instance;
    }

    private static class Holder {
        static VideoForWeb instance = new VideoForWeb();
    }

    public VideoForWeb withOperations(String data) {
        obtainConfigWithData(data);
        return this;
    }

    public VideoForWeb withActivity(Activity pActivity) {
        mWeakReference = new WeakReference<>(pActivity);
        return this;
    }

    /**
     * 对Data 进行解析，解析出参数
     *
     * @param data
     */
    private void obtainConfigWithData(String data) {
        RecorderConfig.reset();
        if (Strings.isNullOrEmpty(data)) {
            return;
        }
        VideoConfig config = new Gson().fromJson(data, VideoConfig.class);
        RecorderConfig.setVideoMaxMs(config.timeMax);
        RecorderConfig.setVideoMinMs(config.timeMin);
    }

    private VideoForWeb() {
    }

    private WeakReference<Activity> mWeakReference;

    public void execute() {
        if (null == mWeakReference || null == mWeakReference.get()) {
            //失败
            onResultError("上下文引用丢失");
            return;
        }
        //TODO 添加从文件夹里面选择视频文件
        Activity lActivity = mWeakReference.get();
        lActivity.startActivityForResult(new Intent(lActivity, VideoRecordActivity.class), REQUEST_VIDEO_RECORD);
        //        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
        //                .fullScreen(true)
        //                .smallVideoWidth(videoWidth)
        //                .smallVideoHeight(videoHeight)
        //                .recordTimeMax(timeMax)
        //                .recordTimeMin(timeMin)
        //                .maxFrameRate(frameRate)
        //                .videoBitrate(0)
        //                .captureThumbnailsTime(1)
        //                .goHome(true, REQUEST_VIDEO_RECORD)
        //                .build();
        //        MediaRecorderActivity.goSmallVideoRecorder(GlobalUtil.getCurrentContext(), null, config);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_RECORD) {
            if (resultCode == Activity.RESULT_OK) {
                String path      = intent.getStringExtra(RecorderConfig.FILE_PATH);
                String name      = intent.getStringExtra(RecorderConfig.FILE_NAME);
                String thumbnail = intent.getStringExtra(RecorderConfig.FILE_THUMBNAIL);
                LogTool.d("视频录制的返回结果:path - " + path + "  name - " + name + "  thumbnail - " + thumbnail);
                if (Strings.isNullOrEmpty(path) || Strings.isNullOrEmpty(name)) {
                    onResultError("返回信息 无效");
                    return true;
                }
                UploadFile lUploadFile = new UploadFile();
                lUploadFile.setFilePath(path);
                lUploadFile.setFileName(name);
                lUploadFile.setThumbnail(thumbnail);
                onResult(lUploadFile);
                if (null != fileUpload) {
                    fileUpload.onUpload(lUploadFile);
                }
                //上传视频文件
            } else {
                onResultError("视频录制取消");
            }
            return true;
        }
        return false;
    }

    private void onResultError(String msg) {
        function.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, msg));
    }

    private void onResult(UploadFile result) {
        function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "success", result));
    }

    public interface FileUploadCallBack {
        void onUpload(UploadFile file);
    }

    class VideoConfig {

        /**
         * 视频宽度
         */
        private int videoWidth;
        /**
         * 视频高度
         */
        private int videoHeight;
        /**
         * 最大录制时间
         */
        private int timeMax;
        /**
         * 最小录制时间
         */
        private int timeMin;
        /**
         * 帧率
         */
        private int frameRate;

        public int getVideoWidth() {
            return videoWidth;
        }

        public void setVideoWidth(int videoWidth) {
            this.videoWidth = videoWidth;
        }

        public int getVideoHeight() {
            return videoHeight;
        }

        public void setVideoHeight(int videoHeight) {
            this.videoHeight = videoHeight;
        }

        public int getTimeMax() {
            return timeMax;
        }

        public void setTimeMax(int timeMax) {
            this.timeMax = timeMax;
        }

        public int getTimeMin() {
            return timeMin;
        }

        public void setTimeMin(int timeMin) {
            this.timeMin = timeMin;
        }

        public int getFrameRate() {
            return frameRate;
        }

        public void setFrameRate(int frameRate) {
            this.frameRate = frameRate;
        }
    }
}
