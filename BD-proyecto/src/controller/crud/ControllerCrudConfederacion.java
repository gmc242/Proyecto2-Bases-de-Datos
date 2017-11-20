package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudConfederacion {

    @FXML private TextField codigoEdit;
    @FXML private TextField nombreEdit;

    private String codigo;

    public ControllerCrudConfederacion(String codigo){ this.codigo = codigo; }

    public ControllerCrudConfederacion(){ codigo = null; }

    @FXML
    public void initialize(){
        if(codigo != null && !codigo.equals("")){
            try {
                String query = "SELECT * FROM confederacion WHERE codigo_confederacion = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, codigo);

                ResultSet resultados = statement.executeQuery();

                popularDatos(resultados);

                statement.close();
            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void aceptarOnClick(){
        try{
            String query = "INSERT INTO confederacion " +
                    "(codigo_confederacion, nombre_confederacion) " +
                    "VALUES (?, ?)";

            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setString(1, codigoEdit.getText());
            statement.setString(2, nombreEdit.getText());
            int resultado = statement.executeUpdate();

            if(resultado == 0)
                throw new Exception("No se pudo realizar la operacion");

            statement.close();
        }catch (SQLException e){
            try{
                String query = "UPDATE confederacion " +
                        "SET nombre_confederacion = ? " +
                        "WHERE codigo_confederacion = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, nombreEdit.getText());
                statement.setString(2, codigoEdit.getText());

                int resultado = statement.executeUpdate();

                if(resultado == 0)
                    throw new Exception("No se pudo realizar la operacion");

                statement.close();
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
        String texto = codigoEdit.getText();
        if(texto != null && !texto.equals(""))
            setCodigo(texto);
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
        initialize();
    }

    private void popularDatos(ResultSet resultados){
        try{
            resultados.next();

            codigoEdit.setText(resultados.getString("codigo_confederacion"));
            nombreEdit.setText(resultados.getString("nombre_confederacion"));

            resultados.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }
}
