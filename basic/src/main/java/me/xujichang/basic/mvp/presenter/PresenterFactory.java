package me.xujichang.basic.mvp.presenter;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午8:05
 */
public interface PresenterFactory<P extends IPresenter> {
    P create();
}
