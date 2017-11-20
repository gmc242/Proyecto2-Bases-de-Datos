package controller.crud;

import controller.Controller;
import controller.DataModel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerCrudCuerpoArbitral {

    @FXML private TextField partido_Edit;
    @FXML private ScrollPane contenedorTabla;

    private long numero_partido;

    public ControllerCrudCuerpoArbitral(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudCuerpoArbitral(){ numero_partido = 0; }

    @FXML
    public void initialize(){
        if(numero_partido != 0){
            try {

                String query = "SELECT * FROM cuerpo_arbitral WHERE numero_partido = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(1, numero_partido);

                long[] ids = new long[5];
                ResultSet resultado = statement.executeQuery();

                for(int i = 0; i < 5; i++){
                    resultado.next();
                    ids[i] = resultado.getLong(i+1);
                }

                resultado.close();
                statement.close();

                String queryNvo = "SELECT * FROM persona p " +
                        "INNER JOIN arbitro a ON " +
                        "p.numero_pasaporte = a.numero_pasaporte " +
                        "WHERE p.numero_pasaporte = ? OR " +
                        "WHERE p.numero_pasaporte = ? OR " +
                        "WHERE p.numero_pasaporte = ? OR " +
                        "WHERE p.numero_pasaporte = ? OR " +
                        "WHERE p.numero_pasaporte = ?";

                statement = Controller.obtenerConexion().prepareStatement(queryNvo);

                for(int i = 1; i <= 5; i++){
                    statement.setLong(i, ids[i-1]);
                }

                ResultSet resultadoNvo = statement.executeQuery();
                popularTabla(resultadoNvo);

                statement.close();

            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void buscarOnClick(){
        try{
            long numero_partido = Long.valueOf(partido_Edit.getText());
            setNumero_partido(numero_partido);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void agregarOnClick(){

        // Crea un popup y obtiene el codigo
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudCuerpoArbitralAux.fxml"));
            VBox n = loader.load();

            Stage ventana = new Stage();
            ventana.setTitle("Crud cuerpo arbitral");
            ventana.setScene(new Scene(n));
            ventana.showAndWait();
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML
    public void actualizarOnClick(){
        try {

            long numero = Controller.crearPopup(Long.class, "Consulta de Cuerpos Arbitrales");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudCuerpoArbitralAux.fxml"));
            ControllerCrudCuerpoArbitralAux controller = (ControllerCrudCuerpoArbitralAux)loader.getController();
            controller.setPasaporte(numero);
            VBox n = loader.load();

            Stage ventana = new Stage();
            ventana.setTitle("Crud cuerpo arbitral");
            ventana.setScene(new Scene(n));
            ventana.showAndWait();

            initialize();

        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    public void setNumero_partido(long numero_partido){
        this.numero_partido = numero_partido;
        initialize();
    }

    private void popularTabla(ResultSet resultado){

        String[] tipos = {"Principal", "Linea1", "Linea2", "Cuarto Arbitro", "Quinto Arbitro"};
        ArrayList<ArrayList<String>> matriz = new ArrayList<>();

        SwingNode tabla = new SwingNode();

        Controller.createSwingContent(tabla, resultado);

        contenedorTabla.setContent(tabla);
    }

}
