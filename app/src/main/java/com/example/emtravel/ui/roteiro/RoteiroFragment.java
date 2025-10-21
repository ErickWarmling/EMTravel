package com.example.emtravel.ui.roteiro;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emtravel.R;
import com.example.emtravel.model.Roteiro;
import com.example.emtravel.ui.login.LoginActivity;
import com.example.emtravel.ui.roteiro.cadastro.CadastroRoteiroActivity;

import java.util.ArrayList;

public class RoteiroFragment extends Fragment implements RoteiroAdapter.OnRoteiroClickListener {

    private RecyclerView recyclerViewRoteiros;
    private TextView tvMensagemVazio;
    private Button btnAdicionarRoteiro;
    private RoteiroAdapter roteiroAdapter;
    private RoteiroViewModel roteiroViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_roteiro, container, false);

        recyclerViewRoteiros = view.findViewById(R.id.recyclerViewRoteiros);
        tvMensagemVazio = view.findViewById(R.id.tvMensagemVazio);
        btnAdicionarRoteiro = view.findViewById(R.id.btnAdicionarRoteiro);

        recyclerViewRoteiros.setLayoutManager(new LinearLayoutManager(getContext()));

        roteiroAdapter = new RoteiroAdapter(new ArrayList<>(), this);
        recyclerViewRoteiros.setAdapter(roteiroAdapter);

        roteiroViewModel = new ViewModelProvider(this).get(RoteiroViewModel.class);

        // 🔹 Mostra e configura o botão de logout na toolbar
        ImageView icLogout = requireActivity().findViewById(R.id.icLogout);
        if (icLogout != null) {
            icLogout.setOnClickListener(v -> {
                // Ação de logout
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });
        }


        observarRoteiros();
        configurarBotaoAdicionar();

        return view;
    }

    private void observarRoteiros() {
        roteiroViewModel.getRoteirosLiveData().observe(getViewLifecycleOwner(), roteiros -> {
            if (roteiros == null || roteiros.isEmpty()) {
                recyclerViewRoteiros.setVisibility(View.GONE);
                tvMensagemVazio.setVisibility(View.VISIBLE);
            } else {
                recyclerViewRoteiros.setVisibility(View.VISIBLE);
                tvMensagemVazio.setVisibility(View.GONE);
                roteiroAdapter.setRoteiros(roteiros);
            }
        });
    }

    private void configurarBotaoAdicionar() {
        btnAdicionarRoteiro.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CadastroRoteiroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRoteiroClick(Roteiro roteiro) {
        // Abre tela de detalhes do roteiro
        Intent intent = new Intent(getActivity(), com.example.emtravel.ui.roteiro.detalhe.DetalheRoteiroActivity.class);
        intent.putExtra("roteiro", roteiro);
        startActivity(intent);
    }

    @Override
    public void onExcluirClick(Roteiro roteiro) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Excluir Roteiro")
                .setMessage("Deseja realmente excluir o roteiro \"" + roteiro.getNome() + "\"?")
                .setPositiveButton("Excluir", (dialog, which) -> roteiroViewModel.excluirRoteiro(roteiro.getUid()))
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
