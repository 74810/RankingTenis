package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.CargadorTorneo;
import com.mycompany.rankingtenis.modelo.GestorDatos;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.vista.VentanaInicio;
import com.mycompany.rankingtenis.vista.VentanaTorneo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorInicio implements ActionListener {

    private VentanaInicio vista;

    public ControladorInicio() {
        vista = new VentanaInicio();
        vista.setControlador(this);
        vista.setListaTorneos(GestorDatos.listarTorneosGuardados().toArray(new String[0]));
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("crear")) {
            String nombre = vista.getNombreNuevoTorneo();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Escribe un nombre para el torneo.");
                return;
            }

            Torneo torneo = new Torneo(nombre);
            new ControladorCrearTorneo(torneo);
            vista.dispose();

        } else if (comando.equals("cargar")) {
            String seleccionado = vista.getTorneoSeleccionado();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Selecciona un torneo.");
                return;
            }

            Torneo torneo = GestorDatos.cargarTorneo(seleccionado);
            if (torneo != null) {
                new ControladorTorneo(torneo);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al cargar torneo.");
            }
        }
    }

    public void iniciarTorneo() {
        try {
            String ruta = "recursos/torneos_guardados/tu_torneo_con_historial.json";
            Torneo torneo = CargadorTorneo.cargarTorneoDesdeArchivo(ruta);
            if (torneo != null) {
                new ControladorTorneo(torneo);
            } else {
                System.out.println("Error al cargar el torneo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
