package me.xujichang.step.counter;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;

import me.xujichang.step.room.dao.StepDao;
import me.xujichang.step.room.database.StepCountDatabase;
import me.xujichang.step.room.entities.DayCount;
import me.xujichang.step.room.entities.TempCount;
import me.xujichang.step.utils.TimeUtil;
import me.xujichang.util.tool.LogTool;

/**
 * Des:主要处理除计步外的操作
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午8:15
 */
public abstract class BaseCounter implements SensorEventListener, ICounter {
    static boolean isTempEnable = false;
    protected TempCount tempCount;
    private Application application;
    private CounterListener counterListener;

    public BaseCounter(Application application) {
        this.application = application;
    }

    @Override
    public void startTempCounter() {
        isTempEnable = true;
        initTempCount();
    }

    private void initTempCount() {
        long current = System.currentTimeMillis();
        long dateMs = TimeUtil.formatDayMs();

        //获取Base记步数
        int base = stepDao().getCurrentSysTempCount(dateMs);
        //初始化 开始时的TempCount
        tempCount = new TempCount();
        tempCount.setBaseSc(base);
        tempCount.setFlag(current);
        tempCount.setStartMs(current);

        stepDao().startTempStep(tempCount);
    }

    private StepDao stepDao() {
        return StepCountDatabase.getInstance(application).stepDao();
    }

    @Override
    public int stopTempCounter() {
        isTempEnable = false;
        return getTempStepCount();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //精度改变
    }

    @Override
    public int[] getStepCount() {
        int[] steps = new int[2];
        steps[0] = getAllStepCount();
        steps[1] = getTempStepCount();
        return steps;
    }

    @Override
    public int getTempStepCount() {
        if (null == tempCount) {
            return 0;
        }
        return stepDao().getTempStepCount(tempCount.getFlag());
    }

    @Override
    public int getAllStepCount() {
        //从数据库中获取总计步数
        long dateMs = TimeUtil.formatDayMs();
        return stepDao().getStepCount(dateMs);
    }

    @Override
    public void setCounterListener(CounterListener listener) {
        counterListener = listener;
    }

    /**
     * 计步
     *
     * @param step
     */
    void doOnceStep(int step) {
        LogTool.d("又走了一步");
        if (isTempEnable) {
            if (null != counterListener) {
                counterListener.onStepTemp(step);
            }
            tempCount.increaseStep(step);
            stepDao().updateTempStep(tempCount);
        }
        if (null != counterListener) {
            counterListener.onStepOnce(step);
        }
    }

    /**
     * 总计步
     *
     * @param sum
     */
    void doCountStep(float sum) {
        LogTool.d("总共走了" + sum + "步");
        long dateMs = TimeUtil.formatDayMs();
        updateDayCount(dateMs, sum);
        if (isTempEnable) {
            tempCount.setEndSc((int) sum);
            stepDao().updateTempStep(tempCount);
        }
        if (null != counterListener) {
            counterListener.onStepCount(sum);
        }
    }

    private void updateDayCount(long dateMs, float sum) {
        int step = (int) sum;
        DayCount dayCount = stepDao()
                .getDayCount(dateMs);

        if (null == dayCount) {
            initDayCount(dateMs, step);
        } else {
            dayCount.setCurrentSc(step);
            dayCount.setReboot(false);
            //今天的记步数 = 系统的记步数-开始时的记步数+关机前的记步数
            int currentDayCount = step - dayCount.getBaseSc() + dayCount.getBeforeSdSc();
            dayCount.setCurrentDaySc(currentDayCount);
            stepDao().updateDayCount(dayCount);
        }
    }

    private void initDayCount(long dateMs, float sum) {
        int base = (int) sum;
        DayCount dayCount = new DayCount();
        dayCount.setBaseSc(base);
        dayCount.setCurrentSc(base);
        dayCount.setDayMs(dateMs);
        dayCount.setReboot(false);
        stepDao().addDayCount(dayCount);
    }

    @Override
    public void onSystemShutDownOrReboot() {
        long dateMs = TimeUtil.formatDayMs();
        //保存当前记步数
        DayCount dayCount = stepDao()
                .getDayCount(dateMs);
        if (null == dayCount) {
            LogTool.d("记录为 null");
            return;
        }
        if (dayCount.isReboot()) {
            LogTool.d("已经处理过");
            return;
        }
        if (dayCount.getCurrentSc() > 10) {
            LogTool.d("");
            return;
        }
        LogTool.d("前 ：" + dayCount.toString());
        int step = dayCount.getCurrentDaySc();
        dayCount.setReboot(true);
        dayCount.setBeforeSdSc(step);
        //因为关机前记步数 都给了before 所以 base 重置为0；
        dayCount.setBaseSc(0);
        stepDao().updateDayCount(dayCount);
        LogTool.d("后 ：" + dayCount.toString());
    }
}

