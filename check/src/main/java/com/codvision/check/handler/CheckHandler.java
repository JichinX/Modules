package com.codvision.check.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.hybirdbase.module.web.handler.WebHandler;
import me.xujichang.hybirdbase.module.web.interfaces.IWebJsCallBack;

public class CheckHandler extends WebHandler {
    /**
     * 获取Token信息
     */
    public static final String REQUEST_TOKEN = "token";
    /**
     * 返回上一页
     */
    public static final String REQUEST_BACK = "back";
    /**
     * 退出容器
     */
    public static final String REQUEST_EXIT = "exit";

    public CheckHandler(BridgeWebView webView) {
        super(webView);
    }

    public CheckHandler(BridgeWebView webView, IWebJsCallBack callBack) {
        super(webView, callBack);
    }

    @Override
    protected void patchWebHandler() {
        registerNativeHandler(REQUEST_TOKEN);
        registerNativeHandler(REQUEST_BACK);
        registerNativeHandler(REQUEST_EXIT);
    }
}
