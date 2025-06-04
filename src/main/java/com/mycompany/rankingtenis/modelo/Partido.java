package com.mycompany.rankingtenis.modelo;

import java.util.Arrays;

public class Partido {

    public Jugador jugador1;
    public Jugador jugador2;

    public int[] setsJugador1;
    public int[] setsJugador2;

    public boolean jugado;

    public Partido(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.setsJugador1 = new int[3];
        this.setsJugador2 = new int[3];
        this.jugado = false;
    }

    //Constructor de copia profunda
    public Partido(Partido otro) {
        this.jugador1 = new Jugador(otro.jugador1); // constructor copia
        this.jugador2 = new Jugador(otro.jugador2);
        this.setsJugador1 = otro.setsJugador1 != null ? otro.setsJugador1.clone() : null;
        this.setsJugador2 = otro.setsJugador2 != null ? otro.setsJugador2.clone() : null;
        this.jugado = otro.jugado;
    }

    public void reiniciar() {
        this.setsJugador1 = null;
        this.setsJugador2 = null;
        this.jugado = false;
    }

    public void registrarResultado(int[] sets1, int[] sets2, Jugador j1, Jugador j2) {
        this.setsJugador1 = sets1;
        this.setsJugador2 = sets2;
        this.jugado = true;

        int setsGanadosJ1 = 0;
        int setsGanadosJ2 = 0;

        for (int i = 0; i < sets1.length; i++) {
            if (sets1[i] > sets2[i]) {
                setsGanadosJ1++;
            } else if (sets1[i] < sets2[i]) {
                setsGanadosJ2++;
            }
        }

        boolean victoriaJ1 = setsGanadosJ1 > setsGanadosJ2;

        j1.registrarPartido(victoriaJ1, setsGanadosJ1, setsGanadosJ2);
        j2.registrarPartido(!victoriaJ1, setsGanadosJ2, setsGanadosJ1);
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public int[] getSetsJugador1() {
        return setsJugador1;
    }

    public int[] getSetsJugador2() {
        return setsJugador2;
    }

    public boolean estaJugado() {
        return jugado;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    @Override
    public String toString() {
        return jugador1.getNombre() + " vs " + jugador2.getNombre();
    }

}
