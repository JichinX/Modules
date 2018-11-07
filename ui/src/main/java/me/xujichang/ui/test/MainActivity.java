package me.xujichang.ui.test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Arrays;
import java.util.Collections;

import me.xujichang.ui.R;
import me.xujichang.ui.activity.DefaultActionBarActivity;
import me.xujichang.ui.test.login.LoginActivity;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午9:20
 */
public class MainActivity extends DefaultActionBarActivity {
    private static final int MSG_LOADING = 1;
    private static final int MSG_PROCESS = 2;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADING:
                    stopLoading();
                    break;
                case MSG_PROCESS:
                    int num = msg.arg1;
                    LogTool.d("num:" + num);
                    updateProcess(num);
                    if (num == 100) {
                        stopProgress();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });
    private WebView mWb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_test);
        initView();
    }

    private void initView() {
        initActionBar();
        requestPermission(Arrays.asList(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION));
        mWb = findViewById(R.id.wb);

        mWb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initActionBar() {
//        showBack();
        setLeftText("返回");
        setActionBarTitle("首页");
        setRightImage(R.drawable.ic_arrow_forward_white);
    }

    public void onWarning(View view) {
        showWarning("警告测试");
    }

    public void onError(View view) {
        showError("出错提示");
    }

    public void onTip(View view) {
        showTips("提示测试");
    }

    public void onProcess(View view) {

        startProgress("图片缓存..", "上传中...");
        new Thread(new ProcessRunnable()).start();
    }

    public void onLoading(View view) {
        startLoading("图片缓存", "上传中...");
        new Thread(new LoadingRunnable()).start();
    }

    public void onFile(View view) {
        //文件
        workWithPermissionCheck(Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), new SimplePermissionCallback() {
            @Override
            public void onGain() {
                toast("权限已获得");
            }
        });
    }

    public void onCamera(View view) {
        //相机
        workWithPermissionRequest(Collections.singletonList(Manifest.permission.CAMERA), new SimplePermissionCallback() {
            @Override
            public void onGain() {
                toast("权限已获得");
            }
        });
    }

    public void disableMark(View view) {
        disableWaterMark();
    }

    public void enableMark(View view) {
        enableWaterMark("许继昌 测试");
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void hideActionBar(View view) {
        hideActionBar();
    }

    public void showActionBar(View view) {
        showActionBar();
    }

    public void loadWeb(View view) {
        mWb.loadUrl("http://www.baidu.com");
    }

    private class ProcessRunnable implements Runnable {
        private volatile int num = 0;

        @Override
        public void run() {
            while (num < 100) {
                num += 1;
                handler.obtainMessage(MSG_PROCESS, num, 0).sendToTarget();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoadingRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.obtainMessage(MSG_LOADING).sendToTarget();
        }
    }
}
