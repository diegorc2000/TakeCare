package com.diealbalb.publicaciones;

import com.google.firebase.database.Exclude;

public class Anuncio {
    private String mNombre;
    private String mDescripcion;
    private String mImageUrl;
    private String mKey;

    public Anuncio() {
        //empty constructor needed
    }

    public Anuncio(String nombre, String descripcion, String imageUrl) {
        if (nombre.trim().equals("") && descripcion.trim().equals("")) {
            nombre = "No nombre";
            descripcion = "No descripcion";

        }

        mNombre = nombre;
        mDescripcion = descripcion;
        mImageUrl = imageUrl;
    }

    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }

    public void setmDescripcion(String mDescripcion) {
        this.mDescripcion = mDescripcion;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}