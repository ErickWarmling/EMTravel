package com.example.emtravel.ui.destino;

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

import java.util.List;

public class DestinoAdapter extends RecyclerView.Adapter<DestinoAdapter.DestinoViewHolder> {

    private List<Destino> destinos;

    public DestinoAdapter(List<Destino> destinos) {
        this.destinos = destinos;
    }

    public void setDestinos(List<Destino> destinos) {
        this.destinos = destinos;
    }

    @NonNull
    @Override
    public DestinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destino, parent, false);
        return new DestinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinoViewHolder holder, int position) {
        Destino destino = destinos.get(position);

        holder.tvNomeDestino.setText(destino.getNome());
        holder.tvLocalizacao.setText(destino.getLocalizacao());
        holder.tvCategoria.setText(destino.getCategoria());

        Glide.with(holder.imageViewDestino.getContext())
                .load(destino.getImagemUrl())
                .into(holder.imageViewDestino);
    }

    @Override
    public int getItemCount() {
        return destinos.size();
    }

    static class DestinoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDestino;
        TextView tvNomeDestino;
        TextView tvLocalizacao;
        TextView tvCategoria;
        ImageButton btnFavoritar;

        public DestinoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDestino = itemView.findViewById(R.id.imageViewDestino);
            tvNomeDestino = itemView.findViewById(R.id.tvNomeDestino);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            btnFavoritar = itemView.findViewById(R.id.btnFavoritar);
        }
    }
}
