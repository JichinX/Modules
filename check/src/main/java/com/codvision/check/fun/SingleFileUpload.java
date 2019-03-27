package com.codvision.check.fun;


import com.codvision.check.CheckInit;
import com.codvision.check.api.CommonApi;
import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import me.xujichang.basic.wrapper.WrapperEntity;
import me.xujichang.util.file.Files;
import me.xujichang.util.retrofit.RetrofitManager;
import me.xujichang.util.simple.SilentResourceObserver;
import me.xujichang.util.tool.StringTool;
import me.xujichang.web.WebConst;

/**
 * Des:
 *
 * @author xjc
 * Created on 2017/12/4 16:20.
 */

public class SingleFileUpload {
    private String baseUrl;
    private File uploadFile;
    private CallBackFunction function;

    public SingleFileUpload withFile(String data) {
        uploadFile = new File(data);
        return this;
    }


    public SingleFileUpload withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public SingleFileUpload withFunction(CallBackFunction function) {
        this.function = function;
        return this;
    }

    public void upload(CallBackFunction function) {
        this.function = function;
        upload();
    }

    public void upload() {
        if (!uploadFile.exists()) {
            function.onCallBack(DataType.createRespData(WebConst.StatusCode.ERROR_FILE_NOT_FOUND, "文件不存在", uploadFile.getName()));
            return;
        }
        uploadFile(uploadFile);
    }

    protected void uploadFile(final File file) {

        uploadFile(file, function);
    }

    protected void uploadFile(final File file, CallBackFunction function) {
        SilentResourceObserver<WrapperEntity<String>> observer = new SilentResourceObserver<WrapperEntity<String>>(null) {

            @Override
            public void onNext(WrapperEntity<String> entity) {
                if (entity.getCode() == 200) {
                    function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "文件上传成功", entity.getData()));

                } else {
                    function.onCallBack(DataType.createRespData(entity.getCode(), WebConst.StatusCode.STATUS_SERVER_RESP, entity.getMessage(), uploadFile.getName()));
                }
            }

            @Override
            public void onError(Throwable e) {
                function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_ERROR, StringTool.getErrorMsg(e), uploadFile.getName()));
            }
        };
        uploadFile(file, observer);
    }

    protected void uploadFile(final File file, ResourceObserver<WrapperEntity<String>> observer) {
        RetrofitManager
                .getOurInstance()
                .createReq(CommonApi.class)
                .uploadSingleFile(CheckInit.PHOTO_UPLOAD_PATH, Files.fileToMultipartBodyPart(file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public SingleFileUpload withFile(File file) {
        uploadFile = file;
        return this;
    }
}
