package com.codvision.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codvision.check.handler.CheckHandler;
import com.codvision.check.web.DataType;
import com.codvision.check.web.DefaultWebViewActivity;
import com.codvision.check.web.WebConst;
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
        switch (type) {
            case CheckHandler.REQUEST_BACK:
                doHistory();
                break;
            case CheckHandler.REQUEST_EXIT:
                doExit();
                break;
            case CheckHandler.REQUEST_TOKEN:
                if (Strings.isNullOrEmpty(token)) {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "token 信息为空", ""));
                } else {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "位置信息获取成功", token));
                }
                break;
            default:
                super.onJsCallBack(type, data, function);
                break;
        }
    }

    /**
     * 退出
     */
    private void doExit() {
        showWarningDialog("返回此页面的数据将不会保留，确认返回？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    finish();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 返回上一页面
     */
    private void doHistory() {
        WebView webView = getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            doExit();
        }
    }
}
