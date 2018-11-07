package me.xujichang.autil;

import android.app.Application;

import me.xujichang.basic.IBaseInit;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午10:17
 */
public class AUtilInit implements IBaseInit {

    @Override
    public boolean onInitSpeed(Application application) {
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
