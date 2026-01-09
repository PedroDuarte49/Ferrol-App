package com.example.ferrol_app;

public class Foro {

    private String id;
    private String descripcion;

    public Foro(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}