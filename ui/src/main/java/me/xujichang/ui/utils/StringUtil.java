package me.xujichang.ui.utils;

import java.util.List;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/8/28-上午11:33
 */
public class StringUtil {
    public static String connectStr(List<String> strings) {

        return connectStr(strings, ",");
    }

    public static String connectStr(List<String> strings, String flag) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string).append(flag);
        }
        builder.deleteCharAt(builder.lastIndexOf(flag));
        return builder.toString();
    }
}
