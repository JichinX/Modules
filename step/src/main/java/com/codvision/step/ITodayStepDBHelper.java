package com.codvision.step;

import android.content.Context;

import com.codvision.step.bean.TodayStepData;

import java.util.List;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/22
 * @desc :
 */

public interface ITodayStepDBHelper {

    void createTable();

    void deleteTable();

    void clearCapacity(String curDate, int limit);

    boolean isExist(TodayStepData todayStepData);

    void insert(TodayStepData todayStepData);

    void delete(String date, Context context);

    List<TodayStepData> getQueryAll();

    List<TodayStepData> getStepListByDate(String dateString);

    List<TodayStepData> getStepListByStartDateAndDays(String startDate, int days);

    int getCurrentStep(String date);
}
