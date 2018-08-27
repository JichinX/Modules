package me.xujichang.step.counter;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午8:36
 */
public interface CounterListener {
    void onStepCount(float sum);

    void onStepOnce(int step);

    void onStepTemp(int count);
}
