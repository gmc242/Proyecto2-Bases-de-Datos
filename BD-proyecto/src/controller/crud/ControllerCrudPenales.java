package controller.crud;

import javafx.fxml.FXML;

public class ControllerCrudPenales {
    private long numero_partido;

    public ControllerCrudPenales(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudPenales(){ numero_partido = 0; }

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
