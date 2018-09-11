package com.codvision.check.api;


import com.codvision.check.bean.LocationUpload;

import java.util.List;

import io.reactivex.Observable;
import me.xujichang.basic.wrapper.WrapperEntity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by xjc on 2017/6/8.
 */

public interface CommonApi {

    @Multipart
    @POST("/upload/index")
    Observable<WrapperEntity> uploadImage(@Part("file\"; filename=\"dld.jpg\"") RequestBody img);

    @Multipart
    @POST("/upload/index")
    Observable<WrapperEntity<String>> uploadFiles(@Part() List<MultipartBody.Part> parts);

    /**
     * 上传文佳集合
     *
     * @param multipartBody
     * @return
     */
    @POST("/upload/index")
    Observable<WrapperEntity<String>> uploadFiles(@Body MultipartBody multipartBody);

    /**
     * 上传单文件
     *
     * @param part
     * @return
     */
    @Multipart
    @POST("/{path}/api/v3/upload/index")
    Observable<WrapperEntity<String>> uploadSingleFile(@Path("path") String path, @Part() MultipartBody.Part part);
    /**
     * 上传单文件
     *
     * @param part
     * @return
     */
    @Multipart
    @POST("/upload/index")
    Observable<WrapperEntity<String>> uploadSingleFile( @Part() MultipartBody.Part part);

    @POST("/post/unread_count")
    Observable<WrapperEntity<Integer>> getUnreadMessageCount();

    @POST("/{path}/api/v3/reportgps")
    Observable<WrapperEntity> uploadGps(@Path("path") String path, @Body LocationUpload location);
}
