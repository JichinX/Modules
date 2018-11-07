package me.xujichang.step.counter;

import android.app.Application;
import android.hardware.SensorEvent;

/**
 * Des: 作为传感器的监听，处理传感器数据
 * 主要负责对传感器的数据进行数据库存储
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午7:42
 */
public class StepAccCounter extends BaseCounter implements IDetectorListener {
    private AccDetector detector;
    private int baseStep;
    private long timeOfLastPeak;
    private long timeOfThisPeak;
    private int count;
    private int mCount;

    public StepAccCounter(Application application) {
        super(application);
        detector = new AccDetector(this);
        baseStep = getAllStepCount();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        detector.onSensorChanged(event);
    }

    @Override
    public void onOnceStep() {
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        if (this.timeOfThisPeak - this.timeOfLastPeak <= 3_000L) {
            if (this.count < 9) {
                this.count++;
            } else if (this.count == 9) {
                this.count++;
                this.mCount += this.count;
                doOnceStep(mCount);
                doCountStep(baseStep + mCount);
            } else {
                this.mCount++;
                doOnceStep(1);
                doCountStep(baseStep + mCount);
            }
        } else {//超时
            //为1,不是0
            this.count = 1;
        }
    }
}
