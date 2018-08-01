package com.codvision.background.job;

import com.codvision.background.center.JobCenter;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import me.xujichang.util.tool.LogTool;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 4:11 PM
 */
public abstract class UploadJob extends BaseJob {
    public static final String TAG = "upload";
    private static JobCenter.CenterConfig defaultConfig;
    private static IBaseJobCallBack defaultJobCallBack;

    static {
        defaultConfig = new JobCenter
                .CenterConfig
                .Builder()
                .withMinFlex(TimeUnit.MINUTES.toMillis(10))
                .withMinInterval(TimeUnit.MINUTES.toMillis(10))
                .withRequireNetType(JobRequest.NetworkType.CONNECTED)
                .withTag(TAG)
                .build();
        defaultJobCallBack = new DefaultUploadCallBack();
    }

    public UploadJob(JobCenter.CenterConfig config) {
        this(config, null);
    }

    public UploadJob() {
        this(null);
    }

    public UploadJob(JobCenter.CenterConfig config, IBaseJobCallBack callBack) {
        if (null == config) {
            usedConfig = defaultConfig;
        }
        if (null == callBack) {
            usedCallback = createJobCallBack();
        }
        if (null == usedCallback) {
            usedCallback = defaultJobCallBack;
        }
    }

    protected abstract IBaseJobCallBack createJobCallBack();

    private static class DefaultUploadCallBack implements IBaseJobCallBack {
        @Override
        public Result onJobRun() {
            LogTool.d("执行的是默认 空 方法......");
            return Result.SUCCESS;
        }
    }
}
