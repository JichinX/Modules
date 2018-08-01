package com.codvision.background.job;

import android.support.annotation.NonNull;

import com.codvision.background.center.JobCenter;
import com.evernote.android.job.Job;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 4:08 PM
 */
public abstract class BaseJob extends Job {

    @NonNull
    protected JobCenter.CenterConfig usedConfig;
    protected IBaseJobCallBack usedCallback;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        //记录开始时间

        Result result = usedCallback.onJobRun();
        //记录结束时间
        //保存记录
        return result;
    }

    public interface IBaseJobCallBack {
        /**
         * 执行任务
         */

        Result onJobRun();
    }

    @NonNull
    public JobCenter.CenterConfig getUsedConfig() {
        return usedConfig;
    }
}
