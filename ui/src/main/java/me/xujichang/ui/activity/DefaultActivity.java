package me.xujichang.ui.activity;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import android.widget.TextView;

import me.xujichang.ui.activity.actionbar.ActionWhich;
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
    private DefaultActionBar defaultActionBar;

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
        defaultActionBar = new DefaultActionBar();
        return defaultActionBar;
    }

    protected void setLeftText(String text) {
        defaultActionBar.setLeftText(text);
    }

    public void setRightText(String text) {
        defaultActionBar.setRightText(text);
    }

    public void setLeftImage(@DrawableRes int id) {
        defaultActionBar.setLeftImage(getDrawableWithSc(id));
    }

    public void setRightImage(@DrawableRes int id) {
        defaultActionBar.setRightImage(getDrawableWithSc(id));
    }

    // ==================================获取属性====================================================/
    protected TextView getActionbarTitle() {
        return defaultActionBar.getActionBarTitle();
    }

    public ImageView getActionbarLeftImg() {
        return defaultActionBar.getImageView(ActionWhich.LEFT_IMAGE);
    }

    public TextView getActionbarLeftText() {
        return defaultActionBar.getTextView(ActionWhich.LEFT_TEXT);
    }

    public ImageView getActionbarRightImg() {
        return defaultActionBar.getImageView(ActionWhich.RIGHT_IMAGE);
    }

    public TextView getActionbarRightText() {
        return defaultActionBar.getTextView(ActionWhich.RIGHT_TEXT);
    }

    @Override
    public void onClick(ActionWhich which) {
        if (which == ActionWhich.RIGHT_AREA) {
            onRightAreaClick();
        } else if (which == ActionWhich.LEFT_AREA) {
            onLeftAreaClick();
        } else if (which == ActionWhich.TITLE) {
            onTitleClick();
        } else {
            super.onClick(which);
        }
    }

    protected void onRightAreaClick() {

    }

    protected void onLeftAreaClick() {

    }

    protected void onTitleClick() {

    }

    //====================================兼容============================================
    @Deprecated
    protected void setLeftImg(@DrawableRes int id) {
        setLeftImage(id);
    }

    @Deprecated
    protected void setRightImg(@DrawableRes int id) {
        setRightImage(id);
    }

    @Deprecated
    protected void showBackArrow() {
        showBack();
    }
}
