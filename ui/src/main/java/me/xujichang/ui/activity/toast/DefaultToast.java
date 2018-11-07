package me.xujichang.ui.activity.toast;

import android.content.Context;
import android.widget.Toast;

import me.xujichang.ui.utils.GlobalUtil;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午4:14
 */
public class DefaultToast implements IToast {

    @Override
    public void showToast(String msg) {
        showShortToast(msg);
    }

    @Override
    public void showLongToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showShortToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String msg, int duration) {
        Toast.makeText(getContext(), msg, duration).show();
    }

    private static Context getContext() {
        return GlobalUtil.getCurrentContext();
    }
}
