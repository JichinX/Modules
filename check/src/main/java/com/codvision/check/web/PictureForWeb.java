package com.codvision.check.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;


import com.codvision.base.utils.file.Files;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/11/27 15:54.
 */

public class PictureForWeb {
    private static final int ALBUM_REQUEST_CODE = 31;
    private static final int CAMERA_REQUEST_CODE = 32;
    private static PictureForWeb instance;
    private CallBackFunction sFunction;
    private int option;
    private Context mContext;
    private File storeDir;
    private static boolean justCamera = false;
    private File cachedPhotoFile;

    public static PictureForWeb getInstance() {
        if (null == instance) {
            instance = ClassHolder.instance;
        }
        justCamera = false;
        return instance;
    }

    private PictureForWeb() {
    }

    public PictureForWeb withOptions(String data) {
        DataType.ReqData optionBean = new Gson().fromJson(data, DataType.ReqData.class);
        if (null != optionBean) {
            option = optionBean.getOption();
        }
        return instance;
    }

    public PictureForWeb withContext(Context context) {
        mContext = context;
        return instance;
    }

    public void execute() {
        if (null == mContext) {
            throw new RuntimeException("the Context is null");
        }
        storeDir = mContext.getFilesDir();
        final Activity activity = (Activity) mContext;
        if (justCamera) {
            //实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //开启相机，传入
            cachedPhotoFile = Files.createImageFile(activity, "tmpImg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Files.getUriFromFile(activity, cachedPhotoFile));
            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
            return;
        }
        ArrayList<String> items = new ArrayList<>();
        items.add("相机");
        items.add("相册");
        new MaterialDialog.Builder(mContext)
                .title("图片来源")
                .content("请选择拍照或者从相册中获取")
                .items(items)
                //.listSelector(R.color.green)//列表的背景颜色
                .autoDismiss(true)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position,
                                            CharSequence text) {
                        if (position == 1) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            activity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
                        } else {
                            //实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //开启相机，传入
                            cachedPhotoFile = Files.createImageFile(activity, "tmpImg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Files.getUriFromFile(activity, cachedPhotoFile));
                            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }
                    }
                })
                .show();
    }

    public PictureForWeb withFunction(CallBackFunction function) {
        sFunction = function;
        return instance;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALBUM_REQUEST_CODE || requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return true;
            }
            File srcFile = null;
            String path = null;
            if (requestCode % 2 == 0) {
                //相机
                srcFile = cachedPhotoFile;
            } else {
                //相册
                Uri uri = data.getData();
                if (null != uri) {
                    path = Files.getRealFilePath(mContext, uri);
                    srcFile = new File(path);
                }
            }
            if (null == srcFile) {
                sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "获取图片路径,失败"));
            } else {
                if (Files.getFileSizeFormatM(srcFile) > 1) {
                    File destFile = null;
                    try {
                        destFile = new Compressor(mContext).setDestinationDirectoryPath(storeDir.getAbsolutePath()).compressToFile(srcFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (null == destFile) {
                        sFunction.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, "获取相册路径,失败"));
                    } else {
                        path = destFile.getAbsolutePath();
                        sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "成功获取相册路径", path));
                    }
                } else {
                    path = srcFile.getAbsolutePath();
                    sFunction.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "成功获取相册路径", path));
                }
            }
            return true;
        }
        return false;
    }

    private File createPictureFile() {
        return new File(storeDir, "picture_" + System.currentTimeMillis() + ".jpg");
    }

    public PictureForWeb justCamera(boolean just) {
        justCamera = just;
        return instance;
    }

    private static class ClassHolder {
        public static PictureForWeb instance = new PictureForWeb();
    }
}
