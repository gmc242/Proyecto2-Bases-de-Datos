package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudGolAux {

    @FXML private TextField partidoEdit;
    @FXML private TextField jugadorEdit;
    @FXML private ComboBox<String> equipoBox;
    @FXML private TextField minutoEdit;

    private long partido;

    @FXML
    public void initialize(){
        popularEquipos();

        if(partido > 0){

            try{
                String query = "SELECT * FROM gol WHERE numero_partido = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, partido);
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
            String query = "INSERT INTO gol" +
                    "(numero_partido, equipo, jugador_gol, minuto) " +
                    "VALUES(?, ?, ?, ?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setLong(1, Long.valueOf(partidoEdit.getText()));
            statement.setString(2, equipoBox.getSelectionModel().getSelectedItem());
            statement.setLong(3, Long.valueOf(jugadorEdit.getText()));
            statement.setShort(4, Short.valueOf(minutoEdit.getText()));
            int resultado = statement.executeUpdate();

            if(resultado == 0)
                throw new Exception("La operación ha sido incorrecta");

            statement.close();

            Stage stage = (Stage) partidoEdit.getScene().getWindow();
            stage.close();

        }catch(SQLException e){
            try{

                String query = "UPDATE gol " +
                        "SET equipo = ?" +
                        "jugador_gol = ? " +
                        "minuto = ?" +
                        "WHERE numero_partido = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, equipoBox.getSelectionModel().getSelectedItem());
                statement.setLong(2, Long.valueOf(jugadorEdit.getText()));
                statement.setShort(3, Short.valueOf(minutoEdit.getText()));
                statement.setLong(4, Long.valueOf(partidoEdit.getText()));
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
        this.partido = pasaporte;
        initialize();
    }

    public void popularDatos(ResultSet resultado){
        try{
            resultado.next();

            partidoEdit.setText(String.valueOf(resultado.getLong("numero_partido")));
            jugadorEdit.setText(String.valueOf(resultado.getLong("jugador")));
            equipoBox.getSelectionModel().select(resultado.getString("equipo"));
            minutoEdit.setText(String.valueOf(resultado.getShort("minuto")));

            resultado.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }

    }

    public void popularEquipos(){
        try{
            String query = "SELECT codigo_fifa FROM equipo";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            ResultSet resultado = statement.executeQuery();

            while(resultado.next())
                equipoBox.getItems().add(resultado.getString("codigo_equipo"));

            resultado.close();
            statement.close();
        }catch (SQLException e){

        }
    }
}
