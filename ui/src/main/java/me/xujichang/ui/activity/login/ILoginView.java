package me.xujichang.ui.activity.login;

import me.xujichang.basic.mvp.view.IView;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:26
 */
public interface ILoginView<U> extends IView {
    /**
     * 登陆成功
     *
     * @param info
     */
    void onLoginSuccess(U info);

    /**
     * 登陆失败
     *
     * @param msg
     */
    void onLoginFail(String msg);
}
