package com.alejandro.coolnotes;

import java.util.ArrayList;

public class PersistenceVault {
    private ArrayList<Note> notesList;

    public PersistenceVault() {
    }

    public ArrayList<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }
}
