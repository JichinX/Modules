package com.codvision.check.test;

import com.codvision.check.CheckInit;

import me.xujichang.basic.BaseInit;
import me.xujichang.ui.UIInit;

/**
 * Project: accidenthandling
 * Des:
 *
 * @author xujichang
 * created by 2018/7/17 - 10:56 AM
 */
public class CheckConst {

    public static final class Modules {
        public static String[] inits = {BaseInit.INIT_TAG, CheckInit.INIT_TAG, UIInit.INIT_TAG};
    }
}
