package me.xujichang.basic.mvp.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.xujichang.basic.mvp.view.IView;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午6:13
 */
public abstract class BasicPresenter<V extends IView> implements IPresenter<V> {
    protected V view;
    private CompositeDisposable disposables;

    protected void addObserver(Disposable disposable) {
        if (null != disposable) {
            disposables.add(disposable);
        }
    }

    protected void destroyObservers() {
        if (null != disposables) {
            disposables.dispose();
        }
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
        this.disposables = new CompositeDisposable();
        start(view);
        start();
    }

    public void start() {

    }

    @Override
    public void onDetach() {
        destroy();
        view = null;
    }

    public boolean isAttachView() {
        return view != null;
    }

    public V getMvpView() {
        return view;
    }

    public void checkViewAttach() {
        if (!isAttachView()) {
            throw new ViewNotAttachedException();
        }
    }

    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException() {
            super("请求数据前请先调用 attachView(MvpView) 方法与View建立连接");
        }
    }

    @Override
    public void destroy() {
        destroyObservers();
    }

    @Override
    public void start(V v) {

    }
}
