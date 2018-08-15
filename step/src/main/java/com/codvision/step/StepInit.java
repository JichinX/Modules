package com.codvision.step;

import android.app.Application;

import com.codvision.base.BaseInit;
import com.codvision.base.ext.IBaseInit;

/**
 * 模块的初始化
 */
public class StepInit implements IBaseInit {
    public static final String INIT_TAG = "com.codvision.step.StepInit";

    @Override
    public boolean onInitSpeed(Application application) {

        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
