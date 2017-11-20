package controller.crud;

import controller.Controller;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerCrudDelegacion {

    @FXML private TextField partido_Edit;
    @FXML private VBox contenedorTablas;

    private long numero_partido;

    public ControllerCrudDelegacion(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudDelegacion(){ numero_partido = 0; }

    @FXML
    public void initialize(){
        if(numero_partido != 0){
            try {

                String titulares = "SELECT * FROM titulares WHERE numero_partido = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(titulares);
                statement.setLong(1, Long.valueOf(partido_Edit.getText()));
                ResultSet resultado = statement.executeQuery();

                SwingNode nodoTitulares = new SwingNode();
                Controller.createSwingContent(nodoTitulares, resultado);
                resultado.close();

                String suplentes = "SELECT * FROM suplentes WHERE numero_partido = ?";
                statement = Controller.obtenerConexion().prepareStatement(suplentes);
                statement.setLong(1, Long.valueOf(partido_Edit.getText()));
                resultado = statement.executeQuery();

                SwingNode nodoSuplentes = new SwingNode();
                Controller.createSwingContent(nodoSuplentes, resultado);
                resultado.close();

                String asistentes = "SELECT * FROM asistentes_en_partidos WHERE numero_partido = ?";
                statement = Controller.obtenerConexion().prepareStatement(asistentes);
                statement.setLong(1, Long.valueOf(partido_Edit.getText()));
                resultado = statement.executeQuery();

                SwingNode nodoAsistentes = new SwingNode();
                Controller.createSwingContent(nodoAsistentes, resultado);
                resultado.close();

                String medicos = "SELECT * FROM medicos_en_partidos WHERE numero_partido = ?";
                statement = Controller.obtenerConexion().prepareStatement(medicos);
                statement.setLong(1, Long.valueOf(partido_Edit.getText()));
                resultado = statement.executeQuery();

                SwingNode nodoMedicos = new SwingNode();
                Controller.createSwingContent(nodoMedicos, resultado);
                resultado.close();

                String delegados = "SELECT * FROM delegados_en_partidos WHERE numero_partido = ?";
                statement = Controller.obtenerConexion().prepareStatement(delegados);
                statement.setLong(1, Long.valueOf(partido_Edit.getText()));
                resultado = statement.executeQuery();

                SwingNode nodoDelegados = new SwingNode();
                Controller.createSwingContent(nodoDelegados, resultado);
                resultado.close();

                contenedorTablas.getChildren().addAll(nodoTitulares, nodoSuplentes, nodoAsistentes, nodoMedicos, nodoDelegados);

            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    public void setNumero_partido(long numero_partido){
        this.numero_partido = numero_partido;
        initialize();
    }

    @FXML
    public void agregarTitularOnClick(){
        agregarPopupAux("titulares", "Agregar titular", "jugador_titular");
    }

    @FXML
    public void agregarSuplenteOnClick(){ agregarPopupAux("suplentes", "Agregar suplente", "jugador_suplente"); }

    @FXML
    public void agregarAsistenteOnClick(){ agregarPopupAux("asistentes_en_partidos", "Agregar asistente", "asistente"); }

    @FXML
    public void agregarMedicoOnClick(){ agregarPopupAux("medico_en_partidos", "Agregar medico", "medico"); }

    @FXML
    public void agregarDelegadoOnClick(){{ agregarPopupAux("delegados_en_partidos", "Agregar delegados", "delegado"); }}

    @FXML
    public void borrarTitularOnClick(){ borrarAux("titulares", "jugador_titular"); }

    @FXML
    public void borrarSuplenteOnClick(){ borrarAux("suplentes", "jugador_suplente"); }

    @FXML
    public void borrarAsistenteOnClick(){ borrarAux("asistentes_en_partidos", "asistente"); }

    @FXML
    public void borrarMedicoOnClick(){ borrarAux("medicos_en_partido", "medico"); }

    @FXML
    public void borrarDelegadoOnClick(){ borrarAux("delegados_en_partido", "delegado"); }

    private void agregarPopupAux(String tabla, String titulo, String atributoPersona){
        try {

            if(numero_partido == 0){
                numero_partido = Controller.crearPopup(Long.class, "Numero de partido al cual agregar dato");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudDelegacionAux.fxml"));
            ControllerCrudDelegacionAux controller = (ControllerCrudDelegacionAux) loader.getController();
            controller.setDatos(tabla, numero_partido, atributoPersona);
            VBox n = loader.load();

            Stage ventana = new Stage();
            ventana.setTitle(titulo);
            ventana.setScene(new Scene(n));
            ventana.showAndWait();

            initialize();
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    private void borrarAux(String tabla, String atributoPersona){
        try {
            if (numero_partido == 0)
                numero_partido = Controller.crearPopup(Long.class, "Numero de partido");

            long pasaporte = Controller.crearPopup(Long.class,  "Borrar persona de delegacion");

            String query = "DELETE FROM ? WHERE numero_partido = ? AND ? = ?";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            statement.setString(1, tabla);
            statement.setLong(2, numero_partido);
            statement.setString(3, atributoPersona);
            statement.setLong(4, pasaporte);
            statement.executeUpdate();

            statement.close();
            initialize();

        }
        catch (SQLException e){
            Controller.manejarExcepcion(e);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }
}
