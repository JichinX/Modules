//package com.codvision.check.services;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//import com.codvision.check.CheckInit;
//import com.codvision.check.test.CheckConst;
//import com.codvision.check.api.CommonApi;
//import com.codvision.check.bean.LocationUpload;
//
//import java.util.Date;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import me.xujichang.basic.wrapper.WrapperEntity;
//import me.xujichang.util.retrofit.RetrofitManager;
//import me.xujichang.util.simple.SilentResourceObserver;
//import me.xujichang.util.tool.LogTool;
//
///**
// * @author xujichang
// */
//public class UploadLocationService extends Service {
//
//    private static final String CHANNEL_ID = "101";
//    private static final CharSequence CHANNEL_NAME = "we";
//    private int wzsbqssj = 0;
//    private int wzsbjzsj = 240000;
//    private int wzsbjg = 30000;
//    private int yxdwjl = 200;
//    private boolean uploadEnabled = true;
//    private Location lastUploadLocation;
//    private long lastUploadTime = 0;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        LogTool.d("====== onStartCommand ======");
//        NotificationChannel channel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
//                    NotificationManager.IMPORTANCE_HIGH);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.createNotificationChannel(channel);
//
//            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
//            startForeground(1, notification);
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (uploadEnabled) {
//
//                    try {
//                        Thread.sleep(wzsbjg);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                    Date date = new Date(System.currentTimeMillis());
//                    int n = date.getHours() * 10000 + date.getMinutes() * 100 + date.getSeconds();
//                    if (n < wzsbqssj || n > wzsbjzsj) {
//                        LogTool.d("not in work time");
//                        continue;
//                    }
//
//                    Location location = CheckInit.location;
//                    if (null != location) {
//                        if (null != lastUploadLocation) {
//                            if (LocationUtils.isBetterLocation(location, lastUploadLocation, yxdwjl)) {
//                                LogTool.d("Got Better Location");
//                                float distance = location.distanceTo(lastUploadLocation);
//                                if (10 > distance) {
//                                    LogTool.d("距离不到10米：" + distance);
//                                    if (System.currentTimeMillis() - lastUploadTime < 300000) {
//                                        continue;
//                                    }
//                                }
//                                upload(location);
//                            } else if (System.currentTimeMillis() - lastUploadTime >= 300000) {
//                                upload(lastUploadLocation);
//                            }
//                        } else {
//                            upload(location);
//                        }
//                    } else {
//                        LogTool.d("currentBestLocation is null");
//                    }
//                }
//
//
//            }
//        }).start();
//    }
//
//    @Override
//    public void onDestroy() {
//        uploadEnabled = false;
//        if (Build.VERSION.SDK_INT >= 26) {
//            stopForeground(true);
//        }
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    private void upload(final Location location) {
//        LogTool.d("上传位置:" + location);
//        SilentResourceObserver<WrapperEntity> observer = new SilentResourceObserver<WrapperEntity>(null) {
//            @Override
//            public void onNext(WrapperEntity wrapper) {
//                if (wrapper.getCode() == 200) {
//                    lastUploadLocation = location;
//                    lastUploadTime = System.currentTimeMillis();
//                }
//            }
//        };
//        LocationUpload locationUpload = new LocationUpload();
//        locationUpload.setKey(DeviceUtils.getAndroidID());
//        locationUpload.setLat(location.getLatitude());
//        locationUpload.setLng(location.getLongitude());
//        locationUpload.setType(CheckConst.LOCATION_UPLOAD_TYPE);
//        RetrofitManager.getOurInstance()
//                .createReq(CommonApi.class)
//                .uploadGps(CheckConst.LOCATION_UPLOAD_PATH, locationUpload)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//}
