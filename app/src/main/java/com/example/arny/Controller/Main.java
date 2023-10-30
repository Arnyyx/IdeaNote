package com.example.arny.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.arny.Adapters.NoteAdapter;
import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FireStore fireStore;
    private EditText editSearch;
    private ImageView btnClear, btnSetLayout;
    private ShimmerFrameLayout shimmerLayout;
    private RecyclerView.LayoutManager layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    public static int dataChange = 0;
    private TextView tvEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnClear = findViewById(R.id.btnClear);
        btnSetLayout = findViewById(R.id.btnSetLayout);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        tvEmpty = findViewById(R.id.tvEmpty);
        fireStore = new FireStore();
        setUpRecyclerView(null);

        findViewById(R.id.btnNew).setOnClickListener(view -> startActivity(new Intent(this, NoteDetail.class)));
        findViewById(R.id.btnSetting).setOnClickListener(view -> startActivity(new Intent(this, Settings.class)));
        btnSetLayout.setOnClickListener(view -> {
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                btnSetLayout.setImageResource(R.drawable.ic_list);
                layout = new GridLayoutManager(this, 1);
            } else {
                btnSetLayout.setImageResource(R.drawable.ic_gridview);
                layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            }
            recyclerView.setLayoutManager(layout);
//            setUpRecyclerView(null);
        });
        btnClear.setOnClickListener(view -> reset());

        editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnClear.setVisibility(View.VISIBLE);
                btnSetLayout.setVisibility(View.GONE);
                String strSearch = editSearch.getText().toString();
                setUpRecyclerView(strSearch);

            }
        });
        editSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                editSearch.clearFocus();
                hideKeyboard();
                return true;
            }
            return false;
        });


        swipeRefreshLayout.setOnRefreshListener(() -> reset());

    }

    private void reset() {
        editSearch.setText("");
        editSearch.clearFocus();
        btnClear.setVisibility(View.GONE);
        btnSetLayout.setVisibility(View.VISIBLE);
        hideKeyboard();
        setUpRecyclerView(null);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }

    private void setUpRecyclerView(String strSearch) {
        fireStore.getAllDoc("timestamp", new FireStore.OnGetDataListener() {
            @Override
            public void onSuccess(List<Note> noteList) {
                tvEmpty.setVisibility(View.GONE);
                if (strSearch != null) noteList = search(strSearch, noteList);
                if (noteList.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                noteAdapter = new NoteAdapter(Main.this, noteList);
                recyclerView.setLayoutManager(layout);
                recyclerView.setAdapter(noteAdapter);
                shimmerLayout.stopShimmerAnimation();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onStart() {
//                shimmerLayout.startShimmerAnimation();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onFailure() {
                Toast.makeText(Main.this, R.string.load_data_failed, Toast.LENGTH_SHORT).show();
                shimmerLayout.stopShimmerAnimation();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<Note> search(String strSearch, List<Note> noteList) {
        strSearch = strSearch.toLowerCase();
        if (strSearch.matches("")) {
            return noteList;
        } else {
            List<Note> listResult = new ArrayList<>();
            for (Note note : noteList) {
                if (note.getTitle().toLowerCase().contains(strSearch) ||
                        note.getSubtitle().toLowerCase().contains(strSearch) ||
                        Utility.timeStampToString(note.getTimestamp()).toLowerCase().contains(strSearch)) {
                    listResult.add(note);
                }
            }
            return listResult;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataChange == 0) return;
        setUpRecyclerView(null);
        dataChange = 0;
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