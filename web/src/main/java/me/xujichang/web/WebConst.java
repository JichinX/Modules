package me.xujichang.web;

import java.io.File;

/**
 * Created by xjc on 2017/6/7.
 */

public class WebConst {
    public static String  BASE_PACKAGE_NAME;
    public static boolean USE_MAP         = false;
    public static boolean USE_OFFLINE_MAP = false;

    public static class FLAG {
        public static final String WEB_URL  = "web_url";
        public static final String LOCATION = "location";
    }

    public static class SCHEME {
        public static final String JS_SCHEME     = "uniview";
        public static final String NATIVE_SCHEME = "native";
        public static final String CALL_BACK     = "callback";
        public static final String HTTP_SCHEME   = "http";
    }

    public static class Operation {
        public static final String START = "start";
        public static final String STOP  = "stop";
    }

    public static class Url {
        public static final String[] APP_BASE_URL = new String[1];
        public static       String   APP_UPDATE_URL;
        public static       String   APP_UPDATE_PATH;
    }

    public static class PATH {
        public static final String mapPath       = "/BaiduMapSDKNew/vmp";
        public static final String cmapCachePath = "/BaiduMapCache";
        public static       String HTML_DIR;
        public static       String PATH_WEB_CACHE;
    }

    public static class Fragment {
        public static final String toast    = "toast";
        public static final String loading  = "loading";
        public static final String dialog   = "dialog";
        public static final String location = "location";
        public static final String device   = "device";
        public static final String self     = "self";
        public static final String activity = "activity";
    }

    public static final long EXIT_DURATION = 1000;

    public static class StatusCode {
        /**
         * 正常状态
         */
        public static final int STATUS_OK            = 200;
        /**
         * 重复的请求
         */
        public static final int REQUEST_REPEAT       = 201;
        /**
         *
         */
        public static final int REQUEST_OTHER        = 202;
        /**
         * 本地错误
         */
        public static final int STATUS_NATIVE_ERROR  = 500;
        /**
         * 文件未找到
         */
        public static final int ERROR_FILE_NOT_FOUND = 501;
        /**
         * 来自服务端的回复
         */
        public static final int STATUS_SERVER_RESP   = 600;
        /**
         * 未获取到数据
         */
        public static final int STATUS_DATA_NULL     = 205;
        /**
         * 不确定 哪一方的错误
         */
        public static final int STATUS_ERROR         = 700;
        /**
         * 不确定 哪一方的错误
         */
        public static final int STATUS_ERROR_PARAMS  = 203;
        public static final int STATUS_SUCCESS_PART  = 204;
    }


    public static class RequestCode {
        public static final int QRCODE       = 10005;
        public static final int NAV          = 10006;
        public static final int QUICK_CAMERA = 1007;
    }

}
