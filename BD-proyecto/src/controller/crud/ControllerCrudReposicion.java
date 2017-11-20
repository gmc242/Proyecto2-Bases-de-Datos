package controller.crud;

import javafx.fxml.FXML;

public class ControllerCrudReposicion {
    private long numero_partido;

    public ControllerCrudReposicion(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudReposicion(){ numero_partido = 0; }

    @FXML
    public void initialize(){
        if(numero_partido != 0){

        }
    }

    public void setNumero_partido(long numero_partido){
        this.numero_partido = numero_partido;
        initialize();
    }
}
