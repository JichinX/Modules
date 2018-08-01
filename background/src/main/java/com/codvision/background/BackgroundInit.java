package com.codvision.background;

import android.app.Application;

import com.codvision.background.center.JobCenter;
import com.codvision.background.creator.SimpleJobCreator;
import com.codvision.background.job.BaseJob;
import com.codvision.background.job.UploadJob;
import com.codvision.base.ext.IBaseInit;
import com.evernote.android.job.Job;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 1:58 PM
 */
public class BackgroundInit implements IBaseInit {
    @Override
    public boolean onInitSpeed(Application application) {
        JobCenter.init(application, new SimpleJobCreator(), true);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        //默认周期配置，默认数据上传
        JobCenter.startPeriodJob(new UploadJob());

        //使用在自定义周期配置
        JobCenter.CenterConfig config = new JobCenter.CenterConfig.Builder().build();
        JobCenter.startPeriodJob(new UploadJob(config));

        //自定义周期上传的数据
        BaseJob.IBaseJobCallBack callBack = new BaseJob.IBaseJobCallBack() {
            @Override
            public Job.Result onJobRun() {
                return Job.Result.SUCCESS;
            }
        };
        JobCenter.startPeriodJob(new UploadJob(config, callBack));
        return false;
    }
}
