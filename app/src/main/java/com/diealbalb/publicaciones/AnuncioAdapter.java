package com.diealbalb.publicaciones;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diealbalb.R;

import java.util.List;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.ImageViewHolder>{

    private Context mContext;
    private List<Anuncio> mPublicacion;
    private OnItemClickListener mListener;

    public AnuncioAdapter(Context mContext, List<Anuncio> mPublicacion) {
        this.mContext = mContext;
        this.mPublicacion = mPublicacion;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.imagen_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Anuncio publicacionCorriente = mPublicacion.get(position);
        holder.tvName.setText(publicacionCorriente.getName());
        Glide.with(mContext)
                .load(publicacionCorriente.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fitCenter()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mPublicacion.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView tvName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNamei);
            imageView = itemView.findViewById(R.id.ivAnuncio);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selecciona una accion");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Haz lo que sea");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Borrar");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnItemClickListner(OnItemClickListener listner) {
        mListener = listner;
    }
}
