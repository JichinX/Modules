package me.xujichang.step.counter;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午7:42
 */
public class StepCounter extends BaseCounter {

    public StepCounter(Application application) {
        super(application);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_STEP_DETECTOR) {
            //检测一次计步
            if (event.values[0] == 1.0) {
                doOnceStep(1);
            }
        } else if (type == Sensor.TYPE_STEP_COUNTER) {
            //统计
            doCountStep(event.values[0]);
        }
    }
}
