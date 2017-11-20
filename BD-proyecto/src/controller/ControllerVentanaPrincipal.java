package controller;

import controller.crud.*;
import controller.popup.ControllerPopupConsulta;
import interfaz.MessageBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class ControllerVentanaPrincipal {

    @FXML private VBox contenedorPrincipal;

    public ControllerVentanaPrincipal(){

    }

    @FXML public void initialize(){}

    /* Administracion de la db */
    @FXML public void porDefectoOnClick(){
        Controller.iniciarConexion();
    }

    @FXML public void inicializarTablasOnClick(){
        try {
            Controller.inicializarTablas();
        }catch(Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void cargarDatosOnClick(){
        Controller.inicializarDatos();
    }

    @FXML public void borrarTablasOnClick(){ Controller.borrarTablas(null); }

    @FXML public void iniciarSesionOnClick() {
        try {
            String usuario = crearPopup(String.class, "Ingrese su usuario.", "Ingrese su usuario.");
            String password = crearPopup(String.class, "Ingrese su password.", "Ingrese su password.");

            Controller.iniciarConexion(usuario, password);
        }catch (Exception e){
            Controller.manejarExcepcion(e);
        }
    }

    // Consultas

    @FXML public void consultaConfederacionOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("../interfaz/principales/consultaConfederacion.fxml")));

        }catch(Exception e){
            System.out.println(e.getMessage());
            //Muestra mensaje
        }

    }

    @FXML public void consultaGrupoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("../interfaz/principales/consultaEquipo.fxml")));

        }catch(Exception e){
            //Muestra mensaje
        }

    }

    @FXML public void consultaPartidoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("../interfaz/principales/consultaGrupo.fxml")));

        }catch(Exception e){
            //Muestra mensaje
        }

    }

    @FXML public void consultaEquipoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("../interfaz/principales/consultaPartido.fxml")));

        }catch(Exception e){
            //Muestra mensaje
        }

    }

    @FXML public void generalOnClick(){}

    @FXML public void goleoOnClick(){}

    @FXML public void amarillasOnClick(){}

    @FXML public void rojasOnClick(){}

    // Crud-Agregar

    @FXML public void agregarConfederacionOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudConfederacion.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarEquipoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudEquipo.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarJugadorOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudJugador.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarEntrenadorOnClick(){

        limpiarContenedor();

        try{

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado("Tecnico");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarFederativoOnClick(){

        limpiarContenedor();

        try{

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado("Federativo");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarAsistenteOnClick(){

        limpiarContenedor();

        try{

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado("Asistente");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarPartidoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudPartido.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarGrupoOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudGrupo.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarSedeOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudSede.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void agregarArbitroOnClick(){

        limpiarContenedor();

        try{

            contenedorPrincipal.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/interfaz/crud/crudArbitro.fxml")));

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarDelegacionOnClick(){
        limpiarContenedor();

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudDelegacion.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarCuerpoArbitralOnClick(){
        limpiarContenedor();

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudCuerpoArbitral.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarGolOnClick(){
        limpiarContenedor();

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudGol.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarReposicionOnClick(){
        limpiarContenedor();

        try{
            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudReposicion.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarTarjetaOnClick(){
        limpiarContenedor();

        try{
            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudTarjeta.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void agregarPenalOnClick(){
        limpiarContenedor();

        try{
            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudPenales.fxml"));
            Node n = loader.load();
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    // Crud-Actualizar

    @FXML public void actualizarConfederacionOnClick(){

        limpiarContenedor();

        try{

            String codigo = crearPopup(String.class, "Consulta de Confederaciones");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudConfederacion.fxml"));
            Node n = loader.load();
            ((ControllerCrudConfederacion)(loader.getController())).setCodigo(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarEquipoOnClick(){

        limpiarContenedor();

        try{

            String codigo = crearPopup(String.class, "Consulta de Equipos");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEquipo.fxml"));
            Node n = loader.load();
            ((ControllerCrudEquipo)(loader.getController())).setCodigo(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            Controller.manejarExcepcion(e);
            //Muestra mensaje
        }

    }

    @FXML public void actualizarJugadorOnClick(){

        limpiarContenedor();

        try{

            long numero = crearPopup(Long.class, "Consulta de Jugadores");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudJugador.fxml"));
            Node n = loader.load();
            ((ControllerCrudJugador)(loader.getController())).setNumero_pasaporte(numero);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            Controller.manejarExcepcion(e);
            //Muestra mensaje
        }

    }

    @FXML public void actualizarEntrenadorOnClick(){

        limpiarContenedor();

        try{

            long numero = crearPopup(Long.class, "Consulta de Entrenadores");

            // Crea un popup y obtiene el codigo

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado(numero,"Tecnico");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarFederativoOnClick(){

        limpiarContenedor();

        try{

            long numero = crearPopup(Long.class, "Consulta de Federativos");

            // Crea un popup y obtiene el codigo

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado(numero,"Federativo");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarAsistenteOnClick(){

        limpiarContenedor();

        try{

            long numero = crearPopup(Long.class, "Consulta de Asistentes");

            // Crea un popup y obtiene el codigo

            ControllerCrudEmpleado controller = new ControllerCrudEmpleado(numero,"Asistente");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudEmpleado.fxml"));
            loader.setController(controller);
            contenedorPrincipal.getChildren().add(loader.load());

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarPartidoOnClick(){

        limpiarContenedor();

        try{

            int numero = crearPopup(Integer.class, "Consulta de Partidos");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudPartido.fxml"));
            Node n = loader.load();
            ((ControllerCrudPartido)(loader.getController())).setNumero_partido(numero);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarGrupoOnClick(){

        limpiarContenedor();

        try{

            Character codigo = crearPopup(Character.class, "Consulta de Grupos");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudGrupo.fxml"));
            Node n = loader.load();
            ((ControllerCrudGrupo) (loader.getController())).setCodigo(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarSedeOnClick(){

        limpiarContenedor();

        try{

            String codigo = crearPopup(String.class, "Consulta de Sedes");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudSede.fxml"));
            Node n = loader.load();
            ((ControllerCrudSede)(loader.getController())).setCodigo(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void actualizarArbitroOnClick(){

        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Arbitros");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudArbitro.fxml"));
            Node n = loader.load();
            ((ControllerCrudArbitro)(loader.getController())).setNumero_pasaporte(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void actualizarDelegacionOnClick(){
        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Delegacion");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudDelegacion.fxml"));
            Node n = loader.load();
            ((ControllerCrudDelegacion)(loader.getController())).setNumero_partido(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void actualizarCuerpoArbitralOnClick(){
        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Cuerpo Arbitral");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudCuerpoArbitral.fxml"));
            Node n = loader.load();
            ((ControllerCrudCuerpoArbitral)(loader.getController())).setNumero_partido(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void actualizarGolOnClick(){
        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Goles");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudGol.fxml"));
            Node n = loader.load();
            ((ControllerCrudGol)(loader.getController())).setNumero_partido(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void actualizarReposicionOnClick(){
        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Reposicion");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudReposicion.fxml"));
            Node n = loader.load();
            ((ControllerCrudReposicion)(loader.getController())).setNumero_partido(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void actualizarPenalOnClick(){
        limpiarContenedor();

        try{

            long codigo = crearPopup(Long.class, "Consulta de Penales");

            // Crea un popup y obtiene el codigo

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/crud/crudPenales.fxml"));
            Node n = loader.load();
            ((ControllerCrudPenales)(loader.getController())).setNumero_partido(codigo);
            contenedorPrincipal.getChildren().add(n);

        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    // Crud - borrar

    @FXML public void borrarArbitroOnClick(){

        try{

            // Crea popup y llama a controller para borrar jugador
            long numero = crearPopup(Long.class, "Borrar Arbitro");
            Controller.borrarArbitro(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarConfederacionOnClick(){

        try{

            // Crea popup y llama a controller para borrar confederacion
            String codigo = crearPopup(String.class, "Borrar confederacion");
            Controller.borrarConfederacion(codigo);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarEquipoOnClick(){

        try{

            // Crea popup y llama a controller para borrar equipo
            String codigo = crearPopup(String.class, "Borrar Equipo");
            Controller.borrarEquipo(codigo);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarJugadorOnClick(){

        try{

            // Crea popup y llama a controller para borrar jugador
            long numero = crearPopup(Long.class, "Borrar Jugador");
            Controller.borrarJugador(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarEntrenadorOnClick(){

        try{

            // Crea popup y llama a controller para borrar entrenador
            long numero = crearPopup(Long.class, "Borrar Entrenador");
            Controller.borrarEntrenador(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarFederativoOnClick(){

        try{

            // Crea popup y llama a controller para borrar federativo
            long numero = crearPopup(Long.class, "Borrar federativo");
            Controller.borrarFederativo(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarAsistenteOnClick(){

        try{

            // Crea popup y llama a controller para borrar asistente
            long numero = crearPopup(Long.class, "Borrar Asistente");
            Controller.borrarAsistente(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarPartidoOnClick(){

        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar partido");
            Controller.borrarPartido(numero);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarGrupoOnClick(){

        try{

            // Crea popup y llama a controller para borrar grupo
            String nombre = crearPopup(String.class, "Borrar Grupo");
            Controller.borrarGrupo(nombre);

            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarSedeOnClick(){

        try{

            // Crea popup y llama a controller para borrar sede
            String nombre = crearPopup(String.class, "Borrar sede");
            Controller.borrarSede(nombre);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }

    }

    @FXML public void borrarDelegacionOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar delegacion");
            Controller.borrarDelegacion(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarCuerpoArbitralOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar Cuerpo Arbitral");
            Controller.borrarCuerpoArbitral(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarGolOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar goles");
            Controller.borrarGoles(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarPenalesOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar Penales");
            Controller.borrarPenales(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarReposicionOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar reposicion");
            Controller.borrarReposicion(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    @FXML public void borrarTarjetaOnClick(){
        try{

            // Crea popup y llama a controller para borrar partido
            long numero = crearPopup(Long.class, "Borrar tarjeta");
            Controller.borrarTarjetas(numero);
            MessageBox alert = new MessageBox(Alert.AlertType.INFORMATION, "Se ha borrado con exito");
        }catch(Exception e){
            //Muestra mensaje
            Controller.manejarExcepcion(e);
        }
    }

    private void limpiarContenedor(){

        contenedorPrincipal.getChildren().clear();

    }

    private <T> T crearPopup(Class<T> tipoDeResultado, String mensajeTitulo)throws Exception{
        try{

            FXMLLoader loaderPopup = new FXMLLoader(getClass().getResource("/interfaz/popup/popupPrimaryKey.fxml"));
            VBox contenedorPopup = loaderPopup.load();
            ControllerPopupConsulta controller = (ControllerPopupConsulta)loaderPopup.getController();
            Stage ventana = new Stage();
            ventana.setTitle(mensajeTitulo);
            ventana.setScene(new Scene(contenedorPopup));
            ventana.showAndWait();

            if(!controller.isCancelado()){
                T resultado = controller.getResultado(tipoDeResultado);
                return resultado;
            }
            else{
                throw new Exception("La consultó se canceló.");
            }
        }catch(Exception e){
            throw e;
        }
    }

    private <T> T crearPopup(Class<T> tipoDeResultado, String mensajeTitulo, String labelText)throws Exception{
        try{

            FXMLLoader loaderPopup = new FXMLLoader(getClass().getResource("/interfaz/popup/popupPrimaryKey.fxml"));
            VBox contenedorPopup = loaderPopup.load();
            ControllerPopupConsulta controller = (ControllerPopupConsulta)loaderPopup.getController();
            controller.setTextLabel(labelText);
            Stage ventana = new Stage();
            ventana.setTitle(mensajeTitulo);
            ventana.setScene(new Scene(contenedorPopup));
            ventana.showAndWait();

            if(!controller.isCancelado()){
                T resultado = controller.getResultado(tipoDeResultado);
                return resultado;
            }
            else{
                throw new Exception("La consultó se canceló.");
            }
        }catch(Exception e){
            throw e;
        }
    }
}
