package com.example.arny.Controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;


public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Configuration configuration = getResources().getConfiguration();
        Locale systemLocale = configuration.locale;

// Set the application locale.
        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                startActivity(new Intent(Splash.this, SignIn.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                startActivity(new Intent(Splash.this, Main.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            finish();
        }, 1);
    }
}
