package me.xujichang.basic;

import android.app.Application;

/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 8:59 PM
 */
public interface IBaseInit {
    boolean onInitSpeed(Application application);

    boolean onInitLow(Application application);
}
