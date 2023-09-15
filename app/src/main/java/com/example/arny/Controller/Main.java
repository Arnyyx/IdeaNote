package com.example.arny.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arny.Adapters.NoteAdapter;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class Main extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    int spanCount = 2;
    public static Activity main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main = this;

        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.btnSetLayout).setOnClickListener(view -> {
            //Todo setlayout
        });
        findViewById(R.id.btnNew).setOnClickListener(view -> startActivity(new Intent(this, NoteDetail.class)));
        findViewById(R.id.btnSetting).setOnClickListener(view -> startActivity(new Intent(this, Settings.class)));
        findViewById(R.id.ic_search).setOnClickListener(view -> search());

        setUpRecyclerView();

    }

    private void search() {
        //Todo search
        EditText editSearch = findViewById(R.id.editSearch);
        String stringSearch = editSearch.getText().toString();

        if (stringSearch.equals("")) {
            setUpRecyclerView();
            noteAdapter.startListening();
            noteAdapter.notifyDataSetChanged();
            return;
        }

        Query query = Utility.getCollectionReferenceForNotes().orderBy("title").startAt(stringSearch).endAt(stringSearch + '~');
        ;
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();

        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        noteAdapter = new NoteAdapter(options, this);

        recyclerView.setAdapter(noteAdapter);
        noteAdapter.startListening();
        noteAdapter.notifyDataSetChanged();
    }

    void setUpRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
        Toast.makeText(main, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
        Toast.makeText(main, "Stop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
        Toast.makeText(main, "Resume", Toast.LENGTH_SHORT).show();
    }

}

//Todo Item ClickListener ✓
//Todo Pin note
//Todo Search
//Todo Arrange
//Todo Setting
//Todo Set Layout
//Todo add image
//Todo Notes color
//Todo Upload App
//Todo logout

