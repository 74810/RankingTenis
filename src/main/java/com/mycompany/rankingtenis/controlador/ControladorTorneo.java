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

    public ControladorTorneo(Torneo torneo) {
        this.torneo = torneo;
        this.vista = new VentanaTorneo(torneo.getNombreTorneo());
        this.vista.setControlador(this);
        this.vista.getBotonAnterior().addActionListener(e -> mostrarJornada(indiceJornada - 1));
        this.vista.getBotonSiguiente().addActionListener(e -> mostrarJornada(indiceJornada + 1));
        this.vista.getBotonVolver().addActionListener(e -> volverAlInicio());
        this.vista.getBotonAscensos().addActionListener(this);

        if (torneo.getHistoricoJornadas() != null && !torneo.getHistoricoJornadas().isEmpty()) {
            historicoJornadas = torneo.getHistoricoJornadas();
            indiceJornada = historicoJornadas.size() - 1;
        } else {
            guardarJornada();
        }

        mostrarJornada(indiceJornada);
        this.vista.setVisible(true);
    }

    private void guardarJornada() {
        List<Partido> partidos = new ArrayList<>();
        for (Grupo grupo : torneo.getGrupos()) {
            for (Partido partido : grupo.getPartidos()) {
                partidos.add(partido);
            }
        }
        historicoJornadas.add(partidos);
        torneo.setHistoricoJornadas(historicoJornadas);
        indiceJornada = historicoJornadas.size() - 1;
    }

    public void mostrarJornada(int indice) {
        if (indice < 0 || indice >= historicoJornadas.size()) {
            return;
        }

        this.indiceJornada = indice;

        List<Partido> partidos = historicoJornadas.get(indice);
        Map<String, List<Partido>> partidosPorGrupo = new HashMap<>();

        for (Partido partido : partidos) {
            String grupo = obtenerGrupoDeJugador(partido.getJugador1());
            if (grupo == null) {
                grupo = "?";
            }
            partidosPorGrupo.computeIfAbsent(grupo, k -> new ArrayList<>()).add(partido);
        }

        List<JPanel> paneles = new ArrayList<>();
        for (Map.Entry<String, List<Partido>> entry : partidosPorGrupo.entrySet()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Grupo " + entry.getKey()));

            Grupo grupo = torneo.getGruposComoMapa().get(entry.getKey());
            if (grupo == null) {
                continue;
            }
            List<Jugador> jugadores = grupo.getJugadores();

            JTable tabla = construirTablaClasificacion(jugadores);
            int totalWidth = 600;
            int numCols = tabla.getColumnCount();
            int anchoNombre = totalWidth * 2 / (numCols + 1);
            int anchoOtros = (totalWidth - anchoNombre) / (numCols - 1);

            tabla.setPreferredScrollableViewportSize(new Dimension(totalWidth, tabla.getRowHeight() * jugadores.size()));
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            tabla.getColumnModel().getColumn(0).setPreferredWidth(anchoNombre);
            for (int i = 1; i < numCols; i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(anchoOtros);
            }
            
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane scroll = new JScrollPane(tabla);

            JButton boton = new JButton("Gestionar");
            boton.addActionListener(e -> {
                Grupo g = torneo.getGruposComoMapa().get(entry.getKey());
                new ControladorGrupo(torneo, g);
            });

            panel.add(scroll, BorderLayout.CENTER);
            panel.add(boton, BorderLayout.SOUTH);
            panel.setPreferredSize(new Dimension(300, (150 + 6 * jugadores.size())));

            paneles.add(panel);
        }

        vista.mostrarGrupos(paneles);
        vista.getBotonAnterior().setEnabled(indice > 0);
        vista.getBotonSiguiente().setEnabled(indice < historicoJornadas.size() - 1);
        vista.actualizarEtiquetaJornada(indiceJornada, historicoJornadas.size());
    }

    private JTable construirTablaClasificacion(List<Jugador> jugadores) {
        jugadores.sort(Comparator.comparingInt(Jugador::getPuntos).reversed()
                .thenComparingInt(Jugador::getDiferenciaSets).reversed());

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

    private void volverAlInicio() {
        vista.dispose();
        new ControladorInicio();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
        Map<String, Grupo> mapaGrupos = torneo.getGruposComoMapa();
        List<String> nombres = new ArrayList<>(mapaGrupos.keySet());
        Collections.sort(nombres);

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
            return;
        }

        List<Grupo> gruposOriginales = torneo.getGrupos();
        VentanaPrevisualizarCambios vistaCambios = new VentanaPrevisualizarCambios(gruposOriginales, sugeridos);
        vistaCambios.setVisible(true);

        if (!vistaCambios.isCambiosConfirmados()) {
            JOptionPane.showMessageDialog(vista, "Cambios cancelados.");
            return;
        }

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

        for (String nombre : nombres) {
            Grupo grupo = mapaGrupos.get(nombre);
            grupo.setJugadores(nuevosGrupos.get(nombre));
            grupo.generarPartidos();
        }

        guardarJornada();
        mostrarJornada(indiceJornada);
        GestorDatos.guardarTorneo(torneo);
        JOptionPane.showMessageDialog(vista, "Cambios aplicados y torneo guardado.");
    }
}
