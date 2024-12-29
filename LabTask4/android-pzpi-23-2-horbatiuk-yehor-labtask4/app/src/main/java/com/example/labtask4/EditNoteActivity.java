package com.example.labtask4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Spinner prioritySpinner;
    private ImageView noteImageView;
    private Button chooseImageButton;
    private String imagePath = "";
    public static final String EXTRA_NOTE_POSITION = "note_position";
    private static final int REQUEST_CODE_PICK_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.edit_note_title);
        descriptionEditText = findViewById(R.id.edit_note_description);
        prioritySpinner = findViewById(R.id.edit_note_priority);
        noteImageView = findViewById(R.id.note_image_view);
        chooseImageButton = findViewById(R.id.button_choose_image);
        Button saveButton = findViewById(R.id.button_save);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            titleEditText.setText(intent.getStringExtra("title"));
            descriptionEditText.setText(intent.getStringExtra("description"));
            prioritySpinner.setSelection(intent.getIntExtra("priority", 0) - 1);

            String receivedImagePath = intent.getStringExtra("imagePath");
            if (receivedImagePath != null && !receivedImagePath.isEmpty()) {
                imagePath = receivedImagePath;
                try {

                    Uri imageUri = Uri.parse(imagePath);
                    noteImageView.setImageURI(imageUri);
                } catch (Exception e) {
                    noteImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    imagePath = "";
                }
            }
        }

        chooseImageButton.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> saveNote());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imagePath = selectedImageUri.toString();
                noteImageView.setImageURI(null);
                noteImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        int priority = prioritySpinner.getSelectedItemPosition() + 1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR_OF_DAY, 2);

        String currentDateTime = sdf.format(calendar.getTime());


        int position = getIntent().getIntExtra(EXTRA_NOTE_POSITION, -1);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("description", description);
        resultIntent.putExtra("priority", priority);
        resultIntent.putExtra("dateTime", currentDateTime);
        resultIntent.putExtra("imagePath", imagePath);
        resultIntent.putExtra(EXTRA_NOTE_POSITION, position);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

