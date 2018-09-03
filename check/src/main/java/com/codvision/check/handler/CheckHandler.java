package com.codvision.check.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.web.handler.WebHandler;
import me.xujichang.web.interfaces.IWebJsCallBack;


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
    /**
     * 请求二维码扫描
     */
    public static final String REQUEST_QR = "qr";
    /**
     * 获取照片的扩展信息
     */
    public static final String REQUEST_IMG_EXT = "picture_ext";
    /**
     * 直接使用相机
     */
    public static final String REQUEST_CAMERA_EXT = "camera_ext";

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
        registerNativeHandler(REQUEST_QR);
        registerNativeHandler(REQUEST_IMG_EXT);
        registerNativeHandler(REQUEST_CAMERA_EXT);
    }
}
