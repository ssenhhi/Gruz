package com.example.gruz.network;

import android.content.Context;

import com.example.gruz.utils.TokenStorage;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://95.31.156.238:8081/";
    private static Retrofit retrofit;
    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            TokenStorage tokenStorage = new TokenStorage(context);
            Interceptor authInterceptor = chain -> {
                Request original = chain.request();
                String token = tokenStorage.getAccessToken();
                Request.Builder builder = original.newBuilder();
                if (token != null && !token.isEmpty()) {
                    builder.header("Authorization", "Bearer " + token);
                }
                return chain.proceed(builder.build());
            };
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(authInterceptor)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ApiService getApiService(Context context) {
        return getRetrofit(context).create(ApiService.class);
    }
}
