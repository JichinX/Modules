package me.xujichang.ui.test;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import me.xujichang.basic.util.InitUtil;


/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午9:20
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitUtil.initModulesSpeed(Const.Modules.inits, this);
        InitUtil.initModulesLow(Const.Modules.inits, this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
