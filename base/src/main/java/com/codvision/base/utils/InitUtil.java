package com.codvision.base.utils;

import android.app.Application;

import com.codvision.base.ext.IBaseInit;

/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/19 - 9:10 PM
 */
public class InitUtil {
    public static void initModulesSpeed(String[] inits, Application application) {
        try {
            for (String init : inits) {
                Class<?> clazz = Class.forName(init);
                IBaseInit baseInit = (IBaseInit) clazz.newInstance();
                baseInit.onInitSpeed(application);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void initModulesLow(String[] inits, Application application) {
        try {
            for (String init : inits) {
                Class<?> clazz = Class.forName(init);
                IBaseInit baseInit = (IBaseInit) clazz.newInstance();
                baseInit.onInitLow(application);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
