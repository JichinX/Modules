package me.xujichang.step.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/20-下午4:33
 */
@Entity(tableName = "tbl_day_step")
public class DayCount {
    /**
     * 当天日期
     */
    @PrimaryKey
    private long dayMs;
    /**
     * 基础记步数(系统)
     */
    private int baseSc;
    /**
     * 当天结束时(系统) 记步数
     */
    private int finishSc;
    /**
     * 目前为止的记步数(系统)
     */
    private int currentSc;
    /**
     * 到目前为止的记步数(day) 在零点时即当天的记步数
     */
    private int currentDaySc;
    /**
     * 是否重启过
     */
    private boolean isReboot = false;
    /**
     * 关机时的记步数
     */
    private int beforeSdSc;


    public long getDayMs() {
        return dayMs;
    }

    public void setDayMs(long dayMs) {
        this.dayMs = dayMs;
    }

    public int getBaseSc() {
        return baseSc;
    }

    public void setBaseSc(int baseSc) {
        this.baseSc = baseSc;
    }

    public int getFinishSc() {
        return finishSc;
    }

    public void setFinishSc(int finishSc) {
        this.finishSc = finishSc;
    }

    public int getCurrentSc() {
        return currentSc;
    }

    public void setCurrentSc(int currentSc) {
        this.currentSc = currentSc;
    }

    public int getCurrentDaySc() {
        return currentDaySc;
    }

    public void setCurrentDaySc(int currentDaySc) {
        this.currentDaySc = currentDaySc;
    }

    public boolean isReboot() {
        return isReboot;
    }

    public void setReboot(boolean reboot) {
        isReboot = reboot;
    }

    public int getBeforeSdSc() {
        return beforeSdSc;
    }

    public void setBeforeSdSc(int beforeSdSc) {
        this.beforeSdSc = beforeSdSc;
    }

    @Override
    public String toString() {
        return "DayCount{" +
                "dayMs=" + dayMs +
                ", baseSc=" + baseSc +
                ", finishSc=" + finishSc +
                ", currentSc=" + currentSc +
                ", currentDaySc=" + currentDaySc +
                ", isReboot=" + isReboot +
                ", beforeSdSc=" + beforeSdSc +
                '}';
    }
}
