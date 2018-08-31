package me.xujichang.basic;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import me.xujichang.basic.services.PacketService;
import me.xujichang.util.tool.LogTool;


/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 9:07 PM
 */
public class BaseInit implements IBaseInit {
    public static final String INIT_TAG = BaseInit.class.getName();

    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        if (BaseConst.enablePacketService) {
            LogTool.d("开启心跳Service");
            //开启心跳Service
            Intent service = new Intent(application, PacketService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.startForegroundService(service);
            } else {
                application.startService(service);
            }
        }
        return false;
    }
}
