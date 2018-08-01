package com.codvision.background.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Project: Modules
 * Des：后台任务执行记录表
 *
 * @author xujichang
 * created by 2018/7/26 - 1:58 PM
 */
@Entity(tableName = Record.TABLE_NAME)
public class Record {
    public static final String TABLE_NAME = "tbl_job_record";
    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 任务标志
     */
    private String tag;
    /**
     * 任务ID
     */
    private int jobid;
    /**
     * Interval间隔
     */
    private long intervalMs;
    /**
     * flex间隔
     */
    private long flexMs;
    /**
     * 任务开始时间
     */
    private String startTime;
    /**
     * 任务结束时间
     */
    private String endTime;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public long getIntervalMs() {
        return intervalMs;
    }

    public void setIntervalMs(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    public long getFlexMs() {
        return flexMs;
    }

    public void setFlexMs(long flexMs) {
        this.flexMs = flexMs;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
