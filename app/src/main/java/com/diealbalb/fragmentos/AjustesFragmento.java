package com.diealbalb.fragmentos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.diealbalb.R;
import com.diealbalb.listeners.OnControlerFragmentListener;

public class AjustesFragmento extends Fragment {

    private OnControlerFragmentListener listener;
    ImageView imgAjustes;
    Button btnNosotros;
    Button btnBibliografia;
    Button btnDesconectarse;

    public AjustesFragmento() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        imgAjustes = view.findViewById(R.id.imgAjustes);
        Glide.with(this).load(R.drawable.ic_ajustes).into(imgAjustes);

        btnNosotros = (Button) view.findViewById(R.id.btnNosotros);
        btnBibliografia = (Button) view.findViewById(R.id.btnPrivacidadSeguridad);
        btnDesconectarse = (Button) view.findViewById(R.id.btnDesconectarse);

        btnNosotros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = "nosotros";

                listener.selectFrgment(texto);
            }
        });

        btnBibliografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = "bibliografia";

                listener.selectFrgment(texto);
            }
        });

        btnDesconectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = "desconectarse";

                listener.selectFrgment(texto);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnControlerFragmentListener) {
            listener = (OnControlerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;

    }
}