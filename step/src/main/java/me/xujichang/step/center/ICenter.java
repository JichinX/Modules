package me.xujichang.step.center;

import android.util.SparseArray;

/**
 * Des:定义基本方法
 *
 * @author xujichang
 * <p>
 * created by 2018/8/16-下午7:48
 */
public interface ICenter {
    /**
     * 针对单次计步 开始计步
     */
    void startCounter();

    /**
     * 针对单次计步 停止计步 并返回 记步数
     *
     * @return 从start 到stop 之间的记步数
     */
    int stopCounter();

    /**
     * 获取当天0点到目前时间点的记步数
     *
     * @return
     */
    int getStepCount();

    /**
     * 获取单次计数
     *
     * @return
     */
    int getTempStepCount();

    /**
     * 获取 起止时间段内的记步数
     *
     * @param start
     * @param stop
     * @return
     */
    int getStepCount(long start, long stop);
}
