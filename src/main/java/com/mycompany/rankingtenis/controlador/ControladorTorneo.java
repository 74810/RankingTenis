package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.GestorDatos;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.vista.VentanaTorneo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorTorneo implements ActionListener {

    private Torneo torneo;
    private VentanaTorneo vista;

    private int indiceJornada = 0;
    private List<List<Grupo>> historicoJornadas = new ArrayList<>();

    public ControladorTorneo(Torneo torneo) {
        this.torneo = torneo;
        this.vista = new VentanaTorneo(torneo.getNombreTorneo());
        this.vista.setControlador(this);
        this.vista.getBotonAnterior().addActionListener(e -> mostrarJornada(indiceJornada - 1));
        this.vista.getBotonSiguiente().addActionListener(e -> mostrarJornada(indiceJornada + 1));
        vista.getBotonVolver().addActionListener(e -> volverAlInicio());
        guardarJornada(); // guardar la jornada inicial
        System.out.println(historicoJornadas.size());
        mostrarJornada(indiceJornada);
        this.vista.setVisible(true);
    }

    private void guardarJornada() {
        List<Grupo> copia = new ArrayList<>();
        for (Grupo grupo : torneo.getGrupos()) {
            copia.add(grupo.copiaProfunda()); // debe existir este método en Grupo
        }
        historicoJornadas.add(copia);
        indiceJornada = historicoJornadas.size() - 1;
    }

    private void mostrarJornada(int indice) {
        if (indice < 0 || indice >= historicoJornadas.size()) {
            return;
        }

        this.indiceJornada = indice;
        List<JPanel> paneles = new ArrayList<>();
        List<Grupo> gruposMostrar = historicoJornadas.get(indice);

        for (Grupo grupo : gruposMostrar) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(grupo.getNombreGrupo()));
            panel.setPreferredSize(new Dimension(200, 200));

            String[] columnas = {"Nom", "Pts", "PJ", "PG", "PP", "SG", "SP", "Dif"};
            Object[][] datos = grupo.obtenerClasificacion().stream()
                    .map(j -> new Object[]{
                j.getNombre(),
                j.getPuntos(),
                j.getPartidosJugados(),
                j.getPartidosGanados(),
                j.getPartidosPerdidos(),
                j.getSetsGanados(),
                j.getSetsPerdidos(),
                j.getDiferenciaSets()
            }).toArray(Object[][]::new);

            JTable tabla = new JTable(datos, columnas);
            tabla.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tabla.setRowHeight(14);
            tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
            tabla.getColumnModel().getColumn(0).setPreferredWidth(120);

            JScrollPane scrollTabla = new JScrollPane(tabla);
            scrollTabla.setPreferredSize(new Dimension(200, 60));

            JButton botonGrupo = new JButton("Gestionar");
            botonGrupo.setFont(new Font("SansSerif", Font.PLAIN, 10));
            botonGrupo.setMargin(new Insets(2, 4, 2, 4));
            botonGrupo.addActionListener(e -> new ControladorGrupo(torneo, grupo));

            panel.add(scrollTabla, BorderLayout.CENTER);
            panel.add(botonGrupo, BorderLayout.SOUTH);
            paneles.add(panel);
        }

        vista.mostrarGrupos(paneles);
    }

    private void volverAlInicio() {
        vista.dispose(); // cerrar ventana actual
        new ControladorInicio(); // volver a la pantalla inicial (ajústalo según tu clase)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ascensos")) {
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Ejecutar ascensos/descensos?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                torneo.ejecutarAscensosYDescensos();
                GestorDatos.guardarTorneo(torneo);
                guardarJornada(); // guardar nuevo estado tras ascensos
                mostrarJornada(indiceJornada);
                JOptionPane.showMessageDialog(vista, "Torneo guardado tras aplicar ascensos/descensos.");
            }
        }
    }
}
