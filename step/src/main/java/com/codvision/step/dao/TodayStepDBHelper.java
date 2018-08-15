package com.codvision.step.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codvision.step.ITodayStepDBHelper;
import com.codvision.step.bean.StepData;
import com.codvision.step.utils.DateUtils;
import com.codvision.step.utils.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用来记录当天步数列表，传感器回调30次记录一条数据
 * Created by jiahongfei on 2017/10/9.
 */

public class TodayStepDBHelper extends SQLiteOpenHelper implements ITodayStepDBHelper {

    private int istep;
    private static final String TAG = "TodayStepDBHelper";

    private static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TodayStepDB.db";
    private static final String TABLE_NAME = "TodayStepData";
    private static final String PRIMARY_KEY = "_id";
    public static final String TODAY = "today";
    public static final String DATE = "date";
    public static final String STEP = "step";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TODAY + " TEXT, "
            + DATE + " long, "
            + STEP + " long);";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_QUERY_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_QUERY_STEP = "SELECT * FROM " + TABLE_NAME + " WHERE " + TODAY + " = ? AND " + STEP + " = ?";
    private static final String SQL_QUERY_STEP_BY_DATE = "SELECT * FROM " + TABLE_NAME + " WHERE " + TODAY + " = ?";
    private static final String SQL_DELETE_TODAY = "DELETE FROM " + TABLE_NAME + " WHERE " + TODAY + " = ?";

    //只保留mLimit天的数据
    private int mLimit = -1;

    public static ITodayStepDBHelper factory(Context context){
        return new TodayStepDBHelper(context);
    }

    private TodayStepDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Logger.e(TAG, SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable();
        onCreate(db);
    }

    @Override
    public synchronized boolean isExist(StepData stepData) {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_STEP, new String[]{stepData.getToday(), stepData.getStep() + ""});
        boolean exist = cursor.getCount() > 0 ? true : false;
        cursor.close();
        return exist;
    }

    @Override
    public synchronized void createTable() {
        getWritableDatabase().execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public synchronized void insert(StepData stepData) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TODAY, stepData.getToday());
        contentValues.put(DATE, stepData.getDate());
        contentValues.put(STEP, stepData.getStep());
        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public synchronized void delete(String date, Context context) {
        ContentValues values = new ContentValues();
        values.put(STEP,0L);
        getWritableDatabase().update(TABLE_NAME,values,"today = ?",new String[]{date});
        Log.d("0000",getQueryAll().toString());
        PreferencesHelper.cleanStep(context);

    }

    @Override
    public synchronized List<StepData> getQueryAll() {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_ALL, new String[]{});
        List<StepData> stepData = getTodayStepDataList(cursor);
        cursor.close();
        return stepData;
    }

    /**
     * 根据时间获取步数列表
     *
     * @param dateString 格式yyyy-MM-dd
     * @return
     */
    @Override
    public synchronized List<StepData> getStepListByDate(String dateString) {
        Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_STEP_BY_DATE, new String[]{dateString});
        List<StepData> stepData = getTodayStepDataList(cursor);
        cursor.close();
        return stepData;
    }

    /**
     * 根据时间和天数获取步数列表
     * 例如：
     * lastDate = 2018-01-15
     * days = 3
     * 获取 2018-01-15、2018-01-14、2018-01-13三天的步数
     *
     * @param lastDate 格式yyyy-MM-dd
     * @param days
     * @return
     */
    @Override
    public synchronized List<StepData> getStepListByStartDateAndDays(String lastDate, int days) {
        List<StepData> stepData = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateUtils.getDateMillis(lastDate, DATE_PATTERN_YYYY_MM_DD));
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Cursor cursor = getReadableDatabase().rawQuery(SQL_QUERY_STEP_BY_DATE,
                    new String[]{DateUtils.dateFormat(calendar.getTimeInMillis(), DATE_PATTERN_YYYY_MM_DD)});
            stepData.addAll(getTodayStepDataList(cursor));
            cursor.close();
        }
        return stepData;
    }

    @Override
    public int getCurrentStep(String date) {
        Cursor cursor = getWritableDatabase().query(TABLE_NAME,new String[]{STEP},"today = ?",new String[]{date},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String step = cursor.getString(cursor.getColumnIndex(STEP));
                istep = Integer.valueOf(step);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return istep;
    }

    private List<StepData> getTodayStepDataList(Cursor cursor) {

        List<StepData> stepData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String today = cursor.getString(cursor.getColumnIndex(TODAY));
            long date = cursor.getLong(cursor.getColumnIndex(DATE));
            long step = cursor.getLong(cursor.getColumnIndex(STEP));
            StepData stepData = new StepData();
            stepData.setToday(today);
            stepData.setDate(date);
            stepData.setStep(step);
            stepData.add(stepData);
        }
        return stepData;
    }

    /**
     * 根据limit来清除数据库
     * 例如：
     * curDate = 2018-01-10 limit=0;表示只保留2018-01-10
     * curDate = 2018-01-10 limit=1;表示保留2018-01-10、2018-01-09等
     * @param curDate
     * @param limit -1失效
     */
    @Override
    public synchronized void clearCapacity(String curDate, int limit) {
        mLimit = limit;
        if (mLimit <= 0) {
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateUtils.getDateMillis(curDate, DATE_PATTERN_YYYY_MM_DD));
            calendar.add(Calendar.DAY_OF_YEAR, -(mLimit));
            String date = DateUtils.dateFormat(calendar.getTimeInMillis(), DATE_PATTERN_YYYY_MM_DD);
            Log.e(TAG, date);

            List<StepData> stepDataList = getQueryAll();
            Set<String> delDateSet = new HashSet<>();
            for (StepData tmpStepData : stepDataList) {
                long dbTodayDate = DateUtils.getDateMillis(tmpStepData.getToday(), DATE_PATTERN_YYYY_MM_DD);
                if (calendar.getTimeInMillis() >= dbTodayDate) {
                    delDateSet.add(tmpStepData.getToday());
                }
            }

            for (String delDate : delDateSet) {
                getWritableDatabase().execSQL(SQL_DELETE_TODAY, new String[]{delDate});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteTable() {
        getWritableDatabase().execSQL(SQL_DELETE_TABLE);
    }

}
