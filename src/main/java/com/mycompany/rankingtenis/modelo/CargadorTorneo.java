package com.mycompany.rankingtenis.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CargadorTorneo {

    public static Torneo cargarTorneoDesdeArchivo(String ruta) {
        try (Reader reader = new FileReader(ruta)) {
            Gson gson = new GsonBuilder().create();
            Torneo torneo = gson.fromJson(reader, Torneo.class);
            System.out.println("Torneo cargado desde: " + ruta);

            reconstruirEstadisticas(torneo);
            return torneo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void reconstruirEstadisticas(Torneo torneo) {
        for (List<Grupo> grupos : torneo.getHistoricoGrupos()) {
            for (Grupo grupo : grupos) {
                for (Jugador jugador : grupo.getJugadores()) {
                    jugador.restablecerEstadisticas();
                }
            }
        }

        for (int i = 0; i < torneo.getHistoricoJornadas().size(); i++) {
            List<Partido> jornada = torneo.getHistoricoJornadas().get(i);
            List<Grupo> grupos = torneo.getHistoricoGrupos().get(i);

            for (Partido partido : jornada) {
                Jugador j1 = buscarJugadorPorNombre(grupos, partido.getJugador1().getNombre());
                Jugador j2 = buscarJugadorPorNombre(grupos, partido.getJugador2().getNombre());

                if (j1 != null && j2 != null && partido.estaJugado()) {
                    partido.setJugador1(j1);
                    partido.setJugador2(j2);
                    partido.registrarResultado(partido.getSetsJugador1(), partido.getSetsJugador2(), j1, j2);
                }
            }
        }
    }

    private static Jugador buscarJugadorPorNombre(List<Grupo> grupos, String nombre) {
        for (Grupo grupo : grupos) {
            for (Jugador jugador : grupo.getJugadores()) {
                if (jugador.getNombre().equals(nombre)) {
                    return jugador;
                }
            }
        }
        return null;
    }
}
