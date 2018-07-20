package com.codvision.checksdk;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import me.xujichang.util.retrofit.RetrofitManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
//        new RetrofitManager.Builder().token(RetrofitManager.TOKEN_IN_QUERY, "", Const.TOKEN_KEY).baseUrl(Const.Url.BASE_URL).build();
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
