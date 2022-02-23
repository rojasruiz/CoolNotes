package com.alejandro.coolnotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class PersistenceVault implements Serializable {
    private ArrayList<Note> notesList;
    private ArrayList<Notification> notificationsList;

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

    public ArrayList<Notification> getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(ArrayList<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public void loadVaultFromFile(File path) {

        File fileName = new File(path, "/" + "vault.dat");

        // Deserialization
        try {
            // Reading the object from the file
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            PersistenceVault vault = (PersistenceVault) in.readObject();

            in.close();
            file.close();

            this.notesList = vault.getNotesList();
            this.notificationsList = vault.getNotificationsList();

            System.out.println(notesList);
            System.out.println(notificationsList);

        } catch (IOException ex) {
            System.out.println("Vault not found");
            this.notesList = new ArrayList<Note>();
            this.notificationsList = new ArrayList<Notification>();
            saveVaultToFile(path);
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
            out.writeObject(this);

            out.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        }
    }
}
