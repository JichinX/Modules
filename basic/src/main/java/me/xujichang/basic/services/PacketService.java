package me.xujichang.basic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import me.xujichang.basic.BaseConst;
import me.xujichang.basic.BaseInit;

/**
 * 后台类似于心跳包的Service
 * 单位为分钟 去取任务队列中的任务，并分别执行
 *
 * @author xujichang
 */
public class PacketService extends Service {
    private static final String CHANNEL_ID = "100";
    private static final CharSequence CHANNEL_NAME = "PacketService";
    private static ThreadPoolExecutor poolExecutor;
    private static volatile boolean isDestroy = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            startForeground(1, notification);
        }
        //被Kill掉还会重启
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //创建线程池
        poolExecutor = new ThreadPoolExecutor(BaseInit.MODULE_SIZE, BaseInit.MODULE_SIZE * 3 / 2, BaseInit.PACKET_DURATION, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new Factory());
        //开启无限循环线程
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new LoopRunnable());
    }

    class Factory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "PacketService  Task#" + mCount.getAndIncrement());
        }
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
        super.onDestroy();
    }

    private class LoopRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (!isDestroy) {
                    TaskCenter.executeTasks(poolExecutor);
                    Thread.sleep(BaseInit.PACKET_DURATION);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
