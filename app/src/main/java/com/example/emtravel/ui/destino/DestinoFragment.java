package com.example.emtravel.ui.destino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emtravel.databinding.FragmentDestinoBinding;

import java.util.ArrayList;

public class DestinoFragment extends Fragment {

    private FragmentDestinoBinding binding;
    private DestinoAdapter destinoAdapter;
    private DestinoViewModel destinoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDestinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Adapter
        destinoAdapter = new DestinoAdapter(new ArrayList<>());
        binding.recicleViewDestinos.setLayoutManager((new LinearLayoutManager(getContext())));
        binding.recicleViewDestinos.setAdapter(destinoAdapter);

        // ViewModel
        destinoViewModel = new ViewModelProvider(this).get(DestinoViewModel.class);

        destinoViewModel.getDestinosLiveData().observe(getViewLifecycleOwner(), destinos -> {
            if (destinos != null) {
                destinoAdapter.setDestinos(destinos);
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