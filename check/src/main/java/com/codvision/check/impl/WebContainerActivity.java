package com.codvision.check.impl;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codvision.check.CheckInit;
import com.codvision.check.bean.UploadFile;
import com.codvision.check.data.DataType;
import com.codvision.check.fun.PictureForWeb;
import com.codvision.check.fun.QRForWeb;
import com.codvision.check.fun.QrcodeForWeb;
import com.codvision.check.fun.RecordForWeb;
import com.codvision.check.fun.VideoForWeb;
import com.codvision.check.fun.camera.CameraForWeb;
import com.codvision.check.handler.CheckHandler;
import com.codvision.check.handler.Handler;
import com.codvision.check.permission.WebPermissionCallback;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import java.util.Arrays;

import me.xujichang.web.WebConst;
import me.xujichang.web.interfaces.IWebJsCallBack;

/**
 * @author xujichang
 */
public class WebContainerActivity extends DefaultWebViewActivity implements VideoForWeb.FileUploadCallBack {
    private static final int                  CAMERA_ACTIVITY = 11;
    private static final int                  REQUEST_FILE    = 12;
    private              String               token;
    private              ValueCallback<Uri[]> mValueCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        token = intent.getStringExtra(CheckHandler.REQUEST_TOKEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initExtHandler(IWebJsCallBack callBack) {
        //        super.initExtHandler(callBack);
        //        new CheckHandler(getWebView(), this);
        new Handler(getWebView()).addJsCallBack(callBack);
    }

    @Override
    public void onJsCallBack(String type, String data, CallBackFunction function) {
        switch (type) {
            case Handler.REQUEST_BACK:
                //返回上一页
                doHistory();
                break;
            case Handler.REQUEST_EXIT:
                //退出
                doExit();
                break;
            case Handler.REQUEST_TOKEN:
                //获取Token
                if (Strings.isNullOrEmpty(token)) {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "token 信息为空", ""));
                } else {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "位置信息获取成功", token));
                }
                break;
            case Handler.REQUEST_QR:
                //二维码扫描
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        QRForWeb.withContext(getActivity()).withFunction(function).withData(data).execute();
                    }
                });
                break;
            case Handler.REQUEST_IMG_EXT:
                workWithPermissionCheck(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        PictureForWeb.getInstance()
                                .withFunction(function)
                                .withOptions(data)
                                .withContext(getActivity())
                                .withExif(true)
                                .execute();
                    }
                });
                break;
            case Handler.REQUEST_CAMERA_EXT:
                workWithPermissionCheck(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        PictureForWeb.getInstance()
                                .justCamera(true)
                                .withFunction(function)
                                .withOptions(data)
                                .withContext(getActivity())
                                .withExif(true)
                                .execute();
                    }
                });
                break;
            case Handler.QRCODE:
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        QrcodeForWeb.getInstance().withContext(getActivity()).withFunction(function).execute();
                    }
                });
                break;
            case Handler.CALLBACK_RECORD:
                workWithPermissionCheck(Manifest.permission.RECORD_AUDIO, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        RecordForWeb.getInstance(function)
                                .withOptions(data);
                    }
                });
                break;
            case Handler.CALLBACK_VIDEO_RECORD:
                //视频录制
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        VideoForWeb.getInstance(function)
                                .withActivity(getActivity())
                                .withFileUploadCallBack(obtainCallback())
                                .withOperations(data)
                                .execute();
                    }
                });
                break;
            case Handler.QUICK_CAMERA:
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        CameraForWeb
                                .getInstance()
                                .withFunction(function)
                                .withContext(getActivity())
                                .withOpt(data)
                                .execute();
                    }
                });
                break;
            default:
                if (CheckInit.webDebug) {
                    showDataFromWeb(type, data, function);
                } else {
                    super.onJsCallBack(type, data, function);
                }
                break;
        }
    }

    private void showDataFromWeb(String type, String data, CallBackFunction function) {
        StringBuilder content = new StringBuilder("接口标志：\n").append(type).append("\n");
        content.append("传递的数据:\n").append(data);

        new MaterialDialog
                .Builder(getContext())
                .title("来自Web的数据")
                .content(content)
                .negativeText("返回错误")
                .positiveText("返回正确")
                .neutralText("不返回数据")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            function.onCallBack(DataType.createRespData(200, "测试成功数据", null));
                        } else if (which == DialogAction.NEGATIVE) {
                            function.onCallBack(DataType.createErrorRespData(502, "测试成功数据"));
                        }
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .autoDismiss(false)
                .show();
    }

    /**
     * File上传的回调
     * 子类可以实现自己的回调
     *
     * @return
     */

    protected VideoForWeb.FileUploadCallBack obtainCallback() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean catched = QRForWeb.onActivityResult(requestCode, resultCode, data);
        if (!catched) {
            catched = QrcodeForWeb.getInstance().onActivityResult(requestCode, resultCode, data);
        }
        if (!catched) {
            catched = VideoForWeb.getInstance().onActivityResult(requestCode, resultCode, data);
        }
        if (!catched) {
            catched = CameraForWeb.getInstance().onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_FILE) {
            catched = true;
            mValueCallback.onReceiveValue(data == null ? null : new Uri[]{data.getData()});
            mValueCallback = null;
        }
        if (!catched) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 上传文件的具体方法，供子类使用，并提供默认实现方法
     *
     * @param file
     */
    @Override
    public void onUpload(UploadFile file) {
    }

    /**
     * TODO 仅适配Android5.0以上版本，一下版本不与适配
     *
     * @param webView
     * @param filePathCallback
     * @param fileChooserParams
     * @return
     */
    @Override
    public boolean onPageFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        Intent intent = null;
        if (null != mValueCallback) {
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
        mValueCallback = filePathCallback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent = fileChooserParams.createIntent();
        }
        startActivityForResult(intent, REQUEST_FILE);
        return true;
    }
}
