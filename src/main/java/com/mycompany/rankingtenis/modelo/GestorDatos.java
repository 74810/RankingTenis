
package com.mycompany.rankingtenis.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.Jugador;
import com.mycompany.rankingtenis.modelo.Torneo;

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

        // Reasignar jugadores correctamente en los partidos
        for (Grupo grupo : torneo.getGrupos()) {
            List<Jugador> jugadores = grupo.getJugadores();
            List<Partido> partidos = grupo.getPartidos();

            if (partidos == null) continue;

            for (Partido partido : partidos) {
                Jugador j1 = buscarJugadorPorNombre(jugadores, partido.getJugador1().getNombre());
                Jugador j2 = buscarJugadorPorNombre(jugadores, partido.getJugador2().getNombre());

                if (j1 == null || j2 == null) {
                    System.err.println("Jugador no encontrado al cargar partido:");
                    System.err.println("  jugador1: " + (partido.getJugador1() != null ? partido.getJugador1().getNombre() : "null"));
                    System.err.println("  jugador2: " + (partido.getJugador2() != null ? partido.getJugador2().getNombre() : "null"));
                    continue; // saltamos el partido para evitar null
                }

                partido.setJugador1(j1);
                partido.setJugador2(j2);

                if (partido.estaJugado()) {
                    partido.registrarResultado(partido.getSetsJugador1(), partido.getSetsJugador2());
                }
            }
        }

        return torneo;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}


//    public static Torneo cargarTorneo(String nombreTorneo) {
//        try {
//            String nombreArchivo = RUTA_DIRECTORIO + nombreTorneo + ".json";
//            BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));
//            Torneo torneo = gson.fromJson(reader, Torneo.class);
//            reader.close();
//
//            //Reparar datos tras cargar
//            for (Grupo grupo : torneo.getGrupos()) {
//                List<Jugador> jugadores = grupo.getJugadores();
//                List<Partido> partidos = grupo.getPartidos();
//
//                if (partidos == null || partidos.isEmpty()) {
//                    grupo.generarPartidos();
//                } else {
//                    // Reconectar referencias correctas de jugadores en grupo
//                    for (Partido partido : partidos) {
//                        Jugador j1 = buscarJugadorPorNombre(jugadores, partido.getJugador1().getNombre());
//                        Jugador j2 = buscarJugadorPorNombre(jugadores, partido.getJugador2().getNombre());
//
//                        if (j1 == null || j2 == null) {
//                            System.err.println("Error al vincular jugadores en partido:");
//                            System.err.println("Jugador1: " + partido.getJugador1().getNombre());
//                            System.err.println("Jugador2: " + partido.getJugador2().getNombre());
//                            continue; // evitar crash
//                        }
//
//                        partido.setJugador1(j1);
//                        partido.setJugador2(j2);
//
//                        if (partido.estaJugado()) {
//                            partido.registrarResultado(partido.getSetsJugador1(), partido.getSetsJugador2());
//                        }
//                    }
//                }
//            }
//
//            //Reconectar partidos del histórico de jornadas
//            if (torneo.getHistoricoJornadas() != null) {
//                for (List<Partido> jornada : torneo.getHistoricoJornadas()) {
//                    for (Partido partido : jornada) {
//                        Grupo grupoJugador = buscarGrupoDeJugador(torneo.getGrupos(), partido.getJugador1().getNombre());
//                        if (grupoJugador != null) {
//                            Jugador j1 = buscarJugadorPorNombre(grupoJugador.getJugadores(), partido.getJugador1().getNombre());
//                            Jugador j2 = buscarJugadorPorNombre(grupoJugador.getJugadores(), partido.getJugador2().getNombre());
//                            partido.setJugador1(j1);
//                            partido.setJugador2(j2);
//
//                            if (partido.estaJugado()) {
//                                partido.registrarResultado(partido.getSetsJugador1(), partido.getSetsJugador2());
//                            }
//                        }
//                    }
//                }
//            }
//
//            return torneo;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
