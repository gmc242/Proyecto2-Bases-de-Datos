package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudDelegacionAux {

    @FXML private TextField partidoEdit;
    @FXML private TextField pasaporteEdit;

    private String tabla;
    private Long partido;
    private String atributo;

    @FXML
    public void initialize(){
        if(partido > 0)
            partidoEdit.setText(String.valueOf(partido));
    }

    @FXML
    public void aceptarOnClick(){

        if(tabla != null && !tabla.equals("") && atributo != null && !atributo.equals("") && partido > 0 ) {
            try {

                String query = "INSERT INTO ? " +
                        "(numero_partido, ?) " +
                        "VALUES (?,?)";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, tabla);
                statement.setString(2, atributo);
                statement.setLong(3, Long.valueOf(partidoEdit.getText()));
                statement.setLong(4, Long.valueOf(pasaporteEdit.getText()));
                int resultado = statement.executeUpdate();

                if (resultado == 0)
                    throw new Exception("La operación ha sido incorrecta");

                statement.close();

                Stage stage = (Stage) partidoEdit.getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                Controller.manejarExcepcion(e);
            } catch (Exception e) {
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void cancelarOnClick(){
        Stage stage = (Stage) partidoEdit.getScene().getWindow();
        stage.close();
    }

    public void setDatos(String tabla, long partido, String atributo){
        this.tabla = tabla;
        this.partido = partido;
        this.atributo = atributo;
        initialize();
    }

}
