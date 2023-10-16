package com.example.arny.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arny.Controller.NoteDetail;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import java.util.List;


public class NoteAdapter extends FirestoreRecyclerAdapter<> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewholder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitleTextView.setText(note.getTitle());
        holder.noteTimeTextView.setText(Utility.timeStampToString(note.getTimestamp()));
        holder.noteSubtitleTextView.setText(note.getSubtitle());
        holder.itemView.setOnClickListener(view -> {
            Utility.getCollectionReferenceForNotes().document().getId();
            Intent intent = new Intent(context, NoteDetail.class);
            intent.putExtra("isEdit", 1);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("time", Utility.timeStampToString(note.getTimestamp()));
            intent.putExtra("subtitle", note.getSubtitle());
            intent.putExtra("color", note.getColor());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewholder extends RecyclerView.ViewHolder {
        public TextView noteTitleTextView, noteTimeTextView, noteSubtitleTextView;

        public NoteViewholder(@NonNull View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            noteTimeTextView = itemView.findViewById(R.id.noteTimeTextView);
            noteSubtitleTextView = itemView.findViewById(R.id.noteSubtitleTextView);
        }
    }
}

