package com.example.a81.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatApi {

    @POST("chat/completions")
    Call<OpenAIResponse> sendMessage(@Body OpenAIRequest request);
}