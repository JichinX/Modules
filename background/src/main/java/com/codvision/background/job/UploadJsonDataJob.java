package com.codvision.background.job;

import com.codvision.background.center.JobCenter;

import me.xujichang.util.tool.LogTool;

/**
 * Project: Modules
 * Des:上传json数据给服务器
 *
 * @author xujichang
 * created by 2018/7/26 - 4:26 PM
 */
public class UploadJsonDataJob extends UploadJob {
    public static final String TAG = "upload_json";

    public UploadJsonDataJob(JobCenter.CenterConfig config) {
        this(config, null);
    }

    public UploadJsonDataJob() {
        this(null);
    }

    public UploadJsonDataJob(JobCenter.CenterConfig config, IBaseJobCallBack callBack) {
        super(config, callBack);
    }

    @Override
    protected IBaseJobCallBack createJobCallBack() {
        return new JsonUpload();
    }

    private class JsonUpload implements IBaseJobCallBack {
        @Override
        public Result onJobRun() {
            //模拟从数据库取出json数据
            String jsonStr = "";
            LogTool.d("上传数据...json 数据");
//            //默认的上传json数据的方法
//            RetrofitCenter
//                    .getApi(ApiStores.class)
//                    .uploadJsonData(jsonStr)
//                    .compose(RxSchedulers.observableTransformer_io_main())
//                    .subscribe(new SimpleResourceObserver<WrapperEntity<String>>(null) {
//                        @Override
//                        public void onNext(WrapperEntity s) {
//
//                        }
//                    });
            return Result.SUCCESS;
        }
    }
}
