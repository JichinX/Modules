package me.xujichang.ui.activity.actionbar;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Des:对所有ActionBar做统一方法要求，
 * 类似于制定统一规格，才能在Activity中适用
 *
 * @author xujichang
 * <p>
 * created by 2018/8/23-下午8:05
 */
public interface IActionBar {
    /**
     * 将ActionBar置于哪个View内
     *
     * @param view
     */
    void attachRoot(LinearLayout view);

    int getResourceID();

    void onInflate(View inflated);

    void setTitle(String text);

    void setActionClick(IActionClick click);

    /**
     * 隐藏某部分
     *
     * @param which
     */
    void hide(ActionWhich which);

    void showBack();

    void setActionBarDrawable(Drawable drawable);

    View getActionBar();

}
