package com.vip.marrakech.retrofit.interfaces;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


public interface ApiInterface {


    @FormUrlEncoded
    @POST()
    Call<String> getPost(@Url String url, @FieldMap HashMap<String, String> param, @Header("tag") String tag, @Header("x-api-key") String api_key);


    @FormUrlEncoded
    @POST()
    Call<String> getPost(@Url String url, @Field("action") String action, @Header("tag") String tag, @Header("x-api-key") String api_key);

    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Header("tag") String tag, @Header("x-api-key") String api_key);


    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part List<MultipartBody.Part> files, @Header("tag") String tag, @Header("x-api-key") String api_key);

    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part List<MultipartBody.Part> files, @Part List<MultipartBody.Part> files1, @Header("tag") String tag, @Header("x-api-key") String api_key);


    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Part List<MultipartBody.Part> files, @Part List<MultipartBody.Part> files1, @Header("tag") String tag, @Header("x-api-key") String api_key);

    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Part List<MultipartBody.Part> files, @Header("tag") String tag, @Header("x-api-key") String api_key);


    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part List<MultipartBody.Part> files, @Part List<MultipartBody.Part> files1, @Header("tag") String tag, @Header("x-api-key") String api_key);

    @Multipart
    @POST()
    Call<String> getPost(@Url String url, @PartMap HashMap<String, RequestBody> param, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part List<MultipartBody.Part> files, @Header("tag") String tag, @Header("x-api-key") String api_key);


    @GET()
    Call<String> getGet(@Url String url, @Header("tag") String tag, @Header("x-api-key") String api_key);

    @DELETE()
    Call<String> getDelete(@Url String url, @Header("tag") String tag, @Header("x-api-key") String api_key);


}