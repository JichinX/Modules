package com.codvision.check.fun;

import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import me.xujichang.web.WebConst;


/**
 * Des:
 *
 * @author xjc
 * Created on 2017/12/9 10:37.
 */

public class FilesUploadForWeb {
    public static final String DATA_FILES = "files";
    private CallBackFunction nativeCallback;
    private Result result;

    public void uploadFilesNew(String data, CallBackFunction function) {
        uploadFilesNew(data, function, true);
    }

    public void uploadFilesNew(String data, CallBackFunction function, boolean isNew) {
        ArrayList<File> files = parseFiles(data);
        if (files.size() == 0) {
            function.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_ERROR, "未解析出需要上传的文件"));
            return;
        }
        if (files.size() == 1 && isNew) {
            onUpload(files.get(0), function);
        } else {
            final CallBackFunction filesFunction = function;
            result = new Result(files.size());

            nativeCallback = new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    DataType.RespData<String> respData = DataType.parseRespData(data, String.class);
                    if (respData.getCode() == 200) {
                        result.increaseSuccessNum();
                        result.getSuccessUrl().add(respData.getData());
                    } else {
                        result.increaseFailNum();
                        result.getFailData().add(respData.getData());
                    }
                    if (result.isOK()) {
                        String msg = "全部文件上传成功";
                        int status = WebConst.StatusCode.STATUS_OK;
                        if (result.hasFailData()) {
                            status = WebConst.StatusCode.STATUS_SUCCESS_PART;
                            msg = String.format(Locale.CHINA, "%d个文件上传成功，%d个文件上传失败", result.getSuccessNum(), result.getFailNum());
                        }
                        filesFunction.onCallBack(DataType.createRespData(status, msg, result));
                    }
                }
            };
            for (File file : files) {
                onUpload(file, nativeCallback);
            }
        }
    }

    /**
     * 开放方法，方便扩展
     *
     * @param file
     * @param function
     */
    protected void onUpload(File file, CallBackFunction function) {
        new SingleFileUpload()
                .withFile(file)
                .upload(function);
    }

    private static ArrayList<File> parseFiles(String data) {
        ArrayList<File> files = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray(DATA_FILES);
            for (int i = 0; i < array.length(); i++) {
                if ("null" == array.get(i)) {
                    continue;
                }
                String url = (String) array.get(i);
                File file = new File(url);
                if (file.exists()) {
                    files.add(file);
                }
            }
            array.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return files;
    }

    public void uploadFiles(String data, CallBackFunction function) {
        uploadFilesNew(data, function, false);
    }

    public static class Result {
        private int count;
        private int successNum;
        private int failNum;
        private ArrayList<String> successUrl;
        private ArrayList<String> failData;

        public Result(int count) {
            successUrl = new ArrayList<>();
            failData = new ArrayList<>();
            this.count = count;
        }

        public ArrayList<String> getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(ArrayList<String> successUrl) {
            this.successUrl = successUrl;
        }

        public ArrayList<String> getFailData() {
            return failData;
        }

        public void setFailData(ArrayList<String> failData) {
            this.failData = failData;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getSuccessNum() {
            return successNum;
        }

        public void setSuccessNum(int successNum) {
            this.successNum = successNum;
        }

        public int getFailNum() {
            return failNum;
        }

        public void setFailNum(int failNum) {
            this.failNum = failNum;
        }

        public void increaseFailNum() {
            failNum++;
        }

        public void increaseSuccessNum() {
            successNum++;
        }

        public boolean isOK() {
            return failNum + successNum == count;
        }

        public boolean hasFailData() {
            return failNum != 0;
        }
    }
}
