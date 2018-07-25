package com.codvision.check;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import com.codvision.base.ext.IBaseInit;
import com.codvision.base.utils.third.thirdparty.Utils;
import com.codvision.check.services.UploadLocationService;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/23 - 9:15 AM
 */
public class CheckInit implements IBaseInit {

    public static Location location;
    @Override
    public boolean onInitSpeed(Application application) {
        Utils.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {

        Intent service = new Intent(application, UploadLocationService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            application.startForegroundService(service);
        } else {
            application.startService(service);
        }
        return false;
    }
}
