package com.arny.ideanote.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arny.ideanote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;


public class Splash extends AppCompatActivity {
    public static File localFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(() -> {
            createNotificationChannel();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                startActivity(new Intent(Splash.this, SignIn.class));
            } else {
                startActivity(new Intent(Splash.this, Main.class));
                try {
                    localFile = File.createTempFile("images", "jpg");
                    FirebaseStorage.getInstance().getReference().child("profile_images")
                            .child(currentUser.getUid()).getFile(localFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1);
    }

    public void createNotificationChannel() {
        String description = "Some description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("CHANNEL_ID_NOTIFICATION",
                "CHANNEL_ID_NOTIFICATION",
                importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
