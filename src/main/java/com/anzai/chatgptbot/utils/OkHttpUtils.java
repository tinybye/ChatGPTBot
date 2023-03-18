package com.anzai.chatgptbot.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求
 *
 * @author tinybye
 * @date 2023/3/17
 */
@Slf4j
public class OkHttpUtils {

    private static Long DEFAULT_TIME_OUT = 30L;

    public static String post(String url, String cookie, RequestBody requestBody, Map<String, String> header) throws Exception {

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("post请求,result:{}", result);
        return result;
    }

    public static String get(String url, String cookie, Map<String, String> header) throws Exception {

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("get请求,result:{}", result);
        return result;
    }

    public static String get(String url) throws Exception {

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("get请求,result:{}", result);
        return result;
    }

}
