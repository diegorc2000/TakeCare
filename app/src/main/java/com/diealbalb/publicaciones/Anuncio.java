package com.diealbalb.publicaciones;

import com.google.firebase.database.Exclude;

public class Anuncio {
    private String aName;
    private String aImageUrl;
    private String aKey;

    public Anuncio() {
        //empty constructor needed
    }

    public Anuncio(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        aName = name;
        aImageUrl = imageUrl;
    }

    public String getName() {
        return aName;
    }

    public void setName(String name) {
        aName = name;
    }

    public String getImageUrl() {
        return aImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        aImageUrl = imageUrl;
    }

    @Exclude
    public String getmKey() {
        return aKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.aKey = mKey;
    }
}
