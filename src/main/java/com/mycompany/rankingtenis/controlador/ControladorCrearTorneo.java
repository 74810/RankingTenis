package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.GestorDatos;
import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.Jugador;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.vista.PanelGrupoEditable;
import com.mycompany.rankingtenis.vista.VentanaCrearTorneo;
import java.awt.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorCrearTorneo implements ActionListener {
    private VentanaCrearTorneo vista;
    private Torneo torneo;
    private int contadorGrupos = 0;

    public ControladorCrearTorneo(Torneo torneo) {
        this.torneo = torneo;
        this.vista = new VentanaCrearTorneo(torneo.getNombreTorneo());
        this.vista.setControlador(this);
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("agregar_grupo")) {
            if (contadorGrupos >= 20) {
                JOptionPane.showMessageDialog(vista, "Máximo 20 grupos.");
                return;
            }
            contadorGrupos++;
            PanelGrupoEditable panel = new PanelGrupoEditable(contadorGrupos);
            vista.agregarPanelGrupo(panel);
        }

        if (comando.equals("finalizar")) {
            for (Component comp : vista.getGrupoPanels()) {
                if (comp instanceof PanelGrupoEditable) {
                    PanelGrupoEditable panel = (PanelGrupoEditable) comp;

                    String nombreGrupo = panel.getNombreGrupo();
                    String[] nombres = panel.getNombresJugadores();

                    if (nombres.length < 4 || nombres.length > 5) {
                        JOptionPane.showMessageDialog(vista, "El grupo " + nombreGrupo + " debe tener entre 4 y 5 jugadores.");
                        return;
                    }

                    Grupo grupo = new Grupo(nombreGrupo);
                    for (String nombre : nombres) {
                        grupo.agregarJugador(new Jugador(nombre));
                    }
                    grupo.generarPartidos();
                    torneo.agregarGrupo(grupo);
                }
            }
            GestorDatos.guardarTorneo(torneo);
            new ControladorTorneo(torneo);
            vista.dispose();
        }
    }
}
