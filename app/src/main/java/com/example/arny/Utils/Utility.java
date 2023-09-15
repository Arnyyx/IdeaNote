package com.example.arny.Utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utility {
    public static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").
                document(currentUser.getUid()).collection("my_notes");
    }

    public static String timeStampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(timestamp.toDate());
    }
}
