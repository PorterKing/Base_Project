package com.porterking.netlibrary;


import com.porterking.netlibrary.interceptor.BaseUrlInterceptor;
import com.porterking.netlibrary.interceptor.HeadInterceptor;

import java.util.HashMap;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static ApiService SERVICE;

    public static final String BASE_URL_KEY = "base_url_key";
    public static final String BASE_IP = "base_ip";

    public static final String OTHER_IP = "other_ip";


    public static HashMap<String, String> BaseUrlMap = new HashMap<String, String>() {{
        put(BASE_IP, NetHelper.getInstance().getContext().getResources().getString(R.string.base_ip));
        put(OTHER_IP, NetHelper.getInstance().getContext().getString(R.string.other_ip));
    }};

    public static ApiService getDefault() {
        if (SERVICE == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BaseUrlInterceptor())
                    .addInterceptor(new HeadInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor())
                    .build();
            SERVICE = new Retrofit.Builder()
                    .baseUrl(NetHelper.getInstance().getContext().getResources().getString(R.string.base_ip))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build().create(ApiService.class);
        }
        return SERVICE;
    }

}
