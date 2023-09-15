package com.example.arny.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.textSignIn).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());

        EditText editEmail = findViewById(R.id.email),
                editPassword = findViewById(R.id.password),
                editConfirmPassword = findViewById(R.id.confirmPassword);


        findViewById(R.id.btnSignUp).setOnClickListener(view -> {
            String email = editEmail.getText().toString(),
                    password = editPassword.getText().toString(),
                    confirmPassword = editConfirmPassword.getText().toString();

            if (emailCheck(email) && passwordCheck(password)) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Sign up successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, SignIn.class));
                    } else Toast.makeText(SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                });
            }

        });
    }

    public boolean emailCheck(String a) {
        return (!(a == null) && Patterns.EMAIL_ADDRESS.matcher(a).matches());
    }

    public boolean passwordCheck(String a) {
        if (a.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
