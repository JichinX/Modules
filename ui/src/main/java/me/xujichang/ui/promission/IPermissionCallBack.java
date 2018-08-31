package me.xujichang.ui.promission;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-上午11:14
 */
public interface IPermissionCallBack {
    void onGain();

    boolean onDenied();
}
