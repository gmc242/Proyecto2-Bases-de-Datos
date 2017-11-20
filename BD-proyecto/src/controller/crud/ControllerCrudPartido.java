package controller.crud;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.*;

public class ControllerCrudPartido {

    @FXML private TextField partidoEdit;
    @FXML private TextField horaEdit;
    @FXML private TextField aficionadosEdit;
    @FXML private ComboBox<String> etapaBox;
    @FXML private ComboBox<String> grupoBox;
    @FXML private ComboBox<String> equipo1Box;
    @FXML private ComboBox<String> equipo2Box;
    @FXML private ComboBox<String> sedeBox;
    @FXML private DatePicker fechaDate;
    @FXML private RadioButton extrasSi;
    @FXML private RadioButton extrasNo;

    private long numero_partido;

    public ControllerCrudPartido(long numero_partido){ this.numero_partido = numero_partido; }

    public ControllerCrudPartido(){ numero_partido = 0; }

    @FXML
    public void initialize(){
        popularEquipos();
        popularEtapas();
        popularGrupos();
        popularSedes();

        if(numero_partido != 0){
            try {
                String query = "SELECT * FROM partido WHERE numero_partido = ?";
                PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
                statement.setLong(2, numero_partido);

                ResultSet resultado = statement.executeQuery();
                popularDatos(resultado);

                statement.close();
            }catch (SQLException e){
                Controller.manejarExcepcion(e);
            }
        }
    }

    @FXML
    public void buscarOnClick(){
        try{
            Short valor = Short.valueOf(partidoEdit.getText());
            if(valor < 55)
                setNumero_partido(valor);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    public void aceptarOnClick(){}

    public void setNumero_partido(long numero_partido){
        this.numero_partido = numero_partido;
        initialize();
    }

    private void popularDatos(ResultSet resultado){
        try{

            partidoEdit.setText(String.valueOf(resultado.getLong("numero_partido")));
            aficionadosEdit.setText(String.valueOf(resultado.getInt("aficionados")));

            Timestamp fecha = resultado.getTimestamp("fecha");
            fechaDate.setValue(fecha.toLocalDateTime().toLocalDate());

            String horas = fecha.toLocalDateTime().getHour() + ":" + fecha.toLocalDateTime().getMinute();
            horaEdit.setText(horas);

            etapaBox.getSelectionModel().select(resultado.getString("etapa"));
            grupoBox.getSelectionModel().select(resultado.getString("nombre_grupo"));
            equipo1Box.getSelectionModel().select(resultado.getString("equipo1"));
            equipo2Box.getSelectionModel().select(resultado.getString("equipo2"));
            sedeBox.getSelectionModel().select(resultado.getString("nombre_estadio"));

        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    private void popularEquipos(){
        try{
            String query = "SELECT codigo_fifa FROM equipo";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            ResultSet resultado = statement.executeQuery();

            while(resultado.next()) {
                String equipo = resultado.getString("codigo_fifa");
                equipo1Box.getItems().add(equipo);
                equipo2Box.getItems().add(equipo);
            }

            resultado.close();
            statement.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    private void popularGrupos(){
        try{
            String query = "SELECT nombre_grupo FROM grupo";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            ResultSet resultado = statement.executeQuery();

            while(resultado.next())
                grupoBox.getItems().add(resultado.getString("nombre_grupo"));

            resultado.close();
            statement.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    private void popularSedes(){
        try{
            String query = "SELECT nombre_estadio FROM sede";
            PreparedStatement statement = Controller.obtenerConexion().prepareStatement(query);
            ResultSet resultado = statement.executeQuery();

            while(resultado.next())
                sedeBox.getItems().add(resultado.getString("nombre_estadio"));

            resultado.close();
            statement.close();
        }catch (SQLException e){
            Controller.manejarExcepcion(e);
        }
    }

    private void popularEtapas(){
        String[] etapas = {"Grupos", "Octavos de Final", "Cuartos de Final", "Semifinal", "Partido por Tercer Lugar", "Final"};
        for(String s : etapas)
            etapaBox.getItems().add(s);
    }
}
