package com.example.arny.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.arny.Adapters.NoteAdapter;
import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;

import java.util.List;

public class Main extends AppCompatActivity {
    private RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    public static Activity main;
    SwipeRefreshLayout swipeRefreshLayout;
    FireStore fireStore;
    List<Note> noteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main = this;

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        fireStore = new FireStore();
        noteList = fireStore.getAllDoc("timestamp");

        setUpRecyclerView();

        findViewById(R.id.btnNew).setOnClickListener(view -> startActivity(new Intent(this, NoteDetail.class)));
        findViewById(R.id.btnSetting).setOnClickListener(view -> startActivity(new Intent(this, Settings.class)));
        findViewById(R.id.ic_search).setOnClickListener(view -> {
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setUpRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setUpRecyclerView() {
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.starli
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
//Todo Upload Photo
//Todo logout ✓
