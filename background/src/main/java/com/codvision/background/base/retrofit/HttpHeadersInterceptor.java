package com.codvision.background.base.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/26 - 7:19 PM
 */
public class HttpHeadersInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }

    public static class Config {

    }
}
