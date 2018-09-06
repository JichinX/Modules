package me.xujichang.step;

import android.app.Application;


import me.xujichang.basic.IBaseInit;
import me.xujichang.step.center.StepCenter;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午6:03
 */
public class StepInit implements IBaseInit {
    public static final String INIT_TAG = StepInit.class.getName();

    @Override
    public boolean onInitSpeed(Application application) {
        StepCenter.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
