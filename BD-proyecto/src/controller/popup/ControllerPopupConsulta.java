package controller.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import controller.Controller;
import javafx.stage.Stage;

public class ControllerPopupConsulta {

    @FXML private TextField valorField;
    @FXML private Label labelTexto;

    private String resultado;
    private boolean cancelado;

    public ControllerPopupConsulta(){ cancelado = true; }

    @FXML public void initiliaze(){}

    public <T> T getResultado(Class<T> type) throws Exception{
        try{
            if(type == Integer.class){
                return type.cast(Integer.valueOf(resultado));
            }
            else if(type == Double.class){
                return type.cast(Double.valueOf(resultado));
            }
            else if(type == Long.class){
                return type.cast(Long.valueOf(resultado));
            }
            else if(type == Character.class){
                if(resultado.length() == 1)
                    return type.cast(resultado.charAt(0));
                else
                    throw new Exception("El valor por consultar debe ser exactamente un caracter");
            }
            else if(type == String.class){
                return type.cast(resultado);
            }
            else{
                return type.cast(resultado);
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @FXML public void aceptarOnClick(){

        cancelado = false;
        String temp = valorField.getText();
        if(!temp.equals("") && temp != null){
            resultado = temp;
            Stage stage = (Stage) valorField.getScene().getWindow();
            stage.close();
        }
        else{
            Controller.manejarExcepcion(new Exception("El valor por consultar no puede estar vacio"));
        }
    }

    @FXML public void cancelarOnClick(){
        cancelado = true;
        Stage stage = (Stage) valorField.getScene().getWindow();
        stage.close();
    }

    @FXML public void setTextLabel(String valor){
        labelTexto.setText(valor);
    }

    public boolean isCancelado(){
        return cancelado;
    }
}
