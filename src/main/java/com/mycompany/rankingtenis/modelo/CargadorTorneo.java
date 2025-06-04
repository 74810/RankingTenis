package com.mycompany.rankingtenis.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.rankingtenis.modelo.Torneo;
import java.io.File;

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

            torneo.reconstruirHistorico(); // <--- CRUCIAL
            System.out.println("Estadísticas reconstruidas correctamente.");

            return torneo;
        } catch (Exception e) {
            System.err.println("Error al cargar torneo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void reconstruirHistorial(Torneo torneo) {
        for (List<Partido> jornada : torneo.getHistoricoJornadas()) {
            for (int i = 0; i < jornada.size(); i++) {
                Partido partidoOriginal = jornada.get(i);

                Jugador j1 = buscarJugadorEnTorneo(torneo, partidoOriginal.getJugador1().getNombre());
                Jugador j2 = buscarJugadorEnTorneo(torneo, partidoOriginal.getJugador2().getNombre());

                if (j1 == null || j2 == null) {
                    continue;
                }

                Partido reconstruido = new Partido(j1, j2);
                reconstruido.registrarResultado(
                        partidoOriginal.getSetsJugador1(),
                        partidoOriginal.getSetsJugador2(),
                        j1,
                        j2
                );

                jornada.set(i, reconstruido);
            }
        }
    }

    private static Jugador buscarJugadorEnTorneo(Torneo torneo, String nombre) {
        for (Grupo grupo : torneo.getGrupos()) {
            for (Jugador jugador : grupo.getJugadores()) {
                if (jugador.getNombre().equals(nombre)) {
                    return jugador;
                }
            }
        }
        return null;
    }

    private static Jugador buscarJugadorPorNombre(List<Jugador> lista, String nombre) {
        for (Jugador j : lista) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

}
