package com.codvision.check.web;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 15:16.
 */

public class WebConst {
    public static final long EXIT_DURATION = 1000;

    public static class StatusCode {
        /**
         * 正常状态
         */
        public static final int STATUS_OK = 200;
        /**
         * 重复的请求
         */
        public static final int REQUEST_REPEAT = 201;
        /**
         *
         */
        public static final int REQUEST_OTHER = 202;
        /**
         * 本地错误
         */
        public static final int STATUS_NATIVE_ERROR = 500;
        /**
         * 文件未找到
         */
        public static final int ERROR_FILE_NOT_FOUND = 501;
        /**
         * 来自服务端的回复
         */
        public static final int STATUS_SERVER_RESP = 600;
        /**
         * 未获取到数据
         */
        public static final int STATUS_DATA_NULL = 205;
        /**
         * 不确定 哪一方的错误
         */
        public static final int STATUS_ERROR = 700;
        public static final int STATUS_SUCCESS_PART = 204;
    }


    public static class RequestCode {
        public static final int QRCODE = 10005;
        public static final int NAV = 10006;
    }
}
