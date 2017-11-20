package controller.consultas;

import controller.Controller;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerConsultaConfederacion {

    @FXML private TextField primaryKey;
    @FXML private VBox contenedorTablas;
    @FXML private VBox contenedorPrincipal;

    private String codigo;

    public ControllerConsultaConfederacion(){}

    @FXML public void initialize(){
        contenedorTablas.getChildren().clear();

        if(codigo != null && !codigo.equals("")){
            try{

                String query = "SELECT nombre_pais FROM confederacion c " +
                        "INNER JOIN equipo e " +
                        "ON c.codigo_confederacion = e.codigo_confederacion " +
                        "WHERE c.codigo_confederacion = ?";

                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setString(1, codigo);
                ResultSet resultados = statement.executeQuery();

                SwingNode node = new SwingNode();
                Controller.createSwingContent(node, resultados);

                contenedorTablas.getChildren().add(node);
            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML public void buscarOnClick(){
        try{

            String key = primaryKey.getText();

            if(key.equals(""))
                throw new Exception("El valor no puede estar vacio");

            setCodigo(key);
        }catch(Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void todosOnClick(){
        contenedorTablas.getChildren().clear();

        if(codigo != null && !codigo.equals("")){
            try{
                String queryConfederaciones = "SELECT codigo_confederacion FROM confederacion";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(queryConfederaciones);
                ResultSet resultados = statement.executeQuery();

                while(resultados.next()){

                    String query = "SELECT nombre_pais FROM confederacion c " +
                            "INNER JOIN equipo e " +
                            "ON c.codigo_confederacion = e.codigo_confederacion " +
                            "WHERE c.codigo_confederacion = ?";

                    PreparedStatement statementInterno = Controller.obtenerConexion().prepareStatement(query);
                    statementInterno.setString(1, resultados.getString("codigo_confederacion"));
                    ResultSet resultadosInterno = statementInterno.executeQuery();

                    SwingNode node = new SwingNode();
                    Controller.createSwingContent(node, resultadosInterno);

                    contenedorTablas.getChildren().add(node);
                }

            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }


    @FXML public void setCodigo(String codigo){
        this.codigo = codigo;
        initialize();
    }
}
