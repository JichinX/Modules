package com.codvision.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.List;

public class Files {
    /**
     * 解析文件MIME_TYPE
     *
     * @param file
     * @return
     */
    public static String parseMimeType(File file) {
        String fileName = file.getName();
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionName(fileName));
        return null == type ? "file/*" : type;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static boolean openFile(File file, Context context) {
        Intent in = new Intent("android.intent.action.VIEW");
        in.addCategory("android.intent.category.DEFAULT");
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, parseFileProviderAuth(context), file);
            // 给目标应用一个临时授权
            in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.setDataAndType(data, Files.parseMimeType(file));
        if (isIntentAvailable(context, in)) {
            context.startActivity(in);
            return true;
        } else {
            return false;
        }
    }

    private static String parseFileProviderAuth(Context context) {
        return String.format("%s.fileprovider", context.getPackageName());
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }
}