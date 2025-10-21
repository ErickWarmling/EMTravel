package com.example.emtravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emtravel.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewLogoEMTravel;
    private TextView tvLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        imageViewLogoEMTravel = findViewById(R.id.imageViewLogoEMTravel);
        tvLogo = findViewById(R.id.tvLogo);

        String nomeApp = "EMTravel";
        SpannableString spannable = new SpannableString(nomeApp);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_primary)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogo.setText(spannable);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        imageViewLogoEMTravel.startAnimation(anim);
        tvLogo.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}