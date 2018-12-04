package com.codvision.check.fun.factory;

import com.codvision.check.fun.bean.FunType;

/**
 * Des:工厂类
 *
 * @author xujichang
 * Created on 2018/11/20 - 15:20
 */
public interface IFunFactory {
    public IFun createFunForWeb(FunType type);
}
