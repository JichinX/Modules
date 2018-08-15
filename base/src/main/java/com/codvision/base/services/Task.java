package com.codvision.base.services;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/7-下午8:26
 */
public class Task {
    /**
     * 该任务 应该执行的方法
     */
    private Runnable runnable;

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
