package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaInicio extends JFrame {

    private JTextField campoNombreTorneo;
    private JButton botonCrear;
    private JComboBox<String> comboTorneos;
    private JButton botonCargar;

    public VentanaInicio(List<String> torneosGuardados) {
        setTitle("RankingTenis - Inicio");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 220);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // espaciado interno

        // Campo para nuevo torneo
        JLabel etiquetaNuevo = new JLabel("Nombre del nuevo torneo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(etiquetaNuevo, gbc);

        campoNombreTorneo = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campoNombreTorneo, gbc);

        botonCrear = new JButton("Crear nuevo torneo");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCrear, gbc);

        // Combo para torneos guardados
        JLabel etiquetaCargar = new JLabel("Torneos guardados:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(etiquetaCargar, gbc);

        comboTorneos = new JComboBox<>();
        for (String nombre : torneosGuardados) {
            comboTorneos.addItem(nombre);
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(comboTorneos, gbc);

        botonCargar = new JButton("Cargar torneo seleccionado");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCargar, gbc);

        setVisible(true);
    }

    public String getNombreNuevoTorneo() {
        return campoNombreTorneo.getText().trim();
    }

    public String getTorneoSeleccionado() {
        return (String) comboTorneos.getSelectedItem();
    }

    public void setControlador(ActionListener listener) {
        botonCrear.setActionCommand("crear");
        botonCrear.addActionListener(listener);
        botonCargar.setActionCommand("cargar");
        botonCargar.addActionListener(listener);
    }
}
