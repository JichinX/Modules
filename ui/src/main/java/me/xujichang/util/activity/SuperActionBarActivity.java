//package me.xujichang.util.activity;
//
//import android.graphics.drawable.ColorDrawable;
//import android.support.annotation.ColorInt;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import ActionWhich;
//import me.xujichang.ui.activity.DefaultActionBarActivity;
//
///**
// * des:最基本布局 包括 ActionBar statusBar errorTip
// *
// * @author xjc
// * Created by xjc on 2017/6/23.
// */
//
//public abstract class SuperActionBarActivity extends DefaultActionBarActivity {
//
//    protected void setActionBarDrawable(@DrawableRes int id) {
//        setActionBarDrawable(getDrawableWithSc(id));
//    }
//
//    protected void setActionBarColor(@ColorInt int color) {
//        ColorDrawable drawable = new ColorDrawable(color);
//        setActionBarDrawable(drawable);
//    }
//
//    /**
//     * 显示返回箭头
//     */
//    protected void showBackArrow() {
//        showBack();
//    }
//
//
//    /**
//     * 设置左侧图片
//     */
//    protected void setLeftImg(@DrawableRes int id) {
//        setLeftImage(id);
//    }
//
//    /**
//     * 显示返回箭头
//     */
//    protected void showForwardArrow() {
//
//    }
//
//    /**
//     * 设置右侧图片
//     */
//    protected void setRightImg(@DrawableRes int id) {
//        setRightImage(id);
//    }
//
//
//    protected void onTitleClick() {
//
//    }
//
//    protected void onRightAreaClick() {
//
//    }
//
//    protected void onLeftAreaClick() {
//        onBackPressed();
//    }
//
//    @Override
//    public void onClick(ActionWhich which) {
//        if (which == ActionWhich.RIGHT_IMAGE || which == ActionWhich.RIGHT_TEXT) {
//            onRightAreaClick();
//        } else if (which == ActionWhich.LEFT_IMAGE || which == ActionWhich.LEFT_TEXT) {
//            onLeftAreaClick();
//        } else if (which == ActionWhich.TITLE) {
//            onTitleClick();
//        } else {
//            super.onClick(which);
//        }
//    }
//
//    @Deprecated
//    @Nullable
//    protected LinearLayout getSuperActionBar() {
//        return null;
//    }
//
//    protected ViewGroup getSuperRoot() {
//        return getContentContainer();
//    }
//
//    protected ViewGroup getSuperRootContainer() {
//        return getBaseRoot();
//    }
//
//
//    protected boolean isActionBarShow() {
//        return getActionBarStatus() == View.VISIBLE;
//    }
//
//}
