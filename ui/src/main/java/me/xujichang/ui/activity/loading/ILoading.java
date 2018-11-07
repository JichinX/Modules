package me.xujichang.ui.activity.loading;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/27-上午11:28
 */
public interface ILoading {

    void showProgress(String title, LoadingStatus status, String msg, int progress);

    void showLoading(String title, LoadingStatus status, String msg);
}
