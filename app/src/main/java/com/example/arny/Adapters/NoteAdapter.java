package com.example.arny.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arny.Controller.Main;
import com.example.arny.Controller.NoteDetail;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Random;


public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.noteViewholder> {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull noteViewholder holder, int position, @NonNull Note note) {
        int[] androidColors = holder.itemView.getResources().getIntArray(R.array.itemColor);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.itemView.setBackgroundColor(randomAndroidColor);

        holder.title.setText(note.getTitle());
        holder.time.setText(Utility.timeStampToString(note.getTimestamp()));
        holder.subtitle.setText(note.getSubtitle());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NoteDetail.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("subtitle", note.getSubtitle());
            intent.putExtra("timestamp", Utility.timeStampToString(note.getTimestamp()));
            String docID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docID", docID);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public noteViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.noteViewholder(view);
    }

    class noteViewholder extends RecyclerView.ViewHolder {
        TextView title, subtitle, time;

        public noteViewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitleTextView);
            subtitle = itemView.findViewById(R.id.noteSubtitleTextView);
            time = itemView.findViewById(R.id.noteTimeTextView);
        }
    }

}

