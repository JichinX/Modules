package me.xujichang.ui.test.login;

import me.xujichang.ui.activity.login.BasicLoginPresenter;
import me.xujichang.ui.activity.login.ILoginView;

/**
 * Des:自己需要实现
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:39
 */
public class LoginPresenter extends BasicLoginPresenter<ILoginView<UserInfo>> implements LoginContract.Presenter {
    @Override
    public void login(String account, String pwd) {
        view.onLoginFail("网络错误");
    }
}
