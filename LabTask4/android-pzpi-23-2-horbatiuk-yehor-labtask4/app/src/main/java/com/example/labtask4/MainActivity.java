package com.example.labtask4;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteListener {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private List<Note> allNotes;
    private ImageButton buttonSearch;
    private ImageButton buttonAddNote;
    private SearchView searchView;
    private AlertDialog searchDialog;
    private String currentSearchQuery = "";
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_EDIT_NOTE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList = new ArrayList<>();
        allNotes = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);

        buttonSearch = findViewById(R.id.button_search);
        buttonAddNote = findViewById(R.id.button_add_note);

        buttonSearch.setOnClickListener(v -> {
            showSearchDialog();
        });

        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
        });
    }

    @Override
    public void onNoteClick(int position) {
        showNoteOptions(position);
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        searchView = new SearchView(this);
        searchView.setIconified(false);
        searchView.setQuery(currentSearchQuery, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                filterNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterNotes(newText);
                return true;
            }
        });
        builder.setView(searchView);
        searchDialog = builder.create();
        searchDialog.setOnDismissListener(dialog -> {
            if (currentSearchQuery.isEmpty()) {
                restoreAllNotes();
            }
        });
        searchDialog.show();
    }

    private void showNoteOptions(int position) {
        View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.note_options_menu);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                editNote(position);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                deleteNote(position);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void editNote(int position) {
        Note note = noteList.get(position);
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("priority", note.getPriority());
        intent.putExtra("imagePath", note.getImagePath());

        intent.putExtra(EditNoteActivity.EXTRA_NOTE_POSITION, position);

        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }

    private void deleteNote(int position) {
        if (position >= 0 && position < noteList.size()) {
            Note noteToRemove = noteList.get(position);
            noteList.remove(position);
            allNotes.remove(noteToRemove);
            noteAdapter.notifyItemRemoved(position);
        } else {
        }
    }

    private void filterNotes(String query) {
        currentSearchQuery = query;
        List<Note> filteredList = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        noteList.clear();
        noteList.addAll(filteredList);
        noteAdapter.notifyDataSetChanged();
    }

    private void restoreAllNotes() {
        currentSearchQuery = "";
        noteList.clear();
        noteList.addAll(allNotes);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            int priority = data.getIntExtra("priority", 1);
            String dateTime = data.getStringExtra("dateTime");
            String imagePath = data.getStringExtra("imagePath");

            if (requestCode == REQUEST_CODE_ADD_NOTE) {
                Note newNote = new Note(title, description, priority, dateTime, imagePath);
                allNotes.add(0, newNote);
                noteList.add(0, newNote);
                noteAdapter.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }
            else if (requestCode == REQUEST_CODE_EDIT_NOTE) {
                int position = data.getIntExtra(EditNoteActivity.EXTRA_NOTE_POSITION, -1);
                if (position != -1) {
                    Note updatedNote = new Note(title, description, priority, dateTime, imagePath);

                    noteList.set(position, updatedNote);

                    if (position < allNotes.size()) {
                        allNotes.set(position, updatedNote);
                    }
                    noteAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}





