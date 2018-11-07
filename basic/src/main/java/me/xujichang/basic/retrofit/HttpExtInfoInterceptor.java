package me.xujichang.basic.retrofit;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import me.xujichang.basic.BaseConst;
import me.xujichang.basic.BaseInit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/26 - 9:40 PM
 */
class HttpExtInfoInterceptor implements Interceptor {
    private List<IExtInfo> extInfo;

    public HttpExtInfoInterceptor(List<IExtInfo> val) {
        extInfo = val;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers.Builder headersBuilder = request.headers().newBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        Request.Builder requestBuilder = request.newBuilder();

        if (null == extInfo) {
            return chain.proceed(request);
        } else {
            Map<String, String> values = null;
            for (IExtInfo info : extInfo) {
                values = info.getValues();
                if (values.containsKey(BaseInit.TOKEN_KEY)) {
                    values.put(BaseInit.TOKEN_KEY, RetrofitCenter.tokenValue);
                }
                switch (info.getPos()) {
                    case Headers:
                        for (Map.Entry<String, String> entry : values.entrySet()) {
                            headersBuilder.add(entry.getKey(), entry.getValue());
                        }
                        break;
                    case Query:
                        for (Map.Entry<String, String> entry : values.entrySet()) {
                            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                        }
                        break;
                    case Body:
                        //TODO
                        break;
                    default:
                        break;
                }
            }
            requestBuilder.url(urlBuilder.build());
            requestBuilder.headers(headersBuilder.build());
            return chain.proceed(requestBuilder.build());
        }
    }
}
