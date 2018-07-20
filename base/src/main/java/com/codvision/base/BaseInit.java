package com.codvision.base;

import android.app.Application;

import com.codvision.base.ext.IBaseInit;


/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 9:07 PM
 */
public class BaseInit implements IBaseInit {
    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
