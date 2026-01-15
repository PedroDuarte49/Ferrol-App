package com.example.ferrol_app;

public class Foro {
    private int id;
    private String titulo;
    private String contenido;

    public Foro(int id, String titulo, String contenido) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }
}