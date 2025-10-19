package com.example.emtravel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.emtravel.R;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private ImageView imageViewSetaVoltar;
    private EditText edNomeUsuario;
    private EditText edEmailUsuario;
    private EditText edDataNascimentoUsuario;
    private EditText edTelefoneUsuario;
    private EditText edSenhaUsuario;
    private Button btnCadastrarUsuario;
    private CadastroUsuarioViewModel cadastroUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_usuario);

        imageViewSetaVoltar = findViewById(R.id.imageViewSetaVoltar);
        edNomeUsuario = findViewById(R.id.edNomeUsuario);
        edEmailUsuario = findViewById(R.id.edEmailUsuario);
        edDataNascimentoUsuario = findViewById(R.id.edDataNascimentoUsuario);
        edTelefoneUsuario = findViewById(R.id.edTelefoneUsuario);
        edSenhaUsuario = findViewById(R.id.edSenhaUsuario);
        btnCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);

        cadastroUsuarioViewModel = new ViewModelProvider(this).get(CadastroUsuarioViewModel.class);

        cadastroUsuarioViewModel.getStatusCadastro().observe(this, status -> {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

            if ("Cadastro realizado com sucesso!".equals(status)) {
                voltarParaLogin();
            }
        });

        configurarSetaVoltar();
        configurarBotaoCadastrarUsuario();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarBotaoCadastrarUsuario() {
        btnCadastrarUsuario.setOnClickListener(v -> {
            String nome = edNomeUsuario.getText().toString().trim();
            String email = edEmailUsuario.getText().toString().trim();
            String dataNasc = edDataNascimentoUsuario.getText().toString().trim();
            String telefone = edTelefoneUsuario.getText().toString().trim();
            String senha = edSenhaUsuario.getText().toString().trim();

            cadastroUsuarioViewModel.salvarUsuario(nome, email, dataNasc, telefone, senha);
        });
    }

    private void configurarSetaVoltar() {
        imageViewSetaVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void voltarParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Volta para a tela de login
    }

}