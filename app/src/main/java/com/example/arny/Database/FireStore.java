package com.example.arny.Database;


import android.content.Context;
import android.widget.Toast;

import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
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
                            note.setId(document.getId());
                            note.setTitle(document.getString("title"));
                            note.setSubtitle(document.getString("subtitle"));
                            note.setTimestamp(document.getTimestamp("timestamp"));
                            noteList.add(note);
                        }
                        listener.onSuccess(noteList);
                    } else {
                        listener.onFailure();
                    }
                });
    }

    public void setDoc(Context context, Note note, String docID) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", note.getTitle());
        data.put("subtitle", note.getSubtitle());
        data.put("timestamp", note.getTimestamp());

        DocumentReference documentReference;
        if (docID == null)
            documentReference = Utility.getCollectionReferenceForNotes().document();
        else
            documentReference = Utility.getCollectionReferenceForNotes().document(docID);

        documentReference.set(data)
                .addOnCompleteListener(task -> Toast.makeText(context, R.string.note_saved, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, R.string.note_is_note_saved, Toast.LENGTH_SHORT).show());
    }

    public void deleteDoc(Context context, String documentId) {
        Utility.getCollectionReferenceForNotes().document(documentId).delete()
                .addOnCompleteListener(task -> Toast.makeText(context, R.string.note_deleted, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, R.string.note_is_not_deleted, Toast.LENGTH_SHORT).show());
    }


    public interface OnGetDataListener {
        void onSuccess(List<Note> noteList);

        void onStart();

        void onFailure();
    }
}
