package com.example.emtravel.ui.destino;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Destino;
import com.example.emtravel.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinoViewModel extends ViewModel {

    private MutableLiveData<List<Destino>> destinosLiveData;

    public DestinoViewModel() {
        destinosLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Destino>> getDestinosLiveData() {
        return destinosLiveData;
    }

    public void carregarDestinos(Context context) {
        RetrofitClient client = new RetrofitClient(context);
        client.getApiService().getDestinos().enqueue(new Callback<List<Destino>>() {
            @Override
            public void onResponse(Call<List<Destino>> call, Response<List<Destino>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    destinosLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Destino>> call, Throwable t) {
                destinosLiveData.postValue(null);
            }
        });
    }
}