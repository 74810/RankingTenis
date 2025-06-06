package com.mycompany.rankingtenis.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GestorDatos {

    private static final String RUTA_DIRECTORIO = "recursos/torneos_guardados/";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardarTorneo(Torneo torneo) {
        try {
            Files.createDirectories(Paths.get(RUTA_DIRECTORIO));
            String nombreArchivo = RUTA_DIRECTORIO + torneo.getNombreTorneo() + ".json";
            FileWriter writer = new FileWriter(nombreArchivo);
            gson.toJson(torneo, writer);
            writer.close(); 
            System.out.println("Torneo guardado en: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Torneo cargarTorneo(String nombreTorneo) {
        try {
            String ruta = RUTA_DIRECTORIO + nombreTorneo + ".json";
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            Torneo torneo = gson.fromJson(reader, Torneo.class);
            reader.close();

            for (Grupo grupo : torneo.getGrupos()) {
                List<Jugador> jugadores = grupo.getJugadores();
                List<Partido> partidos = grupo.getPartidos();

                if (partidos == null) {
                    continue;
                }

                for (Partido partido : partidos) {
                    Jugador j1 = buscarJugadorPorNombre(jugadores, partido.getJugador1().getNombre());
                    Jugador j2 = buscarJugadorPorNombre(jugadores, partido.getJugador2().getNombre());

                    if (j1 == null || j2 == null) {
                        System.err.println("Jugador no encontrado al cargar partido:");
                        System.err.println("  jugador1: " + (partido.getJugador1() != null ? partido.getJugador1().getNombre() : "null"));
                        System.err.println("  jugador2: " + (partido.getJugador2() != null ? partido.getJugador2().getNombre() : "null"));
                        continue;
                    }

                    partido.setJugador1(j1);
                    partido.setJugador2(j2);

                    //Aplicar estadísticas si el partido ya estaba jugado
                    if (partido.estaJugado() && partido.getSetsJugador1() != null && partido.getSetsJugador2() != null) {
                        partido.registrarResultado(partido.getSetsJugador1(), partido.getSetsJugador2(), j1, j2);
                    }
                }
            }

            return torneo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> listarTorneosGuardados() {
        List<String> nombres = new ArrayList<>();
        File carpeta = new File(RUTA_DIRECTORIO);
        if (carpeta.exists() && carpeta.isDirectory()) {
            for (File archivo : carpeta.listFiles()) {
                if (archivo.isFile() && archivo.getName().endsWith(".json")) {
                    nombres.add(archivo.getName().replace(".json", ""));
                }
            }
        }
        return nombres;
    }

    private static Jugador buscarJugadorPorNombre(List<Jugador> lista, String nombre) {
        for (Jugador j : lista) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    private static Grupo buscarGrupoDeJugador(List<Grupo> grupos, String nombreJugador) {
        for (Grupo grupo : grupos) {
            for (Jugador j : grupo.getJugadores()) {
                if (j.getNombre().equals(nombreJugador)) {
                    return grupo;
                }
            }
        }
        return null;
    }

}
