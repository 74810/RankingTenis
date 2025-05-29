package com.mycompany.rankingtenis.vista;

import com.mycompany.rankingtenis.modelo.Grupo;
import com.mycompany.rankingtenis.modelo.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentanaPrevisualizarCambios extends JDialog {

    private boolean cambiosConfirmados = false;
    private Map<String, String> grupoFinalSeleccionado = new HashMap<>();

    public VentanaPrevisualizarCambios(List<Grupo> gruposActuales, Map<Jugador, String> grupoSugerido) {
        super((Frame) null, "Previsualizar cambios", true);
        setTitle("Previsualizar y Editar Ascensos/Descensos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<String> nombresGrupos = new ArrayList<>();
        for (Grupo g : gruposActuales) {
            nombresGrupos.add(g.getNombreGrupo());
        }

        JTabbedPane pestañas = new JTabbedPane();
        Map<String, List<Jugador>> agrupadosPorSugerido = new HashMap<>();

        for (Grupo grupo : gruposActuales) {
            for (Jugador jugador : grupo.getJugadores()) {
                String actual = grupo.getNombreGrupo();
                String sugerido = grupoSugerido.getOrDefault(jugador, actual);
                grupoFinalSeleccionado.put(jugador.getNombre(), sugerido);
                agrupadosPorSugerido.computeIfAbsent(sugerido, k -> new ArrayList<>()).add(jugador);
            }
        }

        for (String grupoDestino : agrupadosPorSugerido.keySet()) {
            List<Jugador> jugadores = agrupadosPorSugerido.get(grupoDestino);

            String[] columnas = {"Jugador", "Puntos", "Dif. Sets", "Grupo actual", "Grupo sugerido", "Grupo final"};
            DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5;
                }
            };

            for (Jugador jugador : jugadores) {
                String actual = gruposActuales.stream()
                        .filter(g -> g.getJugadores().contains(jugador))
                        .findFirst()
                        .map(Grupo::getNombreGrupo)
                        .orElse("?");

                modelo.addRow(new Object[]{
                    jugador.getNombre(),
                    jugador.getPuntos(),
                    jugador.getDiferenciaSets(),
                    actual,
                    grupoDestino,
                    grupoDestino
                });
            }

            JTable tabla = new JTable(modelo);
            JComboBox<String> combo = new JComboBox<>(nombresGrupos.toArray(new String[0]));
            tabla.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(combo));

            JScrollPane scroll = new JScrollPane(tabla);
            pestañas.addTab("Grupo " + grupoDestino, scroll);

            tabla.putClientProperty("modelo", modelo);
            tabla.putClientProperty("jugadores", jugadores);
        }

        JButton botonConfirmar = new JButton("Confirmar cambios");
        JButton botonCancelar = new JButton("Cancelar");

        botonConfirmar.addActionListener(e -> {
            for (int i = 0; i < pestañas.getTabCount(); i++) {
                JScrollPane scroll = (JScrollPane) pestañas.getComponentAt(i);
                JTable tabla = (JTable) ((JViewport) scroll.getViewport()).getView();
                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

                TableCellEditor editor = tabla.getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }

                for (int r = 0; r < modelo.getRowCount(); r++) {
                    String jugador = (String) modelo.getValueAt(r, 0);
                    String grupoFinal = (String) modelo.getValueAt(r, 5);
                    grupoFinalSeleccionado.put(jugador, grupoFinal);
                }
            }

            Map<String, Integer> contadorGrupos = new HashMap<>();
            for (String grupo : grupoFinalSeleccionado.values()) {
                contadorGrupos.put(grupo, contadorGrupos.getOrDefault(grupo, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : contadorGrupos.entrySet()) {
                int size = entry.getValue();
                if (size < 4 || size > 5) {
                    JOptionPane.showMessageDialog(this,
                            "El grupo " + entry.getKey() + " tiene " + size + " jugadores.\nCada grupo debe tener entre 4 y 5.",
                            "Error de validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            cambiosConfirmados = true;
            dispose();
        });

        botonCancelar.addActionListener(e -> {
            cambiosConfirmados = false;
            dispose();
        });

        JPanel botones = new JPanel();
        botones.add(botonConfirmar);
        botones.add(botonCancelar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(pestañas, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        setContentPane(contenedor);
    }

    public boolean isCambiosConfirmados() {
        return cambiosConfirmados;
    }

    public Map<String, String> getGrupoFinalSeleccionado() {
        return grupoFinalSeleccionado;
    }
}
