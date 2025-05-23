package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaTorneo extends JFrame {

    private JPanel panelGrupos;
    private JButton botonAscensos;

    private JButton botonAnterior;
    private JButton botonSiguiente;
    private JButton botonVolver;

    public VentanaTorneo(String nombreTorneo) {
        setTitle("Torneo: " + nombreTorneo);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelGrupos = new JPanel();
        panelGrupos.setLayout(new GridLayout(8, 2, 5, 5));
        JScrollPane scrollPane = new JScrollPane(panelGrupos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de navegación y control
        JPanel panelControl = new JPanel(new FlowLayout());
        botonVolver = new JButton("⏎ Volver al inicio");
        panelControl.add(botonVolver);
        botonAnterior = new JButton("⏮ Jornada anterior");
        botonSiguiente = new JButton("⏭ Jornada siguiente");
        botonAscensos = new JButton("Ejecutar ascensos/descensos");

        panelControl.add(botonAnterior);
        panelControl.add(botonSiguiente);
        panelControl.add(botonAscensos);

        add(panelControl, BorderLayout.SOUTH);
    }

    public void mostrarGrupos(List<JPanel> panelesGrupo) {
        panelGrupos.removeAll();
        for (JPanel panel : panelesGrupo) {
            panelGrupos.add(panel);
        }
        revalidate();
        repaint();
    }

    // Conecta controlador al botón de ascensos
    public void setControlador(ActionListener listener) {
        botonAscensos.setActionCommand("ascensos");
        botonAscensos.addActionListener(listener);
    }

    public JButton getBotonAscensos() {
        return botonAscensos;
    }

    public JButton getBotonAnterior() {
        return botonAnterior;
    }

    public JButton getBotonSiguiente() {
        return botonSiguiente;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

}
