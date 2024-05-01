package com.arny.ideanote.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.arny.ideanote.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utility {
    public static CollectionReference getCollectionReferenceForNotes() {
        return FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("Notes");
    }

    public static void makeNotification(Context context, String title, String content) {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), chanelID)
                .setSmallIcon(R.drawable.notes)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE + System.currentTimeMillis()), builder.build());
    }


    public static String timeStampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(timestamp.toDate());
    }

    public static boolean checkValidPassword(EditText editText) {
        String password = editText.getText().toString();
        if (password == null || password.isEmpty()) {
            editText.requestFocus();
            editText.setError("Password cannot be empty");
            return false;
        }

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])"
                + "(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=!])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        if (!m.matches()) {
            String errorMessage = "Password must meet the following requirements:\n" +
                    "- At least one digit (0-9).\n" +
                    "- At least one lowercase letter (a-z).\n" +
                    "- At least one uppercase letter (A-Z).\n" +
                    "- At least one special symbol (@#$%^&+=!).\n" +
                    "- No whitespace characters.\n" +
                    "- Length between 8 and 20 characters.\n";
            editText.requestFocus();
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}