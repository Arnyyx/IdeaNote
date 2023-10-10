package com.example.arny.Controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.google.firebase.Timestamp;

public class NoteDetail extends AppCompatActivity {
    private TextView title, time, subtitle;
    private int color;
    private FireStore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);

        title = findViewById(R.id.noteTitle);
        time = findViewById(R.id.noteTime);
        subtitle = findViewById(R.id.noteSubtile);
        color = ContextCompat.getColor(this, R.color.black);
        fireStore = new FireStore();

        time.setText(Utility.timeStampToString(Timestamp.now()));


        findViewById(R.id.btnSave).setOnClickListener(view -> saveNote());

        findViewById(R.id.view).setBackgroundColor(color);
    }

    private void saveNote() {
        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setSubtitle(subtitle.getText().toString());
        note.setTimestamp(Timestamp.now());
        note.setColor(String.valueOf(color));
        fireStore.setDoc(this, note);
        finish();
    }
}