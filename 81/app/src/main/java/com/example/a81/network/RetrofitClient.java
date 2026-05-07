package com.example.a81.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://api.clarifai.com/v2/ext/openai/v1/";

    private static final String API_KEY = ""; // ADD API Key

    private static ChatApi api;

    public static ChatApi getApi() {

        if (api == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {

                        Request request = chain.request().newBuilder()
                                .header("Authorization", "Bearer " + API_KEY)
                                .header("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(ChatApi.class);
        }

        return api;
    }
}
