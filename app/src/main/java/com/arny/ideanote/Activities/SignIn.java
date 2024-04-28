package com.arny.ideanote.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arny.ideanote.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private EditText mEditEmail, mEditPassword;
    private String mStringEmail, mStringPassword;
    private FirebaseAuth auth;
    private Button mButtonSignIn;
    private CircularProgressIndicator progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        auth = FirebaseAuth.getInstance();

        mEditEmail = findViewById(R.id.email);
        mEditPassword = findViewById(R.id.password);

        mButtonSignIn = findViewById(R.id.btnSignIn);
        progress = findViewById(R.id.progress);


        clickSignIn();
        findViewById(R.id.textSignUp).setOnClickListener(view -> startActivity(new Intent(this, SignUp.class)));
        findViewById(R.id.tvForgotPassword).setOnClickListener(view -> showDialogResetPassword());
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
        mButtonSignIn.setOnClickListener(v -> {
            startLoading();
            if (isValidateInputField())
                auth.signInWithEmailAndPassword(mStringEmail, mStringPassword)
                        .addOnSuccessListener(authResult -> {
                            startActivity(new Intent(SignIn.this, Main.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
                        }).addOnCompleteListener(command -> stopLoading());
            else stopLoading();
        });

    }

    private boolean isValidateInputField() {
        mStringEmail = mEditEmail.getText().toString().trim();
        mStringPassword = mEditPassword.getText().toString();
        if (mStringEmail.isBlank()) {
            mEditEmail.requestFocus();
            mEditEmail.setError("Email is empty");
            return false;
        }
        if (mStringPassword.isBlank()) {
            mEditPassword.requestFocus();
            mEditPassword.setError("Password is empty");
            return false;
        }
        return true;
    }

    private void startLoading() {
        mButtonSignIn.setClickable(false);
        mButtonSignIn.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.show();
    }

    public void stopLoading() {
        mButtonSignIn.setClickable(true);
        mButtonSignIn.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
        progress.hide();
    }
}
