package com.example.arny.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText editEmail, editPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        auth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);


        findViewById(R.id.btnSignIn).setOnClickListener(view -> clickLogin());

        findViewById(R.id.textSignUp).setOnClickListener(view -> {
            startActivity(new Intent(this, SignUp.class));
        });
    }

    public void clickLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(SignIn.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this, Main.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show());
    }
}
