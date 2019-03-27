package me.xujichang.web.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;


/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 14:09.
 */

public class InformationHandler extends WebHandler {
    /**
     * 定位
     */
    public static final String CALLBACK_LOCATION = "location";
    /**
     * 相机
     */
    public static final String CALLBACK_CAMERA = "camera";
    /**
     * 录音
     */
    public static final String CALLBACK_RECORD = "record";
    /**
     * 获取图片
     */
    public static final String CALLBACK_PICTURE = "picture";
    /**
     * USerID
     */
    public static final String CALLBACK_USERID = "userid";

    /**
     * setRightIcon
     */
    public static final String CALLBACK_SET_RIRGT_ICON = "setRightIcon";

    /**
     * upload
     */
    public static final String CALLBACK_UPLOAD = "upload";
    /**
     * upload
     */
    public static final String CALLBACK_UPLOAD_NEW = "upload_new";


    /**
     * 跳转
     */
    public static final String OPEN_LINK = "openLink";


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
