package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaInicio extends JFrame {

    private JTextField campoNombre;
    private JButton botonCrear;
    private JComboBox<String> listaTorneos;
    private JButton botonCargar;

    public VentanaInicio() {
        setTitle("Ranking Tenis - Inicio");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo de nombre de nuevo torneo
        JLabel labelNombre = new JLabel("Nombre del nuevo torneo:");
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelNombre);

        campoNombre = new JTextField();
        campoNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(campoNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón crear
        botonCrear = new JButton("Crear torneo");
        botonCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(botonCrear);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Lista de torneos guardados y botón cargar
        JLabel labelGuardados = new JLabel("Torneos guardados:");
        labelGuardados.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelGuardados);

        listaTorneos = new JComboBox<>();
        listaTorneos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(listaTorneos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        botonCargar = new JButton("Cargar torneo");
        botonCargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(botonCargar);

        add(panel);
    }

    public void setControlador(java.awt.event.ActionListener controlador) {
        botonCrear.setActionCommand("crear");
        botonCargar.setActionCommand("cargar");
        botonCrear.addActionListener(controlador);
        botonCargar.addActionListener(controlador);
    }

    public String getNombreNuevoTorneo() {
        return campoNombre.getText();
    }

    public String getTorneoSeleccionado() {
        Object seleccionado = listaTorneos.getSelectedItem();
        return seleccionado != null ? seleccionado.toString() : null;
    }

    public void setListaTorneos(String[] torneos) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(torneos);
        listaTorneos.setModel(model);
    }
}
