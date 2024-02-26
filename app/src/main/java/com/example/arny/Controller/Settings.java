package com.example.arny.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.arny.R;
import com.example.arny.Utils.GlideApp;
import com.example.arny.Utils.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Settings extends AppCompatActivity {
    private ImageView avt;
    private ShimmerFrameLayout shimmerFrameLayout;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(user.getUid());
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(user.getEmail());
        avt = findViewById(R.id.avt);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        shimmerFrameLayout.startShimmerAnimation();
        if (Splash.localFile != null) {
            GlideApp.with(avt).load(Splash.localFile).into(avt);
        } else {
            GlideApp.with(avt).load(storageRef).into(avt);
        }
        shimmerFrameLayout.stopShimmerAnimation();

        avt.setOnClickListener(view -> selectImage());
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.tvSignOut).setOnClickListener(view -> clickSignOut());
        findViewById(R.id.tvChangePassword).setOnClickListener(view -> changePasswordDialog());
    }

    private void clickSignOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true)
                .setTitle(R.string.sign_out)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(R.string.sign_out, (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNeutralButton(R.string.cancel, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            avt.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        shimmerFrameLayout.startShimmerAnimation();
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, R.string.upload_successful, Toast.LENGTH_SHORT).show();
            shimmerFrameLayout.stopShimmerAnimation();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
            shimmerFrameLayout.stopShimmerAnimation();
        });
    }

    private void changePasswordDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_change_password, null);
        final EditText editCurrentPassword = promptsView.findViewById(R.id.editCurrentPassword);
        final EditText editNewPassword = promptsView.findViewById(R.id.editNewPassword);
        final EditText editConfirmPassword = promptsView.findViewById(R.id.editConfirmPassword);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true)
                .setTitle(R.string.change_password)
                .setPositiveButton(R.string.save, null)
                .setNeutralButton(R.string.cancel, (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String currentPassword = editCurrentPassword.getText().toString(),
                    newPassword = editNewPassword.getText().toString(),
                    confirmPassword = editConfirmPassword.getText().toString();
            if (!Utility.isValidPassword(newPassword)) {
                editNewPassword.setError(getString(R.string.password_is_not_valid));
            } else if (!newPassword.equals(confirmPassword)) {
                editConfirmPassword.setError(getString(R.string.passwords_does_not_match));
            } else {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(Settings.this, R.string.password_updated_successfully, Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(Settings.this, R.string.password_update_failed, Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                });
                            } else {
                                editCurrentPassword.setError(getString(R.string.password_is_not_correct));
                            }
                        });
            }
        });


    }
}
