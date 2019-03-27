package com.codvision.check.fun.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.File;

import me.xujichang.util.file.Files;
import me.xujichang.web.WebConst;

import static android.app.Activity.RESULT_OK;

/**
 * Des:Modules - com.codvision.check.fun.camera
 * 对web的数据解析然后执行对应的操作
 * 只涉及对原声相机的操作
 * 拍照 0
 * 录制视频 1
 *
 * @author xujichang
 * @date 2019/1/11 09:22
 * <p>
 * modify:
 */
public class CameraForWeb {
    private CallBackFunction mBackFunction;
    private              Activity         mContext;
    private              int              mOpt;
    private static final int              CAMERA_CAPTURE = 0;
    private static final int              CAMERA_RECORD  = 1;
    private              Uri              photoUri;
    private              File             cachedPhotoFile;
    private static       String           DCIM_PATH;

    private CameraForWeb() {
        DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WebConst.RequestCode.QUICK_CAMERA) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(photoUri);
                mContext.sendBroadcast(intent);
                mBackFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "success", null));
            } else {
                mBackFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_ERROR, "操作被取消"));
            }
            return true;
        }
        return false;
    }

    private static class classHolder {
        private static final CameraForWeb instance = new CameraForWeb();
    }

    public CameraForWeb withFunction(CallBackFunction function) {
        mBackFunction = function;
        return this;
    }

    public static CameraForWeb getInstance() {
        return classHolder.instance;
    }

    public CameraForWeb withContext(Activity context) {
        mContext = context;
        return this;
    }

    public CameraForWeb withOpt(String data) {
        if (Strings.isNullOrEmpty(data)) {
            mOpt = CAMERA_CAPTURE;
        } else {
            mOpt = new Gson().fromJson(data, CameraOpt.class).getOpt();
        }
        return this;
    }

    public void execute() {
        if (mOpt == CAMERA_CAPTURE) {
            doCapture();
        } else if (mOpt == CAMERA_RECORD) {
            doRecord();
        } else {
            mBackFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_ERROR_PARAMS, "未匹配到参数：" + mOpt));
        }
    }

    private void doCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String fileName = "JJ_" + System.currentTimeMillis() + ".jpg";
        cachedPhotoFile = new File(DCIM_PATH, fileName);
        photoUri = Files.getUriFromFile(mContext, cachedPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        mContext.startActivityForResult(intent, WebConst.RequestCode.QUICK_CAMERA);
    }

    private void doRecord() {
        mBackFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "success", null));
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15_000);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.startActivity(intent);
    }
}
