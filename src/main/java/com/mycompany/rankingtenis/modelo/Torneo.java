package com.mycompany.rankingtenis.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Torneo implements Serializable {

    private String nombreTorneo;
    private List<Grupo> grupos;
    private List<List<Partido>> historicoJornadas;

    public Torneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
        this.grupos = new ArrayList<>();
        this.historicoJornadas = new ArrayList<>();
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public List<List<Partido>> getHistoricoJornadas() {
        return historicoJornadas;
    }

    public void setHistoricoJornadas(List<List<Partido>> historicoJornadas) {
        this.historicoJornadas = historicoJornadas;
    }

    public Map<String, Grupo> getGruposComoMapa() {
        Map<String, Grupo> mapa = new LinkedHashMap<>();
        char letra = 'A';
        for (Grupo grupo : grupos) {
            mapa.put(String.valueOf(letra), grupo);
            letra++;
        }
        return mapa;
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
        List<Grupo> nuevosGrupos = new ArrayList<>();
        for (Grupo g : grupos) {
            nuevosGrupos.add(new Grupo(g.getNombreGrupo()));
        }

        for (int i = 0; i < grupos.size(); i++) {
            Grupo grupo = grupos.get(i);
            List<Jugador> clasificacion = grupo.obtenerClasificacion();
            int tam = clasificacion.size();

            for (int pos = 0; pos < tam; pos++) {
                Jugador jugador = clasificacion.get(pos);
                int nuevoGrupoIndex = calcularNuevoGrupo(i, pos, tam, grupos.size());

                if (nuevoGrupoIndex < 0) {
                    nuevoGrupoIndex = 0;
                }
                if (nuevoGrupoIndex >= grupos.size()) {
                    nuevoGrupoIndex = grupos.size() - 1;
                }

                nuevosGrupos.get(nuevoGrupoIndex).agregarJugador(jugador);
            }
        }

        grupos.clear();
        for (Grupo g : nuevosGrupos) {
            g.generarPartidos();
            grupos.add(g);
        }
    }

    public int calcularNuevoGrupo(int grupoActual, int posicion, int numJugadores, int totalGrupos) {
        if (grupoActual == 0) {
            if (posicion <= 1) return grupoActual;
            if (posicion < numJugadores - 1) return grupoActual + 1;
            return grupoActual + 2;
        }
        if (grupoActual == 1) {
            if (posicion <= 1) return grupoActual - 1;
            if (posicion == 2) return grupoActual;
            if (posicion < numJugadores - 1) return grupoActual + 1;
            return grupoActual + 2;
        }
        if (grupoActual == totalGrupos - 2) {
            if (posicion == 0) return grupoActual - 2;
            if (posicion == 1) return grupoActual - 1;
            if (posicion == 2) return grupoActual;
            return grupoActual + 1;
        }
        if (grupoActual == totalGrupos - 1) {
            if (posicion == 0) return grupoActual - 2;
            if (posicion == 1 || posicion == 2) return grupoActual - 1;
            return grupoActual;
        }
        if (numJugadores == 5) {
            if (posicion == 0) return grupoActual - 2;
            if (posicion == 1) return grupoActual - 1;
            if (posicion == 2) return grupoActual;
            if (posicion == 3) return grupoActual + 1;
            return grupoActual + 2;
        } else {
            if (posicion == 0) return grupoActual - 2;
            if (posicion == 1) return grupoActual - 1;
            if (posicion == 2) return grupoActual + 1;
            return grupoActual + 2;
        }
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
