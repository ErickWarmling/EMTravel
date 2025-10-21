package com.example.emtravel.ui.roteiro.detalhe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Roteiro;

public class DetalheRoteiroViewModel extends ViewModel {

    private final MutableLiveData<Roteiro> roteiroLiveData = new MutableLiveData<>();

    public void setRoteiro(Roteiro roteiro) {
        roteiroLiveData.setValue(roteiro);
    }

    public LiveData<Roteiro> getRoteiro() {
        return roteiroLiveData;
    }
}
