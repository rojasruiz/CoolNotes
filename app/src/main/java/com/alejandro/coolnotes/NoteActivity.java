package com.alejandro.coolnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    private EditText tittle;
    private EditText description;
    private ArrayList<Note> noteList;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        tittle = findViewById(R.id.etNoteTittle);
        description = findViewById(R.id.etNoteDescription);

        initNote();
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("notesList", noteList);
        setResult(RESULT_OK, result);
        finish();
    }

    private void initNote() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        noteList = (ArrayList<Note>) extras.get("notesList");
        Boolean newNote = intent.getExtras().getBoolean("newNote");

        if (newNote){
            note = noteList.get(noteList.size()-1);
        }else{
            note = new Note(); //TODO change this
        }

        tittle.setText(note.getTittle());
        description.setText(note.getDescription());

        tittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setTittle(tittle.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    }
}