package controller.crud;

import controller.Controller;
import interfaz.MessageBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class ControllerCrudArbitro {

    @FXML private TextField pasaporteEdit;
    @FXML private TextField nombreEdit;
    @FXML private TextField apellidoEdit;
    @FXML private TextField apellido2Edit;
    @FXML private DatePicker nacimientoDate;
    @FXML private DatePicker inicioDate;
    @FXML private TextField nacionalidadEdit;

    private long numero_pasaporte;

    public ControllerCrudArbitro(long numero_pasaporte){ this.numero_pasaporte = numero_pasaporte; }

    public ControllerCrudArbitro(){ numero_pasaporte = 0; }

    @FXML
    public void initialize(){
        if(numero_pasaporte != 0){
            try {

                String query = "SELECT * FROM persona p " +
                        "INNER JOIN arbitro a " +
                        "ON p.numero_pasaporte = a.numero_pasaporte " +
                        "WHERE p.numero_pasaporte = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, numero_pasaporte);

                ResultSet resultado = statement.executeQuery();

                popularDatos(resultado);

                statement.close();

            }catch(SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void buscarOnClick(){
        try{
            long numero = Long.valueOf(pasaporteEdit.getText());
            setNumero_pasaporte(numero);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void aceptarOnClick() {
        try {
            Controller.obtenerConexion().setAutoCommit(false);

            String query = "INSERT INTO persona " +
                    "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento) " +
                    "VALUES(?, ?, ?, ?, ?)";

            String queryPersona = "INSERT INTO arbitro " +
                    "(numero_pasaporte, inicio_mundiales, nacionalidad) " +
                    "VALUES(?, ?, ?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setLong(1, Long.valueOf(pasaporteEdit.getText()));
            statement.setString(2, nombreEdit.getText());
            statement.setString(3, apellidoEdit.getText());
            statement.setString(4, apellido2Edit.getText());
            statement.setDate(5, Date.valueOf(nacimientoDate.getValue()));

            int resultado = statement.executeUpdate();

            if (resultado == 0)
                throw new Exception("La operación ha sido incorrecta");

            statement.close();

            statement = Controller.obtenerConexion().prepareStatement(queryPersona);
            statement.setLong(1, Long.valueOf(pasaporteEdit.getText()));
            statement.setDate(2, Date.valueOf(inicioDate.getValue()));
            statement.setString(3, nacionalidadEdit.getText());

            resultado = statement.executeUpdate();
            if (resultado == 0) {
                throw new Exception("La operacion no se ha realizado correctamente.");
            }

            Controller.obtenerConexion().commit();
            Controller.obtenerConexion().setAutoCommit(true);

            MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se ha creado con exito el arbitro");

        } catch (SQLException e) {
            try{

                String query = "UPDATE persona " +
                        "SET nombreP = ?, " +
                        "apellido1 = ?, " +
                        "apellido2 = ?, " +
                        "fecha_nacimiento = ? " +
                        "WHERE numero_pasaporte = ?";

                String queryArbitro = "UPDATE arbitro " +
                        "SET inicio_mundiales = ?, " +
                        "nacionalidad = ? " +
                        "WHERE numero_pasaporte = ?";

                Controller.obtenerConexion().setAutoCommit(false);

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, nombreEdit.getText());
                statement.setString(2, apellidoEdit.getText());
                statement.setString(3, apellido2Edit.getText());
                statement.setDate(4, Date.valueOf(nacimientoDate.getValue()));
                statement.setLong(5, Long.valueOf(pasaporteEdit.getText()));

                int resultado = statement.executeUpdate();

                if (resultado == 0)
                    throw new Exception("La operación ha sido incorrecta");

                statement.close();

                statement = Controller.obtenerConexion().prepareStatement(queryArbitro);
                statement.setDate(1, Date.valueOf(inicioDate.getValue()));
                statement.setString(2, nacionalidadEdit.getText());
                statement.setLong(3, Long.valueOf(pasaporteEdit.getText()));

                resultado = statement.executeUpdate();
                if (resultado == 0) {
                    throw new Exception("La operacion no se ha realizado correctamente.");
                }

                Controller.obtenerConexion().commit();
                Controller.obtenerConexion().setAutoCommit(true);

                MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se ha creado con exito el arbitro");

            }catch (SQLException e2){
                Controller.manejarExcepcion(e);
            }catch (Exception e2){
                Controller.manejarExcepcion(e2);
            }
        } catch (Exception e) {
            Controller.manejarExcepcion(e);
        }
    }

    public void setNumero_pasaporte(long numero_pasaporte){
        this.numero_pasaporte = numero_pasaporte;
        this.initialize();
    }

    private void popularDatos(ResultSet resultados){
        try{

            resultados.next();

            pasaporteEdit.setText(String.valueOf(resultados.getLong("numero_pasaporte")));
            nombreEdit.setText(resultados.getString("nombreP"));
            apellidoEdit.setText(resultados.getString("apellido1"));
            apellido2Edit.setText(resultados.getString("apellido2"));
            nacionalidadEdit.setText(resultados.getString("nacionalidad"));

            inicioDate.setValue(resultados.getDate("inicio_mundiales").toLocalDate());
            inicioDate.setValue(resultados.getDate("fecha_nacimiento").toLocalDate());

            resultados.close();

        }catch (SQLException e){

            Controller.manejarExcepcion(e);

            try{
                if(resultados != null)
                    resultados.close();
            }catch (SQLException e2){

            }
        }

    }
}
