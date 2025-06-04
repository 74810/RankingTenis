package com.mycompany.rankingtenis.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Torneo implements Serializable {

    private List<List<Grupo>> historicoGrupos = new ArrayList<>();
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

    public void setHistoricoGrupos(List<List<Grupo>> historicoGrupos) {
        this.historicoGrupos = historicoGrupos;
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

    public void reconstruirHistorico() {
        for (int i = 0; i < historicoJornadas.size(); i++) {
            List<Partido> jornada = historicoJornadas.get(i);
            List<Grupo> grupos = historicoGrupos.get(i); // grupos reales de esa jornada

            System.out.println("=== Jornada " + i + " ===");
            System.out.println("Grupos en esta jornada: " + grupos.size());

            for (Grupo grupo : grupos) {
                System.out.println("  - " + grupo.getNombre() + " (" + grupo.getJugadores().size() + " jugadores)");
                for (Jugador jugador : grupo.getJugadores()) {
                    jugador.restablecerEstadisticas(); // Limpia stats antes de reconstruir
                }
            }

            for (int j = 0; j < jornada.size(); j++) {
                Partido partidoOriginal = jornada.get(j);

                Jugador j1 = buscarJugadorEnGrupos(partidoOriginal.getJugador1().getNombre(), grupos);
                Jugador j2 = buscarJugadorEnGrupos(partidoOriginal.getJugador2().getNombre(), grupos);

                if (j1 == null || j2 == null) {
                    System.out.println("Jugadores no encontrados para partido: "
                            + partidoOriginal.getJugador1().getNombre() + " vs " + partidoOriginal.getJugador2().getNombre());
                    continue;
                }

                Partido nuevo = new Partido(j1, j2);
                nuevo.registrarResultado(
                        partidoOriginal.getSetsJugador1(),
                        partidoOriginal.getSetsJugador2(),
                        j1,
                        j2
                );

                jornada.set(j, nuevo);

                System.out.println("Registrado: " + j1.getNombre() + " vs " + j2.getNombre());
            }

            // Mostrar resultado del primer grupo
            if (!grupos.isEmpty()) {
                Grupo g0 = grupos.get(0);
                System.out.println(">>> Clasificación grupo " + g0.getNombre() + ":");
                for (Jugador j : g0.getJugadores()) {
                    System.out.println("  • " + j.getNombre() + " | PJ: " + j.getPartidosJugados() + ", Puntos: " + j.getPuntos());
                }
            }
        }
    }

    private Jugador buscarJugadorEnGrupos(String nombre, List<Grupo> grupos) {
        for (Grupo grupo : grupos) {
            for (Jugador jugador : grupo.getJugadores()) {
                if (jugador.getNombre().equals(nombre)) {
                    return jugador;
                }
            }
        }
        return null;
    }

    private Jugador buscarJugadorPorNombre(String nombre) {
        for (Grupo grupo : grupos) {
            for (Jugador j : grupo.getJugadores()) {
                if (j.getNombre().equals(nombre)) {
                    return j;
                }
            }
        }
        return null;
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
        guardarJornada();
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

    public void guardarJornada() {
        // Copia profunda de los grupos
        List<Grupo> copiaGrupos = new ArrayList<>();
        for (Grupo grupo : this.grupos) {
            copiaGrupos.add(new Grupo(grupo));
        }
        historicoGrupos.add(copiaGrupos);

        // Copia de los partidos actuales
        List<Partido> partidosActuales = new ArrayList<>();
        for (Grupo grupo : this.grupos) {
            for (Partido partido : grupo.getPartidos()) {
                partidosActuales.add(new Partido(partido)); // requiere constructor de copia
            }
        }
        historicoJornadas.add(partidosActuales);
    }

    public List<List<Grupo>> getHistoricoGrupos() {
        return historicoGrupos;
    }

    public int calcularNuevoGrupo(int grupoActual, int posicion, int numJugadores, int totalGrupos) {
        if (grupoActual == 0) {
            if (posicion <= 1) {
                return grupoActual;
            }
            if (posicion < numJugadores - 1) {
                return grupoActual + 1;
            }
            return grupoActual + 2;
        }
        if (grupoActual == 1) {
            if (posicion <= 1) {
                return grupoActual - 1;
            }
            if (posicion == 2) {
                return grupoActual;
            }
            if (posicion < numJugadores - 1) {
                return grupoActual + 1;
            }
            return grupoActual + 2;
        }
        if (grupoActual == totalGrupos - 2) {
            if (posicion == 0) {
                return grupoActual - 2;
            }
            if (posicion == 1) {
                return grupoActual - 1;
            }
            if (posicion == 2) {
                return grupoActual;
            }
            return grupoActual + 1;
        }
        if (grupoActual == totalGrupos - 1) {
            if (posicion == 0) {
                return grupoActual - 2;
            }
            if (posicion == 1 || posicion == 2) {
                return grupoActual - 1;
            }
            return grupoActual;
        }
        if (numJugadores == 5) {
            if (posicion == 0) {
                return grupoActual - 2;
            }
            if (posicion == 1) {
                return grupoActual - 1;
            }
            if (posicion == 2) {
                return grupoActual;
            }
            if (posicion == 3) {
                return grupoActual + 1;
            }
            return grupoActual + 2;
        } else {
            if (posicion == 0) {
                return grupoActual - 2;
            }
            if (posicion == 1) {
                return grupoActual - 1;
            }
            if (posicion == 2) {
                return grupoActual + 1;
            }
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
