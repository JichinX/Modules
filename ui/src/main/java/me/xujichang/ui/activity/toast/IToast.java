package me.xujichang.ui.activity.toast;

/**
 * Des:对Toast提示做方法规定
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-下午1:42
 */
public interface IToast {
    /**
     * 显示Msg
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示长Toast
     *
     * @param msg
     */
    void showLongToast(String msg);

    /**
     * 显示端Toast
     *
     * @param msg
     */
    void showShortToast(String msg);

    /**
     * 显示自定义时间的Toast
     *
     * @param msg
     * @param duration
     */
    void showToast(String msg, int duration);
}
