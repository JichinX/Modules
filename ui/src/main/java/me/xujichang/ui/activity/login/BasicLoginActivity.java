package me.xujichang.ui.activity.login;

import me.xujichang.ui.activity.BasicMvpActivity;

/**
 * Des:登录页面 仅封装逻辑
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:18
 */
public abstract class BasicLoginActivity<V extends ILoginView, P extends BasicLoginPresenter<V>> extends BasicMvpActivity<V, P> {

    protected void login(String account, String pwd) {
        presenter.login(account, pwd);
    }
}
