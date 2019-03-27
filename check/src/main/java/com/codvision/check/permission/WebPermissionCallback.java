package com.codvision.check.permission;

import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import me.xujichang.ui.promission.IPermissionCallBack;
import me.xujichang.web.WebConst;

/**
 * Des:
 *
 * @author xujichang
 * <p>
 * created by 2018/9/11-下午3:37
 */
public abstract class WebPermissionCallback implements IPermissionCallBack {
    private CallBackFunction function;

    public WebPermissionCallback(CallBackFunction function) {
        this.function = function;
    }

    @Override
    public boolean onDenied() {
        if (null != function) {
            function.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.REQUEST_OTHER, "未获取相关权限"));
        }
        return false;
    }
}
