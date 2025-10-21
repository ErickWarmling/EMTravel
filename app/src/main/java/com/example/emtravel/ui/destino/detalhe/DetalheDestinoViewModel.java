package com.example.emtravel.ui.destino.detalhe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Destino;

public class DetalheDestinoViewModel extends ViewModel {

    private final MutableLiveData<Destino> destinoLiveData;

    public DetalheDestinoViewModel() {
        destinoLiveData = new MutableLiveData<>();
    }

    public void setDestino(Destino destino) {
        destinoLiveData.setValue(destino);
    }

    public LiveData<Destino> getDestino() {
        return destinoLiveData;
    }
}
