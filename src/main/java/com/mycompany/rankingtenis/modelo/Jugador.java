package com.mycompany.rankingtenis.modelo;

import java.util.UUID;

public class Jugador {
    private String nombre;
    private String id;

    private int partidosJugados;
    private int partidosGanados;
    private int partidosPerdidos;

    private int setsGanados;
    private int setsPerdidos;

    private int puntos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.id = UUID.randomUUID().toString();
        this.partidosJugados = 0;
        this.partidosGanados = 0;
        this.partidosPerdidos = 0;
        this.setsGanados = 0;
        this.setsPerdidos = 0;
        this.puntos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public int getSetsGanados() {
        return setsGanados;
    }

    public int getSetsPerdidos() {
        return setsPerdidos;
    }

    public int getDiferenciaSets() {
        return setsGanados - setsPerdidos;
    }

    public int getPuntos() {
        return puntos;
    }

    public void registrarPartido(boolean victoria, int setsGanados, int setsPerdidos) {
        this.partidosJugados++;
        if (victoria) {
            this.partidosGanados++;
            this.puntos += 2;
        } else {
            this.partidosPerdidos++;
        }
        this.setsGanados += setsGanados;
        this.setsPerdidos += setsPerdidos;
        this.puntos += 1; // Punto por participación
    }

    public void restablecerEstadisticas() {
        this.partidosJugados = 0;
        this.partidosGanados = 0;
        this.partidosPerdidos = 0;
        this.setsGanados = 0;
        this.setsPerdidos = 0;
        this.puntos = 0;
    }

    @Override
    public String toString() {
        return nombre + " | Puntos: " + puntos + " | PJ: " + partidosJugados +
               " | PG: " + partidosGanados + " | PP: " + partidosPerdidos +
               " | SG: " + setsGanados + " | SP: " + setsPerdidos;
    }

    void setId(String id) {
        this.id=id;
    }

    void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    void setPartidosPerdidos(int partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    void setSetsGanados(int setsGanados) {
        this.setsGanados = setsGanados;
    }

    void setSetsPerdidos(int setsPerdidos) {
        this.partidosPerdidos = setsPerdidos;
    }

    void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
