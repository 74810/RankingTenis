package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaTorneo extends JFrame {

    private JButton botonAnterior;
    private JButton botonSiguiente;
    private JButton botonAscensos;
    private JButton botonVolver;
    private JPanel panelPrincipal;
    private JLabel etiquetaJornada;

    public VentanaTorneo(String nombreTorneo) {
        setTitle("Torneo: " + nombreTorneo);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Etiqueta de jornada
        etiquetaJornada = new JLabel("Jornada: 1 / 1", SwingConstants.CENTER);
        etiquetaJornada.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(etiquetaJornada, BorderLayout.NORTH);

        // Panel principal con scroll
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columnas
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        botonAnterior = new JButton("Anterior");
        botonSiguiente = new JButton("Siguiente");
        botonAscensos = new JButton("Ascensos/Descensos");
        botonVolver = new JButton("Volver");

        panelBotones.add(botonAnterior);
        panelBotones.add(botonSiguiente);
        panelBotones.add(botonAscensos);
        panelBotones.add(botonVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrarGrupos(List<JPanel> paneles) {
        panelPrincipal.removeAll();
        for (JPanel panel : paneles) {
            panelPrincipal.add(panel);
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    public void actualizarEtiquetaJornada(int actual, int total) {
        etiquetaJornada.setText("Jornada: " + (actual + 1) + " / " + total);
    }

    public JButton getBotonAnterior() {
        return botonAnterior;
    }

    public JButton getBotonSiguiente() {
        return botonSiguiente;
    }

    public JButton getBotonAscensos() {
        return botonAscensos;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    public void setControlador(java.awt.event.ActionListener controlador) {
        botonAnterior.setActionCommand("anterior");
        botonSiguiente.setActionCommand("siguiente");
        botonAscensos.setActionCommand("ascensos");
        botonVolver.setActionCommand("volver");

        botonAnterior.addActionListener(controlador);
        botonSiguiente.addActionListener(controlador);
        botonAscensos.addActionListener(controlador);
        botonVolver.addActionListener(controlador);
    }
}
