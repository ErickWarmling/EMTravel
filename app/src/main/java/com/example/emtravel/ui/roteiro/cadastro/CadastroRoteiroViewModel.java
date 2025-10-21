package com.example.emtravel.ui.roteiro.cadastro;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Destino;
import com.example.emtravel.model.Roteiro;
import com.example.emtravel.service.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroRoteiroViewModel extends ViewModel {

    private final MutableLiveData<List<Destino>> destinosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cadastroSucesso = new MutableLiveData<>();
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    private final DatabaseReference roteiroRef;
    private final FirebaseAuth auth;

    public CadastroRoteiroViewModel() {
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "sem_usuario";
        roteiroRef = FirebaseDatabase.getInstance().getReference("roteiros").child(uid);
    }

    public LiveData<List<Destino>> getDestinosLiveData() {
        return destinosLiveData;
    }

    public LiveData<Boolean> getCadastroSucesso() {
        return cadastroSucesso;
    }

    public LiveData<String> getMensagemErro() {
        return mensagemErro;
    }

    public void carregarDestinos(Context context) {
        RetrofitClient client = new RetrofitClient(context);
        client.getApiService().getDestinos().enqueue(new Callback<List<Destino>>() {
            @Override
            public void onResponse(@NonNull Call<List<Destino>> call, @NonNull Response<List<Destino>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    destinosLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Destino>> call, @NonNull Throwable t) {
                mensagemErro.postValue("Erro ao carregar destinos.");
            }
        });
    }

    public void salvarRoteiro(Roteiro roteiro) {
        if (roteiro.getNome() == null || roteiro.getNome().isEmpty()) {
            mensagemErro.setValue("Informe o nome do roteiro.");
            return;
        }

        if (roteiro.getDestinos() == null || roteiro.getDestinos().isEmpty()) {
            mensagemErro.setValue("Selecione ao menos um destino.");
            return;
        }

        if (roteiro.getDataInicio() == null || roteiro.getDataInicio().isEmpty() ||
                roteiro.getDataFim() == null || roteiro.getDataFim().isEmpty()) {
            mensagemErro.setValue("Informe as datas de início e fim.");
            return;
        }

        // Verifica se a data de início é antes da data de fim
        try {
            long inicio = Long.parseLong(roteiro.getDataInicio().replaceAll("[^0-9]", ""));
            long fim = Long.parseLong(roteiro.getDataFim().replaceAll("[^0-9]", ""));
            if (inicio > fim) {
                mensagemErro.setValue("A data de início não pode ser posterior à data de fim.");
                return;
            }
        } catch (Exception ignored) { }

        String id = roteiroRef.push().getKey();
        if (id != null) {
            roteiro.setUid(id);
            roteiroRef.child(id).setValue(roteiro)
                    .addOnSuccessListener(aVoid -> cadastroSucesso.setValue(true))
                    .addOnFailureListener(e -> mensagemErro.setValue("Erro ao salvar roteiro."));
        }
    }
}
