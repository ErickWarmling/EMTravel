package com.example.emtravel.ui.roteiro.cadastro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.emtravel.R;
import com.example.emtravel.model.Destino;

import java.util.List;

public class DestinoSelecaoAdapter extends RecyclerView.Adapter<DestinoSelecaoAdapter.ViewHolder> {

    public interface OnDestinoSelecionadoListener {
        void onDestinoSelecionado(Destino destino);
    }

    private List<Destino> destinos;
    private OnDestinoSelecionadoListener listener;

    public DestinoSelecaoAdapter(List<Destino> destinos, OnDestinoSelecionadoListener listener) {
        this.destinos = destinos;
        this.listener = listener;
    }

    public void setDestinos(List<Destino> novos) {
        this.destinos = novos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destino_selecao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destino destino = destinos.get(position);
        holder.tvNome.setText(destino.getNome());
        holder.tvLocalizacao.setText(destino.getLocalizacao());
        Glide.with(holder.imageView.getContext())
                .load(destino.getImagemUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) listener.onDestinoSelecionado(destino);
            else listener.onDestinoSelecionado(destino);
        });
    }

    @Override
    public int getItemCount() {
        return destinos != null ? destinos.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNome, tvLocalizacao;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewDestino);
            tvNome = itemView.findViewById(R.id.tvNomeDestino);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacaoDestino);
            checkBox = itemView.findViewById(R.id.checkBoxSelecionar);
        }
    }
}
