package com.codvision.check.video;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.xujichang.util.tool.LogTool;
import me.xujichang.util.tool.StringTool;

import static android.media.CamcorderProfile.QUALITY_480P;

/**
 * Des:视频 录制相关的操作
 *
 * @author xujichang
 * Created on 2018/10/16 - 10:50 AM
 */
public class RecorderBase implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private OnPreparedListener preparedListener;
    private boolean mPrepared = false;
    private boolean mSurfaceCreated = false;
    private boolean mStartPreview = false;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    protected int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int[] mFrameRate = new int[2];
    private Camera.Size mPreviewSize;
    private boolean mRecording;
    private MediaRecorder mMediaRecorder;
    private File currentOutFile;
    private TimerTask mTimerTask;
    private long timeMax;
    private long timeDuration;
    private ScheduledExecutorService pool;

    public void setPreparedListener(OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void prepare() {

        if (mRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMediaRecorder.resume();
                return;
            }
        }
        mPrepared = true;
        if (mSurfaceCreated) {
            startPreview();
        }
    }

    /**
     * 初始化 视频录制
     */
    private void initMediaRecorder() {
        mCamera.stopPreview();
        mCamera.unlock();
        if (null == mMediaRecorder) {
            mMediaRecorder = new MediaRecorder();
        }
        mMediaRecorder.reset();
        //设置视频源
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setProfile(CamcorderProfile.get(QUALITY_480P));
//        //输出格式
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //编码格式
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        //视频大小
//        mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);
//        //帧率
//        mMediaRecorder.setVideoFrameRate(mFrameRate[0] / 1000);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        currentOutFile = createOutputFile();
        mMediaRecorder.setOutputFile(currentOutFile.getPath());
        mMediaRecorder.setMaxDuration((int) RecorderConfig.VIDEO_MAX_MS);
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                switch (what) {
                    case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                        stopRecord();
                        onTips("到达最大的录制时间");
                        //到达最大时间
                        break;
                    case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
                        break;
                    default:
                }
            }
        });
        try {
            mMediaRecorder.prepare();
        } catch (IOException pE) {
            pE.printStackTrace();
            onError("MediaRecorder 初始化失败");
        }
    }

    /**
     * @return
     */
    private File createOutputFile() {
        String dir = RecorderConfig.getVideoDir();
        long current = System.currentTimeMillis();
        String timStr = String.valueOf(current) + "_";
        String remoteDir = StringTool.formatTime(current, "yyyy-MM-dd");
        String radom = getRandom();
        File parent = new File(dir, remoteDir);
        if (!parent.exists() || parent.isFile()) {
            parent.mkdirs();
        }
        String preFix = "-VIDEO_";
        String suffix = ".mp4";
        String fileName = remoteDir + preFix + timStr + radom + suffix;
        File videoFile = new File(parent, fileName);
        if (!videoFile.exists() || videoFile.isDirectory()) {
            try {
                videoFile.createNewFile();
            } catch (IOException pE) {
                pE.printStackTrace();
                videoFile = null;
            }
        }
        return videoFile;
    }

    private String getRandom() {
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(num);
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        if (mStartPreview || null == mSurfaceHolder || !mPrepared) {
            return;
        }
        mStartPreview = true;
        if (null == mCamera) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open();
            } else {
                mCamera = Camera.open(mCameraId);
            }
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException pE) {
                LogTool.d("Camera Error:");
                pE.printStackTrace();
                onError("相机预览启动失败");
            }
            initCameraParameters(mCamera, mCamera.getParameters());
