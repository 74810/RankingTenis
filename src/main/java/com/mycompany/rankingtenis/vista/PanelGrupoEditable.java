package com.mycompany.rankingtenis.vista;

import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.Jugador;
import javax.swing.*;
import java.awt.*;

public class PanelGrupoEditable extends JPanel {
    private JTextField[] camposJugadores;
    private JTextField campoNombreGrupo;

    public PanelGrupoEditable(int numeroGrupo) {
        setLayout(new GridLayout(7, 1));
        setBorder(BorderFactory.createTitledBorder("Grupo " + numeroGrupo));

        campoNombreGrupo = new JTextField("Grupo " + (char) ('A' + numeroGrupo - 1));
        add(new JLabel("Nombre del grupo:"));
        add(campoNombreGrupo);

        camposJugadores = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            camposJugadores[i] = new JTextField();
            add(new JLabel("Jugador " + (i + 1) + ":"));
            add(camposJugadores[i]);
        }
    }
    
        public PanelGrupoEditable(Grupo grupo) {
        setLayout(new GridLayout(7, 1));
        setBorder(BorderFactory.createTitledBorder("Grupo " + grupo.getNombre()));

        campoNombreGrupo = new JTextField(grupo.getNombre());
        add(new JLabel("Nombre del grupo:"));
        add(campoNombreGrupo);

        camposJugadores = new JTextField[5];
        java.util.List<Jugador> jugadores = grupo.getJugadores();

        for (int i = 0; i < camposJugadores.length; i++) {
            camposJugadores[i] = new JTextField();
            if (i < jugadores.size()) {
                Jugador jugador = jugadores.get(i);
                camposJugadores[i].setText(jugador.getNombre()
                        + " | PJ: " + jugador.getPartidosJugados()
                        + ", PG: " + jugador.getPartidosGanados()
                        + ", SG: " + jugador.getSetsGanados()
                        + ", SP: " + jugador.getSetsPerdidos());
            }
            add(new JLabel("Jugador " + (i + 1) + ":"));
            add(camposJugadores[i]);
        }
    }
    public String getNombreGrupo() {
        return campoNombreGrupo.getText().trim();
    }

    public String[] getNombresJugadores() {
        return java.util.Arrays.stream(camposJugadores)
                .map(JTextField::getText)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }
}
