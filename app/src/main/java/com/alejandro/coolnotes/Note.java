package com.alejandro.coolnotes;

import java.io.Serializable;

public class Note implements Serializable {
    private String tittle;
    private String description;

    public Note() {
        tittle = "";
        description = "";
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
}
