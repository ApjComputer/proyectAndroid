package com.apjcompany.apjcomputer.proyecto.model;

public class Usuario {
    private String administrador;
    private String password;
    private String name;

    public Usuario() {
    }

    public Usuario(String esAdmin, String password, String name) {
        this.administrador=esAdmin;
        this.password = password;
        this.name = name;
    }

    public Usuario(String nombre, String password) {
        administrador="false";
        this.name = nombre;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Usuario: " + name;
    }

}
