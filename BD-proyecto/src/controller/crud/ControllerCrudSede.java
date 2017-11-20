package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudSede {

    @FXML private TextField estadioEdit;
    @FXML private TextField sedeEdit;
    @FXML private TextField capacidadEdit;

    private String codigo;

    public ControllerCrudSede(String codigo) {
        this.codigo = codigo;
    }

    public ControllerCrudSede() {
        codigo = null;
    }

    @FXML
    public void initialize() {
        if (codigo != null && !codigo.equals("")) {
            try{
                String query = "SELECT * FROM sede WHERE nombre_estadio = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, codigo);

                ResultSet resultado = statement.executeQuery();
                popularDatos(resultado);

                statement.close();
            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void buscarOnClick(){
        String nombre = estadioEdit.getText();
        if(nombre != null && !nombre.equals(""))
            setCodigo(nombre);
    }

    @FXML
    public void aceptarOnClick(){

    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
        initialize();
    }

    private void popularDatos(ResultSet resultado){
        try{
            resultado.next();

            estadioEdit.setText(resultado.getString("nombre_estadio"));
            sedeEdit.setText(resultado.getString("nombre_sede"));
            capacidadEdit.setText(String.valueOf(resultado.getInt("capacidad")));

            resultado.close();
        }catch (SQLException e){

        }
    }
}
