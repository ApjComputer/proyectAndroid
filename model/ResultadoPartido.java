package com.apjcompany.apjcomputer.proyecto.model;

public class ResultadoPartido {
    private String EquipoAzul,EquipoBlanco;
    public ResultadoPartido() {
    }

    public ResultadoPartido(String equipoAzul, String equipoBlanco) {
        EquipoAzul = equipoAzul;
        EquipoBlanco = equipoBlanco;
    }

    public String getEquipoAzul() {
        return EquipoAzul;
    }

    public void setEquipoAzul(String equipoAzul) {
        EquipoAzul = equipoAzul;
    }

    public String getEquipoBlanco() {
        return EquipoBlanco;
    }

    public void setEquipoBlanco(String equipoBlanco) {
        EquipoBlanco = equipoBlanco;
    }
}
