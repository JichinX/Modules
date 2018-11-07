package me.xujichang.ui.test.login;

import me.xujichang.ui.activity.login.ILoginPresenter;
import me.xujichang.ui.activity.login.ILoginView;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:40
 */
public class LoginContract {
    public interface View extends ILoginView<UserInfo> {

    }

    public interface Presenter extends ILoginPresenter {

    }
}
