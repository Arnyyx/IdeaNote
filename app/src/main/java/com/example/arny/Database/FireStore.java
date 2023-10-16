package com.example.arny.Database;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.arny.Model.Note;
import com.example.arny.Utils.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStore {

    public void getAllDoc(String orderBy, final OnGetDataListener listener) {
        listener.onStart();
        List<Note> noteList = new ArrayList<>();
        Utility.getCollectionReferenceForNotes()
                .orderBy(orderBy, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Note note = new Note();
                            note.setTitle(document.getString("title"));
                            note.setSubtitle(document.getString("subtitle"));
                            note.setTimestamp(document.getTimestamp("timestamp"));
                            noteList.add(note);
                        }
                        listener.onSuccess(noteList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        listener.onFailure();
                    }
                });
    }

    public void setDoc(Context context, Note note) {
        CollectionReference collectionReference = Utility.getCollectionReferenceForNotes();

        Map<String, Object> data = new HashMap<>();
        data.put("id", note.getId());
        data.put("title", note.getTitle());
        data.put("subtitle", note.getSubtitle());
        data.put("timestamp", note.getTimestamp());
        data.put("color", note.getColor());

        DocumentReference documentReference = collectionReference.document();
        documentReference.set(data)
                .addOnCompleteListener(task -> Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Note is note saved", Toast.LENGTH_SHORT).show());
    }

    public static void deleteDoc(String documentId) {
        Utility.getCollectionReferenceForNotes().document(documentId).delete();
    }


    public interface OnGetDataListener {
        void onSuccess(List<Note> noteList);

        void onStart();

        void onFailure();
    }
}
