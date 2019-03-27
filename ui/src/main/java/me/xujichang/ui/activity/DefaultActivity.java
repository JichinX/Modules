package me.xujichang.ui.activity;

import me.xujichang.ui.activity.actionbar.DefaultActionBar;
import me.xujichang.ui.activity.actionbar.IActionBar;
import me.xujichang.ui.activity.dialog.IDialog;
import me.xujichang.ui.activity.dialog.MDDialog;
import me.xujichang.ui.activity.loading.ILoading;
import me.xujichang.ui.activity.loading.MDLoading;
import me.xujichang.ui.activity.toast.DefaultToast;
import me.xujichang.ui.activity.toast.IToast;
import me.xujichang.ui.promission.DefaultPermission;
import me.xujichang.ui.promission.IPermission;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午4:38
 */
public class DefaultActivity extends XBaseActivity {


    @Override
    protected IToast obtainToast() {
        return new DefaultToast();
    }

    @Override
    protected IPermission obtainPermission() {
        return DefaultPermission.instance();
    }

    @Override
    protected ILoading obtainLoading() {
        return MDLoading.instance();
    }

    @Override
    protected IDialog obtainDialog() {
        return MDDialog.instance();
    }


    @Override
    protected IActionBar onObtainActionBar() {
        return new DefaultActionBar();
    }
}
