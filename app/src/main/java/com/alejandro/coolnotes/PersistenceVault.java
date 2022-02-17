package com.alejandro.coolnotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PersistenceVault {
    private ArrayList<Note> notesList;

    public PersistenceVault() {
    }

    public  PersistenceVault(File path){
        loadVaultFromFile(path);
    }

    public ArrayList<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    public void loadVaultFromFile(File path) {

        File fileName = new File(path, "/" + "vault.dat");

        // Deserialization
        try {
            // Reading the object from the file
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ArrayList<Note> notes = (ArrayList<Note>) in.readObject();

            in.close();
            file.close();

            this.notesList = notes;

        } catch (IOException ex) {
            System.out.println("Vault not found");
            this.notesList = new ArrayList<Note>();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

    }

    public void saveVaultToFile(File path) {

        File fileName = new File(path, "/" + "vault.dat");

        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(this.notesList);

            out.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }
    }
}
