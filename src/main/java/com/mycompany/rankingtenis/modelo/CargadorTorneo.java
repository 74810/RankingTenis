package com.mycompany.rankingtenis.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.rankingtenis.modelo.Torneo;

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

            reconstruirEstadisticas(torneo); // ← ESTE PASO ES CLAVE

            return torneo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void reconstruirEstadisticas(Torneo torneo) {
        for (Grupo grupo : torneo.getGrupos()) {
            List<Jugador> jugadores = grupo.getJugadores();
            // Reiniciar estadísticas
            for (Jugador jugador : jugadores) {
                jugador.restablecerEstadisticas();
            }

            List<Partido> partidosReconstruidos = new ArrayList<>();
            for (Partido partidoOriginal : grupo.getPartidos()) {
                if (!partidoOriginal.estaJugado()) {
                    partidosReconstruidos.add(partidoOriginal); // conservar partido pendiente
                    continue;
                }

                Jugador j1 = buscarJugadorPorNombre(jugadores, partidoOriginal.getJugador1().getNombre());
                Jugador j2 = buscarJugadorPorNombre(jugadores, partidoOriginal.getJugador2().getNombre());

                if (j1 != null && j2 != null) {
                    Partido nuevo = new Partido(j1, j2);
                    nuevo.registrarResultado(
                            partidoOriginal.getSetsJugador1(),
                            partidoOriginal.getSetsJugador2()
                    );
                    partidosReconstruidos.add(nuevo);
                }
            }

            grupo.setPartidos(partidosReconstruidos);
        }
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
