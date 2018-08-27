package me.xujichang.ui;

import android.app.Application;

import com.codvision.base.ext.IBaseInit;

import me.xujichang.ui.utils.UIGlobalUtil;

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
        UIGlobalUtil.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }
}
