package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudEmpleado {

    @FXML private TextField pasaporteEdit;
    @FXML private TextField nombreEdit;
    @FXML private TextField apellidoEdit;
    @FXML private TextField apellido2Edit;
    @FXML private DatePicker nacimientoDate;
    @FXML private DatePicker inicioDate;
    @FXML private TextField nacionalidadEdit;
    @FXML private ComboBox<String> equipoBox;
    @FXML private TextField tipoEdit;

    private long numero_pasaporte;
    private String tipo;

    public ControllerCrudEmpleado(long numero_pasaporte, String tipo){
        this.numero_pasaporte = numero_pasaporte;
        this.tipo = tipo;
    }

    public ControllerCrudEmpleado(String tipo){
        numero_pasaporte = 0;
        this.tipo = tipo;
    }

    public ControllerCrudEmpleado(){
        numero_pasaporte = 0;
        tipo = "";
    }

    @FXML
    public void initialize(){

        popularEquipos();

        if(numero_pasaporte != 0){
            try {

                String query = "SELECT * FROM persona p " +
                        "INNER JOIN empleado e " +
                        "ON p.numero_pasaporte = e.numero_pasaporte " +
                        "WHERE p.numero_pasaporte = ? AND e.tipo_empleado = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, numero_pasaporte);
                statement.setString(2, tipo);

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
            long temp = Long.valueOf(pasaporteEdit.getText());
            setNumero_pasaporte(temp);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void aceptarOnClick(){
        try{
            Controller.obtenerConexion().setAutoCommit(false);

            String query = "INSERT INTO persona " +
                    "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento) " +
                    "VALUES(?, ?, ?, ?, ?)";

            String queryEmpleado = "INSERT INTO empleado " +
                    "(numero_pasaporte, nacionalidad, fecha_inicio_puesto, tipo_empleado, tipo_especifico_empleado, codigo_fifa_equipo)" +
                    "VALUES(?,?,?,?,?,?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setLong(1, Long.valueOf(pasaporteEdit.getText()));
            statement.setString(2, nombreEdit.getText());
            statement.setString(3, apellidoEdit.getText());
            statement.setString(4, apellido2Edit.getText());
            statement.setDate(5, Date.valueOf(nacimientoDate.getValue()));
            statement.executeUpdate();
            statement.close();

            statement = Controller.obtenerConexion().prepareStatement(queryEmpleado);
            statement.setLong(1, Long.valueOf(pasaporteEdit.getText()));
            statement.setString(2, nacionalidadEdit.getText());
            statement.setDate(3, Date.valueOf(inicioDate.getValue()));
            statement.setString(4, this.tipo);
            statement.setString(5, tipoEdit.getText());
            statement.setString(6, equipoBox.getSelectionModel().getSelectedItem());
            statement.executeUpdate();
            statement.close();

            Controller.obtenerConexion().commit();
            Controller.obtenerConexion().setAutoCommit(true);
        }catch (SQLException e){
            try{
                Controller.obtenerConexion().setAutoCommit(false);

                String query = "UPDATE persona " +
                        "SET nombreP = ?, " +
                        "apellido1 = ?, " +
                        "apellido2 = ?, " +
                        "fecha_nacimiento = ? " +
                        "WHERE numero_pasaporte = ?";

                String queryEmpleado = "UPDATE empleado " +
                        "SET nacionalidad = ?, " +
                        "fecha_inicio_puesto = ?, " +
                        "tipo_empleado = ?, " +
                        "tipo_especifico_empleado = ?, " +
                        "codigo_fifa_equipo = ? " +
                        "WHERE numero_pasaporte = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);

                statement.setString(1, nombreEdit.getText());
                statement.setString(2, apellidoEdit.getText());
                statement.setString(3, apellido2Edit.getText());
                statement.setDate(4, Date.valueOf(nacimientoDate.getValue()));
                statement.setLong(5, Long.valueOf(pasaporteEdit.getText()));
                statement.executeUpdate();
                statement.close();

                statement = Controller.obtenerConexion().prepareStatement(queryEmpleado);
                statement.setString(1, nacionalidadEdit.getText());
                statement.setDate(2, Date.valueOf(inicioDate.getValue()));
                statement.setString(3, this.tipo);
                statement.setString(4, tipoEdit.getText());
                statement.setString(5, equipoBox.getSelectionModel().getSelectedItem());
                statement.setLong(6, Long.valueOf(pasaporteEdit.getText()));
                statement.executeUpdate();
                statement.close();

                Controller.obtenerConexion().commit();
                Controller.obtenerConexion().setAutoCommit(true);
            }catch (SQLException e2){
                Controller.manejarExcepcion(e2);
            }
        }
    }

    public void setNumero_pasaporte(long numero_pasaporte){
        this.numero_pasaporte = numero_pasaporte;
        this.initialize();
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    private void popularDatos(ResultSet resultados){

        try{

            resultados.next();

            pasaporteEdit.setText(String.valueOf(resultados.getLong("numero_pasaporte")));
            nombreEdit.setText(resultados.getString("nombreP"));
            apellidoEdit.setText(resultados.getString("apellido1"));
            apellido2Edit.setText(resultados.getString("apellido2"));
            nacionalidadEdit.setText(resultados.getString("nacionalidad"));

            Date fechaInicio = resultados.getDate("fecha_inicio_puesto");
            Date fechaNacimiento = resultados.getDate("fecha_nacimiento");

            if(fechaInicio != null && fechaNacimiento != null){
                inicioDate.setValue(resultados.getDate("fecha_inicio_puesto").toLocalDate());
                nacimientoDate.setValue(resultados.getDate("fecha_nacimiento").toLocalDate());
            }

            if(tipo != "Tecnico")
                tipoEdit.setText(resultados.getString("tipo_especifico_empleado"));

            equipoBox.getSelectionModel().select(resultados.getString("codigo_fifa_equipo"));

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

    private void popularEquipos(){
        String query = "SELECT codigo_fifa FROM equipo";

        try {

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            ResultSet resultado = statement.executeQuery();

            while(resultado.next())
                equipoBox.getItems().add(resultado.getString("codigo_fifa"));

            resultado.close();
            statement.close();


        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }


    }
}
