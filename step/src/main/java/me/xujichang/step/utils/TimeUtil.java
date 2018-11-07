package me.xujichang.step.utils;

import android.text.format.DateFormat;
import android.util.SparseArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/20-下午6:35
 */
public class TimeUtil {
    /**
     * 24小时制
     */
    public static final String default_format = "yyyy-MM-dd kk:mm:ss";
    public static final String date_format = "yyyy-MM-dd 00:00:01";
    /**
     * 按日期存储格式化过的 日期->ms
     */
    public static volatile SparseArray<Long> dateMsMap = new SparseArray<>();

    //TOOD  待开发
    public static long formatDayMs() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (dateMsMap.get(day) != null) {
            return dateMsMap.get(day);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        LogTool.d("当前时间 ：" + hour);
        String dataStr = String.format("%04d-%02d-%02d 00:00:01", year, month, day);
        long ms = getMsForStr(dataStr);
        dateMsMap.put(day, ms);
        return ms;
    }

    private static long getMsForStr(String str) {
        try {
            Date date = new SimpleDateFormat(date_format).parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
