package me.xujichang.autil.rxjava.observer;

import com.google.common.base.Strings;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import me.xujichang.autil.exception.ExceptionUtil;
import me.xujichang.basic.mvp.view.IView;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午10:22
 */
public abstract class ABaseResourceObserver<T> extends ResourceObserver<T> {
    private IView view;
    private String msg;

    public ABaseResourceObserver(IView view, String msg) {
        this.view = view;
        this.msg = msg;
    }

    public ABaseResourceObserver(IView view) {
        this(view, null);
    }

    @Override
    protected void onStart() {
        if (!Strings.isNullOrEmpty(msg)) {
            view.onStartLoading(msg);
        }
    }

    @Override
    public void onError(Throwable e) {
        onComplete();
        view.onError(ExceptionUtil.getErrorMsg(e));
    }

    @Override
    public void onComplete() {
        view.onStopLoading();
        view.loadingComplete();
    }

}
