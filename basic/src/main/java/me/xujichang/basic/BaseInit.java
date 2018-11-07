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
    /**
     * 默认Token key
     */
    public static volatile String TOKEN_KEY = BaseConst.TOKEN_KEY;
    /**
     * 默认不启用 心跳Service
     */
    public static volatile boolean enablePacketService = BaseConst.enablePacketService;
    /**
     * 默认 module 数量，用来初始化心跳Service中的线程池
     */
    public static volatile int MODULE_SIZE = BaseConst.MODULE_SIZE;
    /**
     * 默认 心跳Service的间隔
     */
    public static volatile int PACKET_DURATION = BaseConst.PACKET_DURATION;

    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        if (enablePacketService) {
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
