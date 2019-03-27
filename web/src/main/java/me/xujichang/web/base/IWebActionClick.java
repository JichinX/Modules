package me.xujichang.web.base;

import me.xujichang.ui.activity.actionbar.IActionClick;

/**
 * Des:Modules - me.xujichang.web.base
 *
 * @author xujichang
 * @date 2019/3/19
 * <p>
 * modify:
 */
public interface IWebActionClick {

    /**
     * 退出
     */
    void onExit();

    /**
     * 返回
     */
    void onBack();

    /**
     * 右侧点击
     */
    void onRightClick();
}
