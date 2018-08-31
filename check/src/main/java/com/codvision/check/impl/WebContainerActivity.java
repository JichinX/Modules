package com.codvision.check.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codvision.check.handler.CheckHandler;
import com.codvision.check.data.DataType;
import com.codvision.check.fun.QRForWeb;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import me.xujichang.web.WebConst;
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
                QRForWeb.withContext(this).withFunction(function).withData(data).execute();
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
        if (!catched) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
