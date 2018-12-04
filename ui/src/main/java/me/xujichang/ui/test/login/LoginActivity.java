package me.xujichang.ui.test.login;

import me.xujichang.ui.activity.login.ILoginView;
import me.xujichang.ui.activity.login.SimpleLoginActivity;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:37
 */
public class LoginActivity extends SimpleLoginActivity<ILoginView<UserInfo>, LoginPresenter> implements LoginContract.View {
    @Override
    protected LoginPresenter obtainPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLoginSuccess(UserInfo info) {
        showToast("登录成功");
    }

    @Override
    public void onLoginFail(String msg) {
        showToast("登录失败");
    }

    @Override
    protected void initData() {

    }
}
