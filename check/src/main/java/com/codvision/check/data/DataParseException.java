package com.codvision.check.data;

/**
 * Des:数据解析问题
 *
 * @author xjc
 * Created on 2017/11/27 14:40.
 */

public class DataParseException extends RuntimeException {
    public DataParseException(Class k, String data) {
        super(String.format("Can't parse data:[ %s ] to the bean:%s", data, k.getSimpleName()));
    }
}
