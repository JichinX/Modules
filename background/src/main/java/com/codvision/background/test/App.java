package com.codvision.background.test;

import android.app.Application;

import com.codvision.background.BackgroundApplication;
import com.codvision.background.BackgroundConst;
import com.codvision.base.utils.InitUtil;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 2:13 PM
 */
public class App extends BackgroundApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        InitUtil.initModulesSpeed(BackgroundConst.Modules.inits, this);
        InitUtil.initModulesLow(BackgroundConst.Modules.inits, this);
    }
}
