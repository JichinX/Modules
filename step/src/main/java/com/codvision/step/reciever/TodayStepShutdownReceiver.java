package com.codvision.step.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codvision.step.dao.PreferencesHelper;
import com.codvision.step.utils.Logger;

/**
 * Created by jiahongfei on 2017/9/27.
 */

public class TodayStepShutdownReceiver extends BroadcastReceiver {

    private static final String TAG = "TodayStepShutdownReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            Logger.e(TAG,"TodayStepShutdownReceiver");
            PreferencesHelper.setShutdown(context,true);
        }
    }

}
