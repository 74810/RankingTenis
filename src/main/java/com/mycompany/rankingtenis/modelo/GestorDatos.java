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
            String nombreArchivo = RUTA_DIRECTORIO + nombreTorneo + ".json";
            BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));
            Torneo torneo = gson.fromJson(reader, Torneo.class);
            reader.close();

            // 🔄 Reparar datos tras cargar
            for (Grupo grupo : torneo.getGrupos()) {
                List<Jugador> jugadores = grupo.getJugadores();
                List<Partido> partidos = grupo.getPartidos();

                if (partidos == null || partidos.isEmpty()) {
                    grupo.generarPartidos();
                } else {
                    // Reconectar referencias correctas de jugadores
                    for (Partido partido : partidos) {
                        Jugador j1 = buscarJugadorPorNombre(jugadores, partido.getJugador1().getNombre());
                        Jugador j2 = buscarJugadorPorNombre(jugadores, partido.getJugador2().getNombre());
                        partido.setJugador1(j1);
                        partido.setJugador2(j2);
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

}
