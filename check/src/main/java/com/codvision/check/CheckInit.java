package com.codvision.check;

import android.app.Application;
import android.location.Location;

import com.codvision.check.api.CommonApi;
import com.codvision.check.bean.LocationUpload;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xujichang.basic.IBaseInit;
import me.xujichang.basic.services.Task;
import me.xujichang.basic.services.TaskCenter;
import me.xujichang.basic.wrapper.WrapperEntity;
import me.xujichang.util.retrofit.RetrofitManager;
import me.xujichang.util.simple.SilentResourceObserver;
import me.xujichang.util.thirdparty.DeviceUtils;
import me.xujichang.util.thirdparty.LocationUtils;
import me.xujichang.util.thirdparty.Utils;
import me.xujichang.util.tool.LogTool;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/23 - 9:15 AM
 */
public class CheckInit implements IBaseInit {
    public static final String INIT_TAG = "com.codvision.check.CheckInit";
    public static Location location;
    private Location lastUploadLocation;
    private long lastUploadTime = 0;
    private static int wzsbqssj = 0;
    private static int wzsbjzsj = 240000;
    private static int wzsbjg = 30000;
    private static int yxdwjl = 200;

    @Override
    public boolean onInitSpeed(Application application) {
//        BaseConst.enablePacketService = false;
        Task uploadTask = new Task();
        uploadTask.setRunnable(new UploadLocationRunnable());
        TaskCenter.push(uploadTask);
        Utils.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
//        Intent service = new Intent(application, UploadLocationService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            application.startForegroundService(service);
//        } else {
//            application.startService(service);
//        }
        return false;
    }

    private class UploadLocationRunnable implements Runnable {
        @Override
        public void run() {

            Date date = new Date(System.currentTimeMillis());
            int n = date.getHours() * 10000 + date.getMinutes() * 100 + date.getSeconds();
            if (n < wzsbqssj || n > wzsbjzsj) {
                LogTool.d("not in work time");
                return;
            }

            if (null != location) {
                if (null != lastUploadLocation) {
                    if (LocationUtils.isBetterLocation(location, lastUploadLocation, yxdwjl)) {
                        LogTool.d("Got Better Location");
                        float distance = location.distanceTo(lastUploadLocation);
                        if (10 > distance) {
                            LogTool.d("距离不到10米：" + distance);
                            if (System.currentTimeMillis() - lastUploadTime < 300000) {
                                return;
                            }
                        }
                        upload(location);
                    } else if (System.currentTimeMillis() - lastUploadTime >= 300000) {
                        upload(lastUploadLocation);
                    }
                } else {
                    upload(location);
                }
            } else {
                LogTool.d("currentBestLocation is null");
            }
        }
    }

    private void upload(final Location location) {
        LogTool.d("上传位置:" + location);
        SilentResourceObserver<WrapperEntity> observer = new SilentResourceObserver<WrapperEntity>(null) {
            @Override
            public void onNext(WrapperEntity wrapper) {
                if (wrapper.getCode() == 200) {
                    lastUploadLocation = location;
                    lastUploadTime = System.currentTimeMillis();
                }
            }
        };
        LocationUpload locationUpload = new LocationUpload();
        locationUpload.setKey(DeviceUtils.getAndroidID());
        locationUpload.setLat(location.getLatitude());
        locationUpload.setLng(location.getLongitude());
        locationUpload.setType(CheckConst.LOCATION_UPLOAD_TYPE);
        RetrofitManager.getOurInstance()
                .createReq(CommonApi.class)
                .uploadGps(CheckConst.LOCATION_UPLOAD_PATH, locationUpload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
