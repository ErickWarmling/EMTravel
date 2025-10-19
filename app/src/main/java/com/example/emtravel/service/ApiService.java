package com.example.emtravel.service;

import com.example.emtravel.model.Destino;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("destinos")
    Call<List<Destino>> getDestinos();
}
