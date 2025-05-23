package vista;

import javax.swing.*;
import java.awt.*;

public class DialogoResultado extends JDialog {
    private JTextField[] setsJ1 = new JTextField[3];
    private JTextField[] setsJ2 = new JTextField[3];
    private boolean confirmado = false;

    public DialogoResultado(JFrame parent, String nombreJ1, String nombreJ2) {
        super(parent, "Resultado: " + nombreJ1 + " vs " + nombreJ2, true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panelSets = new JPanel(new GridLayout(4, 3));
        panelSets.add(new JLabel(""));
        panelSets.add(new JLabel("J1: " + nombreJ1));
        panelSets.add(new JLabel("J2: " + nombreJ2));

        for (int i = 0; i < 3; i++) {
            panelSets.add(new JLabel("Set " + (i + 1)));
            setsJ1[i] = new JTextField(2);
            setsJ2[i] = new JTextField(2);
            panelSets.add(setsJ1[i]);
            panelSets.add(setsJ2[i]);
        }

        add(panelSets, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");
        panelBotones.add(aceptar);
        panelBotones.add(cancelar);
        add(panelBotones, BorderLayout.SOUTH);

        aceptar.addActionListener(e -> {
            confirmado = true;
            setVisible(false);
        });

        cancelar.addActionListener(e -> {
            confirmado = false;
            setVisible(false);
        });
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public int[] getSetsJugador1() {
        return parsearSets(setsJ1);
    }

    public int[] getSetsJugador2() {
        return parsearSets(setsJ2);
    }

    private int[] parsearSets(JTextField[] campos) {
        int[] sets = new int[3];
        for (int i = 0; i < 3; i++) {
            try {
                sets[i] = Integer.parseInt(campos[i].getText());
            } catch (NumberFormatException e) {
                sets[i] = 0;
            }
        }
        return sets;
    }
}
