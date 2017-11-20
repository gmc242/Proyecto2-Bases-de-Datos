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

public class ControllerCrudJugador {

    @FXML private TextField pasaporteEdit;
    @FXML private TextField nombreEdit;
    @FXML private TextField apellidoEdit;
    @FXML private TextField apellido2Edit;
    @FXML private DatePicker nacimientoDate;
    @FXML private TextField camisetaEdit;
    @FXML private ComboBox<String> equipoBox;
    @FXML private ComboBox<String> posicionBox;

    private long numero_pasaporte;

    public ControllerCrudJugador(long numero_pasaporte){ this.numero_pasaporte = numero_pasaporte; }

    public ControllerCrudJugador(){ numero_pasaporte = 0; }

    @FXML
    public void initialize(){

        String[] posiciones = {"Portero", "Defensa", "Mediocampista", "Delantero"};
        for(String s : posiciones)
            posicionBox.getItems().add(s);
        popularEquipos();

        if(numero_pasaporte != 0){
            try {

                String query = "SELECT * FROM persona p " +
                        "INNER JOIN jugador j " +
                        "ON p.numero_pasaporte = j.numero_pasaporte " +
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
            long temp = Long.valueOf(pasaporteEdit.getText());
            setNumero_pasaporte(temp);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void aceptarOnClick(){}

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
            camisetaEdit.setText(String.valueOf(resultados.getShort("numero_camiseta")));

            Date fechaNacimiento = resultados.getDate("fecha_nacimiento");

            if(fechaNacimiento != null){
                nacimientoDate.setValue(resultados.getDate("fecha_nacimiento").toLocalDate());
            }

            posicionBox.getSelectionModel().select(resultados.getString("posicion"));
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
