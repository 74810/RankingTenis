package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.GestorDatos;
import com.mycompany.rankingtenis.modelo.Jugador;
import com.mycompany.rankingtenis.modelo.Partido;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.vista.VentanaPrevisualizarCambios;
import com.mycompany.rankingtenis.vista.VentanaTorneo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorTorneo implements ActionListener {

    private Torneo torneo;
    private VentanaTorneo vista;
    private List<List<Partido>> historicoJornadas = new ArrayList<>();
    private int indiceJornada = 0;
    private String nombreGrupo;
    private List<Jugador> jugadores;

    public ControladorTorneo(Torneo torneo) {
        this.torneo = torneo;
        this.vista = new VentanaTorneo(torneo.getNombreTorneo());
        this.vista.mostrarGrupos(torneo.getGrupos());
        this.vista.setControlador(this);
        this.vista.getBotonAnterior().addActionListener(e -> mostrarJornada(indiceJornada - 1));
        this.vista.getBotonSiguiente().addActionListener(e -> mostrarJornada(indiceJornada + 1));
        this.vista.getBotonVolver().addActionListener(e -> volverAlInicio());

        if (torneo.getHistoricoJornadas() != null && !torneo.getHistoricoJornadas().isEmpty()) {
            historicoJornadas = torneo.getHistoricoJornadas();
            indiceJornada = historicoJornadas.size() - 1;
        } else {
            guardarJornada();
        }

        mostrarJornada(indiceJornada);
        this.vista.setVisible(true);
        System.out.println(historicoJornadas.size());
    }

    private void guardarJornada() {
        // Guardar copia profunda de los grupos
        List<Grupo> copiaGrupos = new ArrayList<>();
        for (Grupo grupo : torneo.getGrupos()) {
            copiaGrupos.add(new Grupo(grupo));
        }
        List<List<Grupo>> historial = torneo.getHistoricoGrupos();
        historial.add(copiaGrupos);
        torneo.setHistoricoGrupos(historial);

        // Guardar copia de los partidos
        List<Partido> copiaJornada = new ArrayList<>();
        for (Grupo grupo : torneo.getGrupos()) {
            for (Partido partido : grupo.getPartidos()) {
                copiaJornada.add(new Partido(partido)); // Copia profunda
            }
        }
        historicoJornadas.add(copiaJornada);
        torneo.setHistoricoJornadas(historicoJornadas);

        indiceJornada = historicoJornadas.size() - 1;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    private String obtenerGrupoDeJugador(Jugador jugador) {
        for (Grupo grupo : torneo.getGrupos()) {
            for (Jugador j : grupo.getJugadores()) {
                if (j.getNombre().equals(jugador.getNombre())) {
                    return grupo.getNombreGrupo();
                }
            }
        }
        return null;
    }

    private Jugador buscarJugadorPorNombre(List<Jugador> jugadores, String nombre) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    public void mostrarJornada(int indice) {
        if (indice < 0 || indice >= torneo.getHistoricoJornadas().size()) {
            return;
        }

        this.indiceJornada = indice;

        List<Grupo> gruposDeLaJornada = torneo.getHistoricoGrupos().get(indice);
        List<Partido> partidos = torneo.getHistoricoJornadas().get(indice);

        Map<String, List<Partido>> partidosPorGrupo = new HashMap<>();
        for (Partido partido : partidos) {
            String grupo = obtenerGrupoDeJugador(partido.getJugador1());
            if (grupo == null) {
                grupo = "?";
            }
            partidosPorGrupo.computeIfAbsent(grupo, k -> new ArrayList<>()).add(partido);
        }

        List<JPanel> paneles = new ArrayList<>();

        for (Grupo grupo : gruposDeLaJornada) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Grupo " + grupo.getNombre()));

            List<Jugador> jugadores = grupo.obtenerClasificacion();
            JTable tabla = construirTablaClasificacion(jugadores);

            int totalWidth = 600;
            int anchoNombre = 150;
            int anchoResto = (totalWidth - anchoNombre) / 7;

            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(anchoNombre);
            for (int i = 1; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(anchoResto);
            }

            tabla.setPreferredScrollableViewportSize(
                    new Dimension(totalWidth, tabla.getRowHeight() * jugadores.size())
            );
            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JButton boton = new JButton("Gestionar");
            boton.addActionListener(e -> new ControladorGrupo(torneo, grupo));

            panel.add(scroll, BorderLayout.CENTER);
            panel.add(boton, BorderLayout.SOUTH);
            panel.setPreferredSize(new Dimension(300, 150 + 6 * jugadores.size()));
            paneles.add(panel);
        }

        vista.mostrarGruposVisuales(paneles);
        vista.getBotonAnterior().setEnabled(indice > 0);
        vista.getBotonSiguiente().setEnabled(indice < torneo.getHistoricoJornadas().size() - 1);
        vista.actualizarEtiquetaJornada(indiceJornada, torneo.getHistoricoJornadas().size());
        vista.getBotonAscensos().setEnabled(indice == torneo.getHistoricoJornadas().size() - 1);

    }

    private String encontrarGrupoDeJugador(Jugador jugador, List<Grupo> grupos) {
        for (Grupo grupo : grupos) {
            for (Jugador j : grupo.getJugadores()) {
                if (j.getNombre().equals(jugador.getNombre())) {
                    return grupo.getNombre();
                }
            }
        }
        return null;
    }

    private JTable construirTablaClasificacion(List<Jugador> jugadoresOriginal) {
        // Crear copia para no modificar la lista original
        List<Jugador> jugadores = new ArrayList<>(jugadoresOriginal);

        // Ordenar de mayor a menor por puntos, luego diferencia de sets, luego nombre
        jugadores.sort(
                Comparator.comparingInt(Jugador::getPuntos).reversed()
                        .thenComparing(Comparator.comparingInt(Jugador::getDiferenciaSets).reversed())
                        .thenComparing(Jugador::getNombre)
        );

        // Construir la tabla
        String[] columnas = {"Nom", "Pts", "PJ", "PG", "PP", "SG", "SP", "Dif"};
        Object[][] datos = new Object[jugadores.size()][8];

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            datos[i][0] = j.getNombre();
            datos[i][1] = j.getPuntos();
            datos[i][2] = j.getPartidosJugados();
            datos[i][3] = j.getPartidosGanados();
            datos[i][4] = j.getPartidosPerdidos();
            datos[i][5] = j.getSetsGanados();
            datos[i][6] = j.getSetsPerdidos();
            datos[i][7] = j.getDiferenciaSets();
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tabla.setRowHeight(14);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        return tabla;
    }

    private void volverAlInicio() {
        vista.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento recibido: " + e.getActionCommand());

        if (e.getActionCommand().equals("ascensos")) {
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Previsualizar y editar los ascensos/descensos antes de confirmar?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                ejecutarAscensosConVistaPrevia();
            }
        }
    }

    public void ejecutarAscensosConVistaPrevia() {
        // Desactivar botón
        vista.getBotonAscensos().setEnabled(false);

        Map<String, Grupo> mapaGrupos = torneo.getGruposComoMapa();
        List<String> nombres = new ArrayList<>(mapaGrupos.keySet());
        Collections.sort(nombres);

        // Calcular sugerencias
        Map<Jugador, String> sugeridos = new HashMap<>();
        for (int i = 0; i < nombres.size(); i++) {
            Grupo grupo = mapaGrupos.get(nombres.get(i));
            List<Jugador> clasificados = grupo.obtenerClasificacion();
            int tam = clasificados.size();
            for (int pos = 0; pos < tam; pos++) {
                Jugador jugador = clasificados.get(pos);
                int destino = torneo.calcularNuevoGrupo(i, pos, tam, nombres.size());
                destino = Math.max(0, Math.min(destino, nombres.size() - 1));
                sugeridos.put(jugador, nombres.get(destino));
            }
        }

        if (sugeridos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se pudo calcular ninguna sugerencia.");
            vista.getBotonAscensos().setEnabled(true);
            return;
        }

        // Mostrar ventana de cambios
        List<Grupo> gruposOriginales = torneo.getGrupos();
        VentanaPrevisualizarCambios vistaCambios = new VentanaPrevisualizarCambios(gruposOriginales, sugeridos);
        vistaCambios.setVisible(true);

        if (!vistaCambios.isCambiosConfirmados()) {
            JOptionPane.showMessageDialog(vista, "Cambios cancelados.");
            vista.getBotonAscensos().setEnabled(true);
            return;
        }

        // 1. Guardar la jornada actual (estado antes de aplicar cambios)
        guardarJornada();

        // 2. Resetear estadísticas ANTES de crear nuevos partidos
        for (Grupo grupo : torneo.getGrupos()) {
            for (Jugador j : grupo.getJugadores()) {
                j.restablecerEstadisticas();
            }
        }

        // 3. Reorganizar jugadores en nuevos grupos
        Map<String, List<Jugador>> nuevosGrupos = new HashMap<>();
        for (String nombre : nombres) {
            nuevosGrupos.put(nombre, new ArrayList<>());
        }

        Map<String, String> destinoFinal = vistaCambios.getGrupoFinalSeleccionado();
        for (Grupo grupo : torneo.getGrupos()) {
            for (Jugador jugador : grupo.getJugadores()) {
                String destino = destinoFinal.get(jugador.getNombre());
                if (destino != null) {
                    nuevosGrupos.get(destino).add(jugador);
                }
            }
        }

        // 4. Asignar jugadores y generar nuevos partidos
        for (String nombre : nombres) {
            Grupo grupo = mapaGrupos.get(nombre);
            grupo.setJugadores(nuevosGrupos.get(nombre));
            grupo.generarPartidos();
        }

        // 6. Mostrar y guardar
        mostrarJornada(indiceJornada);
        GestorDatos.guardarTorneo(torneo);
        JOptionPane.showMessageDialog(vista, "Cambios aplicados y torneo guardado.");

        vista.getBotonAscensos().setEnabled(true);
        vista.getRootPane().requestFocus();
    }

}
