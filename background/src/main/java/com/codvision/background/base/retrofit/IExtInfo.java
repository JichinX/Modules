package com.codvision.background.base.retrofit;

import java.util.Map;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/27 - 10:01 AM
 */
interface IExtInfo {
    /**
     * 获取扩展信息 的值
     *
     * @return
     */
    Map<String, String> getValues();

    /**
     * 获取扩展信息 位置
     *
     * @return
     */
    BaseExtInfo.ValuePos getPos();
}