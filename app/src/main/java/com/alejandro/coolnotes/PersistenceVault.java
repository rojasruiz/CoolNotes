package com.alejandro.coolnotes;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void saveVaultToCloud(File path, Activity activity) {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);

        if (acct != null) {
            try {

                File fileName = new File(path, "/" + "vault.dat");

                InputStream stream = new FileInputStream(fileName);

                String destiny = "vaults/vault" + acct.getId() + ".dat";
                StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(destiny);

                UploadTask uploadTask = mountainsRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "Save to cloud fail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("save to cloud success");
                    }
                });

            } catch (Exception e) {
                Toast.makeText(activity, "Save to cloud fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
