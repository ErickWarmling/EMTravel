package com.example.emtravel.ui.roteiro.cadastro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emtravel.R;
import com.example.emtravel.model.Destino;
import com.example.emtravel.model.Roteiro;
import com.example.emtravel.ui.roteiro.cadastro.adapter.DestinoSelecaoAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CadastroRoteiroActivity extends AppCompatActivity {

    private CadastroRoteiroViewModel viewModel;
    private EditText edNomeRoteiro, edDataInicio, edDataFim;
    private RecyclerView recyclerViewDestinos;
    private Button btnSalvar;
    private DestinoSelecaoAdapter adapter;
    private List<Destino> destinosSelecionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_roteiro);

        viewModel = new ViewModelProvider(this).get(CadastroRoteiroViewModel.class);

        edNomeRoteiro = findViewById(R.id.edNomeRoteiro);
        edDataInicio = findViewById(R.id.edDataInicio);
        edDataFim = findViewById(R.id.edDataFim);
        recyclerViewDestinos = findViewById(R.id.recyclerViewDestinos);
        btnSalvar = findViewById(R.id.btnSalvarRoteiro);
        ImageView btnVoltar = findViewById(R.id.btnVoltar);

        recyclerViewDestinos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DestinoSelecaoAdapter(new ArrayList<>(), destino -> {
            if (destinosSelecionados.contains(destino)) {
                destinosSelecionados.remove(destino);
            } else {
                destinosSelecionados.add(destino);
            }
        });
        recyclerViewDestinos.setAdapter(adapter);

        // Date pickers
        configurarDatePicker(edDataInicio);
        configurarDatePicker(edDataFim);

        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> salvarRoteiro());

        observarViewModel();
        viewModel.carregarDestinos(this);
    }

    private void configurarDatePicker(EditText editText) {
        editText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                editText.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
            }, year, month, day).show();
        });
    }

    private void observarViewModel() {
        viewModel.getDestinosLiveData().observe(this, destinos -> {
            if (destinos != null) adapter.setDestinos(destinos);
        });

        viewModel.getCadastroSucesso().observe(this, sucesso -> {
            if (Boolean.TRUE.equals(sucesso)) {
                Toast.makeText(this, "Roteiro cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getMensagemErro().observe(this, erro -> {
            if (erro != null && !erro.isEmpty()) {
                Toast.makeText(this, erro, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void salvarRoteiro() {
        String nome = edNomeRoteiro.getText().toString().trim();
        String inicio = edDataInicio.getText().toString().trim();
        String fim = edDataFim.getText().toString().trim();

        if (destinosSelecionados.isEmpty()) {
            Toast.makeText(this, "Selecione ao menos um destino.", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagemUrl = destinosSelecionados.get(0).getImagemUrl();

        Roteiro roteiro = new Roteiro(null, nome, inicio, fim, destinosSelecionados, imagemUrl);
        viewModel.salvarRoteiro(roteiro);
    }
}
