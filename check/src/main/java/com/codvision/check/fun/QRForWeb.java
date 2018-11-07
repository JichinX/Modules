package com.codvision.check.fun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.codvision.check.R;
import com.codvision.check.data.DataType;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.king.zxing.util.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import me.xujichang.util.file.Files;
import me.xujichang.util.tool.StringTool;
import me.xujichang.web.WebConst;

import static android.app.Activity.RESULT_OK;

/**
 * Des:二维码扫描
 * 1.生成二维码
 * 返回给Web生成的Picture Path:resultPath
 * <p>
 * 2.识别二维码
 * 返回给Web识别出的Str : resultStr;
 * 2.1 拍摄识别
 * 2.2 选择相册内图片识别
 *
 * @author xujichang
 * <p>
 * created by 2018/8/15-下午2:03
 */
public class QRForWeb {
    /**
     * 生成
     */
    private static final int TYPE_CREATE = 2;
    /**
     * 识别
     */
    private static final int TYPE_IDENTIFY = 1;
    private static final String KEY_TITLE = "key_title";
    private static final int REQUEST_QR = 10007;
    private static QRForWeb instance;
    /**
     * 来自Web的数据
     */
    private String data;
    /**
     * 上下文
     */
    private WeakReference<Activity> weakReference;
    /**
     * web回调
     */
    private CallBackFunction function;

    /**
     * 生二维码时，图片路径
     */
    private String resultPath;
    /**
     * 识别二维码，识别出的内容
     */
    private String resultStr;
    /**
     * 图片存储路径
     */
    private File storeDir;

    public static QRForWeb withContext(Activity context) {
        instance().weakReference = new WeakReference<>(context);
        return instance;
    }

    private static QRForWeb instance() {
        instance = Holder.instance;
        return instance;
    }

    public QRForWeb withFunction(CallBackFunction function) {
        instance().function = function;
        return instance;
    }

    public QRForWeb withData(String data) {
        instance().data = data;
        return instance;
    }

    public void execute() {
        Context context = weakReference.get();
        if (null == context) {
            return;
        }
        storeDir = context.getFilesDir();
        if (Strings.isNullOrEmpty(data)) {
            throw new RuntimeException("QrForWeb needs QrData parse from Web ,But There is Null！");
        }
        QrData qrData = new Gson().fromJson(data, QrData.class);
        if (qrData.type == TYPE_CREATE) {
            doCreate(qrData.str);
        } else {
            doIdentify();
        }
    }

    /**
     * 识别
     */
    private void doIdentify() {
        Activity context = weakReference.get();
        if (null == context) {
            return;
        }
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.in, R.anim.out);
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.putExtra(KEY_TITLE, "。。。");
        ActivityCompat.startActivityForResult(context, intent, REQUEST_QR, optionsCompat.toBundle());
    }

    /**
     * 生成
     *
     * @param str 需要生成二维码图片的字符串
     */
    private void doCreate(final String str) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Context context = weakReference.get();
                if (null == context) {
                    return;
                }
                //生成二维码
                Bitmap bitmap = CodeUtils.createQRCode(str, 600);
                //保存到相册
                File file = Files.createImageFile(context, "qrImage");
                if (null == file || !file.exists()) {
                    e.onError(new RuntimeException("二维码文件创建失败"));
                } else {
                    OutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    e.onNext(file.getAbsolutePath());
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new ResourceObserver<String>() {
            @Override
            public void onNext(String s) {
                resultPath = s;
            }

            @Override
            public void onError(Throwable e) {
                onResultError(StringTool.getErrorMsg(e));
            }

            @Override
            public void onComplete() {
                if (Strings.isNullOrEmpty(resultPath)) {
                    onResultError("二维码文件创建失败");
                } else {
                    onResult(resultPath);
                }

            }
        });

    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QR) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    //识别结果
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    onResult(result);
                } else {
                    onResultError("解析数据为空！");
                }
            } else {
                onResultError("解析失败！");
            }
            return true;
        }
        return false;
    }

    private static void onResultError(String msg) {
        instance.function.onCallBack(DataType.createErrorRespData(WebConst.StatusCode.STATUS_NATIVE_ERROR, msg));
    }

    private static void onResult(String result) {
        instance.function.onCallBack(DataType.createRespData(WebConst.StatusCode.STATUS_OK, "success", result));
    }

    private static final class Holder {
        static QRForWeb instance = new QRForWeb();
    }

    private QRForWeb() {
    }

    public static final class QrData {
        /**
         * 二维码操作类别 1 识别 2 生成
         */
        private int type;
        /**
         * 二维码生成时提供的Str
         */
        private String str;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }
}
