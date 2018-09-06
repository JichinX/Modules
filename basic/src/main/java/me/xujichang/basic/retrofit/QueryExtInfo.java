package me.xujichang.basic.retrofit;

import java.util.Map;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/27 - 10:19 AM
 */
public class QueryExtInfo extends BaseExtInfo {
    public QueryExtInfo(Map<String, String> values) {
        super(values, ValuePos.Query);
    }
}
