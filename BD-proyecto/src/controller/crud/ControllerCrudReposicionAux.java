package controller.crud;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ControllerCrudReposicionAux {

    @FXML
    private TextField partidoEdit;
    @FXML private TextField principalEdit;
    @FXML private TextField linea1Edit;
    @FXML private TextField linea2Edit;
    @FXML private TextField cuartoEdit;
    @FXML private TextField quintoEdit;

    private long pasaporte;

    @FXML
    public void initialize(){}

    @FXML
    public void aceptarOnClick(){

    }

    @FXML
    public void buscarOnClick(){}

    @FXML
    public void cancelarOnClick(){

    }

    public void setPasaporte(long pasaporte){
        this.pasaporte = pasaporte;
        initialize();
    }

}
