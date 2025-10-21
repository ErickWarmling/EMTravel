package com.example.emtravel.ui.roteiro.detalhe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.emtravel.R;
import com.example.emtravel.model.Destino;
import com.example.emtravel.model.Roteiro;

public class DetalheRoteiroActivity extends AppCompatActivity {

    private DetalheRoteiroViewModel viewModel;
    private ImageView imgRoteiro, btnVoltar;
    private TextView tvNomeRoteiro, tvDataInicio, tvDataFim;
    private LinearLayout layoutDestinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_roteiro);

        // Referências dos elementos de layout
        imgRoteiro = findViewById(R.id.imgRoteiro);
        btnVoltar = findViewById(R.id.btnVoltar);
        tvNomeRoteiro = findViewById(R.id.tvNomeRoteiro);
        tvDataInicio = findViewById(R.id.tvDataInicio);
        tvDataFim = findViewById(R.id.tvDataFim);
        layoutDestinos = findViewById(R.id.layoutDestinos);

        // Inicializa o ViewModel
        viewModel = new ViewModelProvider(this).get(DetalheRoteiroViewModel.class);

        // Recupera o roteiro passado via Intent
        Roteiro roteiro = (Roteiro) getIntent().getSerializableExtra("roteiro");
        if (roteiro != null) {
            viewModel.setRoteiro(roteiro);
        }

        // Observa o LiveData<Roteiro> e atualiza a UI
        viewModel.getRoteiro().observe(this, r -> {
            if (r != null) {
                // Nome do roteiro centralizado
                tvNomeRoteiro.setText(r.getNome());

                // Datas separadas
                tvDataInicio.setText(r.getDataInicio());
                tvDataFim.setText(r.getDataFim());

                // Imagem principal (primeiro destino)
                if (r.getImagemUrl() != null && !r.getImagemUrl().isEmpty()) {
                    Glide.with(this)
                            .load(r.getImagemUrl())
                            .placeholder(R.drawable.placeholder_image)
                            .into(imgRoteiro);
                }

                // Lista de destinos estilizada
                layoutDestinos.removeAllViews();
                if (r.getDestinos() != null && !r.getDestinos().isEmpty()) {
                    for (Destino d : r.getDestinos()) {
                        TextView tvDestino = new TextView(this);
                        tvDestino.setText("📍 " + d.getNome() + " - " + d.getLocalizacao());
                        tvDestino.setTextSize(16);
                        tvDestino.setTextColor(getResources().getColor(R.color.black));
                        tvDestino.setPadding(8, 8, 8, 8);
                        layoutDestinos.addView(tvDestino);
                    }
                }
            }
        });

        // Botão voltar
        btnVoltar.setOnClickListener(v -> finish());
    }
}
