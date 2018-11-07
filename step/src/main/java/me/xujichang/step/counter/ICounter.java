package me.xujichang.step.counter;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午7:43
 */
public interface ICounter {
    /**
     * 开始单次计步统计
     */
    void startTempCounter();

    /**
     * 停止此次单次计步
     * 返回统计的记步数
     *
     * @return
     */
    int stopTempCounter();

    /**
     * 计步的实时回调
     *
     * @param listener
     */
    void setCounterListener(CounterListener listener);

    /**
     * 获取当前记步数
     * 返回的数组
     * int[0] 总记步数
     * int[1] 单次计步
     *
     * @return
     */
    int[] getStepCount();

    /**
     * 获取总记步数
     *
     * @return
     */
    int getAllStepCount();

    /**
     * 获取本次单次记步数
     *
     * @return
     */
    int getTempStepCount();

    /**
     * 关机时保存当前记步数
     */
    void onSystemShutDownOrReboot();
}
