package me.xujichang.web.base;

import android.widget.ImageView;
import android.widget.TextView;

import me.xujichang.ui.activity.DefaultActivity;
import me.xujichang.ui.activity.actionbar.IActionBar;

/**
 * Des:Modules - me.xujichang.web.base
 *
 * @author xujichang
 * @date 2019/3/19
 * <p>
 * modify:
 */
public class WebActionbarActivity extends DefaultActivity implements IWebActionClick {
    private WebActionBar mActionBar;

    @Override
    protected IActionBar onObtainActionBar() {
        mActionBar = new WebActionBar(this);
        return mActionBar;
    }

    protected void onLeftAreaClick() {
        onBackPressed();
    }

    protected void setRightImage(int id) {
        mActionBar.setRightImageRes(id);
    }

    protected void onRightAreaClick() {

    }

    protected ImageView getActionbarRightImg() {
        return mActionBar.getRightImg();
    }

    protected TextView getActionbarTitle() {
        return mActionBar.getTitle();
    }

    protected void hideExit() {
        mActionBar.hideExit();
    }

    protected void showExit() {
        mActionBar.showExit();
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void onRightClick() {
        onRightAreaClick();
    }
}
