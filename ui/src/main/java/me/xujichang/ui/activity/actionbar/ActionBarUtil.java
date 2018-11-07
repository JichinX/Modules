package me.xujichang.ui.activity.actionbar;

import me.xujichang.ui.activity.IActivity;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午8:59
 */
public class ActionBarUtil {

    public static IActionBar init(XActionBar actionBar, IActivity iActivity) {
        if (actionBar == XActionBar.DEFAULT) {
            return new DefaultActionBar();
        }
        return null;
    }
}
