package com.mycompany.rankingtenis.controlador;

import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.Jugador;
import com.mycompany.rankingtenis.modelo.Partido;
import com.mycompany.rankingtenis.modelo.Torneo;
import com.mycompany.rankingtenis.modelo.GestorDatos;
import com.mycompany.rankingtenis.vista.VentanaGrupo;
import com.mycompany.rankingtenis.vista.DialogoResultado;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorGrupo {
    private Grupo grupo;
    private Torneo torneo; // 🔄 Añadido: referencia al torneo
    private VentanaGrupo vista;

    public ControladorGrupo(Torneo torneo, Grupo grupo) {
        this.torneo = torneo;
        this.grupo = grupo;
        this.vista = new VentanaGrupo(grupo.getNombreGrupo());

        actualizarVista();
        vista.getBotonCerrar().addActionListener(e -> vista.dispose());
        vista.setVisible(true);
    }

    private void actualizarVista() {
        vista.actualizarClasificacion(grupo.obtenerClasificacion());
        vista.mostrarPartidos(crearBotonesPartidos());
    }

    private List<JButton> crearBotonesPartidos() {
        List<JButton> botones = new ArrayList<>();

        for (Partido partido : grupo.getPartidos()) {
            String texto = partido.toString();
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> abrirDialogoResultado(partido));
            botones.add(boton);
        }

        return botones;
    }

    private void abrirDialogoResultado(Partido partido) {
        DialogoResultado dialogo = new DialogoResultado(vista, partido);


        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            partido.registrarResultado(dialogo.getSetsJugador1(), dialogo.getSetsJugador2());
            actualizarVista();

            //Guardar el torneo después de registrar/modificar el resultado
            GestorDatos.guardarTorneo(torneo);
        }
    }
}
