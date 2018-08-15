package com.codvision.step.test;

import android.app.Application;

import com.codvision.base.utils.InitUtil;
import com.codvision.step.StepConst;

/**
 * Application 做一些初始化的事情
 *
 * @author xujichang
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitUtil.initModulesSpeed(StepConst.Modules.Inits, this);
        InitUtil.initModulesLow(StepConst.Modules.Inits, this);
    }
}
