package com.codvision.check.test;

import com.codvision.check.bean.UploadFile;
import com.codvision.check.impl.WebContainerActivity;

/**
 * Des:
 *
 * @author xujichang
 * Created on 2018/10/17 - 4:19 PM
 */
public class WebActivity extends WebContainerActivity {
    private boolean show = true;

    @Override
    public void onUpload(UploadFile file) {
        showToast("文件" + file.getFileName() + "加入到上传队列");
    }
}
