package com.example.arny.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.arny.Adapters.NoteAdapter;
import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.List;

public class Main extends AppCompatActivity {
    private RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    public static Activity main;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Note> noteList;
    FireStore fireStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main = this;

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fireStore = new FireStore();

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
        fireStore.getAllDoc("timestamp", new FireStore.OnGetDataListener() {
            @Override
            public void onSuccess(List<Note> noteList) {
                noteAdapter = new NoteAdapter(Main.this, noteList);
                recyclerView.setLayoutManager(new GridLayoutManager(Main.this, 2));
                recyclerView.setAdapter(noteAdapter);
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure() {
                Toast.makeText(Main.this, "Load data failed", Toast.LENGTH_SHORT).show();
            }
        });

        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp");
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpRecyclerView();
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
