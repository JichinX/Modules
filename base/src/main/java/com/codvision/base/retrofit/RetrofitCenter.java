package com.codvision.base.retrofit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project: Modules
 * Des:对Retrofit 的再次封装
 * 主要解决问题：
 * 1. 项目中 存在多个不同的BaseUrl ok
 * 2. 下载进度
 * 3. token ok
 * 4. cookie
 *
 * @author xujichang
 * created by 2018/7/26 - 4:48 PM
 */
public final class RetrofitCenter {
    public static volatile String tokenValue;
    /**
     * 通过将baseUrl 与对应的retrofit来进行绑定，避免过多的创建Retrofit对象
     */
    private static final Map<String, Retrofit> RETROFIT_MAP = new ConcurrentHashMap<>();
    public static final String URL_TAG = "REMOTE_URL";

    public static void init(String baseUrl, ClientConfig clientConfig) {
        init(baseUrl, clientConfig, null, null);
    }

    public static void init(String baseUrl, ClientConfig clientConfig, List<Converter.Factory> converters, List<CallAdapter.Factory> callAdapters) {
        if (!isInitRetrofit(baseUrl)) {
            Retrofit retrofit = createRetrofit(baseUrl, createHttpClient(clientConfig), converters, callAdapters);
            RETROFIT_MAP.put(baseUrl, retrofit);
        }
    }

    public static void init(String baseUrl) {
        init(baseUrl, null);
    }

    public static void init(Class<?> clazz) {
        init(getBaseUrl(clazz), null);
    }


    private static String getBaseUrl(Class clazz) {
        try {
            Field url = clazz.getField(URL_TAG);
            url.setAccessible(true);
            return (String) url.get(clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
        throw new RuntimeException("The class is not contains Field:" + URL_TAG);
    }

    public static boolean isInitRetrofit(String baseUrl) {
        return RETROFIT_MAP.get(baseUrl) != null;
    }

    public static boolean isInitRetrofit(Class<?> clazz) {
        return isInitRetrofit(getBaseUrl(clazz));
    }

    /**
     * 根据baseUrl获取缓存中的Retrofit对象
     *
     * @param baseUrl
     * @return
     */
    public static Retrofit getApi(String baseUrl) {
        Retrofit retrofit = RETROFIT_MAP.get(baseUrl);
        if (retrofit == null) {
            init(baseUrl);
        }
        retrofit = RETROFIT_MAP.get(baseUrl);
        return retrofit;
    }

    public static <T> T getApi(Class<T> clazz) {
        return getApi(getBaseUrl(clazz)).create(clazz);
    }

    private static Retrofit createRetrofit(String baseUrl) {
        return createRetrofit(baseUrl, createHttpClient(), null, null);
    }

    private static Retrofit createRetrofit(String baseUrl, OkHttpClient httpClient, List<Converter.Factory> converters, List<CallAdapter.Factory> callAdapters) {
        if (null == baseUrl) {
            throw new RuntimeException("The Required BaseUrl is null!");
        }
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl(baseUrl);
        //使用RxJava返回格式
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        //其他CallAdapter
        if (null != callAdapters) {
            for (CallAdapter.Factory callAdapter : callAdapters) {
                builder.addCallAdapterFactory(callAdapter);
            }
        }
        //使用Gson转换
        builder.addConverterFactory(GsonConverterFactory.create());
        //其他转换
        if (null != converters) {
            for (Converter.Factory converter : converters) {
                builder.addConverterFactory(converter);
            }
        }
        if (null == httpClient) {
            builder.client(createHttpClient());
        } else {
            builder.client(httpClient);
        }
        return builder.build();
    }

    /**
     * 默认的Client
     *
     * @return
     */
    private static OkHttpClient createHttpClient() {
        ClientConfig clientConfig = new ClientConfig.Builder().build();
        return createHttpClient(clientConfig);
    }

    /**
     * 根据给与的config创建client
     *
     * @param clientConfig
     * @retur：
     */
    private static OkHttpClient createHttpClient(ClientConfig clientConfig) {
        if (null == clientConfig) {
            clientConfig = new ClientConfig.Builder().build();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //连接超时 时间
        builder.connectTimeout(clientConfig.connectTimeOut, TimeUnit.MILLISECONDS);
        builder.readTimeout(clientConfig.readTimeOut, TimeUnit.MILLISECONDS);
        builder.writeTimeout(clientConfig.writeTimeOut, TimeUnit.MILLISECONDS);
        //重试
        builder.retryOnConnectionFailure(true);
        if (null != clientConfig.interceptors) {
            for (Interceptor interceptor : clientConfig.interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (null != clientConfig.extInfos) {
            builder.addInterceptor(new HttpExtInfoInterceptor(clientConfig.extInfos));
        }
        return builder.build();
    }

    /**
     * 创建OkHttpClient的配置类
     */
    public static final class ClientConfig {
        long connectTimeOut;
        long readTimeOut;
        long writeTimeOut;
        List<Interceptor> interceptors;
        List<IExtInfo> extInfos;

        private ClientConfig(Builder builder) {
            connectTimeOut = builder.connectTimeOut;
            readTimeOut = builder.readTimeOut;
            writeTimeOut = builder.writeTimeOut;
            interceptors = builder.interceptors;
            extInfos = builder.extInfos;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            interceptors.add(loggingInterceptor);
        }

        public static final class Builder {
            private long connectTimeOut = TimeUnit.SECONDS.toMillis(15);
            private long readTimeOut = TimeUnit.SECONDS.toMillis(50);
            private long writeTimeOut = TimeUnit.SECONDS.toMillis(50);
            private List<Interceptor> interceptors = new ArrayList<>();
            private List<IExtInfo> extInfos = new ArrayList<>();

            public Builder() {
            }

            public Builder withConnectTimeOut(long val) {
                connectTimeOut = val;
                return this;
            }

            public Builder withReadTimeOut(long val) {
                readTimeOut = val;
                return this;
            }

            public Builder withWriteTimeOut(long val) {
                writeTimeOut = val;
                return this;
            }

            public Builder withInterceptors(List<Interceptor> val) {
                interceptors.addAll(val);
                return this;
            }

            public Builder withExtInfos(List<IExtInfo> val) {
                extInfos.addAll(val);
                return this;
            }

            public Builder clearExtInfos() {
                extInfos.clear();
                return this;
            }

            public Builder clearInterceptors() {
                interceptors.clear();
                return this;
            }

            public ClientConfig build() {
                return new ClientConfig(this);
            }
        }
    }

    public static void refreshToken(String newVal) {
        tokenValue = newVal;
    }

    public static String getTokenValue() {
        return tokenValue;
    }

    public static void clearToken() {
        refreshToken("");
    }
}
