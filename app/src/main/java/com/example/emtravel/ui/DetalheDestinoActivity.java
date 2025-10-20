package com.example.emtravel.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.emtravel.R;
import com.example.emtravel.model.Destino;

import java.io.Serializable;

public class DetalheDestinoActivity extends AppCompatActivity {

    private ImageView imageViewSetaVoltar;
    private ImageView imageViewDestino;
    private TextView tvDestino;
    private TextView tvLocalizacaoDestino;
    private TextView tvCategoriaDestino;
    private TextView tvDesricaoDestino;
    private TextView tvDetalhesDescricao;
    private DetalheDestinoViewModel detalheDestinoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe_destino);

        imageViewSetaVoltar = findViewById(R.id.imageViewSetaVoltar);
        imageViewDestino = findViewById(R.id.imageViewDestino);
        tvDestino = findViewById(R.id.tvDestino);
        tvLocalizacaoDestino = findViewById(R.id.tvLocalizacaoDestino);
        tvCategoriaDestino = findViewById(R.id.tvCategoriaDestino);
        tvDesricaoDestino = findViewById(R.id.tvDescricaoDestino);
        tvDetalhesDescricao = findViewById(R.id.tvDetalhesDescricaoDestino);

        detalheDestinoViewModel = new ViewModelProvider(this).get(DetalheDestinoViewModel.class);

        receberDestinoDoIntent();
        configurarSetaVoltar();
        observarDestino();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void receberDestinoDoIntent() {
        Serializable serializable = getIntent().getSerializableExtra("destino");
        // Verifica se o objeto é do tipo Destino
        if (serializable instanceof Destino) {
            detalheDestinoViewModel.setDestino((Destino) serializable);
        }
    }

    private void configurarSetaVoltar() {
        imageViewSetaVoltar.setOnClickListener(v -> finish());
    }

    private void observarDestino() {
        detalheDestinoViewModel.getDestino().observe(this, destino -> {
            if (destino == null) {
                return;
            }

            tvDestino.setText(destino.getNome());
            tvLocalizacaoDestino.setText("Localização: " + destino.getLocalizacao());
            tvCategoriaDestino.setText("Categoria: " + destino.getCategoria());
            tvDesricaoDestino.setText("Descrição:");
            tvDetalhesDescricao.setText(destino.getDescricao());

            Glide.with(this)
                    .load(destino.getImagemUrl())
                    .into(imageViewDestino);
        });
    }
}