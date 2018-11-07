package com.codvision.check.video;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codvision.check.R;

import java.io.File;

import me.xujichang.ui.activity.DefaultActionBarActivity;
import me.xujichang.util.simple.interfaces.XOnClickListener;
import me.xujichang.util.tool.LogTool;

/**
 * Des:视频录制页面
 *
 * @author xujichang
 * Created on 2018/10/15 - 5:04 PM
 */
public class VideoRecordActivity extends DefaultActionBarActivity implements RecorderBase.OnPreparedListener, RecorderBase.OnProgressCallBack, RecorderBase.OnMsgCallBack {
    private LinearLayout mTitleLayout;
    private CheckBox mRecordCameraLed;
    private CheckBox mRecordCameraSwitcher;
    private ProgressBar mRecordProgress;
    private TextView mRecordController;
    private ImageView mBtnVideoCancel;
    private RecorderBase recorderBase;
    private SurfaceView mSvVideo;
    private GestureDetector mGestureDetector;
    private ImageView mIvVideoDelete;
    private ImageView mIvVideoAccept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_record);
        initData();
        initView();
    }


    private void initData() {

    }

    private void initView() {
        hideActionBar();
        mTitleLayout = findViewById(R.id.title_layout);
        mRecordCameraLed = findViewById(R.id.record_camera_led);
        mRecordCameraSwitcher = findViewById(R.id.record_camera_switcher);
        mRecordProgress = findViewById(R.id.pb_loading_status);
        mRecordController = findViewById(R.id.record_controller);
        mBtnVideoCancel = findViewById(R.id.btn_video_cancel);
        mIvVideoDelete = findViewById(R.id.iv_video_delete);
        mIvVideoAccept = findViewById(R.id.iv_video_accept);
        mSvVideo = findViewById(R.id.sv_video);


        proxyViewClick(mIvVideoAccept, new XOnClickListener<ImageView>() {
            @Override
            public void onClick(ImageView view) {
                //视频录制完成
                onResult(recorderBase.getOutPutFile(), recorderBase.getVideoThumbnail());
            }
        });
        proxyViewClick(mIvVideoDelete, new XOnClickListener<ImageView>() {
            @Override
            public void onClick(ImageView view) {
                //删除
                recorderBase.delete();
            }
        });
        mRecordController.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onControllerEvent(event);
            }
        });
        proxyViewClick(mBtnVideoCancel, new XOnClickListener<ImageView>() {
            @Override
            public void onClick(ImageView view) {
                onBackPressed();
            }
        });
        mRecordProgress.setMax(100);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mRecordProgress.setMin(calculateMin());
