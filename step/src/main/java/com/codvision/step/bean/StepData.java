package com.codvision.step.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class StepData implements Serializable, Parcelable {

    /**
     * 当天时间，只显示到天 yyyy-MM-dd
     */
    private String today;
    /**
     * 步数时间，显示到毫秒
     */
    private long date;
    /**
     * 对应date时间的步数
     */
    private long step;

    public StepData() {

    }

    protected StepData(Parcel in) {
        today = in.readString();
        date = in.readLong();
        step = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(today);
        dest.writeLong(date);
        dest.writeLong(step);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StepData> CREATOR = new Creator<StepData>() {
        @Override
        public StepData createFromParcel(Parcel in) {
            return new StepData(in);
        }

        @Override
        public StepData[] newArray(int size) {
            return new StepData[size];
        }
    };

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    @Override
    public String toString() {
        return "StepData{" +
                ", today=" + today +
                ", date=" + date +
                ", step=" + step +
                '}';
    }
}
