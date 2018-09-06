package me.xujichang.basic.mvp.presenter;

import me.xujichang.basic.mvp.view.IView;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午5:29
 */
public interface IPresenter<V extends IView> {
    @Deprecated
    void start(V v);

    @Deprecated
    void destroy();

    void onAttach(V view);

    void onDetach();
}