//        }

        // 是否支持前置摄像头
        if (RecorderBase.isSupportFrontCamera()) {
            proxyViewClick(mRecordCameraSwitcher,
                    new XOnClickListener<CheckBox>() {
                        @Override
                        public void onClick(CheckBox view) {
                            //切换摄像头
                            switchCamera();
                        }
                    });
        } else {
            mRecordCameraSwitcher.setVisibility(View.GONE);
        }
        // 是否支持闪光灯
        if (RecorderBase.isSupportCameraLedFlash(getPackageManager())) {
            proxyViewClick(mRecordCameraLed,
                    new XOnClickListener<CheckBox>() {
                        @Override
                        public void onClick(CheckBox view) {
                            //LED灯
                            toggleLED();
                        }
                    });
        } else {
            mRecordCameraLed.setVisibility(View.GONE);
        }
    }

    private void switchCamera() {
        if (null == recorderBase) {
            return;
        }
        if (mRecordCameraLed.isChecked()) {
            //关掉LED
            recorderBase.toggleFlashMode();
            mRecordCameraLed.setChecked(false);
        }
        recorderBase.switchCamera();
        if (recorderBase.isFrontCamera()) {
            mRecordCameraLed.setEnabled(false);
        } else {
            mRecordCameraLed.setEnabled(true);
        }
    }

    private void toggleLED() {
        // 开启前置摄像头以后不支持开启闪光灯
        if (null == recorderBase) {
            return;
        }
        if (recorderBase.isFrontCamera()) {
            return;
        }

        recorderBase.toggleFlashMode();
    }

    private int calculateMin() {
        return (int) (RecorderConfig.VIDEO_MIN_MS * 100 / RecorderConfig.VIDEO_MAX_MS);
    }

    private void onResult(File pFile, String pThumbnail) {
        Intent lIntent = new Intent();
        lIntent.putExtra(RecorderConfig.FILE_NAME, pFile.getName());
        lIntent.putExtra(RecorderConfig.FILE_PATH, pFile.getAbsolutePath());
        lIntent.putExtra(RecorderConfig.FILE_THUMBNAIL, pThumbnail);
        setResult(RESULT_OK, lIntent);
        finish();
    }

    /**
     * 录制按钮的事件
     *
     * @param pEvent
     * @return
     */
    private boolean onControllerEvent(MotionEvent pEvent) {
        if (null == recorderBase) {
            return false;
        }
        int action = pEvent.getActionMasked();
        LogTool.d("触摸事件：" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!recorderBase.isRecording()) {
                    startRecord();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                LogTool.d("Action UP");
                if (recorderBase.isRecording()) {
                    stopRecord();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void stopRecord() {
        LogTool.d("stopRecord。。。");
        recorderBase.stopRecord();
    }

    private void setStopUI() {
        LogTool.d("setStopUI");
        mRecordController.animate().scaleX(1).scaleY(1).setDuration(500).start();
        mRecordCameraSwitcher.setEnabled(true);
        mRecordCameraLed.setEnabled(true);
        mIvVideoAccept.setVisibility(View.VISIBLE);
        mIvVideoDelete.setVisibility(View.VISIBLE);
    }

    private void startRecord() {
        recorderBase.startRecord();
    }

    /**
     * 整理录制开始的界面
     */
    private void setStartUI() {
        mRecordController.animate().scaleX(0.8f).scaleY(0.8f).setDuration(500).start();
        mRecordCameraSwitcher.setEnabled(false);
        mRecordCameraLed.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == recorderBase) {
            initRecorderBase();
        } else {
            recorderBase.prepare();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorderBase.onPause();
    }

    /**
     * 初始化录制
     */
    private void initRecorderBase() {
        if (!RecorderConfig.checkConfig()) {
            return;
        }
        recorderBase = new RecorderImpl();

        recorderBase.setPreparedListener(this);
        recorderBase.setProgressCallBack(this);
        recorderBase.setMsgCallBack(this);
        recorderBase.setSurfaceHolder(mSvVideo.getHolder());
        recorderBase.prepare();

    }

    @Override
    public void onPrepared() {
        LogTool.d("onPrepared");
        mIvVideoAccept.setVisibility(View.GONE);
        mIvVideoDelete.setVisibility(View.GONE);
        initSurfaceView();
        mRecordProgress.setProgress(0);
        mRecordProgress.postInvalidate();
    }

    private void initSurfaceView() {
        final int w = getScreenWidth(this);
        Camera.Size lSize = recorderBase.getPreviewSize();
        int height = (int) (w * ((lSize.width * 1.0f) / lSize.height));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSvVideo
                .getLayoutParams();
        lp.width = w;
        lp.height = height;
        LogTool.d("width:" + lp.width + " height:" + lp.height);
        lp.setMargins(0, 0, 0, 0);
        lp.gravity = Gravity.CENTER;
        mSvVideo.setLayoutParams(lp);
    }

    private static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    @Override
    public void onRecordStart() {
        setStartUI();
    }

    @Override
    public void onProgress(long max, long progress) {
        int s = (int) (progress * 100 / max);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRecordProgress.setProgress(s, true);
            LogTool.d("onProgress animate:" + s);
        } else {
            mRecordProgress.setProgress(s);
            LogTool.d("onProgress no animate:" + s);
        }
    }

    @Override
    public void onRecordStop() {
        setStopUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorderBase.onDestroy();
    }

    @Override
    public void onError(String msg) {
        showError(msg);
    }

    @Override
    public void onTips(String msg) {
        showToast(msg);
    }
}
