package com.porterking.netlibrary.interceptor;

import com.porterking.netlibrary.work.NetLogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by PorterKing on 2017/3/23.
 */

public class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        String requestBodyStr = "";
        String content;
        Charset charset = UTF8;
        Request request = chain.request();
        Headers header = request.headers();

        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();

        MediaType mediaType = response.body().contentType();
        String mediaTypeStr = mediaType == null ? "" : mediaType.toString().toLowerCase();

        content = response.body().string();

        // POST请求带有 Log信息
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            requestBodyStr = buffer.readString(charset);
        }

        NetLogUtils.d("ok-http-line=================start=================");
        NetLogUtils.d("ok-http" +
                "\n--------request:" + request.toString() +
                "\n--------request-time:" + (t2 - t1) / 1e6d + "ms" +
                "\n--------headers:\n" + header.toString() +
                "\n--------request-body-contentType:" + mediaTypeStr +
                "\n--------request-body:" + requestBodyStr +
                "\n--------content-length:" + "(" + (requestBody == null ? "0" : requestBody.contentLength()) + "-byte body)" +
                "\n--------response-body:" + content );
        NetLogUtils.d("ok-http-line=================end=================");

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}