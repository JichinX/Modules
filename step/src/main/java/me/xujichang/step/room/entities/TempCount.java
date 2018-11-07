package me.xujichang.step.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Des:
 * 单次计步记录
 *
 * @author xujichang
 * <p>
 * created by 2018/8/20-下午3:56
 */
@Entity(tableName = "tbl_temp_step")
public class TempCount {
    /**
     * 开始时间
     */
    private long startMs;
    /**
     * 结束时间
     */
    private long stopMs;
    /**
     * 计步开始步数
     */
    private int baseSc;
    /**
     * 本次计步数
     */
    private int currentSc;
    /**
     * 计步结束时的记步数
     */
    private int endSc;
    /**
     * 本次记录的唯一标志
     */
    @PrimaryKey
    private long flag;

    public long getStartMs() {
        return startMs;
    }

    public void setStartMs(long startMs) {
        this.startMs = startMs;
    }

    public long getStopMs() {
        return stopMs;
    }

    public void setStopMs(long stopMs) {
        this.stopMs = stopMs;
    }

    public int getBaseSc() {
        return baseSc;
    }

    public void setBaseSc(int baseSc) {
        this.baseSc = baseSc;
    }

    public int getCurrentSc() {
        return currentSc;
    }

    public void setCurrentSc(int currentSc) {
        this.currentSc = currentSc;
    }

    public int getEndSc() {
        return endSc;
    }

    public void setEndSc(int endSc) {
        this.endSc = endSc;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public void increaseStep(int step) {
        currentSc += step;
    }
}
