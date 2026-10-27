package org.example.screen;

import org.example.Connection;
import org.example.screen.components.Buttons;
import org.example.screen.components.PanelResult;
import org.example.screen.components.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Ventana principal: toma los dos números, valida y delega el cálculo a Prolog. */
public class Window extends JFrame {

    private static final String NUMERO = "-?\d+(\.\d+)?";

    private final TextField txtA = new TextField("Ej: 8");
    private final TextField txtB = new TextField("Ej: 2.5");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");

    public Window(Connection motor) {
        super("Calculadora en Prolog");

        txtA.addActionListener(e -> txtB.requestFocusInWindow());

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, etiqueta("Número A"), 6);
        agregar(contenido, txtA, 14);
        agregar(contenido, etiqueta("Número B"), 6);
        agregar(contenido, txtB, 18);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 10);
        agregar(contenido, lblEstado, 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        setContentPane(raiz);
        setMinimumSize(new Dimension(520, 520));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Las operaciones se resuelven con reglas en Prolog");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitulo);
        return p;
    }

    private JPanel crearBotones(Connection motor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.add(boton("+", Theme.BLUE, motor, "suma"));
        p.add(boton("−", Theme.GREEN, motor, "resta"));
        p.add(boton("×", Theme.AMBER, motor, "multiplicacion"));
        p.add(boton("÷", Theme.RED, motor, "division"));
        return p;
    }

    private Buttons boton(String texto, Color color, Connection motor, String predicado) {
        Buttons b = new Buttons(texto, color, Theme.BG, 26, 11, 18);
        b.addActionListener(e -> ejecutar(motor, predicado));
        return b;
    }

    private void ejecutar(Connection motor, String predicado) {
        String a = txtA.getText().trim();
        String b = txtB.getText().trim();
        panelResultado.setTitulo("Resultado");

        if (a.isEmpty() || b.isEmpty()) {
            mostrar("", "Escribí los dos números", Theme.AMBER);
            return;
        }
        if (!a.matches(NUMERO) || !b.matches(NUMERO)) {
            mostrar("", "Solo se aceptan números (ej: 8, 2.5, -3)", Theme.RED);
            return;
        }
        try {
            String resultado = motor.consultar(predicado, a, b);
            if (resultado != null) {
                mostrar(resultado, "Listo", Theme.GREEN);
            } else if (predicado.equals("division")) {
                mostrar("", "No se puede dividir entre cero.", Theme.RED);
            } else {
                mostrar("", "Sin solución", Theme.AMBER);
            }
        } catch (Exception ex) {
            mostrar("", "Error: " + ex.getMessage(), Theme.RED);
        }
    }

    private void mostrar(String resultado, String estado, Color colorEstado) {
        panelResultado.setTexto(resultado);
        lblEstado.setText(estado);
        lblEstado.setForeground(colorEstado);
    }

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(Font.BOLD, 12));
        l.setForeground(Theme.MUTED);
        return l;
    }
}
