package com.mycompany.rankingtenis.vista;

import com.mycompany.rankingtenis.modelo.Partido;

import javax.swing.*;
import java.awt.*;

public class DialogoResultado extends JDialog {

    private boolean confirmado = false;
    private int[] setsJ1;
    private int[] setsJ2;

    public DialogoResultado(Frame parent, Partido partido) {
        super(parent, "Registrar Resultado", true);

        setsJ1 = new int[3];
        setsJ2 = new int[3];

        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Set"));
        panel.add(new JLabel(partido.getJugador1().getNombre()));
        panel.add(new JLabel(partido.getJugador2().getNombre()));

        JTextField[] camposJ1 = new JTextField[3];
        JTextField[] camposJ2 = new JTextField[3];

        for (int i = 0; i < 3; i++) {
            panel.add(new JLabel("Set " + (i + 1)));
            camposJ1[i] = new JTextField(2);
            camposJ2[i] = new JTextField(2);
            panel.add(camposJ1[i]);
            panel.add(camposJ2[i]);
        }

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            try {
                for (int i = 0; i < 3; i++) {
                    setsJ1[i] = Integer.parseInt(camposJ1[i].getText());
                    setsJ2[i] = Integer.parseInt(camposJ2[i].getText());
                }
                confirmado = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Introduce resultados válidos para todos los sets.");
            }
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        JPanel botones = new JPanel();
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public int[] getSetsJugador1() {
        return setsJ1;
    }

    public int[] getSetsJugador2() {
        return setsJ2;
    }
}
