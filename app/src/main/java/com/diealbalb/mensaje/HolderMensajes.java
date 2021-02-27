package com.diealbalb.mensaje;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diealbalb.R;

public class HolderMensajes extends RecyclerView.ViewHolder {
    private TextView nombre;
    private TextView mensaje;
    private TextView hora;
    private ImageView fotoMensajePerfil;
    private ImageView fotoMensaje;

    public HolderMensajes(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombreMensaje);
        mensaje = itemView.findViewById(R.id.mensajeMensaje);
        hora = itemView.findViewById(R.id.horaMensaje);
        fotoMensajePerfil = itemView.findViewById(R.id.fotoPerfilMensaje);
        fotoMensaje= itemView.findViewById(R.id.mensajeFoto);

    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public ImageView getFotoMensajePerfil() {
        return fotoMensajePerfil;
    }

    public void setFotoMensajePerfil(ImageView fotoMensajePerfil) {
        this.fotoMensajePerfil = fotoMensajePerfil;
    }

}
