package com.codvision.step;

import android.content.Context;

import com.codvision.step.bean.StepData;

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

    boolean isExist(StepData stepData);

    void insert(StepData stepData);

    void delete(String date, Context context);

    List<StepData> getQueryAll();

    List<StepData> getStepListByDate(String dateString);

    List<StepData> getStepListByStartDateAndDays(String startDate, int days);

    int getCurrentStep(String date);
}
