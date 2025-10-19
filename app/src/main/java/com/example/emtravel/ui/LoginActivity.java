package com.example.emtravel.ui;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emtravel.R;

public class LoginActivity extends AppCompatActivity {

    private TextView tvNomeApp;

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}