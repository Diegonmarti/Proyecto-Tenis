package proyectotenis;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * Este método es para conectar la base de datos con mysql
 */
public class login {

    public static Connection conectdb() {

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tenis", "root", "");
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     /**
      * 
      * Este método es para iniciar sesión, en caso de que sea profesor puede entrar con el usuario root y contraseña 4444 
      */
    public static void logueo(JTextField usuario, JPasswordField contraseña, JFrame j2) {
        Connection con = login.conectdb();
        iniciarsesion m = new iniciarsesion(usuario.getText(), contraseña.getText());
        Profesor p = new Profesor(usuario.getText(), contraseña.getText());
        PreparedStatement ps = null;
        ResultSet rs = null;
        String login = "SELECT * FROM PRUEBA WHERE USUARIO =? AND CONTRASEÑA =?";
        try {
            ps = con.prepareStatement(login);
            ps.setString(1, usuario.getText());
            ps.setString(2, contraseña.getText());
            rs = ps.executeQuery();
            if (usuario.getText().equals("root") && contraseña.getText().equals("4444")) {
                p.setVisible(true);
                j2.dispose();
            } else {
                if (rs.next()) {

                    m.setVisible(true);
                    j2.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "La contraseña no es correcta.");
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "login fail");
        }
    }

    /**
     * 
     * Este método es para cambiar el formato de la contraseña al darle click al mostrar contraseña
     */
    public static void comprobarcontraseña(JCheckBox mostrar, JPasswordField contraseña) {
        if (mostrar.isSelected()) {
            contraseña.setEchoChar((char) 0);

        } else {
            contraseña.setEchoChar('\u2022');

        }
    }
    /**
     * 
     * este método te comprueba si hemos metido un valor en vacio
     */
    public static void comprobarvacio(JTextField nombre, JPasswordField contraseña, JTextField confcontraseña) {
        if ((nombre.getText() == null) || (contraseña.getText() == null) || (confcontraseña.getText() == null)) {
            JOptionPane.showMessageDialog(null, "Completa datos obligatorios", "Error", JOptionPane.ERROR);
        }
    }
    /**
     * 
     * En el método registar tienes que meterle todos los valores que te pide y los pasa a la base de datos mysql que está conectada con XAMP
     */
    public static void registrar(JTextField usuario, JPasswordField contraseña, JTextField nombre, JTextField apellido, JDateChooser fechanac, JSpinner nivel, JTextField correo) {
        Connection con = login.conectdb();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        String nivelito = nivel.getValue() + "";
        String query = "INSERT INTO PRUEBA VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO CONTRASEÑA VALUES (?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, usuario.getText());
            ps.setString(2, contraseña.getText());
            ps.setString(3, nombre.getText());
            ps.setString(4, apellido.getText());
            ps.setString(5, fechanac.getDateFormatString());
            ps.setString(6, nivelito);
            ps.setString(7, correo.getText());

            ps2 = con.prepareStatement(query2);
            ps2.setString(1, usuario.getText());
            ps2.setString(2, contraseña.getText());
            ps.executeUpdate();
            ps2.executeUpdate();

        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
    }
    /**
     * 
     * El método cambiar te da la opción de hacer alguna modificación en los datos
     */
    public static int cambiar(JTextField usuario, JTextField contraseña, JTextField nombre, JTextField apellido, JDateChooser fechanac, JSpinner nivel, JTextField correo) {
        Connection con = login.conectdb();
        String us = usuario.getText();
        login.conectdb();
        PreparedStatement ps = null;
        String nivelito = nivel.getValue() + "";
        String query = "UPDATE PRUEBA SET usuario = ?, contraseña = ?, nombre=?, apellido=?, fechanac=?, nivel =?, correo =?"
                + "WHERE usuario = '" + us + "'";

        try {
            ps = con.prepareStatement(query);
            ps.setString(1, usuario.getText());
            ps.setString(2, contraseña.getText());
            ps.setString(3, nombre.getText());
            ps.setString(4, apellido.getText());
            ps.setString(5, fechanac.getDateFormatString());
            ps.setString(6, nivelito);
            ps.setString(7, correo.getText());
            int rs = ps.executeUpdate();
            return 1;
        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
        return 0;
    }
    /**
     * En caso de que quieras cambiar la contraseña, usamos este método
     */
    public static int cambiarcontraseña(JTextField usuario, JPasswordField contraseña, JPasswordField contnueva, JPasswordField confcont) {
        Connection con = login.conectdb();
        login.conectdb();
        String us = usuario.getText();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        String query = "UPDATE CONTRASEÑA SET usuario = ?, contra = ?" + "WHERE usuario = '" + us + "'";
        String query2 = "UPDATE PRUEBA SET usuario = ?, contraseña = ?" + "WHERE usuario = '" + us + "'";
        try {

            ps = con.prepareStatement(query);

            ps.setString(1, us);
            ps.setString(2, contnueva.getText());

            ps2 = con.prepareStatement(query2);
            ps2.setString(1, us);
            ps2.setString(2, contnueva.getText());

            ps.executeUpdate();
            ps2.executeUpdate();

            return 1;

        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
        return 0;

    }
    /**
     * 
     * Este método lo usamos en la interfaz del profesor para añadir equipos 
     */
    public static void añadirequipo(JTextField usuario, JTable tabla) {
        Connection con = login.conectdb();
        String us = usuario.getText();
        login.conectdb();
        PreparedStatement ps = null;
        ResultSet st = null;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("grupo");
        model.addColumn("nivel");
        model.addColumn("asistencia");

        tabla.setModel(model);

        String datos[] = new String[3];
        //  String query = "SELECT EQUIPO, NIVEL , ASISTENCIA FROM EQUIPOS WHERE usuario = '" + us + "'";
        String query = "SELECT GRUPO , NIVEL, ASISTENCIA FROM ASISTENCIA WHERE usuario = '" + us + "'";

        try {
            ps = con.prepareStatement(query);
            st = ps.executeQuery();
            while (st.next()) {

                datos[0] = st.getString(1);
                datos[1] = st.getString(2);
                datos[2] = st.getString(3);
                model.addRow(datos);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
    }
    /**
     * 
     *Este método es para crear asistencias, de cuántos alumnos van a asistir a la clase.
     */
    public static int subir(JTextField grupo, JTextField nivel, JComboBox<String> asistencia, JTextField usuario) {
        Connection con = login.conectdb();
        String us = usuario.getText();
        login.conectdb();
        PreparedStatement ps = null;
        String query = "INSERT INTO ASISTENCIA VALUES (?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, grupo.getText());
            ps.setString(2, nivel.getText());
            ps.setString(3, asistencia.getItemAt(0));
            ps.setString(4, usuario.getText());

            ps.executeUpdate();
            return 1;

        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
        return 0;
    }
    /**
     * 
     * El método grupos inserta el nombre en niveles
     */
    public static void grupos(String nombre) {
        Connection con = login.conectdb();
        login.conectdb();
        PreparedStatement ps = null;
        String query = "INSERT INTO NIVELES VALUES (?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, nombre);
            ps.executeUpdate();
        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
    }
    /**
     * 
     *El método partidos te crea una tabla con los partidos que tienes
     */
    public static void partidos(JTextField usuario, JTable tabla) {
        Connection con = login.conectdb();
        String us = usuario.getText();
        login.conectdb();
        PreparedStatement ps = null;
        ResultSet st = null;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("usuario");
        model.addColumn("contrincante");
        model.addColumn("dia");

        tabla.setModel(model);

        String datos[] = new String[3];
        //  String query = "SELECT EQUIPO, NIVEL , ASISTENCIA FROM EQUIPOS WHERE usuario = '" + us + "'";
        String query = "SELECT usuario , contrincante, dia FROM partido WHERE usuario = '" + us + "'";

        try {
            ps = con.prepareStatement(query);
            st = ps.executeQuery();
            while (st.next()) {

                datos[0] = st.getString(1);
                datos[1] = st.getString(2);
                datos[2] = st.getString(3);
                model.addRow(datos);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
    }

    public static int partidos2(JTextField nombre, JTextField nombre1, JDayChooser dia) {
        Connection con = login.conectdb();
        login.conectdb();
        PreparedStatement ps = null;
        String a = dia.getDay() + "";

        String query = "INSERT INTO partido VALUES (?, ?, ?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, nombre.getText());
            ps.setString(2, nombre1.getText());
            ps.setString(3, a);
            ps.executeUpdate();
            return 1;
        } catch (Throwable e) {
            System.out.println("Error" + e);
        }
        return 0;
    }
    /**
     * 
     *Este método te muestra los resultados de los partidos.
     */
    public static void partidosresultados(JTextField usuario, JTable tabla) {
        Connection con = login.conectdb();
        String us = usuario.getText();
        login.conectdb();
        PreparedStatement ps = null;
        ResultSet st = null;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("usuario");
        model.addColumn("contrincante");
        model.addColumn("dia");
        model.addColumn("resultado");
        
        tabla.setModel(model);

        String datos[] = new String[4];
        //  String query = "SELECT EQUIPO, NIVEL , ASISTENCIA FROM EQUIPOS WHERE usuario = '" + us + "'";
        String query = "SELECT usuario , contrincante, dia, resultado FROM partido WHERE usuario = '" + us + "'";

        try {
            ps = con.prepareStatement(query);
            st = ps.executeQuery();
            while (st.next()) {

                datos[0] = st.getString(1);
                datos[1] = st.getString(2);
                datos[2] = st.getString(3);
                datos[3] = st.getString(4);
                model.addRow(datos);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
    }
     public static void main(String[] args) {
      juego e =new juego();
      e.setVisible(true);
    }
}
