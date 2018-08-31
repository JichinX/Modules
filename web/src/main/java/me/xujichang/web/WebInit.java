package me.xujichang.web;

import android.app.Application;

import me.xujichang.basic.IBaseInit;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-下午4:35
 */
public class WebInit implements IBaseInit {
    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
