package com.example.ferrol_app;

public class Mensaje {
    private final String usuarioFecha;
    private final String texto;

    public Mensaje(String usuarioFecha, String texto) {
        this.usuarioFecha = usuarioFecha;
        this.texto = texto;
    }

    public String getUsuarioFecha() {
        return usuarioFecha;
    }

    public String getTexto() {
        return texto;
    }
}
