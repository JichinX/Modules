package me.xujichang.ui.activity.login;

/**
 * Des:定义登录的presenter
 *
 * @author xujichang
 * <p>
 * created by 2018/9/14-上午10:25
 */
public interface ILoginPresenter {
    /**
     * 登录
     *
     * @param account 用户名
     * @param pwd     密码
     */
    void login(String account, String pwd);
}
