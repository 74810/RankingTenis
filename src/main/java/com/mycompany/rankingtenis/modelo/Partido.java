package com.mycompany.rankingtenis.modelo;

public class Partido {

    private Jugador jugador1;
    private Jugador jugador2;

    private int[] setsJugador1;
    private int[] setsJugador2;

    private boolean jugado;

    public Partido(Partido otro) {
        this.jugador1 = otro.jugador1;
        this.jugador2 = otro.jugador2;
        this.setsJugador1 = otro.setsJugador1;
        this.setsJugador2 = otro.setsJugador2;
    }

    public Partido(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.setsJugador1 = new int[3];
        this.setsJugador2 = new int[3];
        this.jugado = false;
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
        return setsJugador1 != null && setsJugador2 != null
                && setsJugador1.length > 0 && setsJugador2.length > 0;
    }

    public void registrarResultado(int[] sets1, int[] sets2) {
        if (sets1.length != sets2.length || sets1.length > 3) {
            throw new IllegalArgumentException("El número de sets debe ser igual y no superior a 3.");
        }

        this.setsJugador1 = sets1;
        this.setsJugador2 = sets2;
        this.jugado = true;

        int setsGanadosJ1 = 0;
        int setsGanadosJ2 = 0;

        for (int i = 0; i < sets1.length; i++) {
            if (sets1[i] > sets2[i]) {
                setsGanadosJ1++;
            } else {
                setsGanadosJ2++;
            }
        }

        boolean victoriaJ1 = setsGanadosJ1 > setsGanadosJ2;

        jugador1.registrarPartido(victoriaJ1, setsGanadosJ1, setsGanadosJ2);
        jugador2.registrarPartido(!victoriaJ1, setsGanadosJ2, setsGanadosJ1);
    }

    public void reiniciarPartido() {
        this.setsJugador1 = new int[3];
        this.setsJugador2 = new int[3];
        this.jugado = false;
    }

    @Override
    public String toString() {
        if (!jugado) {
            return jugador1.getNombre() + " vs " + jugador2.getNombre() + " (Pendiente)";
        } else {
            StringBuilder resultado = new StringBuilder();
            resultado.append(jugador1.getNombre()).append(" vs ").append(jugador2.getNombre()).append(": ");
            for (int i = 0; i < setsJugador1.length; i++) {
                if (setsJugador1[i] == 0 && setsJugador2[i] == 0) {
                    break;
                }
                resultado.append(setsJugador1[i]).append("-").append(setsJugador2[i]).append(" ");
            }
            return resultado.toString().trim();
        }
    }

//    public void actualizarEstadisticas() {
//        int setsGanadosJ1 = 0;
//        int setsGanadosJ2 = 0;
//
//        for (int i = 0; i < setsJugador1.length; i++) {
//            if (setsJugador1[i] > setsJugador2[i]) {
//                setsGanadosJ1++;
//            } else {
//                setsGanadosJ2++;
//            }
//        }
//
//        jugador1.sumarSets(setsGanadosJ1, setsJugador2.length - setsGanadosJ1);
//        jugador2.sumarSets(setsGanadosJ2, setsJugador1.length - setsGanadosJ2);
//
//        if (setsGanadosJ1 > setsGanadosJ2) {
//            jugador1.sumarVictoria();
//            jugador2.sumarDerrota();
//        } else {
//            jugador2.sumarVictoria();
//            jugador1.sumarDerrota();
//        }
//
//        jugador1.sumarPuntoPorJugar();
//        jugador2.sumarPuntoPorJugar();
//    }
//
//    public boolean estaJugado() {
//        return setsJugador1 != null && setsJugador2 != null
//                && setsJugador1.length > 0 && setsJugador2.length > 0;
//    }
    void setJugador1(Jugador j1) {
        this.jugador1 = j1;
    }

    void setJugador2(Jugador j2) {
        this.jugador2 = j2;
    }
}
