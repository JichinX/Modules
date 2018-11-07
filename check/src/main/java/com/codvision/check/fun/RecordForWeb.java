package com.codvision.check.fun;

import android.media.MediaRecorder;

import com.codvision.check.CheckInit;
import com.codvision.check.data.DataParseException;
import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import me.xujichang.web.WebConst;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/27 14:28.
 */

public class RecordForWeb {
    public static final int RECORD_START = 1;
    public static final int RECORD_STOP = 2;
    public static final int RECORD_CANCEL = 3;
    public static final int RECORD_CLEAR = 4;
    /**
     * 录制时间  无限制
     */
    private static final int MAX_LENGTH = 0;

    private static RecordForWeb instance;
    private static CallBackFunction sFunction;
    private int option;
    private boolean recording = false;
    private MediaRecorder mMediaRecorder;
    private File storeDir;
    private File storeFile;

    public static RecordForWeb getInstance(CallBackFunction function) {
        if (null == instance) {
            instance = ClassHolder.instance;
        }
        sFunction = function;
        return instance;
    }

    private RecordForWeb() {
        storeDir = new File(CheckInit.cacheFile, "record");
        if (!storeDir.exists()) {
            storeDir.mkdirs();
        }
    }

    /**
     * 初始化
     */
    private void initRecord() throws IOException {
        //检测权限：

        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
            /* ②setAudioSource/setVedioSource */
        // 设置麦克风
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        storeFile = new File(storeDir, createFileName());
            /* ③准备 */
        mMediaRecorder.setOutputFile(storeFile.getAbsolutePath());
        mMediaRecorder.setMaxDuration(MAX_LENGTH);
        mMediaRecorder.prepare();
    }

    private String createFileName() {
        return new StringBuilder("record_").append(String.valueOf(System.currentTimeMillis())).append(".aac").toString();
    }

    public void withOptions(String data) {
        DataType.ReqData optionBean = new Gson().fromJson(data, DataType.ReqData.class);
        if (null != optionBean) {
            option = optionBean.getOption();
        } else {
            throw new DataParseException(DataType.ReqData.class, data);
        }
        if (recording) {
            if (option == RECORD_STOP) {
                //停止录音
                stopRecording();
            } else if (option == RECORD_START) {
                //开始录音
                sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.REQUEST_REPEAT, "已经在开始录音"));
            } else if (option == RECORD_CANCEL) {
                //取消
                cancelRecording();
            }
        } else {
            if (option == RECORD_START) {
                startRecording();
            } else {
                sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.REQUEST_OTHER, "未在录音状态"));
            }
        }
    }

    private void cancelRecording() {
        try {
            mMediaRecorder.stop();
            sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.REQUEST_OTHER, "已经取消录音"));
        } catch (RuntimeException e) {
            sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "取消录音时出错"));
        }
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        recording = false;
        if (storeFile.exists()) {
            storeFile.delete();
            storeFile = null;
        }
    }

    /**
     * 结束录音
     */
    private void stopRecording() {
        recording = false;
        if (mMediaRecorder == null) {
            return;
        }
        //有一些网友反应在5.0以上在调用stop的时候会报错，翻阅了一下谷歌文档发现上面确实写的有可能会报错的情况，捕获异常清理一下就行了，感谢大家反馈！
        try {
            mMediaRecorder.stop();
            sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "录制结束", storeFile.getAbsolutePath()));
        } catch (RuntimeException e) {
            sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "录制结束时出错"));
        }
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        try {
            initRecord();
            /* ④开始 */
            mMediaRecorder.start();
            recording = true;
            sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_OK, "开始录音"));
        } catch (IOException e) {
            recording = false;
            storeFile = null;
            sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "录音初始化出错"));
        }
    }

    private static class ClassHolder {
        private static RecordForWeb instance = new RecordForWeb();
    }
}
