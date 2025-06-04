package com.mycompany.rankingtenis.controlador;

public class RankingTenis {

    public static void main(String[] args) {
        System.out.println("\uD83D\uDFE0 Lanzando aplicación...");
        // Arrancar el controlador de inicio
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ControladorInicio();
        });
    }
}
