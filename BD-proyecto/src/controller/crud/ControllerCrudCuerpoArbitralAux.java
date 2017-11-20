package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudCuerpoArbitralAux {

    @FXML private TextField partidoEdit;
    @FXML private TextField principalEdit;
    @FXML private TextField linea1Edit;
    @FXML private TextField linea2Edit;
    @FXML private TextField cuartoEdit;
    @FXML private TextField quintoEdit;

    private long pasaporte;

    @FXML
    public void initialize(){
        if(pasaporte > 0){

            try{
                String query = "SELECT * FROM cuerpo_arbitral WHERE numero_partido = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, pasaporte);
                ResultSet resultado = statement.executeQuery();

                popularDatos(resultado);

                statement.close();

            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void aceptarOnClick(){
        try{
            String query = "INSERT INTO cuerpo_arbitral" +
                    "(numero_partido, arbitro_principal, linea1, linea2, arbitro4, arbitro5) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setLong(1, Long.valueOf(partidoEdit.getText()));
            statement.setLong(2, Long.valueOf(principalEdit.getText()));
            statement.setLong(3, Long.valueOf(linea1Edit.getText()));
            statement.setLong(4, Long.valueOf(linea2Edit.getText()));
            statement.setLong(5, Long.valueOf(cuartoEdit.getText()));
            statement.setLong(6, Long.valueOf(quintoEdit.getText()));
            int resultado = statement.executeUpdate();

            if(resultado == 0)
                throw new Exception("La operación ha sido incorrecta");

            statement.close();

            Stage stage = (Stage) partidoEdit.getScene().getWindow();
            stage.close();

        }catch(SQLException e){
            try{

                String query = "UPDATE cuerpo_arbitral " +
                        "SET arbitro_principal = ?, " +
                        "linea1 = ?, " +
                        "linea2 = ?, " +
                        "arbitro4 = ?, " +
                        "arbitro5 = ? " +
                        "WHERE numero_partido = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, Long.valueOf(principalEdit.getText()));
                statement.setLong(2, Long.valueOf(linea1Edit.getText()));
                statement.setLong(3, Long.valueOf(linea2Edit.getText()));
                statement.setLong(4, Long.valueOf(cuartoEdit.getText()));
                statement.setLong(5, Long.valueOf(quintoEdit.getText()));
                statement.setLong(6, Long.valueOf(partidoEdit.getText()));
                int resultado = statement.executeUpdate();

                if(resultado == 0)
                    throw new Exception("La operación ha sido incorrecta");

                statement.close();

                Stage stage = (Stage) partidoEdit.getScene().getWindow();
                stage.close();

            }catch (SQLException e2){
                Controller.manejarExcepcion(e2);
            }catch (Exception e2){
                Controller.manejarExcepcion(e2);
            }
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void buscarOnClick(){
        try{
            long numero = Long.valueOf(partidoEdit.getText());
            setPasaporte(numero);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void cancelarOnClick(){
        Stage stage = (Stage) partidoEdit.getScene().getWindow();
        stage.close();
    }

    public void setPasaporte(long pasaporte){
        this.pasaporte = pasaporte;
        initialize();
    }

    public void popularDatos(ResultSet resultado){
        try{
            resultado.next();

            partidoEdit.setText(String.valueOf(resultado.getLong("numero_partido")));
            principalEdit.setText(String.valueOf(resultado.getLong("arbitro_principal")));
            linea1Edit.setText(String.valueOf(resultado.getLong("linea1")));
            linea2Edit.setText(String.valueOf(resultado.getLong("linea2")));
            cuartoEdit.setText(String.valueOf(resultado.getLong("arbitro4")));
            quintoEdit.setText(String.valueOf(resultado.getLong("arbitro5")));

            resultado.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }

    }
}
