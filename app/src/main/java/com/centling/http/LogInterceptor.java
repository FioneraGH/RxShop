package com.centling.http;

import com.centling.util.L;
import com.centling.util.SPUtil;
import com.centling.util.Utils;

import java.io.IOException;
import java.nio.charset.Charset;

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
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        L.d("Logging Request: " + request.url().toString());
        L.d("Logging Request header: " + request.headers());

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            if (requestBody instanceof MultipartBody) {
                // MultipartBody会乱码
                L.d("Logging Request body: MultiPart");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                L.d("Logging Request body: " + buffer.readString(Charset.defaultCharset()));
                buffer.close();
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
