package com.example.emtravel.ui.roteiro;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.emtravel.R;
import com.example.emtravel.model.Destino;
import com.example.emtravel.model.Roteiro;
import com.example.emtravel.ui.roteiro.detalhe.DetalheRoteiroActivity;

import java.util.List;

public class RoteiroAdapter extends RecyclerView.Adapter<RoteiroAdapter.RoteiroViewHolder> {

    private List<Roteiro> roteiros;
    private OnRoteiroClickListener listener;

    public interface OnRoteiroClickListener {
        void onRoteiroClick(Roteiro roteiro);
        void onExcluirClick(Roteiro roteiro);
    }

    public RoteiroAdapter(List<Roteiro> roteiros, OnRoteiroClickListener listener) {
        this.roteiros = roteiros;
        this.listener = listener;
    }

    public void setRoteiros(List<Roteiro> roteiros) {
        this.roteiros = roteiros;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoteiroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_roteiro, parent, false);
        return new RoteiroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoteiroViewHolder holder, int position) {
        Roteiro roteiro = roteiros.get(position);

        holder.tvNomeRoteiro.setText(roteiro.getNome());
        holder.tvPeriodoRoteiro.setText(roteiro.getDataInicio() + " - " + roteiro.getDataFim());

        // Carrega imagem do primeiro destino (se existir)
        if (roteiro.getDestinos() != null && !roteiro.getDestinos().isEmpty()) {
            Destino primeiroDestino = roteiro.getDestinos().get(0);
            Glide.with(holder.itemView.getContext())
                    .load(primeiroDestino.getImagemUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.imageViewRoteiro);
        } else if (roteiro.getImagemUrl() != null && !roteiro.getImagemUrl().isEmpty()) {
            // Caso o roteiro tenha uma imagem própria
            Glide.with(holder.itemView.getContext())
                    .load(roteiro.getImagemUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.imageViewRoteiro);
        } else {
            // Fallback: imagem padrão
            holder.imageViewRoteiro.setImageResource(R.drawable.placeholder_image);
        }

        // Clique no card → abre detalhes do roteiro
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoteiroClick(roteiro);
            }
        });

        // Clique no botão de lixeira → excluir roteiro
        holder.btnExcluirRoteiro.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExcluirClick(roteiro);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roteiros != null ? roteiros.size() : 0;
    }

    static class RoteiroViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRoteiro;
        TextView tvNomeRoteiro;
        TextView tvPeriodoRoteiro;
        ImageButton btnExcluirRoteiro;

        public RoteiroViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRoteiro = itemView.findViewById(R.id.imageViewRoteiro);
            tvNomeRoteiro = itemView.findViewById(R.id.tvNomeRoteiro);
            tvPeriodoRoteiro = itemView.findViewById(R.id.tvPeriodoRoteiro);
            btnExcluirRoteiro = itemView.findViewById(R.id.btnExcluirRoteiro);
        }
    }
}

