package com.codvision.check.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.web.handler.WebHandler;


/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 14:09.
 */
@Deprecated
public class InformationHandler extends WebHandler {
    /**
     * 定位
     */
    public static final String CALLBACK_LOCATION = Handler.CALLBACK_LOCATION;
    /**
     * 录音
     */
    public static final String CALLBACK_RECORD = Handler.CALLBACK_RECORD;
    /**
     * 相机
     */
    public static final String CALLBACK_CAMERA = Handler.CALLBACK_CAMERA;
    /**
     * 获取图片
     */
    public static final String CALLBACK_PICTURE = Handler.CALLBACK_PICTURE;
    /**
     * USerID
     */
    public static final String CALLBACK_USERID = Handler.CALLBACK_USERID;

    /**
     * setRightIcon
     */
    public static final String CALLBACK_SET_RIRGT_ICON = Handler.CALLBACK_SET_RIRGT_ICON;

    /**
     * upload
     */
    public static final String CALLBACK_UPLOAD = Handler.CALLBACK_UPLOAD;
    /**
     * upload
     */
    public static final String CALLBACK_UPLOAD_NEW = Handler.CALLBACK_UPLOAD_NEW;


    /**
     * 跳转
     */
    public static final String OPEN_LINK = Handler.OPEN_LINK;


    /**
     * 二维码扫描
     */
    public static final String QRCODE = "qrcode";

    /**
     * 导航
     */
    public static final String NAV = "nav";


    public InformationHandler(BridgeWebView webView) {
        super(webView);
    }

    @Override
    protected void patchWebHandler() {
        //设置 定位回调
        registerNativeHandler(CALLBACK_LOCATION);
        //设置 拍照回调
        registerNativeHandler(CALLBACK_CAMERA);
        //设置 录音回调
        registerNativeHandler(CALLBACK_RECORD);
        //选择图片
        registerNativeHandler(CALLBACK_PICTURE);
        //请求UserID
        registerNativeHandler(CALLBACK_USERID);
        //上传
        registerNativeHandler(CALLBACK_UPLOAD);
        //改动后上传
        registerNativeHandler(CALLBACK_UPLOAD_NEW);
        // 设置右上角按钮图标
        registerNativeHandler(CALLBACK_SET_RIRGT_ICON);

        registerNativeHandler(OPEN_LINK);
        registerNativeHandler(QRCODE);
        registerNativeHandler(NAV);
    }
}
