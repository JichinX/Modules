package com.codvision.step.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codvision.step.service.StepService;
import com.codvision.step.utils.Logger;

/**
 * 开机完成广播
 *
 */
public class TodayStepBootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "TodayStepBootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent todayStepIntent = new Intent(context, StepService.class);
        todayStepIntent.putExtra(StepService.INTENT_NAME_BOOT,true);
        context.startService(todayStepIntent);

        Logger.e(TAG,"TodayStepBootCompleteReceiver");

    }
}
