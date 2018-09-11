package com.codvision.check.fun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.codvision.check.activity.QrCodeActivity;
import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.io.File;

import me.xujichang.web.WebConst;

import static android.app.Activity.RESULT_OK;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/27 15:54.
 */

public class QrcodeForWeb {
    private static QrcodeForWeb instance;
    private CallBackFunction sFunction;
    private Context mContext;
    private File storeDir;
    private static boolean justCamera = false;

    public static QrcodeForWeb getInstance() {
        if (null == instance) {
            instance = ClassHolder.instance;
        }
        return instance;
    }

    private QrcodeForWeb() {
    }

    public QrcodeForWeb withContext(Context context) {
        mContext = context;
        return instance;
    }

    public void execute() {
        if (null == mContext) {
            throw new RuntimeException("the Context is null");
        }
        final Activity activity = (Activity) mContext;
        //实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
        Intent intent = new Intent(mContext, QrCodeActivity.class);
        //开启相机，传入
        activity.startActivityForResult(intent, WebConst.RequestCode.QRCODE);

    }

    public QrcodeForWeb withFunction(CallBackFunction function) {
        sFunction = function;
        return instance;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WebConst.RequestCode.QRCODE) {
            if (resultCode != RESULT_OK) {
                return true;
            }

            String path = data.getStringExtra("qrcode");
            if (null != path) {
                sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "", path));
            } else {
                sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, ""));
            }
            return true;
        }
        return false;
    }

    private static class ClassHolder {
        public static QrcodeForWeb instance = new QrcodeForWeb();
    }
}
