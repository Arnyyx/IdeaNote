package com.example.arny.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText editEmail, editPassword;

    private FirebaseAuth auth;
    private Button btnSignIn;
    private CircularProgressIndicator progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        auth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);

//        findViewById(R.id.btnSignIn).setOnClickListener(view -> clickSignIn());

        btnSignIn = findViewById(R.id.btnSignIn);
        progress = findViewById(R.id.progress);
        btnSignIn.setOnClickListener(v -> {
            startLoading();
            clickSignIn();
        });

        findViewById(R.id.textSignUp).setOnClickListener(view -> {
            startActivity(new Intent(this, SignUp.class));
        });
        findViewById(R.id.tvForgotPassword).setOnClickListener(view -> {
            showDialogResetPassword();
        });
    }

    private void startLoading() {
        btnSignIn.setClickable(false);
        btnSignIn.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.show();
    }

    private void showDialogResetPassword() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_reset_password, null);
        final EditText editEmail = promptsView.findViewById(R.id.editEmail);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true)
                .setTitle(R.string.reset_password)
                .setMessage(R.string.reset_password_description)
                .setView(promptsView)
                .setPositiveButton(R.string.get_email, null)
                .setNeutralButton(R.string.cancel, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            FirebaseAuth.getInstance().sendPasswordResetEmail(editEmail.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, R.string.email_sent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_send_email, Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.dismiss();
        });
    }

    public void clickSignIn() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            startActivity(new Intent(SignIn.this, Main.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }).addOnFailureListener(e -> {
                Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(command -> stopLoading());

    }

    public void stopLoading() {
        btnSignIn.setClickable(true);
        btnSignIn.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
        progress.hide();
    }
}
