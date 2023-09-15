package com.example.arny.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetail extends AppCompatActivity {

    EditText editTitle, editSubtitle;
    TextView editNoteTime;
    String title, subtitle, docID, timestamp;
    boolean isEdit = false;
    ImageView btnDelte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);

        editTitle = findViewById(R.id.noteTitle);
        editSubtitle = findViewById(R.id.noteSubtile);
        editNoteTime = findViewById(R.id.noteTime);
        btnDelte = findViewById(R.id.btnDelete);

        btnDelte.setVisibility(View.INVISIBLE);
        editNoteTime.setText(Utility.timeStampToString(Timestamp.now()));

        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        timestamp = getIntent().getStringExtra("timestamp");
        docID = getIntent().getStringExtra("docID");

        if (docID != null && !docID.isEmpty()) {
            isEdit = true;
            editTitle.setText(title);
            editSubtitle.setText(subtitle);
            editNoteTime.setText(timestamp);
            btnDelte.setVisibility(View.VISIBLE);
        }

        btnDelte.setOnClickListener(view -> deleteNote());
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.btnSave).setOnClickListener(view -> saveNote());
    }

    private void deleteNote() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docID);

        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NoteDetail.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else Toast.makeText(NoteDetail.this, "Delete failed", Toast.LENGTH_SHORT).show();
        });
    }

    void saveNote() {
        String title = editTitle.getText().toString();
        String subTitle = editSubtitle.getText().toString();

        Note note = new Note();
        note.setTitle(title);
        note.setSubtitle(subTitle);
        note.setTimestamp(Timestamp.now());

        saveToFirebase(note);
    }

    void saveToFirebase(Note note) {
        DocumentReference documentReference;
        if (isEdit)
            documentReference = Utility.getCollectionReferenceForNotes().document(docID);
        else
            documentReference = Utility.getCollectionReferenceForNotes().document();


        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NoteDetail.this, "Save successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else Toast.makeText(NoteDetail.this, "Save failed", Toast.LENGTH_SHORT).show();
        });
    }
}