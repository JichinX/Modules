package me.xujichang.basic.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午8:03
 */
public class PresenterLoader<P extends IPresenter> extends Loader<P> {
    private final PresenterFactory<P> factory;
    private P presenter;

    public PresenterLoader(@NonNull Context context, PresenterFactory<P> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (null != presenter) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter = null;
    }

}