//            mCamera.cancelAutoFocus();
//            setPreviewCallBack(mCamera.getParameters());
            mCamera.startPreview();
        } else {
            mCamera.startPreview();
        }
        if (null != preparedListener) {
            preparedListener.onPrepared();
        }
    }

    private void setPreviewCallBack(Camera.Parameters pParameters) {
        Camera.Size lSize = pParameters.getPreviewSize();
        if (null != lSize) {
            int bufferSize = lSize.height * lSize.width * 3 / 2;
            mCamera.addCallbackBuffer(new byte[bufferSize]);
            mCamera.addCallbackBuffer(new byte[bufferSize]);
            mCamera.addCallbackBuffer(new byte[bufferSize]);
            mCamera.setPreviewCallbackWithBuffer(this);
        } else {
            mCamera.setPreviewCallback(this);
        }
    }

    /**
     * 设置参数
     *
     * @param pCamera
     * @param pParameters
     */
    private void initCameraParameters(Camera pCamera, Camera.Parameters pParameters) {
        if (null == pParameters) {
            return;
        }
        //支持的帧率
        List<int[]> rates = pParameters.getSupportedPreviewFpsRange();
        //
        if (null != rates) {
            for (int[] lRate : rates) {
                LogTool.d("rates:  " + lRate.length + "  " + Arrays.toString(lRate));
            }
            mFrameRate = rates.get(0);
        } else {
            mFrameRate = new int[]{30_000, 30_000};
        }
        pParameters.setPreviewFpsRange(mFrameRate[0], mFrameRate[1]);
//        LogShow("rates", rates);
        //预览尺寸
        List<Camera.Size> previewSizes = pParameters.getSupportedPreviewSizes();
        //
        if (null != previewSizes) {
            for (Camera.Size lSize : previewSizes) {
                LogTool.d("previewSize: " + lSize.width + "  " + lSize.height);
            }
            mPreviewSize = previewSizes.get(0);
        }
        pParameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

        pParameters.setPreviewFormat(ImageFormat.YV12);
        //video
        List<Camera.Size> videoSizes = pParameters.getSupportedVideoSizes();
        for (Camera.Size lSize : videoSizes) {
            LogTool.d("videoSizes: " + lSize.width + "  " + lSize.height);
        }

        //对焦模式
        List<String> focusMode = pParameters.getSupportedFocusModes();
        LogShow("focus mode : ", focusMode);
        pParameters.setFocusMode(getBestFocusMode(focusMode));
//        //白平衡
        List<String> whiteBlances = pParameters.getSupportedWhiteBalance();
        LogShow("whiteBlances : ", whiteBlances);
        pParameters.setWhiteBalance(getBestWhiteBalance(whiteBlances));
        //是否支持视频防抖
//        if ("true".equals(mParameters.get("video-stabilization-supported")))
//            mParameters.set("video-stabilization", "true");
//
//        //		mParameters.set("recording-hint", "false");
//        //
//        //		mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        if (!DeviceUtils.isDevice("GT-N7100", "GT-I9308", "GT-I9300")) {
//            mParameters.set("cam_mode", 1);
//            mParameters.set("cam-mode", 1);
//        }
        pParameters.setRecordingHint(true);
        pCamera.setParameters(pParameters);
    }

    private String getBestWhiteBalance(List<String> pWhiteBlances) {
        if (null == pWhiteBlances) {
            return null;
        }
        if (pWhiteBlances.contains("auto")) {
            return "auto";
        }
        if (pWhiteBlances.size() == 0) {
            return null;
        }
        return pWhiteBlances.get(0);
    }

    private String getBestFocusMode(List<String> pFocusMode) {
        if (null == pFocusMode) {
            return null;
        }
        if (pFocusMode.contains("continuous-video")) {
            return "continuous-video";
        }
        if (pFocusMode.contains("continuous-picture")) {
            return "continuous-picture";
        }
        if (pFocusMode.contains("auto")) {
            return "auto";
        }
        if (pFocusMode.size() == 0) {
            return null;
        }
        return pFocusMode.get(0);
    }

    private void LogShow(String type, List<String> datas) {
        if (null == datas) {
            LogTool.d(type + "  无数据");
            return;
        }
        for (String lData : datas) {
            LogTool.d(type + "  " + lData);
        }
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        if (null != holder) {
            holder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceCreated = true;
        mSurfaceHolder = holder;
        if (mPrepared && !mStartPreview) {
            startPreview();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        LogTool.d("onPreviewFrame。。。");
    }

    public boolean isRecording() {
        return mRecording;
    }

    public void setRecording(boolean pRecording) {
        mRecording = pRecording;
    }

    public void startRecord() {
        initMediaRecorder();
        mMediaRecorder.start();
        mRecording = true;
        timerStart();
        if (null != mProgressCallBack) {
            mProgressCallBack.onRecordStart();
        }
    }

    private void timerStart() {
        LogTool.d("timerStart");
        //启用1个线程
        pool = Executors.newScheduledThreadPool(1);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                generateProgress();
            }
        };
        timeDuration = 0;
        pool.scheduleWithFixedDelay(mTimerTask, 0, RecorderConfig.PROGRESS_PERIOD, TimeUnit.MILLISECONDS);
    }

    private void generateProgress() {
        timeDuration += RecorderConfig.PROGRESS_PERIOD;
        LogTool.d("generateProgress" + timeDuration);
        if (null != mProgressCallBack) {
            mProgressCallBack.onProgress(RecorderConfig.VIDEO_MAX_MS, timeDuration);
        }
    }

    public void stopRecord() {
        LogTool.d("。。。stopRecord start");
        if (null == mMediaRecorder) {
            return;
        }
        //设置后不会崩
        mMediaRecorder.setOnErrorListener(null);
        mMediaRecorder.setPreviewDisplay(null);
        try {
            mMediaRecorder.stop();
        } catch (IllegalStateException e) {
            LogTool.d(e.toString());
        } catch (RuntimeException e) {
            LogTool.d(e.toString());
        } catch (Exception e) {
            LogTool.d(e.toString());
        }
        mMediaRecorder.reset();
        mRecording = false;
        mStartPreview = false;
        if (null != mProgressCallBack) {
            mProgressCallBack.onRecordStop();
        }
        timerStop();
        LogTool.d("。。。stopRecord end");
    }

    private void timerStop() {
        LogTool.d("。。。stopRecord timerStop");
        if (null != pool) {
            try {
                // 向学生传达“问题解答完毕后请举手示意！”
                pool.shutdown();

                // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
                // (所有的任务都结束的时候，返回TRUE)
                if (!pool.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                System.out.println("awaitTermination interrupted: " + e);
                pool.shutdownNow();

            }
        }
        if (timeDuration < RecorderConfig.VIDEO_MIN_MS) {
            delete();
            onTips("录制时间较短，本次录制无效");
        }
    }

    public void delete() {
        if (currentOutFile.exists()) {
            currentOutFile.delete();
        }
        prepare();
    }

    public void setProgressCallBack(OnProgressCallBack pCallBack) {
        mProgressCallBack = pCallBack;
    }

    public void onPause() {
        if (!mRecording) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaRecorder.pause();
        }
    }

    public void onDestroy() {
        releaseCamera();
        releaseMediaRecorder();
        releaseTimer();
    }

    private void releaseTimer() {
        if (null != pool) {
        }
    }

    private void releaseCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        LogTool.d("release camera");
    }

    private void releaseMediaRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        LogTool.d("release recorder");
    }

    public File getOutPutFile() {
        return currentOutFile;
    }

    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        /** 判断是否支持闪光灯 */
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    //判断设备是否支持闪光灯
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 切换闪关灯，默认关闭
     */
    public boolean toggleFlashMode() {
        if (null == mCamera) {
            return false;
        }
        Camera.Parameters lParameters = mCamera.getParameters();
        if (lParameters != null) {
            try {
                final String mode = lParameters.getFlashMode();
                if (TextUtils.isEmpty(mode) || Camera.Parameters.FLASH_MODE_OFF.equals(mode)) {
                    setFlashMode(mCamera, lParameters, Camera.Parameters.FLASH_MODE_TORCH);
                } else {
                    setFlashMode(mCamera, lParameters, Camera.Parameters.FLASH_MODE_OFF);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                onError("闪光灯 切换失败");
            }
        }
        return false;
    }

    /**
     * 设置闪光灯
     *
     * @param pCamera
     * @param pParameters
     * @param value
     */
    private boolean setFlashMode(Camera pCamera, Camera.Parameters pParameters, String value) {
        if (pParameters != null && pCamera != null) {
            try {
                if (Camera.Parameters.FLASH_MODE_TORCH.equals(value) || Camera.Parameters.FLASH_MODE_OFF.equals(value)) {
                    pParameters.setFlashMode(value);
                    pCamera.setParameters(pParameters);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                onError("闪光灯 设置失败");
            }
        }
        return false;
    }

    public static boolean isSupportFrontCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        if (2 == numberOfCameras) {
            return true;
        }
        return false;
    }

    /**
     * 切换前置/后置摄像头
     */
    public void switchCamera(int cameraFacingFront) {
        switch (cameraFacingFront) {
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                mCameraId = cameraFacingFront;
                stopPreview();
                startPreview();
                break;
            default:
                break;
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                // camera.lock();
                mCamera.release();
            } catch (Exception e) {

            }
            mCamera = null;
        }
        mStartPreview = false;
    }

    /**
     * 切换前置/后置摄像头
     */
    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
    }

    /**
     * 是否前置摄像头
     */
    public boolean isFrontCamera() {
        return mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    /**
     * 视频缩略图
     *
     * @return
     */
    public String getVideoThumbnail() {
        Bitmap lThumbnail = ThumbnailUtils.createVideoThumbnail(currentOutFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
        File parent = currentOutFile.getParentFile();
        String name = currentOutFile.getName().split("\\.")[0] + ".jpg";
        return saveBitmap(lThumbnail, parent, name);
    }

    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @param pParent
     * @param pName
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap, File pParent, String pName) {
        File filePic;

        try {
            filePic = new File(pParent, pName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    public boolean isAccept() {
        return timeDuration > RecorderConfig.VIDEO_MIN_MS;
    }

    /**
     * 预处理监听
     */
    public interface OnPreparedListener {
        /**
         * 预处理完毕，可以开始录制了
         */
        void onPrepared();
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    public interface OnProgressCallBack {
        void onRecordStart();

        void onProgress(long max, long progress);

        void onRecordStop();
    }

    private OnProgressCallBack mProgressCallBack;

    public interface OnMsgCallBack {
        void onError(String msg);

        void onTips(String msg);
    }

    private OnMsgCallBack mMsgCallBack;

    public void setMsgCallBack(OnMsgCallBack pErrorCallBack) {
        mMsgCallBack = pErrorCallBack;
    }

    private void onError(String msg) {
        if (null != mMsgCallBack) {
            mMsgCallBack.onError(msg);
        }
    }

    private void onTips(String msg) {
        if (null != mMsgCallBack) {
            mMsgCallBack.onTips(msg);
        }
    }

}
