package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudEquipo {

    @FXML private TextField codigoEdit;
    @FXML private TextField nombreEdit;
    @FXML private ComboBox<String> confederacionBox;
    @FXML private ComboBox<String> grupoBox;

    private String codigo;

    public ControllerCrudEquipo(String codigo){ this.codigo = codigo; }

    public ControllerCrudEquipo(){ codigo = null; }

    @FXML
    public void initialize(){
        popularConfederaciones();
        popularGrupos();

        if(codigo != null && !codigo.equals("")){
            try{
                String query = "SELECT * FROM equipo WHERE codigo_fifa = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);

                ResultSet resultSet = statement.executeQuery();
                popularDatos(resultSet);

                statement.close();
            }catch (SQLException e){

            }
        }
    }

    @FXML
    public void buscarOnClick(){
        String codigoTemp = codigoEdit.getText();
        if(codigoTemp != null && !codigoTemp.equals(""))
            setCodigo(codigoTemp);
    }

    @FXML
    public void aceptarOnClick(){

    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
        initialize();
    }

    public void popularDatos(ResultSet resultados){
        try{
            resultados.next();

            codigoEdit.setText(resultados.getString("codigo_fifa"));
            nombreEdit.setText(resultados.getString("nombre_pais"));
            confederacionBox.getSelectionModel().select(resultados.getString("codigo_confederacion"));
            grupoBox.getSelectionModel().select(resultados.getString("nombre_grupo"));

            resultados.close();

        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    public void popularGrupos(){
        try{
            String query = "SELECT nombre_grupo FROM grupo";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);

            ResultSet resultados = statement.executeQuery();

            while(resultados.next())
                grupoBox.getItems().add(resultados.getString("nombre_grupo"));

            resultados.close();
            statement.close();

        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    public void popularConfederaciones(){
        try{
            String query = "SELECT codigo_confederacion FROM confederacion";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);

            ResultSet resultados = statement.executeQuery();

            while(resultados.next())
                confederacionBox.getItems().add(resultados.getString("codigo_confederacion"));

            resultados.close();
            statement.close();

        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }
}
