package controller.crud;

import javafx.fxml.FXML;

public class ControllerCrudTarjeta {
    private long numero_partido;

    public ControllerCrudTarjeta(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudTarjeta(){ numero_partido = 0; }

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
