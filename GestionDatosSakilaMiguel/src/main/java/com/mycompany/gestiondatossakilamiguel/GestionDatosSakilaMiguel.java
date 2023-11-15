package com.mycompany.gestiondatossakilamiguel;

import java.awt.*;
import java.io.File;
import java.sql.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GestionDatosSakilaMiguel {

    public static JFrame f;
    private JPanel p;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem elem1;
    private JMenuItem elem2;
    private JMenuItem elem3;
    private JMenuItem elem4;
    private JMenuItem elem5;
    private JMenuItem elem6;
    private JMenuItem elem8;
    private JMenuItem elem9;
    private JMenuItem elem10;

    public static void main(String[] args) {
        GestionDatosSakilaMiguel sakila = new GestionDatosSakilaMiguel();
        sakila.initComponents();
    }

    private void initComponents() {
        reproducirSonido("start.wav");

        f = new JFrame("Gestor de Sakila MySQL");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(new Dimension(800, 600));
        f.setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menu = new JMenu("Opciones");

        elem1 = new JMenuItem("- - > Payment");
        elem2 = new JMenuItem("- - > Rental");
        elem3 = new JMenuItem("- - > Store");
        elem4 = new JMenuItem("- - > Payment-Customer");
        elem5 = new JMenuItem("- - > Customer");
        elem6 = new JMenuItem("- - > Staff");
        elem8 = new JMenuItem("- - > Cities");
        elem9 = new JMenuItem("- - > Movies (según su duración)");
        elem10 = new JMenuItem("- - > Sakila staff info (país y ciudad)");

        elem1.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT payment_id, customer_id, amount, payment_date FROM payment";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem2.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT rental_id, customer_id, return_date FROM rental";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem3.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT store_id, address_id FROM store";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem4.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT p.customer_id, p.amount, p.payment_date FROM payment p JOIN rental r ON p.rental_id = r.rental_id WHERE r.return_date > CURDATE()";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem5.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT customer_id, first_name FROM customer WHERE active = 1";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem6.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String query = "SELECT first_name, last_name, address_id, store_id FROM staff WHERE active = 1";
                ejecutarQuery(query);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem8.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String country = JOptionPane.showInputDialog(f, "País en inglés:");
                if (country != null && !country.isEmpty()) {
                    String query = "SELECT city FROM city c JOIN country co ON c.country_id = co.country_id WHERE co.country = ?";
                    ejecutarQueryMulti(query, country);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem9.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                int duracionMinima = Integer.parseInt(JOptionPane.showInputDialog(f, "Duración mínima en minutos:"));
                int duracionMaxima = Integer.parseInt(JOptionPane.showInputDialog(f, "Duración máxima en minutos:"));
                String query = "SELECT title, length FROM film WHERE length BETWEEN ? AND ?";
                ejecutarQueryMulti(query, duracionMinima, duracionMaxima);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Ingrese valores numéricos válidos para la duración.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        elem10.addActionListener(e -> {
            reproducirSonido("botonSonido.wav");
            try {
                String country = JOptionPane.showInputDialog(f, "País de los empleados:");
                String city = JOptionPane.showInputDialog(f, "Ciudad de los empleados:");
                if (country != null && !country.isEmpty() && city != null && !city.isEmpty()) {
                    String query = "SELECT co.country, ci.city, a.address, s.first_name, s.last_name FROM staff s "
                            + "JOIN address a ON s.address_id = a.address_id "
                            + "JOIN city ci ON a.city_id = ci.city_id "
                            + "JOIN country co ON ci.country_id = co.country_id "
                            + "WHERE co.country = ? AND ci.city = ?";
                    ejecutarQueryMulti(query, country, city);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage());
            }
        });

        menu.add(elem1);
        menu.add(elem2);
        menu.add(elem3);
        menu.add(elem4);
        menu.add(elem5);
        menu.add(elem6);
        menu.add(elem8);
        menu.add(elem9);
        menu.add(elem10);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menu);
        menuBar.add(Box.createHorizontalGlue());

        p = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        textArea.setForeground(Color.BLUE);
        textArea.setBackground(Color.lightGray);
        int padding = 10;
        p.setBorder(new EmptyBorder(padding, padding, padding, padding));
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        p.add(scrollPane, BorderLayout.CENTER);
        textArea.setVisible(true);
        scrollPane.setVisible(true);

        f.setJMenuBar(menuBar);
        f.add(p, BorderLayout.CENTER);
        f.setVisible(true);
    }

    private void ejecutarQuery(String query) throws Exception {
        textArea.setText("");
        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "");  Statement statement = connection.createStatement();  ResultSet resultSet = statement.executeQuery(query)) {
            mostrarResultados(resultSet);
        }
    }

    private void ejecutarQueryMulti(String query, Object... params) throws Exception {
        textArea.setText("");
        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "");  PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                mostrarResultados(resultSet);
            }
        }
    }

    private void mostrarResultados(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            textArea.append(String.format("%25s", metaData.getColumnName(i)));
        }
        textArea.append("\n");
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                textArea.append(String.format("%25s", metaData.getColumnName(i) + ": " + resultSet.getObject(i)));
            }
            textArea.append("\n");
        }
    }

    private void reproducirSonido(String nombreArchivo) {
        try {
            File archivoSonido = new File(nombreArchivo);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(archivoSonido));
            clip.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, "ERROR al reproducir el sonido: " + e.getMessage());
        }
    }
}
