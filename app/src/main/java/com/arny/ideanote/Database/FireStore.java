package com.arny.ideanote.Database;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.arny.ideanote.Model.Note;
import com.arny.ideanote.R;
import com.arny.ideanote.Utils.Utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStore {

    public void getAllDoc(final OnGetDataListener listener) {
        listener.onStart();
        List<Note> noteList = new ArrayList<>();
        Utility.getCollectionReferenceForNotes()
                .orderBy("pin", Query.Direction.DESCENDING)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Note note = new Note();
                            note.setId(document.getId());
                            note.setTitle(document.getString("title"));
                            note.setSubtitle(document.getString("subtitle"));
                            note.setTimestamp(document.getTimestamp("timestamp"));
                            note.setPin(Boolean.TRUE.equals(document.getBoolean("pin")));
                            noteList.add(note);
                        }
                        listener.onSuccess(noteList);
                    } else {
                        listener.onFailure();
                    }
                }).addOnFailureListener(e -> {
                    Log.d("MyTag", e.toString());
                });
    }

    public void setDoc(Context context, Note note, String docID) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", note.getTitle());
        data.put("subtitle", note.getSubtitle());
        data.put("timestamp", note.getTimestamp());
        data.put("pin", note.isPin());

        DocumentReference documentReference;
        if (docID == null)
            documentReference = Utility.getCollectionReferenceForNotes().document();
        else
            documentReference = Utility.getCollectionReferenceForNotes().document(docID);

        documentReference.set(data)
                .addOnCompleteListener(task -> Toast.makeText(context, R.string.note_saved, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, R.string.note_is_note_saved, Toast.LENGTH_SHORT).show());
    }


    public static void deleteDoc(Context context, String documentId) {
        Utility.getCollectionReferenceForNotes().document(documentId).delete()
                .addOnCompleteListener(task -> Toast.makeText(context, R.string.note_deleted, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, R.string.note_is_not_deleted, Toast.LENGTH_SHORT).show());
    }

    public static void togglePinNote(Context context, String docID, boolean isPin) {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docID);
        if (isPin)
            documentReference.update("pin", false)
                    .addOnCompleteListener(task -> Toast.makeText(context, "Unpinned note", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "There was error unpinned note", Toast.LENGTH_SHORT).show());
        else documentReference.update("pin", true)
                .addOnCompleteListener(task -> Toast.makeText(context, "Pinned note", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "There was error pinned note", Toast.LENGTH_SHORT).show());
    }


    public interface OnGetDataListener {
        void onSuccess(List<Note> noteList);

        void onStart();

        void onFailure();
    }
}
