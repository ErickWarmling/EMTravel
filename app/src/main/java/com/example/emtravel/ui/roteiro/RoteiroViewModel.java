package com.example.emtravel.ui.roteiro;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Roteiro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoteiroViewModel extends ViewModel {

    private final MutableLiveData<List<Roteiro>> roteirosLiveData = new MutableLiveData<>();
    private final DatabaseReference roteiroRef;
    private final FirebaseAuth auth;

    public RoteiroViewModel() {
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        roteiroRef = FirebaseDatabase.getInstance().getReference("roteiros").child(uid != null ? uid : "sem_usuario");
        carregarRoteiros();
    }

    private void carregarRoteiros() {
        roteiroRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Roteiro> lista = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Roteiro roteiro = ds.getValue(Roteiro.class);
                    if (roteiro != null) {
                        lista.add(roteiro);
                    }
                }
                roteirosLiveData.setValue(lista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                roteirosLiveData.setValue(null);
            }
        });
    }

    public LiveData<List<Roteiro>> getRoteirosLiveData() {
        return roteirosLiveData;
    }

    public void adicionarRoteiro(Roteiro roteiro) {
        String id = roteiroRef.push().getKey();
        if (id != null) {
            roteiroRef.child(id).setValue(roteiro);
        }
    }

    public void excluirRoteiro(String idRoteiro) {
        roteiroRef.child(idRoteiro).removeValue();
    }
}
