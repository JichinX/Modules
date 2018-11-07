package me.xujichang.step.center;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import me.xujichang.step.counter.BaseCounter;
import me.xujichang.step.counter.CounterListener;
import me.xujichang.step.counter.StepAccCounter;
import me.xujichang.step.counter.StepCounter;
import me.xujichang.util.tool.LogTool;

/**
 * Des:作为计步的统筹类，作用是让外界对传感器得我使用无感
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午4:58
 */
public class StepCenter implements ICenter {
    private static volatile boolean isSensorOK = false;
    private SensorManager manager;
    private BaseCounter counter;

    public static void saveDayCount() {
        instance().counter.onSystemShutDownOrReboot();
    }

    //-====================================单例 START===========================================================
    private static class Holder {
        static StepCenter instance = new StepCenter();
    }

    private StepCenter() {

    }

    public static StepCenter instance() {
        return Holder.instance;
    }
    //-====================================单例 END=======================================================

    /**
     * 初始化加速度传感器
     *
     * @param application
     */
    private void initAccSensor(Application application) {
        LogTool.d("使用加速度传感器...");
        if (null == manager) {
            manager = getSensorManager(application);
        }
        if (isSensorEnable(application, Sensor.TYPE_ACCELEROMETER)) {
            Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            counter = new StepAccCounter(application);
            manager.registerListener(counter, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    /**
     * 初始化计步传感器
     *
     * @param application
     */
    private void initStepSensor(Application application) {
        LogTool.d("使用计步传感器...");
        if (null == manager) {
            manager = getSensorManager(application);
        }
        Sensor mStepCount = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor mStepDoctor = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        counter = new StepCounter(application);
        manager.registerListener(counter, mStepDoctor, SensorManager.SENSOR_DELAY_FASTEST);
        manager.registerListener(counter, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private static boolean isSensorEnable(Application application, int type) {
        SensorManager sensorManager = getSensorManager(application);
        return sensorManager.getDefaultSensor(type) != null;
    }

    private static SensorManager getSensorManager(Application application) {
        return (SensorManager) application.getSystemService(Context.SENSOR_SERVICE);
    }


    public static void cancelListener(Application application) {
        instance().cancelListener();
    }

    private void cancelListener() {
        if (isSensorOK) {
            manager.unregisterListener(counter);
        }
    }
    //========================================对使用者暴露的方法=============================================

    /**
     * 初始化
     * 判断使用环境
     * 决定使用系统计步传感器 还是加速度传感器
     *
     * @param application
     */
    public static void init(Application application) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isSensorOK = isSensorEnable(application, Sensor.TYPE_STEP_COUNTER) && (isSensorEnable(application, Sensor.TYPE_STEP_COUNTER));
        } else {
            isSensorOK = false;
        }

        if (isSensorOK) {
            //       计步传感器
            instance().initStepSensor(application);
        } else {
            //       加速度传感器
            instance().initAccSensor(application);
        }
    }

    @Override
    public void startCounter() {
        counter.startTempCounter();
    }

    @Override
    public int stopCounter() {
        return counter.stopTempCounter();
    }

    @Override
    public int getStepCount() {
        return counter.getStepCount()[0];
    }

    @Override
    public int getTempStepCount() {
        return counter.getStepCount()[1];
    }

    @Override
    public int getStepCount(long start, long stop) {
        return 0;
    }

    public void registerCounterListener(CounterListener listener) {
        counter.setCounterListener(listener);
    }
}
