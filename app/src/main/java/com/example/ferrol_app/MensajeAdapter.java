package com.example.ferrol_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MensajeAdapter  extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {
    private final List<Mensaje> mensajes;

    public MensajeAdapter(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usuarioFecha, mensaje;

        ViewHolder(View view) {
            super(view);
            usuarioFecha = view.findViewById(R.id.txtUsuarioFecha);
            mensaje = view.findViewById(R.id.txtMensaje);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje m = mensajes.get(position);
        holder.usuarioFecha.setText(m.getUsuarioFecha());
        holder.mensaje.setText(m.getTexto());
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }
}
