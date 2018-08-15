package com.codvision.background.api;

import com.codvision.base.wrapper.WrapperEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/26 - 8:47 PM
 */
public interface ApiStores {
    String REMOTE_URL = "";

    @GET("/test")
    Observable<WrapperEntity<String>> uploadJsonData(@Body String json);
}
