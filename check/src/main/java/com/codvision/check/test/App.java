package com.codvision.check.test;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.codvision.check.CheckConst;

import me.xujichang.basic.util.InitUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitUtil.initModulesSpeed(CheckConst.Modules.inits, this);
        InitUtil.initModulesLow(CheckConst.Modules.inits, this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
