package me.xujichang.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * Des:定义Activity的功能
 *
 * @author xujichang
 * <p>
 * created by 2018/8/24-上午11:22
 */
public interface IActivity {
    /**
     * 显示返回按钮
     */
    void showBack();

    /**
     * 设置Title
     *
     * @param title
     */
    void setActionBarTitle(String title);

    Context getContext();

    Drawable getDrawableWithSc(@DrawableRes int id);
}
