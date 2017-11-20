package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudPenalesAux {

    @FXML private TextField partidoEdit;
    @FXML private TextField jugadorEdit;
    @FXML private RadioButton anotadoSi;
    @FXML private RadioButton anotadoNo;
    @FXML private TextField penalEdit;

    private long partido;
    private short penal;

    @FXML
    public void initialize(){

        if(partido != 0 && penal != 0) {
            try {
                String query = "SELECT * FROM penales WHERE numero_partido = ? AND numero_penal = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                ResultSet resultado = statement.executeQuery();
                popularDatos(resultado);
                statement.close();
            } catch (SQLException e) {
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void aceptarOnClick(){
        try{
            String query = "INSERT INTO penales" +
                    "(numero_partido, numero_penal, jugador_penal, anotado) " +
                    "VALUES(?, ?, ?, ?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setLong(1, Long.valueOf(partidoEdit.getText()));
            statement.setLong(2, Long.valueOf(penalEdit.getText()));
            statement.setLong(3, Long.valueOf(jugadorEdit.getText()));
            statement.setInt(4, (anotadoSi.isSelected() ? 1 : 0));
            int resultado = statement.executeUpdate();

            if(resultado == 0)
                throw new Exception("La operación ha sido incorrecta");

            statement.close();

            Stage stage = (Stage) partidoEdit.getScene().getWindow();
            stage.close();

        }catch(SQLException e){
            try{

                String query = "UPDATE gol " +
                        "jugador_penal = ? " +
                        "anotado = ?" +
                        "WHERE numero_partido = ? AND" +
                        "numero_penal = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, Long.valueOf(jugadorEdit.getText()));
                statement.setInt(2, (anotadoSi.isSelected() ? 1 : 0));
                statement.setLong(3, Long.valueOf(partidoEdit.getText()));
                statement.setShort(4, Short.valueOf(penalEdit.getText()));
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
            short penal = Short.valueOf(penalEdit.getText());
            setDatos(numero, penal);
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
        this.partido = pasaporte;
    }

    public void setPenal(short penal){
        this.penal = penal;
    }

    public void setDatos(long pasaporte, short penal){
        setPasaporte(pasaporte);
        setPenal(penal);
        initialize();
    }

    public void popularDatos(ResultSet resultado){
        try{
            resultado.next();

            partidoEdit.setText(String.valueOf(resultado.getLong("numero_partido")));
            jugadorEdit.setText(String.valueOf(resultado.getLong("jugador_penal")));
            anotadoSi.setSelected((resultado.getInt("anotado") == 1) ? true : false);
            penalEdit.setText(String.valueOf(resultado.getShort("numero_penal")));

            resultado.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }

    }

}
