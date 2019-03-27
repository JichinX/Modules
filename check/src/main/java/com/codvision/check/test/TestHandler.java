package com.codvision.check.test;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.web.handler.WebHandler;

/**
 * Des:测试部分
 *
 * @author xujichang
 * Created on 2018/12/10 - 17:52
 */
class TestHandler extends WebHandler {
    public static final String REQUEST_PRINT = "text_print";

    public TestHandler(BridgeWebView webView) {
        super(webView);
    }

    @Override
    protected void patchWebHandler() {
        registerNativeHandler(REQUEST_PRINT);
    }
}
