package com.example.arny.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;

public class NoteDetail extends AppCompatActivity {
    private TextView title, time, subtitle;
    private String noteID = null;
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
        } else {
            subtitle.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }


        findViewById(R.id.btnSave).setOnClickListener(view -> saveNote(noteID));
        findViewById(R.id.btnDelete).setOnClickListener(view -> deleteNote(noteID));
        findViewById(R.id.btnBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.btnPickColor).setOnClickListener(view -> showColorPicker());
    }

    private void showColorPicker() {

    }

    public void deleteNote(String noteID) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_delete)
                .setTitle(R.string.confirm)
                .setMessage(R.string.do_you_want_to_delete_this_note)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    FireStore.deleteDoc(this, noteID);
                    Main.dataChange = 1;
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void saveNote(String noteID) {
        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setSubtitle(subtitle.getText().toString());
        note.setTimestamp(Timestamp.now());
        note.setPin(false);
        fireStore.setDoc(this, note, noteID);
        Main.dataChange = 1;
        finish();
    }
}