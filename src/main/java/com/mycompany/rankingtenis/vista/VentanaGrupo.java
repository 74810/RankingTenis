package com.mycompany.rankingtenis.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.rankingtenis.modelo.Jugador;

public class VentanaGrupo extends JFrame {
    private JTable tablaClasificacion;
    private JPanel panelPartidos;
    private JButton botonCerrar;

    public VentanaGrupo(String nombreGrupo) {
        setTitle("Gestión de " + nombreGrupo);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla de clasificación
        String[] columnas = {"Nombre", "Puntos", "PJ", "PG", "PP", "SG", "SP", "DIF"};
        tablaClasificacion = new JTable(new DefaultTableModel(columnas, 0));
        add(new JScrollPane(tablaClasificacion), BorderLayout.NORTH);

        // Panel de partidos
        panelPartidos = new JPanel();
        panelPartidos.setLayout(new BoxLayout(panelPartidos, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelPartidos), BorderLayout.CENTER);

        botonCerrar = new JButton("Cerrar");
        add(botonCerrar, BorderLayout.SOUTH);
    }

    public void actualizarClasificacion(List<Jugador> jugadores) {
        DefaultTableModel modelo = (DefaultTableModel) tablaClasificacion.getModel();
        modelo.setRowCount(0); // limpiar
        for (Jugador j : jugadores) {
            Object[] fila = {
                j.getNombre(), j.getPuntos(), j.getPartidosJugados(), j.getPartidosGanados(),
                j.getPartidosPerdidos(), j.getSetsGanados(), j.getSetsPerdidos(), j.getDiferenciaSets()
            };
            modelo.addRow(fila);
        }
    }

    public void mostrarPartidos(List<JButton> botones) {
        panelPartidos.removeAll();
        for (JButton b : botones) {
            panelPartidos.add(b);
        }
        revalidate();
        repaint();
    }

    public JButton getBotonCerrar() {
        return botonCerrar;
    }
}
