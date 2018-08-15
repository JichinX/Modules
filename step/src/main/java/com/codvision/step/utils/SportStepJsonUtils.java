package com.codvision.step.utils;


import com.codvision.step.bean.StepData;
import com.codvision.step.dao.TodayStepDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/31
 * @desc : 用于解析和生成运动步数Json字符串
 */

public class SportStepJsonUtils {

    public static final String SPORT_DATE = "sportDate";
    public static final String STEP_NUM = "stepNum";
    public static final String DISTANCE = "km";
    public static final String CALORIE = "kaluli";
    public static final String TODAY = TodayStepDBHelper.TODAY;

    public static JSONArray getSportStepJsonArray(List<StepData> stepDataArrayList) {
        JSONArray jsonArray = new JSONArray();
        if (null == stepDataArrayList || 0 == stepDataArrayList.size()) {
            return jsonArray;
        }
        for (int i = 0; i < stepDataArrayList.size(); i++) {
            StepData stepData = stepDataArrayList.get(i);
            try {
                JSONObject subObject = new JSONObject();
                subObject.put(TODAY, stepData.getToday());
                subObject.put(SPORT_DATE, stepData.getDate());
                subObject.put(STEP_NUM, stepData.getStep());
                subObject.put(DISTANCE, getDistanceByStep(stepData.getStep()));
                subObject.put(CALORIE, getCalorieByStep(stepData.getStep()));
                jsonArray.put(subObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    // 公里计算公式
    public static String getDistanceByStep(long steps) {
        return String.format("%.2f", steps * 0.6f / 1000);
    }

    // 千卡路里计算公式
    public static String getCalorieByStep(long steps) {
        return String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    }


}
