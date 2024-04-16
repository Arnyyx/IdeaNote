package com.example.arny.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText editEmail, editPassword, editConfirmPassword;
    private FirebaseAuth auth;
    private Button btnSignUp;
    private CircularProgressIndicator progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        auth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editConfirmPassword = findViewById(R.id.confirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progress = findViewById(R.id.progress);

        findViewById(R.id.textSignIn).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());

        btnSignUp.setOnClickListener(view -> {
            startLoading();
            clickSignUp();
        });

    }

    private void clickSignUp() {
        String email = editEmail.getText().toString(),
                password = editPassword.getText().toString(),
                confirmPassword = editConfirmPassword.getText().toString();
        if (!Utility.isValidEmailAddress(email)) {
            editEmail.setError(getText(R.string.invalid_email_address));
            editEmail.setError(getText(R.string.invalid_email_address));
            stopLoading();
        } else if (!Utility.isValidPassword(password)) {
            editPassword.setError(getText(R.string.invalid_password));
            stopLoading();
        } else if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError(getText(R.string.passwords_does_not_match));
            stopLoading();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(command -> {
                Toast.makeText(SignUp.this, R.string.sign_up_successfully, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, SignIn.class));
            }).addOnFailureListener(e -> {
                Toast.makeText(SignUp.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
            }).addOnCompleteListener(task -> {
                stopLoading();
            });
        }
    }

    private void startLoading() {
        btnSignUp.setClickable(false);
        btnSignUp.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.show();
    }

    public void stopLoading() {
        btnSignUp.setClickable(true);
        btnSignUp.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
        progress.hide();
    }

}
