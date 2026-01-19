package com.example.ferrol_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForoAdapter extends RecyclerView.Adapter<ForoAdapter.ForoViewHolder> {

    private List<Foro> listaForos;
    private OnForoClickListener listener;

    public interface OnForoClickListener {
        void onForoClick(Foro foro);
    }

    public ForoAdapter(List<Foro> listaForos, OnForoClickListener listener) {
        this.listaForos = listaForos;
        this.listener = listener;
    }

    public void updateData(List<Foro> nuevaLista) {
        this.listaForos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_foro, parent, false);
        return new ForoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForoViewHolder holder, int position) {
        Foro foro = listaForos.get(position);
        holder.tvTitulo.setText(foro.getTitulo());

        holder.itemView.setOnClickListener(v -> listener.onForoClick(foro));
    }

    @Override
    public int getItemCount() {
        return listaForos.size();
    }

    static class ForoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;

        ForoViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
        }
    }
}

