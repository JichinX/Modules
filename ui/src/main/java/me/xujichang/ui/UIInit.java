package me.xujichang.ui;

import android.app.Application;


import me.xujichang.basic.IBaseInit;
import me.xujichang.ui.utils.GlobalUtil;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午7:53
 */
public class UIInit implements IBaseInit {
    public static final String INIT_TAG = UIInit.class.getName();

    @Override
    public boolean onInitSpeed(Application application) {
        GlobalUtil.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
