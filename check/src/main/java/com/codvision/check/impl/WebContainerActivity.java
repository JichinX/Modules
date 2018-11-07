package com.codvision.check.impl;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codvision.check.bean.UploadFile;
import com.codvision.check.data.DataType;
import com.codvision.check.fun.PictureForWeb;
import com.codvision.check.fun.QRForWeb;
import com.codvision.check.fun.QrcodeForWeb;
import com.codvision.check.fun.RecordForWeb;
import com.codvision.check.fun.VideoForWeb;
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
    private static final int CAMERA_ACTIVITY = 11;
    private String token;

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
//                        Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.7);//画质0.5
//                        mIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15000);//70s
//                        startActivityForResult(mIntent, CAMERA_ACTIVITY);//CAMERA_ACTIVITY = 1
                        VideoForWeb.getInstance(function)
                                .withActivity(getActivity())
                                .withFileUploadCallBack(obtainCallback())
                                .withOperations(data)
                                .execute();
//                        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
//
//                                .fullScreen(false)
//                                .smallVideoWidth(360)
//                                .smallVideoHeight(480)
//                                .recordTimeMax(6000)
//                                .recordTimeMin(1500)
//                                .maxFrameRate(20)
//                                .videoBitrate(600000)
//                                .captureThumbnailsTime(1)
//                                .build();
//                        MediaRecorderActivity.goSmallVideoRecorder(GlobalUtil.getCurrentContext(), VideoResultActivity.class.getName(), config);
                    }
                });
                break;
            default:
                super.onJsCallBack(type, data, function);
                break;
        }
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
}
