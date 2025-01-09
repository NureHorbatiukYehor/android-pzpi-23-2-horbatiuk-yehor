package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.TypedValue;
import android.content.Context;  // Для Context
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private OnNoteListener onNoteListener;

    public NoteAdapter(List<Note> noteList, OnNoteListener onNoteListener) {
        this.noteList = noteList;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.noteTitle.setText(note.getTitle());
        holder.noteDateTime.setText(note.getDateTime());
        holder.noteDescription.setText(note.getDescription());

        if (note.getImagePath() != null && !note.getImagePath().isEmpty()) {
            try {
                holder.noteIcon.setImageURI(Uri.parse(note.getImagePath()));
            } catch (Exception e) {
                holder.noteIcon.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.noteIcon.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        switch (note.getPriority()) {
            case 1:
                holder.notePriority.setImageResource(android.R.drawable.star_off);
                break;
            case 2:
                holder.notePriority.setImageResource(android.R.drawable.star_on);
                break;
            case 3:
                holder.notePriority.setImageResource(android.R.drawable.btn_star_big_on);
                break;
            default:
                holder.notePriority.setImageResource(android.R.drawable.ic_menu_help);
                break;
        }

        float fontScale = new SettingsManager(holder.itemView.getContext()).getFontScale();
        holder.noteTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16 * fontScale);
        holder.noteDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14 * fontScale);
        holder.noteDateTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12 * fontScale);

        Context context = holder.itemView.getContext();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int textColor = typedValue.data;

        holder.noteTitle.setTextColor(textColor);
        holder.noteDescription.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle, noteDateTime, noteDescription;
        ImageView noteIcon, notePriority;
        OnNoteListener onNoteListener;

        public NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDateTime = itemView.findViewById(R.id.note_date_time);
            noteDescription = itemView.findViewById(R.id.note_description);
            noteIcon = itemView.findViewById(R.id.note_icon);
            notePriority = itemView.findViewById(R.id.note_priority);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onNoteListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onNoteListener.onNoteClick(position);
                }
            }
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}