package com.codvision.check.handler;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import me.xujichang.web.handler.WebHandler;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/20-下午4:49
 */
public class Handler extends WebHandler {
    /**
     * 定位
     */
    public static final String CALLBACK_LOCATION = "location";
    /**
     * 录音
     */
    public static final String CALLBACK_RECORD   = "record";
    /**
     * 相机
     */
    public static final String CALLBACK_CAMERA   = "camera";
    /**
     * 获取图片
     */
    public static final String CALLBACK_PICTURE  = "picture";
    /**
     * USerID
     */
    public static final String CALLBACK_USERID   = "userid";

    /**
     * setRightIcon
     */
    public static final String CALLBACK_SET_RIRGT_ICON = "setRightIcon";

    /**
     * upload
     */
    public static final String CALLBACK_UPLOAD     = "upload";
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
    public static final String NAV                   = "nav";
    /**
     * 获取Token信息
     */
    public static final String REQUEST_TOKEN         = "token";
    /**
     * 返回上一页
     */
    public static final String REQUEST_BACK          = "back";
    /**
     * 退出容器
     */
    public static final String REQUEST_EXIT          = "exit";
    /**
     * 请求二维码扫描
     */
    public static final String REQUEST_QR            = "qr";
    /**
     * 获取照片的扩展信息
     */
    public static final String REQUEST_IMG_EXT       = "picture_ext";
    /**
     * 直接使用相机
     */
    public static final String REQUEST_CAMERA_EXT    = "camera_ext";
    /**
     * 视频录制
     */
    public static final String CALLBACK_VIDEO_RECORD = "video_record";
    /**
     * 直接调用系统相机
     * 然后根据标志判断是拍照还是录制视频
     * 仅调用系统相机，无返回信息
     */
    public static final String QUICK_CAMERA          = "quick_camera";

    public Handler(BridgeWebView webView) {
        super(webView);
    }

    @Override
    protected void patchWebHandler() {
        registerNativeHandler(REQUEST_TOKEN);
        registerNativeHandler(REQUEST_BACK);
        registerNativeHandler(REQUEST_EXIT);
        registerNativeHandler(REQUEST_QR);
        registerNativeHandler(REQUEST_IMG_EXT);
        registerNativeHandler(REQUEST_CAMERA_EXT);
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

        registerNativeHandler(CALLBACK_VIDEO_RECORD);
        registerNativeHandler(QUICK_CAMERA);
    }
}
