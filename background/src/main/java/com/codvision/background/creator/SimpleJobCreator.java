package com.codvision.background.creator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codvision.background.job.UploadJob;
import com.codvision.background.job.UploadJsonDataJob;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

import me.xujichang.util.tool.LogTool;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 3:01 PM
 */
public class SimpleJobCreator implements JobCreator {
    public static final String TAG = "SimpleJobCreator";

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case UploadJsonDataJob.TAG:
                LogTool.d("SimpleJobCreator: ...........UploadJob");
                return new UploadJsonDataJob();
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            // manager.addJobCreator(new DemoJobCreator());
            LogTool.d("AddReceiver: ...........");
        }
    }
}
