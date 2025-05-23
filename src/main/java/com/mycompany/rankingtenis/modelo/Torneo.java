package com.mycompany.rankingtenis.modelo;

import java.util.ArrayList;
import java.util.List;

public class Torneo {

    private String nombreTorneo;
    private List<Grupo> grupos;

    public Torneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
        this.grupos = new ArrayList<>();
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void agregarGrupo(Grupo grupo) {
        if (grupos.size() >= 20) {
            throw new IllegalStateException("No se pueden tener más de 20 grupos.");
        }
        grupos.add(grupo);
    }

    public void reiniciarJornada() {
        for (Grupo grupo : grupos) {
            grupo.reiniciarPartidos();
            grupo.generarPartidos();
        }
    }

    public boolean jornadaCompleta() {
        for (Grupo grupo : grupos) {
            if (!grupo.jornadaCompleta()) {
                return false;
            }
        }
        return true;
    }

    public void ejecutarAscensosYDescensos() {
        // Paso 1: crear copia de los jugadores a mover y sus nuevos grupos
        List<Grupo> nuevosGrupos = new ArrayList<>();
        for (Grupo g : grupos) {
            nuevosGrupos.add(new Grupo(g.getNombreGrupo())); // misma cantidad, nombres iguales
        }

        for (int i = 0; i < grupos.size(); i++) {
            Grupo grupo = grupos.get(i);
            List<Jugador> clasificacion = grupo.obtenerClasificacion();
            int tam = clasificacion.size();

            for (int pos = 0; pos < tam; pos++) {
                Jugador jugador = clasificacion.get(pos);
                int nuevoGrupoIndex = calcularNuevoGrupo(i, pos, tam, grupos.size());

                // Asegurar que no se sale de rango
                if (nuevoGrupoIndex < 0) {
                    nuevoGrupoIndex = 0;
                }
                if (nuevoGrupoIndex >= grupos.size()) {
                    nuevoGrupoIndex = grupos.size() - 1;
                }

                nuevosGrupos.get(nuevoGrupoIndex).agregarJugador(jugador);
            }
        }

        // Reemplazar grupos actuales por los nuevos y regenerar partidos
        grupos.clear();
        for (Grupo g : nuevosGrupos) {
            g.generarPartidos();
            grupos.add(g);
        }
    }

    private int calcularNuevoGrupo(int grupoActual, int posicion, int numJugadores, int totalGrupos) {
    // PRIMER GRUPO (Grupo A)
    if (grupoActual == 0) {
        if (posicion <= 1) return grupoActual; // 1º y 2º se mantienen
        if (posicion < numJugadores - 1) return grupoActual + 1; // intermedios bajan uno
        if (posicion == numJugadores - 1) return grupoActual + 2; // último baja dos
    }

    // SEGUNDO GRUPO
    if (grupoActual == 1) {
        if (posicion <= 1) return grupoActual - 1; // 1º y 2º suben
        if (posicion == 2) return grupoActual;     // 3º se mantiene
        if (posicion < numJugadores - 1) return grupoActual + 1; // intermedios bajan uno
        if (posicion == numJugadores - 1) return grupoActual + 2; // último baja dos
    }

    // PENÚLTIMO GRUPO
    if (grupoActual == totalGrupos - 2) {
        if (posicion == 0) return grupoActual - 2; // 1º sube dos
        if (posicion == 1) return grupoActual - 1; // 2º sube uno
        if (posicion == 2) return grupoActual;     // 3º se mantiene
        if (posicion >= 3) return grupoActual + 1; // 4º o 5º baja uno
    }

    // ÚLTIMO GRUPO
    if (grupoActual == totalGrupos - 1) {
        if (posicion == 0) return grupoActual - 2; // 1º sube dos
        if (posicion == 1 || posicion == 2) return grupoActual - 1; // 2º y 3º suben uno
        return grupoActual; // los demás se mantienen
    }

    // RESTO DE GRUPOS (Regla general)
    if (numJugadores == 5) {
        if (posicion == 0) return grupoActual - 2;
        if (posicion == 1) return grupoActual - 1;
        if (posicion == 2) return grupoActual;
        if (posicion == 3) return grupoActual + 1;
        if (posicion == 4) return grupoActual + 2;
    } else if (numJugadores == 4) {
        if (posicion == 0) return grupoActual - 2;
        if (posicion == 1) return grupoActual - 1;
        if (posicion == 2) return grupoActual + 1;
        if (posicion == 3) return grupoActual + 2;
    }

    return grupoActual; // por seguridad, no mover si algo falla
}


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Torneo: " + nombreTorneo + "\n");
        for (Grupo grupo : grupos) {
            sb.append(grupo.toString()).append("\n");
        }
        return sb.toString();
    }
}
