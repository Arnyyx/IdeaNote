package com.example.arny.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.signOut).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Settings.this, Splash.class);
            startActivity(intent);
            Main.main.finish();
            finish();
        });
    }
}
