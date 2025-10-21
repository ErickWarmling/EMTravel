package com.example.emtravel.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.emtravel.MainActivity;
import com.example.emtravel.R;
import com.example.emtravel.ui.cadastroUsuario.CadastroUsuarioActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView tvNomeApp;
    private EditText edEmail;
    private EditText edSenha;
    private Button btnEntrar;
    private TextView tvCadastrar;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        tvNomeApp = findViewById(R.id.tvNomeApp);
        String texto = "EMTravel";

        Spannable spannable = new SpannableString(texto);
        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.orange_primary)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.white)), 2, texto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNomeApp.setText(spannable);

        edEmail = findViewById(R.id.edEmail);
        edSenha = findViewById(R.id.edSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvCadastrar = findViewById(R.id.tvCadastrar);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getStatsLogin().observe(this, status -> {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

            if ("Login realizado com sucesso!".equals(status)) {
                abrirTelaPrincipal();
            }
        });

        configurarBotaoEntrar();
        configurarAcaoCadastrar();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarBotaoEntrar() {
        btnEntrar.setOnClickListener(v -> {
            String email = edEmail.getText().toString().trim();
            String senha = edSenha.getText().toString().trim();

            loginViewModel.autenticarUsuario(email, senha);
        });
    }

    private void configurarAcaoCadastrar() {
        tvCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}