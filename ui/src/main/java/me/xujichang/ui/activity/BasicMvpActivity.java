package me.xujichang.ui.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import me.xujichang.basic.mvp.presenter.BasicPresenter;
import me.xujichang.basic.mvp.presenter.PresenterFactory;
import me.xujichang.basic.mvp.presenter.PresenterLoader;
import me.xujichang.basic.mvp.view.IView;

/**
 * Des:封装MVP的一系列操作
 *
 * @author xujichang
 * <p>
 * created by 2018/9/5-下午5:54
 */
public abstract class BasicMvpActivity<V extends IView, P extends BasicPresenter<V>> extends DefaultActivity implements IView, LoaderManager.LoaderCallbacks<P> {
    private static final int BASE_LOADER = 1000;
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(BASE_LOADER, null, this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStartLoading(String msg) {
        startLoading(msg);
    }

    @Override
    public void onStopLoading() {
        stopLoading();
    }

    @Override
    public void onError(String msg) {
        showError(msg);
    }

    @Override
    public void onWarning(String msg) {
        showWarning(msg);
    }

    @Override
    public void onTips(String msg) {
        showTips(msg);
    }

    @Override
    public void onToast(String msg) {
        showToast(msg);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    public void loadingError(String msg) {
        showError(msg);
    }

    @Override
    public void loadingComplete() {
        stopLoading();
    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<P> loader, P data) {
        presenter = data;
        presenter.onAttach((V) this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<P> loader) {
        presenter = null;
    }

    @NonNull
    @Override
    public Loader<P> onCreateLoader(int id, @Nullable Bundle args) {
        return new PresenterLoader<P>(getContext(), new PresenterFactory<P>() {
            @Override
            public P create() {

                return obtainPresenter();
            }
        });
    }

    protected abstract P obtainPresenter();
}
