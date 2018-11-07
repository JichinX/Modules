package me.xujichang.basic.mvp.view;

import android.content.Context;

/**
 * Des: 定义在MVP模式下，公用的方法
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午5:29
 */
public interface IView {
    //======loading

    /**
     * 开启Loading
     *
     * @param msg
     */
    void onStartLoading(String msg);

    /**
     * 停止loading
     */
    void onStopLoading();
    //-========提示

    /**
     * 错误
     *
     * @param msg
     */
    void onError(String msg);

    /**
     * 警告
     *
     * @param msg
     */
    void onWarning(String msg);

    /**
     * 提示
     *
     * @param msg
     */
    void onTips(String msg);

    /**
     * Toast
     *
     * @param msg
     */
    void onToast(String msg);

    Context getContext();
    //=============兼容

    /**
     * 开启Loading
     *
     * @param msg loading时提示
     */
    @Deprecated
    void startLoading(String msg);

    /**
     * 停止Loading
     */
    @Deprecated
    void stopLoading();

    /**
     * 显示Toast提示
     *
     * @param msg 提示信息
     */
    @Deprecated
    void showToast(String msg);

    /**
     * 加载错误
     *
     * @param msg 错误提示信息
     */
    @Deprecated
    void loadingError(String msg);

    /**
     * 加载完成
     */
    @Deprecated
    void loadingComplete();
}
