package com.apjcompany.apjcomputer.proyecto.model;

public class Jugador implements Comparable<Jugador> {
    protected String nombre;
    protected short goles,puntos;
    protected byte partidosWin,partidosLose,partidosPlay;

    public Jugador() {
    }

    public Jugador(String nombre, short goles) {
        this.nombre = nombre;
        this.goles = goles;
    }

    public Jugador(String nombre, short goles, short puntos, byte partidosWin, byte partidosLose, byte partidosPlay) {
        this.nombre = nombre;
        this.goles = goles;
        this.puntos = puntos;
        this.partidosWin = partidosWin;
        this.partidosLose = partidosLose;
        this.partidosPlay = partidosPlay;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getGoles() {
        return goles;
    }

    public void setGoles(short goles) {
        this.goles = goles;
    }

    public short getPuntos() {
        return puntos;
    }

    public void setPuntos(short puntos) {
        this.puntos = puntos;
    }

    public byte getPartidosWin() {
        return partidosWin;
    }

    public void setPartidosWin(byte partidosWin) {
        this.partidosWin = partidosWin;
    }

    public byte getPartidosLose() {
        return partidosLose;
    }

    public void setPartidosLose(byte partidosLose) {
        this.partidosLose = partidosLose;
    }

    public byte getPartidosPlay() {
        return partidosPlay;
    }

    public void setPartidosPlay(byte partidosPlay) {
        this.partidosPlay = partidosPlay;
    }

    @Override
    public int compareTo(Jugador o) {
        return  o.getPuntos()-this.puntos;
    }

    @Override
    public String toString() {
        return nombre + "  " + puntos + "PTOS " + partidosPlay + "PJ" + partidosWin + "  " + partidosLose;
    }
}
