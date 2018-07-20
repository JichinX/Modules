package com.codvision.vpn;

import com.sangfor.ssl.LoginResultListener;

/**
 * Project: Platform
 * Des:
 *
 * @author xujichang
 * created by 2018/7/20 - 11:02 AM
 */
public interface IResultListener extends LoginResultListener {
    void onLoginError(String msg);
}
