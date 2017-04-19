package com.centling.http;

import com.centling.BaseApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Api
 */

public class Api {

    private static ApiService apiService;
    private static ApiService noJsonApiService;

    private static final int CONNECT_TIMEOUT = 20;
    private static final int READ_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 30;

    private static class Holder {
        private static Api instance = new Api(true);
        private static Api noGsonInstance = new Api( false);
    }

    public static Api getInstance() {
        return Holder.instance;
    }

    public static Api getNoGsonInstance() {
        return Holder.noGsonInstance;
    }

    private Api(boolean gsonSupport) {
        init(gsonSupport);
    }

    private void init(boolean gsonSupport) {
        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT,
                TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(
                WRITE_TIMEOUT, TimeUnit.SECONDS).addInterceptor(new LogInterceptor())
                .retryOnConnectionFailure(false).cache(cache);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().client(builder.build()).baseUrl(
                HttpConstants.BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            if (gsonSupport) {
                apiService = retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
                        .build().create(ApiService.class);
            } else {
                noJsonApiService = retrofitBuilder.build().create(ApiService.class);
            }
    }

    public ApiService getApiService() {
        if (apiService == null) {
            init(true);
        }
        return apiService;
    }

    public ApiService getNoGsonApiService() {
        if (noJsonApiService == null) {
            init(false);
        }
        return noJsonApiService;
    }
}
