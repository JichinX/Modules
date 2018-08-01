package com.codvision.background.job;

import com.codvision.background.api.ApiStores;
import com.codvision.background.base.retrofit.RetrofitCenter;
import com.codvision.background.base.rxjava.RxSchedulers;
import com.codvision.background.center.JobCenter;
import com.codvision.base.wrapper.WrapperEntity;

import java.nio.file.WatchEvent;

import me.xujichang.util.simple.SimpleObserver;
import me.xujichang.util.simple.SimpleResourceObserver;

/**
 * Project: Modules
 * Des:上传json数据给服务器
 *
 * @author xujichang
 * created by 2018/7/26 - 4:26 PM
 */
public class UploadJsonDataJob<T> extends UploadJob {
    private T data;

    public UploadJsonDataJob(JobCenter.CenterConfig config, T data) {
        this(config, null, data);
    }

    public UploadJsonDataJob(T data) {
        this(null, data);
    }


    public UploadJsonDataJob(JobCenter.CenterConfig config, IBaseJobCallBack callBack, T data) {
        super(config, callBack);
        this.data = data;
    }

    @Override
    protected IBaseJobCallBack createJobCallBack() {
        return new JsonUpload();
    }

    private class JsonUpload implements IBaseJobCallBack {
        @Override
        public Result onJobRun() {
            //默认的上传json数据的方法
            RetrofitCenter
                    .getApi(ApiStores.class)
                    .getString()
                    .compose(RxSchedulers.observableTransformer_io_main())
                    .subscribe(new SimpleResourceObserver<WrapperEntity<String>>(null) {
                        @Override
                        public void onNext(WrapperEntity s) {

                        }
                    });
            return Result.SUCCESS;
        }
    }
}
