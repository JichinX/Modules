package me.xujichang.step.test;

import android.app.Application;


import me.xujichang.basic.BaseConst;
import me.xujichang.basic.util.InitUtil;
import me.xujichang.step.StepConst;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午6:04
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseConst.enablePacketService = true;
        InitUtil.initModulesSpeed(StepConst.Modules.Inits, this);
        InitUtil.initModulesLow(StepConst.Modules.Inits, this);
    }
}
