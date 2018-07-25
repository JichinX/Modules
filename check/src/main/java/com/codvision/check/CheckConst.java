package com.codvision.check;

/**
 * Project: accidenthandling
 * Des:
 *
 * @author xujichang
 * created by 2018/7/17 - 10:56 AM
 */
public class CheckConst {
    public static final String TOKEN_KEY = "token";
    public static final String LOCATION_UPLOAD_TYPE = "8000010";
    public static String LOCATION_UPLOAD_PATH = "";
    public static String PHOTO_UPLOAD_PATH = "";

    public static final class Url {

        public static final String BASE_URL = "";
    }

    public static final class Modules {
        public static final String BaseInit = "com.codvision.base.BaseInit";
        public static final String CheckInit = "com.codvision.check.CheckInit";

        public static String[] inits = {BaseInit, CheckInit};
    }
}
