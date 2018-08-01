package com.codvision.background;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 2:14 PM
 */
public class BackgroundConst {
    public static final class Modules {
        public static final String BaseInit = "com.codvision.base.BaseInit";
        public static final String BackgroundInit = "com.codvision.background.BackgroundInit";

        public static String[] inits = {BaseInit, BackgroundInit};
    }

    public static final class JobUpload {
        public static String BASEURL = "";
    }
}
