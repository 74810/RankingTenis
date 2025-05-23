package com.mycompany.rankingtenis.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Grupo {

    private String nombreGrupo;
    private List<Jugador> jugadores;
    private List<Partido> partidos;

    public Grupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
        this.jugadores = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void agregarJugador(Jugador jugador) {
        if (jugadores.size() >= 5) {
            throw new IllegalStateException("El grupo ya tiene el máximo de 5 jugadores.");
        }
        jugadores.add(jugador);
    }

    public void generarPartidos() {
        partidos.clear();
        for (int i = 0; i < jugadores.size(); i++) {
            for (int j = i + 1; j < jugadores.size(); j++) {
                partidos.add(new Partido(jugadores.get(i), jugadores.get(j)));
            }
        }
    }

    public void reiniciarPartidos() {
        for (Partido partido : partidos) {
            partido.reiniciarPartido();
        }
        for (Jugador jugador : jugadores) {
            jugador.restablecerEstadisticas();
        }
    }

    public List<Jugador> obtenerClasificacion() {
        List<Jugador> clasificados = new ArrayList<>(jugadores);
        clasificados.sort(Comparator.comparingInt(Jugador::getPuntos).reversed()
                .thenComparingInt(Jugador::getDiferenciaSets).reversed()
                .thenComparingInt(Jugador::getSetsGanados).reversed());
        return clasificados;
    }

    private Jugador buscarJugadorPorNombre(List<Jugador> lista, String nombre) {
        for (Jugador j : lista) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    public Grupo copiaProfunda() {
        Grupo copia = new Grupo(this.nombreGrupo);

        // Copiar jugadores
        List<Jugador> copiaJugadores = new ArrayList<>();
        for (Jugador original : this.jugadores) {
            Jugador j = new Jugador(original.getNombre());
            j.setPartidosJugados(original.getPartidosJugados());
            j.setPartidosGanados(original.getPartidosGanados());
            j.setPartidosPerdidos(original.getPartidosPerdidos());
            j.setSetsGanados(original.getSetsGanados());
            j.setSetsPerdidos(original.getSetsPerdidos());
            j.setPuntos(original.getPuntos());
            copiaJugadores.add(j);
        }
        copia.setJugadores(copiaJugadores);

        // Copiar partidos
        List<Partido> copiaPartidos = new ArrayList<>();
        for (Partido original : this.partidos) {
            Jugador j1 = buscarJugadorPorNombre(copiaJugadores, original.getJugador1().getNombre());
            Jugador j2 = buscarJugadorPorNombre(copiaJugadores, original.getJugador2().getNombre());
            Partido nuevo = new Partido(j1, j2);

            if (original.estaJugado()) {
                nuevo.registrarResultado(
                        original.getSetsJugador1(),
                        original.getSetsJugador2()
                );
            }

            copiaPartidos.add(nuevo);
        }
        copia.setPartidos(copiaPartidos);

        return copia;
    }

    public boolean jornadaCompleta() {
        for (Partido partido : partidos) {
            if (!partido.estaJugado()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nombreGrupo + ":\n");
        List<Jugador> clasificacion = obtenerClasificacion();
        for (int i = 0; i < clasificacion.size(); i++) {
            sb.append((i + 1)).append(". ").append(clasificacion.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

}
