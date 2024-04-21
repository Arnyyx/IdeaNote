package com.example.arny.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arny.Controller.NoteDetail;
import com.example.arny.Database.FireStore;
import com.example.arny.Model.Note;
import com.example.arny.R;
import com.example.arny.Utils.Utility;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {

    private final Context context;
    private final List<Note> noteList;
    private OnMenuItemClicked listener;

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
        holder.notePin.setVisibility(View.GONE);
        if (note.isPin()) holder.notePin.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(view -> {
            Utility.getCollectionReferenceForNotes().document().getId();
            Intent intent = new Intent(context, NoteDetail.class);
            intent.putExtra("isEdit", 1);
            intent.putExtra("docID", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("time", Utility.timeStampToString(note.getTimestamp()));
            intent.putExtra("subtitle", note.getSubtitle());
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
            popupMenu.inflate(R.menu.popup_menu);

            if (note.isPin()) popupMenu.getMenu().getItem(0).setTitle("Unpin note");
            else popupMenu.getMenu().getItem(0).setTitle("Pin note");

            popupMenu.setOnMenuItemClickListener(menuItem -> menuItemClicked(menuItem, noteList.get(position)));
            popupMenu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewholder extends RecyclerView.ViewHolder {
        public TextView noteTitleTextView, noteTimeTextView, noteSubtitleTextView;
        public ImageView notePin;

        public NoteViewholder(@NonNull View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            noteTimeTextView = itemView.findViewById(R.id.noteTimeTextView);
            noteSubtitleTextView = itemView.findViewById(R.id.noteSubtitleTextView);
            notePin = itemView.findViewById(R.id.notePinIcon);
        }
    }

    private boolean menuItemClicked(MenuItem item, Note note) {
        switch (item.getItemId()) {
            case R.id.pin:
                FireStore.togglePinNote(context, note.getId(), note.isPin());
                listener.onClickedPin();
                break;
            case R.id.delete:
                listener.onClickedDelete(note.getId());
                break;
            default:
                Toast.makeText(context, getClass().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    public interface OnMenuItemClicked {
        void onClickedPin();
        void onClickedDelete(String noteID);
    }


    public void setOnItemClickListener(OnMenuItemClicked listener) {
        this.listener = listener;
    }

}

