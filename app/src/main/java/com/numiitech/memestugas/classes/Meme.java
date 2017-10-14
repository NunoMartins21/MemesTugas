package com.numiitech.memestugas.classes;

import com.numiitech.memestugas.R;

/**
 * Created by Nuno Martins on 30-08-2017.
 */

public final class Meme {
    // Attributes
    private String name;
    private int idPath;

    // Constructor
    public Meme(String name, int idPath) {
        this.setName(name);
        this.setPath(idPath);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPath() {
        return idPath;
    }

    public void setPath(int idPath) {
        this.idPath = idPath;
    }

    // toString()
    @Override
    public String toString() {
        return this.getName();
    }
}
