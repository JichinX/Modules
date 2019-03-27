package com.codvision.check.test;

import com.codvision.check.bean.UploadFile;
import com.codvision.check.impl.WebContainerActivity;

import me.xujichang.web.interfaces.IWebJsCallBack;

/**
 * Des:
 *
 * @author xujichang
 * Created on 2018/10/17 - 4:19 PM
 */
public class WebActivity extends WebContainerActivity {

    @Override
    public void onUpload(UploadFile file) {
        showToast("文件" + file.getFileName() + "加入到上传队列");
    }

    @Override
    protected void initExtHandler(IWebJsCallBack callBack) {
        super.initExtHandler(callBack);
        new TestHandler(getWebView()).addJsCallBack(callBack);
    }
}
