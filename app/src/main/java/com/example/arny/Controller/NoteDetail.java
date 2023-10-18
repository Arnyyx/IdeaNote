package com.example.arny.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.google.firebase.Timestamp;

public class NoteDetail extends AppCompatActivity {
    private TextView title, time, subtitle;
    private String noteID;
    private FireStore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);

        title = findViewById(R.id.noteTitle);
        time = findViewById(R.id.noteTime);
        subtitle = findViewById(R.id.noteSubtile);
        fireStore = new FireStore();

        time.setText(Utility.timeStampToString(Timestamp.now()));

        Intent intent = this.getIntent();
        if (getIntent().getIntExtra("isEdit", 0) == 1) {
            noteID = getIntent().getStringExtra("docID");
            title.setText(getIntent().getStringExtra("title"));
            time.setText(getIntent().getStringExtra("time"));
            subtitle.setText(getIntent().getStringExtra("subtitle"));
            time.setText(intent.getStringExtra("time"));
        }


        findViewById(R.id.btnSave).setOnClickListener(view -> saveNote());
        findViewById(R.id.btnDelete).setOnClickListener(view -> deleteNote(noteID));
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
    }

    private void deleteNote(String noteID) {
        fireStore.deleteDoc(this, noteID);
        finish();
    }

    private void saveNote() {
        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setSubtitle(subtitle.getText().toString());
        note.setTimestamp(Timestamp.now());
        fireStore.setDoc(this, note);
        finish();
    }
}