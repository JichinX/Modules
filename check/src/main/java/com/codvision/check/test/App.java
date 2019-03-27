package com.codvision.check.test;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.codvision.check.CheckInit;

import me.xujichang.basic.util.InitUtil;
import me.xujichang.util.tool.LogTool;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.syncIsDebug(this);
        InitUtil.initModulesSpeed(CheckConst.Modules.inits, this);
        CheckInit.enableWebDebug();
        InitUtil.initModulesLow(CheckConst.Modules.inits, this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
