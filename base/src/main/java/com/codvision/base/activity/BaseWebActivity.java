package com.codvision.base.activity;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.xujichang.hybirdbase.api.DownLoadApi;
import me.xujichang.hybirdbase.module.web.HyBirdWebViewActivity;
import me.xujichang.hybirdbase.temp.DownLoadToolTemp;
import me.xujichang.util.bean.AppInfo;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public abstract class BaseWebActivity extends HyBirdWebViewActivity {

    /**
     * 下载所需模块
     */
    protected void showModuleDownLoadDialog(String title, String des, MaterialDialog.SingleButtonCallback callback) {
        MaterialDialog dialog = new MaterialDialog
                .Builder(this)
                .title(title)
                .content(des)
                .autoDismiss(false)
                .cancelable(false)
                .positiveText("下载")
                .negativeText("取消")
                .onAny(callback)
                .build();
        dialog.show();
    }

    /**
     * 下载APK
     *
     * @param appInfo
     */
    protected void downloadApkFile(String baseurl, AppInfo appInfo) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                Headers newHeaders = request.headers().newBuilder().set("Connection", "close").build();
                okhttp3.Request newRequest = request.newBuilder().headers(newHeaders).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        DownLoadToolTemp downLoadTool = new DownLoadToolTemp
                .Builder()
                .fileName(appInfo.getPackageName() + ".apk")
                .showProgress(true)
                .storeDir(getExternalFilesDir(null))
                .withContext(this)
                .build();
        Observable<ResponseBody> observable = new Retrofit
                .Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseurl)
                .build()
                .create(DownLoadApi.class)
                .getApkFile(appInfo.getPackageName());
        downLoadTool.apply(observable);
    }
}
