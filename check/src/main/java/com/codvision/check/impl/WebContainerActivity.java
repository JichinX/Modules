package com.codvision.check.impl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codvision.check.data.DataType;
import com.codvision.check.fun.PictureForWeb;
import com.codvision.check.fun.QRForWeb;
import com.codvision.check.fun.QrcodeForWeb;
import com.codvision.check.fun.RecordForWeb;
import com.codvision.check.handler.CheckHandler;
import com.codvision.check.permission.WebPermissionCallback;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import java.util.Arrays;

import me.xujichang.web.WebConst;
import me.xujichang.web.handler.InformationHandler;
import me.xujichang.web.interfaces.IWebJsCallBack;

/**
 * @author xujichang
 */
public class WebContainerActivity extends DefaultWebViewActivity {
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        token = intent.getStringExtra(CheckHandler.REQUEST_TOKEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initExtHandler(IWebJsCallBack callBack) {
        super.initExtHandler(callBack);
        new CheckHandler(getWebView(), this);
    }

    @Override
    public void onJsCallBack(String type, String data, CallBackFunction function) {
        switch (type) {
            case CheckHandler.REQUEST_BACK:
                //返回上一页
                doHistory();
                break;
            case CheckHandler.REQUEST_EXIT:
                //退出
                doExit();
                break;
            case CheckHandler.REQUEST_TOKEN:
                //获取Token
                if (Strings.isNullOrEmpty(token)) {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "token 信息为空", ""));
                } else {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "位置信息获取成功", token));
                }
                break;
            case CheckHandler.REQUEST_QR:
                //二维码扫描
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        QRForWeb.withContext(getActivity()).withFunction(function).withData(data).execute();
                    }
                });
                break;
            case CheckHandler.REQUEST_IMG_EXT:
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
            case CheckHandler.REQUEST_CAMERA_EXT:
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
            case InformationHandler.QRCODE:
                workWithPermissionCheck(Manifest.permission.CAMERA, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        QrcodeForWeb.getInstance().withContext(getActivity()).withFunction(function).execute();
                    }
                });
                break;
            case InformationHandler.CALLBACK_RECORD:
                workWithPermissionCheck(Manifest.permission.RECORD_AUDIO, new WebPermissionCallback(function) {
                    @Override
                    public void onGain() {
                        RecordForWeb.getInstance(function)
                                .withOptions(data);
                    }
                });
                break;
            default:
                super.onJsCallBack(type, data, function);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean catched = false;
        catched = QRForWeb.onActivityResult(requestCode, resultCode, data);
        catched = QrcodeForWeb.getInstance().onActivityResult(requestCode, resultCode, data);
        if (!catched) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
