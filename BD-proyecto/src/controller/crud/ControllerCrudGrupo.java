package controller.crud;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ControllerCrudGrupo {

    @FXML private TextField nombreEdit;

    private Character grupo;

    public ControllerCrudGrupo(Character grupo){ this.grupo = grupo; }

    public ControllerCrudGrupo(){ grupo = null; }

    @FXML
    public void initialize(){
        if(grupo != null && !grupo.equals("")){

        }
    }

    public void setCodigo(Character grupo){
        this.grupo = grupo;
        initialize();
    }
}
