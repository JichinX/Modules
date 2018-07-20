package com.codvision.checksdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.codvision.checksdk.handler.CheckHandler;
import com.codvision.checksdk.web.DataType;
import com.codvision.checksdk.web.DefaultWebViewActivity;
import com.codvision.checksdk.web.WebConst;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;

import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;

/**
 * @author xujichang
 */
public class WebContainerActivity extends DefaultWebViewActivity {
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        if (CheckHandler.REQUEST_TOKEN.equals(type)) {
            if (Strings.isNullOrEmpty(token)) {
                function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "token 信息为空", ""));
            } else {
                function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "位置信息获取成功", token));
            }
        } else if (CheckHandler.REQUEST_BACK.equals(type)) {
            doBack();
        } else {
            super.onJsCallBack(type, data, function);
        }
    }

    private void doBack() {
        WebView webView = getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
