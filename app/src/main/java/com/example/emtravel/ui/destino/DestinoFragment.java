package com.example.emtravel.ui.destino;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emtravel.databinding.FragmentDestinoBinding;
import com.example.emtravel.model.Destino;
import com.example.emtravel.ui.destino.detalhe.DetalheDestinoActivity;

import java.util.ArrayList;
import java.util.List;

public class DestinoFragment extends Fragment {

    private FragmentDestinoBinding binding;
    private DestinoAdapter destinoAdapter;
    private DestinoViewModel destinoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDestinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ViewModel
        destinoViewModel = new ViewModelProvider(this).get(DestinoViewModel.class);

        // Configura RecycleView
        binding.recicleViewDestinos.setLayoutManager((new LinearLayoutManager(getContext())));

        // Inicia adapter com lista vazia e listener de clique
        destinoAdapter = new DestinoAdapter(new ArrayList<>(), destino -> {
            // Ao clicar em um destino, abre a tela de detalhes
            Intent intent = new Intent(getContext(), DetalheDestinoActivity.class);
            intent.putExtra("destino", destino);
            startActivity(intent);
        });

        binding.recicleViewDestinos.setAdapter(destinoAdapter);

        // Observa mudanças na lista de destinos
        destinoViewModel.getDestinosLiveData().observe(getViewLifecycleOwner(), new Observer<List<Destino>>() {
            @Override
            public void onChanged(List<Destino> destinos) {
                if (destinos != null) {
                    destinoAdapter.setDestinos(destinos);
                }
            }
        });

        destinoViewModel.carregarDestinos(getContext());

        final TextView textView = binding.tvTituloDestino;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}