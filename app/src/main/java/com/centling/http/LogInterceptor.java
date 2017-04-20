package com.centling.http;

import android.text.TextUtils;

import com.centling.util.L;
import com.centling.util.UserInfoUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * LogInterceptor
 */

class LogInterceptor
        implements Interceptor {

    private Gson mGson = new Gson();
    private Type mParseType = new TypeToken<HashMap<String,String>>(){}.getType();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        L.d("Logging Interceptor: " + toString());
        L.d("Logging Request: " + request.url().toString());
        L.d("Logging Request header: " + request.headers());


        if (TextUtils.equals(request.method(), "POST")) {
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                if (requestBody instanceof MultipartBody) {
                    // MultipartBody会乱码
                    L.d("Logging Request body: MultiPart");
                } else if (requestBody instanceof FormBody) {
                    L.d("Logging Request body: Form");
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    /*
                    try to inject new params
                     */
                    Map<String, String> newParams = new HashMap<>();
                    HashMap<String, String> originParams = mGson.fromJson(buffer.readUtf8(),
                            mParseType);
                    newParams.putAll(originParams);
                    newParams.put("key",UserInfoUtil.getKey());
                    newParams.put("client","android");

                    request = request.newBuilder().post(RequestBody
                            .create(MediaType.parse("application/json; charset=utf-8"), mGson.toJson(newParams))).build();

                    buffer.clear();
                    request.body().writeTo(buffer);
                    L.d("Logging Request body: " + buffer.readUtf8());
                    buffer.close();
                }
            }
        }

        Response response = chain.proceed(request);
        if (response != null && response.body() != null) {
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            L.d("Logging Response: " + content);
            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
        }
        return response;
    }
}
