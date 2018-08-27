package me.xujichang.ui.actionbar;

import android.graphics.drawable.Drawable;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/24-下午4:14
 */
public interface IDefaultActionBar extends IActionBar {

    void setLeftText(String text);

    void setLeftImage(Drawable drawable);

    void setRightText(String text);

    void setRightImage(Drawable drawable);

}
