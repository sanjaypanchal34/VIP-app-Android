package com.vip.marrakech.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vip.marrakech.helpers.AppController;
import com.vip.marrakech.helpers.SessionManager;
import com.vip.marrakech.retrofit.exeptions.ConnectivityInterceptor;
import com.vip.marrakech.retrofit.exeptions.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*VIP 31-03 1950*/
public class ApiClient {
    public static final String HOST = "https://vipspace.nyc3.digitaloceanspaces.com";
    public static final String BASE_URL = "https://thevipapp.thevipgroups.com/api/";
    public static final String Payment = "https://thevipapp.thevipgroups.com/";

//    public static final String HOST = "http://178.128.161.201/VIP1/uploads";
//    public static final String Payment = "http://178.128.161.201/VIP1/";
//    public static final String BASE_URL = "http://178.128.161.201/VIP1/api/";

    public static final String USER_PAYMET = Payment + "user/stripe?";
    public static final String USER_ITINARIRY_PAYMET = Payment + "user/itinerary/stripe?";
    public static final String VENDOR_PAYMET = Payment + "vendor/stripe?";
    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(6000, TimeUnit.SECONDS);
        httpClient.connectTimeout(6000, TimeUnit.SECONDS);
        httpClient.addInterceptor(new ConnectivityInterceptor(AppController.getInstance()));
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Log.e("TOKN:::",SessionManager.getToken());
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                        .addHeader("lan",  SessionManager.getLanguage())
                        .build();
                return chain.proceed(newRequest);
            }
        });
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static Retrofit getCachClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        File httpCacheDirectory = new File(AppController.getInstance().getCacheDir(), "cache_file");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        httpClient.cache(cache);
        httpClient.readTimeout(6000, TimeUnit.SECONDS);
        httpClient.connectTimeout(6000, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String cacheHeaderValue = NetworkUtil.isOnline(AppController.getInstance())
                        ? "public, max-age=2419200"
                        : "public, only-if-cached, max-stale=2419200";
                Request request = originalRequest.newBuilder().addHeader("Authorization", "Bearer " + SessionManager.getToken()).build();
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheHeaderValue)
                        .build();

             /*   Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                        .build();*/
                /*     return chain.proceed(newRequest);*/
            }
        });
        httpClient.networkInterceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String cacheHeaderValue = NetworkUtil.isOnline(AppController.getInstance())
                                ? "public, max-age=2419200"
                                : "public, only-if-cached, max-stale=2419200";
                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        );
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static OkHttpClient.Builder getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        } else {
            return null;
        }
    }
}