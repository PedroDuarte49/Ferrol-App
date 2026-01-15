package com.example.ferrol_app;

public class Foro {
    private int id;
    private String titulo;
    private String descripcion;

    public Foro(int id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }
    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}