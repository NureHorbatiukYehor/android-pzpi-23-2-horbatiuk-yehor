package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.content.res.Configuration;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_EDIT_NOTE = 1;
    private static final int REQUEST_SETTINGS = 2;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> notes;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            return;
        }

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notes = loadNotesFromDatabase();
        adapter = new NoteAdapter(notes, position -> {
            selectedPosition = position;
            registerForContextMenu(recyclerView);
            recyclerView.showContextMenu();
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.button_add_note).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Note> loadNotesFromDatabase() {
        List<Note> notes = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRIORITY));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATETIME));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_PATH));

                notes.add(new Note(id, title, description, priority, dateTime, imagePath));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();

        return notes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SETTINGS && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("settings_changed", false)) {
                recreate();
            }
        } else if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            long noteId = data.getLongExtra(EditNoteActivity.EXTRA_NOTE_ID, -1);
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            int priority = data.getIntExtra("priority", 1);
            String dateTime = data.getStringExtra("dateTime");
            String imagePath = data.getStringExtra("imagePath");
            int position = data.getIntExtra(EditNoteActivity.EXTRA_NOTE_POSITION, -1);

            Note note = new Note(noteId, title, description, priority, dateTime, imagePath);

            if (position == -1) {

                notes.add(note);
                adapter.notifyItemInserted(notes.size() - 1);
                recyclerView.scrollToPosition(notes.size() - 1);
            } else {

                notes.set(position, note);
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Обрати дію");
        menu.add(0, v.getId(), 0, "Редагувати");
        menu.add(0, v.getId(), 1, "Видалити");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Редагувати")) {
            editNote();
            return true;
        } else if (item.getTitle().equals("Видалити")) {
            deleteNote();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void editNote() {
        Note note = notes.get(selectedPosition);
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("priority", note.getPriority());
        intent.putExtra("dateTime", note.getDateTime());
        intent.putExtra("imagePath", note.getImagePath());
        intent.putExtra(EditNoteActivity.EXTRA_NOTE_POSITION, selectedPosition);
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }

    private void deleteNote() {
        Note note = notes.get(selectedPosition);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});

        db.close();

        notes.remove(selectedPosition);
        adapter.notifyItemRemoved(selectedPosition);
        Toast.makeText(this, "Нотатку видалено!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if ((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) !=
                (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)) {
            recreate();
        }
    }
}




