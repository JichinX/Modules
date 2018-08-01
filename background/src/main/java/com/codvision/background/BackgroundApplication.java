package com.codvision.background;

import android.app.Application;

import com.codvision.background.center.JobCenter;
import com.codvision.background.creator.SimpleJobCreator;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/25 - 2:43 PM
 */
public class BackgroundApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JobCenter.init(this, new SimpleJobCreator());
    }
}
