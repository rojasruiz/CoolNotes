package com.alejandro.coolnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText tittle;
    private EditText description;
    private ArrayList<Note> noteList;
    private Note note;
    private LinearLayout linearNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        getSupportActionBar().setTitle("");

        tittle = findViewById(R.id.etNoteTittle);
        description = findViewById(R.id.etNoteDescription);

        initNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Set image on view
            ImageView iv = new ImageView(this);
            linearNote = findViewById(R.id.linear_notes);
            linearNote.addView(iv);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
            lp.setMargins(10,10,10,10);
            iv.setLayoutParams(lp);
            iv.setImageBitmap(imageBitmap);

            //Add to Persistence
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageByte = baos.toByteArray();

            note.getPhotos().add(imageByte);

            //Add click listener

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    for (int i = 2; i < linearNote.getChildCount(); i++) {
                        if (linearNote.getChildAt(i).equals(iv)){
                            note.getPhotos().remove(i-2);
                            linearNote.removeView(iv);
                        }
                    }
                    return true;
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("notesList", noteList);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PersistenceVault vault = new PersistenceVault();
        vault.setNotesList(noteList);
        vault.saveVaultToFile(getFilesDir());
    }

    private void initNote() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        noteList = (ArrayList<Note>) extras.get("notesList");
        Boolean newNote = extras.getBoolean("newNote");

        if (newNote) {
            note = noteList.get(noteList.size() - 1);
        } else {
            int notePos = extras.getInt("notePos");
            note = noteList.get(notePos);
        }

        tittle.setText(note.getTittle());
        description.setText(note.getDescription());

        tittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTittle(tittle.getText().toString());
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setDescription(description.getText().toString());
            }
        });

        ArrayList<byte[]> photos = note.getPhotos();

        for(byte[] photo : photos){
            setPhotoInView(photo);
        }
    }

    private void setPhotoInView(byte[] photo) {

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

        ImageView iv = new ImageView(this);
        linearNote = findViewById(R.id.linear_notes);
        linearNote.addView(iv);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
        lp.setMargins(10,10,10,10);
        iv.setLayoutParams(lp);
        iv.setImageBitmap(imageBitmap);

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                for (int i = 2; i < linearNote.getChildCount(); i++) {
                    if (linearNote.getChildAt(i).equals(iv)){
                        note.getPhotos().remove(i-2);
                        linearNote.removeView(iv);
                    }
                }
                return true;
            }
        });
    }
}