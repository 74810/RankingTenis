package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.CargadorTorneo;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.vista.VentanaInicio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ControladorInicio implements ActionListener {

    private VentanaInicio vista;

    public ControladorInicio() {
        vista = new VentanaInicio();
        vista.setControlador(this);

        File carpeta = new File("recursos/torneos_guardados");
        String[] jsons = carpeta.list((dir, name) -> name.toLowerCase().endsWith(".json"));
        vista.setListaTorneos(jsons);

        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vista.getBotonCargar()) {
            iniciarTorneo();
        } else if (source == vista.getBotonCrear()) {
            System.out.println("Crear torneo no implementado aún");
        }
    }

    public void iniciarTorneo() {
        String seleccionado = vista.getTorneoSeleccionado();
        if (seleccionado == null || seleccionado.isBlank()) {
            System.out.println("No se ha seleccionado ningún torneo.");
            return;
        }

        String ruta = "recursos/torneos_guardados/" + seleccionado;
        Torneo torneo = CargadorTorneo.cargarTorneoDesdeArchivo(ruta);
        if (torneo != null) {
            new ControladorTorneo(torneo);
            vista.dispose();
        } else {
            System.out.println("El torneo no se pudo cargar.");
        }
    }
}
