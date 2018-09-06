package me.xujichang.basic.retrofit;

import java.util.Map;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/27 - 9:56 AM
 */
public class HeaderExtInfo extends BaseExtInfo {

    public HeaderExtInfo(Map<String, String> values) {
        super(values, ValuePos.Headers);
    }

}
