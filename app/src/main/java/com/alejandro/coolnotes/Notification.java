package com.alejandro.coolnotes;

import java.io.Serializable;
import java.util.Calendar;

public class Notification  implements Serializable {

    private Calendar date;
    private String tittle;
    private String description;
    private int id;

    public Notification() {
    }

    public Notification(Calendar date, String tittle, String description, int id) {
        this.date = date;
        this.tittle = tittle;
        this.description = description;
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
