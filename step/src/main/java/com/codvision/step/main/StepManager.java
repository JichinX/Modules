package com.codvision.step.main;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.codvision.step.IStepService;
import com.codvision.step.TodayStepManager;
import com.codvision.step.service.StepService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 计步模块的入口
 * <p>
 * 使用时建议在Application初始化
 * 并且在使用到计步功能的页面首先判断服务是否链接
 */
public class StepManager {
    /**
     * log 标志位
     */
    private static String TAG = "StepManager";
    /**
     * 通信Service
     */
    private static IStepService iSportStepInterface;
    /**
     * 上下文
     */
    private static WeakReference<Application> applicationWeakReference;

    private static final int REFRESH_STEP_WHAT = 0;

    //循环取当前时刻的步数中间的间隔时间
    private static long TIME_INTERVAL_REFRESH = 500;

    private static Handler mDelayHandler = new Handler(new TodayStepCounterCall());
    private static int mStepSum;
    private static StepManager stepManager = new StepManager();

    private static String step2;

    public void init(Application application) {
        applicationWeakReference = new WeakReference<>(application);
        TodayStepManager.init(application);

        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(application, StepService.class);
        application.startService(intent);
        application.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = IStepService.Stub.asInterface(service);
                try {
                    //获取当前步数
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //每隔500毫秒获取一次计步数据刷新UI
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    static class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param context:通过getContext()获得
     * @param application:通过getApplication()获得 获取数据库中的所有步数
     * @return 返回一个String类型的list，得到里面的数据只需要获取list的长度，然后根据长度用for循环get数据就可以了
     */
    public static List<String> getAllStep(Context context, Application application) {
        mContext = context;
        mApplication = application;
        stepManager.init();
        List<String> list = new ArrayList<>();
        if (null != iSportStepInterface) {
            try {
                String stepArray = iSportStepInterface.getTodaySportStepArray();
                JSONArray jsonArray = new JSONArray(stepArray);
                JSONObject finalJsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i - 1);

                    String date = jsonObject.getString("today");
                    String date2 = jsonObject2.getString("today");

                    //如果这条数据和下一条数据的日期不一样，那么记录这条数据的步数
                    if (!date.equals(date2)) {
                        String step = jsonObject2.getString("stepNum");
                        list.add(step);
                    }
                }
                list.add(finalJsonObject.getString("stepNum"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 根据日期获取步数
     *
     * @param context:通过getContext()获得
     * @param application:通过getApplication()获得
     * @param date                             传入的日期，格式：例2018-08-02
     * @return
     */
    public static String getStepByDate(Context context, Application application, String date) {
        mContext = context;
        mApplication = application;
        stepManager.init();
        String step = "";
        if (null != iSportStepInterface) {
            try {
                String stepArray = iSportStepInterface.getTodaySportStepArrayByDate(date);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(stepArray);
                    JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                    step = jsonObject.getString("stepNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return step;
    }

    /**
     * 根据日期清零步数
     *
     * @param context:通过getContext()获得
     * @param application:通过getApplication()获得
     * @param date                             传入的日期，格式：例2018-08-02
     * @return
     */
    public static void cleanToZero(Context context, Application application, String date) {
        mContext = context;
        mApplication = application;
        stepManager.init();
        if (null != iSportStepInterface) {
            try {
                iSportStepInterface.cleanStep(date);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据时间和天数获取步数列表
     * 例如：
     * lastDate = 2018-01-15
     * days = 3
     * 获取 2018-01-15、2018-01-14、2018-01-13三天的步数
     *
     * @param context:通过getContext()获得
     * @param application:通过getApplication()获得
     * @param date                             传入的日期，格式：例2018-08-02
     * @param days                             天数
     * @return 步数
     */
    public static String getStepByDateAndDays(Context context, Application application, final String date, final int days) {
        mContext = context;
        mApplication = application;
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                final Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stepManager.init();
                    }
                });
                thread2.start();
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (null != iSportStepInterface) {
                    try {
                        String stepArray = iSportStepInterface.getTodaySportStepArrayByStartDateAndDays(date, days);
                        try {
                            JSONArray jsonArray = new JSONArray(stepArray);
                            for (int i = jsonArray.length() - 1; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                step2 = jsonObject.getString("stepNum");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();
        return step2;
    }


}
