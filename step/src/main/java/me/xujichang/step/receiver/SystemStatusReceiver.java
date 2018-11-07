package me.xujichang.step.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.xujichang.step.center.StepCenter;
import me.xujichang.util.tool.LogTool;

/**
 * Des:开机广播，做一些计步数据库的初始化操作，
 * 1. 重启后，系统记步数会从0开始，
 * 所以需要在数据库base_step表内添加一条base数据，
 * 后续的记录会在此数据基础上进行统计
 *
 * @author xujichang
 * <p>
 * created by 2018/8/17-上午10:21
 */
public class SystemStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogTool.d("接收到广播：action - " + action);
        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            LogTool.d("关机...");
            //关机前 保存数据
            StepCenter.saveDayCount();
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            LogTool.d("开机...");
            //开机时 重置记步数
            StepCenter.saveDayCount();
        }
    }
}
