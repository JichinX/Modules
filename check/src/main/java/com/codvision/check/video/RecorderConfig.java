package com.codvision.check.video;

import android.os.Environment;

import com.google.common.base.Strings;

import java.io.File;

/**
 * Des:视频录制的的配置
 *
 * @author xujichang
 * Created on 2018/10/16 - 11:08 AM
 */
public class RecorderConfig {
    public static final long VIDEO_MIN_MS = 3 * 1000;
    public static final long VIDEO_MAX_MS = 15 * 1000;
    private static final String VIDEO_DEFAULT_DIR_NAME = "/check_video/";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final long PROGRESS_PERIOD = 100L;
    public static final int REQUEST_CODE = 120;
    public static final String FILE_THUMBNAIL = "video_thumbnail";

    private static String VIDEO_DIR;

    public static String getVideoDir() {
        return VIDEO_DIR;
    }

    static {
        init();
    }

    public static void init() {
        init(VIDEO_DEFAULT_DIR_NAME);
    }

    public static void init(String dirName) {
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        String dir = dcim + "/" + dirName + "/";
        setVideoDir(dir);
    }

    public static void setVideoDir(String videoDir) {
        File file = new File(videoDir);
        boolean mkdirs = true;
        if (!file.exists()) {
            mkdirs = file.mkdirs();
        }
        if (mkdirs) {
            VIDEO_DIR = videoDir;
        }
    }

    public static boolean checkConfig() {
        if (Strings.isNullOrEmpty(VIDEO_DIR)) {
            return false;
        }
        return true;
    }
}
