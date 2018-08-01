package com.codvision.base.rxjava;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Project: Modules
 * Des:封装线程调度
 *
 * @author xujichang
 * created by 2018/7/26 - 5:01 PM
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> observableTransformer_io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> flowableTransformer_io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }
}
