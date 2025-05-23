package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaCrearTorneo extends JFrame {
    private JPanel panelGrupos;
    private JButton botonAgregarGrupo;
    private JButton botonFinalizar;

    public VentanaCrearTorneo(String nombreTorneo) {
        setTitle("Configurar Torneo: " + nombreTorneo);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelGrupos = new JPanel();
        panelGrupos.setLayout(new BoxLayout(panelGrupos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelGrupos);

        botonAgregarGrupo = new JButton("Añadir grupo");
        botonFinalizar = new JButton("Finalizar y comenzar torneo");

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonAgregarGrupo);
        panelBotones.add(botonFinalizar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void setControlador(ActionListener listener) {
        botonAgregarGrupo.setActionCommand("agregar_grupo");
        botonFinalizar.setActionCommand("finalizar");
        botonAgregarGrupo.addActionListener(listener);
        botonFinalizar.addActionListener(listener);
    }

    public void agregarPanelGrupo(JPanel grupoPanel) {
        panelGrupos.add(grupoPanel);
        revalidate();
        repaint();
    }

    public Component[] getGrupoPanels() {
        return panelGrupos.getComponents();
    }
}
