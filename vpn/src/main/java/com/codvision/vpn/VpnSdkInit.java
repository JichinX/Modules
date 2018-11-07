package com.codvision.vpn;

import android.app.Application;

import me.xujichang.basic.IBaseInit;


/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 9:08 PM
 */
public class VpnSdkInit implements IBaseInit {
    public static final String INIT_TAG = VpnSdkInit.class.getName();

    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
