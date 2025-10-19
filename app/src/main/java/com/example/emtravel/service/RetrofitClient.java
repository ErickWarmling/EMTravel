package com.example.emtravel.service;

import static retrofit2.converter.jackson.JacksonConverterFactory.create;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {

    private Retrofit retrofit;

    public RetrofitClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://mock.api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    static class MockInterceptor implements Interceptor {

        private Context context;

        public MockInterceptor(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
           Request request = chain.request();
           String responseString = "";

           if (request.url().encodedPath().equals("/destinos")) {
               InputStream inputStream = context.getAssets().open("destinos.json");
               int tamanho = inputStream.available();
               byte[] buffer = new byte[tamanho];
               inputStream.read(buffer);
               inputStream.close();
               responseString = new String(buffer);
           }

           return new Response.Builder()
                   .code(200)
                   .message(responseString)
                   .request(request)
                   .protocol(Protocol.HTTP_1_1)
                   .body(ResponseBody.create(responseString.getBytes(),
                           okhttp3.MediaType.parse("application/json")))
                   .addHeader("content-type", "application/json")
                   .build();
        }
    }
}
