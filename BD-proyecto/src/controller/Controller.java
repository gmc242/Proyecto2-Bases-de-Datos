package controller;

// Clase encargada de comunicacion entre Controllers de cada vista y la base de datos

import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import controller.popup.ControllerPopupConsulta;
import interfaz.MessageBox;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;

public class Controller {

    private static Connection conexion;

    // Metodo de inicalizacion de la conexion, se debe realizar cada vez que se ingrese al programa

    public static void iniciarConexion(){
        iniciarConexion("dbaMundial", "dba123456789");
    }

    public static void iniciarConexion(String usuario, String password){

        try{

            if(conexion != null && !conexion.isClosed()){
                conexion.close();
                conexion = null;
            }

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection("jdbc:oracle:thin:" + usuario + "/" + password + "@localhost");

            MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se ha creado con exito la conexion");

            if(!conexion.isValid(1000)){
                throw new Exception("No se ha podido establecer la conexion a la base de datos");
            }
        }
        catch (ClassNotFoundException e){
            // No se encontró la clase
            manejarExcepcion(new Exception(
                    "No se ha podido establecer una conexion debido a que no se pudo leer la librería"));
        }
        catch (SQLException e){
            // No se pudo establecer la conexion
            manejarExcepcion(e);
        }
        catch(Exception e){
            manejarExcepcion(e);
        }
    }

    /* Metodo de inicializacion de tablas y datos */

    public static void inicializarTablas() throws SQLException{

        ArrayList<String> queries = new ArrayList<>();

        queries.add("CREATE TABLE confederacion(" +
                "codigo_confederacion VARCHAR(10) PRIMARY KEY, " +
                "nombre_confederacion VARCHAR(70) NOT NULL)");

        queries.add("CREATE TABLE grupo(" +
                "nombre_grupo VARCHAR(1) PRIMARY KEY)");

        queries.add("CREATE TABLE persona(" +
                "numero_pasaporte NUMBER(20) PRIMARY KEY, " +
                "nombreP VARCHAR(20) NOT NULL, " +
                "apellido1 VARCHAR(20) NOT NULL, " +
                "apellido2 VARCHAR(20) NOT NULL, " +
                "fecha_nacimiento DATE)");

        queries.add("CREATE TABLE equipo(" +
                "codigo_fifa VARCHAR(3) PRIMARY KEY, " +
                "nombre_pais VARCHAR(40) UNIQUE, " +
                "codigo_confederacion VARCHAR(10) " +
                "REFERENCES confederacion (codigo_confederacion)," +
                "nombre_grupo VARCHAR(1) " +
                "REFERENCES grupo (nombre_grupo))");

        queries.add("CREATE TABLE arbitro(" +
                "numero_pasaporte NUMBER(20) PRIMARY KEY " +
                "REFERENCES persona (numero_pasaporte), " +
                "inicio_mundiales DATE NOT NULL, " +
                "nacionalidad VARCHAR(20) NOT NULL)");

        queries.add("CREATE TABLE jugador(" +
                "numero_pasaporte NUMBER(20) PRIMARY KEY " +
                "REFERENCES persona (numero_pasaporte), " +
                "numero_camiseta NUMBER(2), " +
                "posicion VARCHAR(15), " +
                "codigo_fifa_equipo VARCHAR(3) " +
                "REFERENCES equipo (codigo_fifa), " +
                "CONSTRAINT posiciones " +
                "CHECK (posicion in ('Portero', 'Defensa', 'Mediocampista', 'Delantero')))");

        queries.add("CREATE TABLE empleado(" +
                "numero_pasaporte NUMBER(20) PRIMARY KEY " +
                "REFERENCES persona (numero_pasaporte), " +
                "nacionalidad VARCHAR(20) NOT NULL, " +
                "fecha_inicio_puesto DATE NOT NULL, " +
                "tipo_empleado VARCHAR(15), " +
                "tipo_especifico_empleado VARCHAR(20), " +
                "codigo_fifa_equipo VARCHAR(3) " +
                "REFERENCES equipo (codigo_fifa), " +
                "CONSTRAINT validar_tipo " +
                "CHECK (tipo_empleado in ('Tecnico', 'Asistente', 'Federativo')))");

        queries.add("CREATE TABLE sede(" +
                "nombre_estadio VARCHAR(40) PRIMARY KEY, " +
                "nombre_sede VARCHAR(40) NOT NULL, " +
                "capacidad NUMBER(7) NOT NULL)");

        queries.add("CREATE TABLE partido(" +
                "numero_partido NUMBER(2) PRIMARY KEY, " +
                "etapa VARCHAR(15) NOT NULL, " +
                "fecha_hora DATE NOT NULL, " +
                "aficionados NUMBER(7) NOT NULL, " +
                "extras NUMBER(1) NOT NULL, " +
                "nombre_grupo VARCHAR(1) " +
                "REFERENCES grupo (nombre_grupo), " +
                "equipo1 VARCHAR(3) " +
                "REFERENCES equipo (codigo_fifa), " +
                "equipo2 VARCHAR(3) " +
                "REFERENCES equipo (codigo_fifa), " +
                "nombre_estadio VARCHAR(20) " +
                "REFERENCES sede (nombre_estadio), " +
                "CONSTRAINT numero_partido_valido " +
                "CHECK (numero_partido < 55), " +
                "CONSTRAINT boolean_partido " +
                "CHECK (extras < 2))");

        queries.add("CREATE TABLE tarjeta(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "tipo VARCHAR(8), " +
                "jugador_tarjeta NUMBER(20) " +
                "REFERENCES jugador (numero_pasaporte), " +
                "minuto NUMBER(3), " +
                "PRIMARY KEY(numero_partido, tipo, jugador_tarjeta, minuto), " +
                "CONSTRAINT tipo_tarjeta_valida " +
                "CHECK (tipo IN ('Amarilla', 'Roja')), " +
                "CONSTRAINT minuto_valido_tarjeta " +
                "CHECK (minuto < 121))");

        queries.add("CREATE TABLE gol(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "equipo VARCHAR(8) " +
                "REFERENCES equipo (codigo_fifa), " +
                "jugador_gol NUMBER(20) " +
                "REFERENCES jugador (numero_pasaporte), " +
                "minuto NUMBER(3), " +
                "PRIMARY KEY(numero_partido, equipo, jugador_gol, minuto), " +
                "CONSTRAINT minuto_valido_gol " +
                "CHECK (minuto < 121))");

        queries.add("CREATE TABLE reposicion(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "tiempo NUMBER(1), " +
                "minutos NUMBER(2) NOT NULL, " +
                "PRIMARY KEY(numero_partido, tiempo), " +
                "CONSTRAINT cantidad_tiempos " +
                "CHECK (tiempo < 5))");

        queries.add("CREATE TABLE penales(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "numero_penal NUMBER(2), " +
                "jugador_penal NUMBER(20) " +
                "REFERENCES jugador (numero_pasaporte), " +
                "anotado NUMBER(1) NOT NULL, " +
                "PRIMARY KEY(numero_partido, numero_penal), " +
                "CONSTRAINT boolean_penales " +
                "CHECK (anotado < 2))");

        queries.add("CREATE TABLE cuerpo_arbitral(" +
                "numero_partido NUMBER(2) PRIMARY KEY " +
                "REFERENCES partido (numero_partido), " +
                "arbitro_principal NUMBER(20) " +
                "REFERENCES arbitro (numero_pasaporte), " +
                "linea1 NUMBER(20) " +
                "REFERENCES arbitro (numero_pasaporte), " +
                "linea2 NUMBER(20) " +
                "REFERENCES arbitro (numero_pasaporte), " +
                "arbitro4 NUMBER(20) " +
                "REFERENCES arbitro (numero_pasaporte), " +
                "arbitro5 NUMBER(20) " +
                "REFERENCES arbitro (numero_pasaporte))");

        queries.add("CREATE TABLE titulares(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "jugador_titular NUMBER(20) " +
                "REFERENCES jugador (numero_pasaporte), " +
                "PRIMARY KEY(numero_partido, jugador_titular))");

        queries.add("CREATE TABLE suplentes(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "jugador_suplente NUMBER(20) " +
                "REFERENCES jugador (numero_pasaporte)," +
                "PRIMARY KEY (numero_partido, jugador_suplente))");

        queries.add("CREATE TABLE asistentes_en_partidos(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "asistente NUMBER(20) " +
                "REFERENCES empleado (numero_pasaporte), " +
                "PRIMARY KEY(numero_partido, asistente))");

        queries.add("CREATE TABLE medicos_en_partidos(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "medico NUMBER(20) " +
                "REFERENCES empleado (numero_pasaporte), " +
                "PRIMARY KEY(numero_partido, medico))");

        queries.add("CREATE TABLE delegados_en_partidos(" +
                "numero_partido NUMBER(2) " +
                "REFERENCES partido (numero_partido), " +
                "delegado NUMBER(20) " +
                "REFERENCES empleado (numero_pasaporte), " +
                "PRIMARY KEY(numero_partido, delegado))");

        if(conexion != null){
            Statement statement = null;
            try{
                conexion.setAutoCommit(false);
                statement = conexion.createStatement();

                for(String query : queries){
                    statement.execute(query);
                }

                conexion.commit();
                conexion.setAutoCommit(true);
                MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han creado con exito todas las tablas");
            }
            catch(SQLException e){
                manejarExcepcion(e);
                try {
                    conexion.rollback();
                }catch(SQLException e2){
                    manejarExcepcion(e2);
                }
            }
            finally {
                if(statement != null)
                    statement.close();
            }
        }
    }

    public static void inicializarDatos(){

        try{
            if(conexion != null){
                conexion.setAutoCommit(false);

                // Inicializa datos de confederaciones
                inicializarDatosConfederaciones();

                // Inicializa datos de grupos
                HashMap<Character, Integer> grupos = inicializarDatosGrupos();

                // Inicializa datos de los equipos
                HashMap<String, String> paises = inicializarDatosEquipos(grupos);

                // Inicializa datos de jugadores, tecnicos y asistentes
                int pasaporte = 1;
                pasaporte = inicializarDatosDePersonas(new ArrayList<>(paises.keySet()), pasaporte);

                // Inicializa datos de las sedes
                inicializarDatosSedes();

                conexion.commit();
                conexion.setAutoCommit(true);
                MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han inicializado con exito todos los datos");

            }
        }catch (SQLException e){

            manejarExcepcion(e);
            e.printStackTrace();

            try{
                conexion.rollback();
            }catch (SQLException e2){
                manejarExcepcion(e2);
            }

        }catch (Exception e){

            manejarExcepcion(e);
            e.printStackTrace();

            try{
                conexion.rollback();
            }catch (SQLException e2){
                manejarExcepcion(e2);
            }
        }

    }

    private static HashMap<Character, Integer> inicializarDatosGrupos() throws Exception{

        HashMap<Character, Integer> grupos = new HashMap<>();

        grupos.put('A', 4);
        grupos.put('B', 4);
        grupos.put('C', 4);
        grupos.put('D', 4);
        grupos.put('E', 4);
        grupos.put('F', 4);
        grupos.put('G', 4);
        grupos.put('H', 4);

        PreparedStatement statement = null;

        try {

            for(Character c : grupos.keySet()) {

                String query = "INSERT INTO grupo(nombre_grupo)" +
                        "VALUES(?)";

                statement = conexion.prepareStatement(query);
                statement.setString(1, String.valueOf(c));

                int resultado = statement.executeUpdate();

                if (resultado == 0)
                    throw new Exception("El grupo " + c + " no se ha podido insertar en la base de datos.");

                statement.close();
            }
        }
        catch(SQLException e){
            throw e;
        }catch(Exception e){
            throw e;
        }finally {
            if(statement != null)
                statement.close();
        }

        MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han inicializado con exito todos los grupos");

        return grupos;
    }

    private static HashMap<String, String> inicializarDatosEquipos(HashMap<Character, Integer> grupos) throws Exception{

        HashMap<String, String> paises = new HashMap<>();
        HashMap<String, String> equiposUefa = new HashMap<>();
        HashMap<String, String> conmebol = new HashMap<>();
        HashMap<String, String> concacaf = new HashMap<>();
        HashMap<String, String> asia = new HashMap<>();
        HashMap<String, String> caf = new HashMap<>();

        equiposUefa.put("GER", "Alemania");
        equiposUefa.put("BEL", "Belgica");
        equiposUefa.put("SUI", "Suiza");
        equiposUefa.put("POL", "Polonia");
        equiposUefa.put("FRA", "Francia");
        equiposUefa.put("ESP", "Espana");
        equiposUefa.put("POR", "Portugal");
        equiposUefa.put("CRO", "Croacia");
        equiposUefa.put("ENG", "Inglaterra");
        equiposUefa.put("ISL", "Islandia");
        equiposUefa.put("SWE", "Suecia");
        equiposUefa.put("DEN", "Dinamarca");
        equiposUefa.put("RUS", "Russia");
        equiposUefa.put("SRB", "Serbia");

        conmebol.put("BRA", "Brasil");
        conmebol.put("URU", "Uruguay");
        conmebol.put("ARG", "Argentina");
        conmebol.put("COL", "Colombia");
        conmebol.put("PER", "Peru");

        concacaf.put("MEX", "Mexico");
        concacaf.put("CRC", "Costa Rica");
        concacaf.put("PAN", "Panama");

        asia.put("IRN", "Iran");
        asia.put("JPN", "Japon");
        asia.put("AUS", "Australia");
        asia.put("KOR", "Corea del Sur");
        asia.put("KSA", "Arabia Saudi");

        caf.put("NGA", "Nigeria");
        caf.put("EGY", "Egipto");
        caf.put("MAR", "Marruecos");
        caf.put("TUN", "Tunez");
        caf.put("SEN", "Senegal");

        try{

            grupos = inicializarEquiposDeConfederacion(grupos, equiposUefa, "UEFA");
            grupos = inicializarEquiposDeConfederacion(grupos, concacaf, "CONCACAF");
            grupos = inicializarEquiposDeConfederacion(grupos, conmebol, "CONMEBOL");
            grupos = inicializarEquiposDeConfederacion(grupos, caf, "CAF");
            inicializarEquiposDeConfederacion(grupos, asia, "AFC");

            // Retorna un hashmap con todos los paises
            paises.putAll(equiposUefa);
            paises.putAll(concacaf);
            paises.putAll(conmebol);
            paises.putAll(asia);
            paises.putAll(caf);

            MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han inicializado con exito todos los equipos");

            return paises;
        }catch(Exception e){
            throw e;
        }
    }

    private static HashMap<Character, Integer> inicializarEquiposDeConfederacion(
            HashMap<Character, Integer> grupos, HashMap<String, String> equipos, String codigoConfederacion) throws Exception{

        PreparedStatement statement = null;

        for(String codigo : equipos.keySet()){

            try{

                String nombrePais = equipos.get(codigo);

                // Genera un grupo de manera aleatoria
                int valor = ThreadLocalRandom.current().nextInt(8);
                char c = (char)(65 + valor);

                while(grupos.get(c) <= 0) {
                    valor = ThreadLocalRandom.current().nextInt(8);
                    c = (char) (65 + valor);
                }

                grupos.replace(c, grupos.get(c) - 1);

                String query = "INSERT INTO equipo" +
                        "(codigo_fifa, nombre_pais, codigo_confederacion, nombre_grupo) " +
                        "VALUES (?, ?, ?, ?)";

                if(!conexion.isValid(500))
                    Controller.iniciarConexion();

                statement = conexion.prepareStatement(query);
                statement.setString(1, codigo);
                statement.setString(2, nombrePais);
                statement.setString(3, codigoConfederacion);
                statement.setString(4, String.valueOf(c));

                int resultado = statement.executeUpdate();
                if(resultado == 0)
                    throw new Exception("No se pudo insertar el equipo " + codigo);

                statement.close();

            }catch(SQLException e){
                throw e;
            }catch(Exception e){
                throw e;
            }finally {
                if(statement != null)
                    statement.close();
            }
        }

        return grupos;
    }

    private static void inicializarDatosConfederaciones() throws Exception{

        ArrayList<String> queries = new ArrayList<>();

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('CONCACAF', 'Confederacion de Norteamerica Centroamerica y el Caribe de Futbol')");

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('OFC', 'Confederacion de Futbol de Oceania')");

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('AFC', 'Confederacion Asiatica de Futbol')");

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('CAF', 'Confederacion Africana de Futbol')");

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('CONMEBOL', 'Confederacion Sudamericana de Futbol')");

        queries.add("INSERT INTO confederacion" +
                "(codigo_confederacion, nombre_confederacion) " +
                "VALUES" +
                "('UEFA', 'Unión de Asociaciones Europeas de Futbol')");

        Statement statement = null;

        try{

            statement = conexion.createStatement();
            for(String query : queries){

                while(!conexion.isValid(50));

                int resultado = statement.executeUpdate(query);
                if(resultado == 0)
                    throw new Exception("La insercion de una confederacion ha fallado");
            }
        }catch(SQLException e){
            throw e;
        }catch(Exception e){
            throw e;
        }finally {
            if(statement != null)
                statement.close();
        }

        MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han inicializado con exito todas las confederaciones");
    }

    private static int inicializarDatosDePersonas(ArrayList<String> equipos, int pasaporte) throws Exception{

        // Todos los cambios en este metodo son en forma de transaccion
        // Para eso se le dice a la conexion que no debe hacer auto commit
        try{
            if(conexion.getAutoCommit())
                conexion.setAutoCommit(false);

            for(String codigoEquipo : equipos) {
                pasaporte = inicializarDatosPersonasEnEquipo(codigoEquipo, pasaporte);
            }

            MessageBox alerta = new MessageBox(Alert.AlertType.INFORMATION, "Se han inicializado con exito todas las personas");

        }catch(SQLException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            if(!conexion.getAutoCommit())
                conexion.setAutoCommit(true);
        }

        return pasaporte;
    }

    private static int inicializarDatosPersonasEnEquipo(String codigo, int pasaporte) throws Exception{
        try{
            pasaporte = inicializarDatosJugadores(codigo, pasaporte);
            pasaporte = inicializarDatosAsistentes(codigo, pasaporte);
            pasaporte = inicializarDatosFederativos(codigo, pasaporte);
            return inicializarDatosEntrenador(codigo, pasaporte);
        }catch (Exception e){
            throw e;
        }
    }

    private static int inicializarDatosJugadores(String codigo, int pasaporte) throws Exception{

        PreparedStatement statement = null;
        PreparedStatement statementJugador = null;

        // Crea un generador de valores aleatorios
        Random generado = new Random();

        // Inicializa una lista de camisetas para que no se repitan dentro de los jugadores
        ArrayList<Integer> camisetas = new ArrayList<>();

        for (int j = 1; j <= 99; j++) {
            camisetas.add(j);
        }

        // Declara un diccionario de puestos y su frecuencia en los jugadores creados
        // Tiene como fin lograr una distribucion mas o menos real de los jugadores creados en un equipo
        HashMap<String, Integer> jugadoresPorPuesto = new HashMap<>();

        jugadoresPorPuesto.put("Portero", 0);
        jugadoresPorPuesto.put("Defensa", 0);
        jugadoresPorPuesto.put("Mediocampista", 0);
        jugadoresPorPuesto.put("Delantero", 0);

        try{

            for (int i = 1; i <= 23; i++) {

                // Obtiene un numero de camiseta y lo quita de la lista
                Integer camiseta = camisetas.get(generado.nextInt(camisetas.size()));
                camisetas.remove(Integer.valueOf(camiseta));

                // Crea una fecha de nacimento que tenga sentido dentro del sistema
                String fecha_nacimiento = generarFechaDeNacimiento();

                String posicion = generarPuestoParaJugador(jugadoresPorPuesto);
                jugadoresPorPuesto.replace(posicion, jugadoresPorPuesto.get(posicion) + 1);

                String queryPersona = "INSERT INTO persona" +
                        "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento) " +
                        "VALUES(?, ?, ?, ?, ?)";

                String queryJugador = "INSERT INTO jugador" +
                        "(numero_pasaporte, numero_camiseta, posicion, codigo_fifa_equipo)" +
                        "VALUES(?, ?, ?, ?)";

                statement = conexion.prepareStatement(queryPersona);
                statement.setLong(1, pasaporte);
                statement.setString(2, "nombreJug" + codigo + i);
                statement.setString(3, "apellido1Jug" + codigo + i);
                statement.setString(4, "apellido2Jug" + codigo + i);
                statement.setDate(5, Date.valueOf(fecha_nacimiento));

                statementJugador = conexion.prepareStatement(queryJugador);
                statementJugador.setLong(1, pasaporte);
                statementJugador.setInt(2, camiseta);
                statementJugador.setString(3, posicion);
                statementJugador.setString(4, codigo);

                // Genera una transaccion donde se deben actualizar primero la tabla persona y luego la tabla jugador
                // Ambas deben ser exitosas y sino se hacer un rollback

                if(!conexion.isValid(500))
                    Controller.iniciarConexion();

                int resultadoPersona = statement.executeUpdate();

                if (resultadoPersona == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un jugador en el equipo: " + codigo);
                }

                int resultadoJugador = statementJugador.executeUpdate();

                if (resultadoJugador == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un jugador en el equipo: " + codigo);
                }

                statement.close();
                statementJugador.close();
                pasaporte++;
            }
        }catch (SQLException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
        finally {
            if(statement != null)
                statement.close();
            if(statementJugador != null)
                statementJugador.close();
        }
        return pasaporte;
    }

    private static int inicializarDatosAsistentes(String codigo, int pasaporte) throws Exception{

        Random generado = new Random();
        PreparedStatement statement = null;
        PreparedStatement statementAsistente = null;

        //  Inicializa puestos para asistentes

        int cantidadAsistentes = 7 + generado.nextInt(9);
        int pivote = cantidadAsistentes / 7;
        int cont = 0;
        HashMap<String, Integer> tipos = new HashMap<>();

        tipos.put("Tecnico", 0);
        tipos.put("Preparador Fisico", 0);
        tipos.put("Medico", 0);
        tipos.put("Psicologo", 0);
        tipos.put("Nutricionista", 0);
        tipos.put("Administrativo", 0);
        tipos.put("Delegado", 0);

        try{

            for (int i = 0; i < cantidadAsistentes; i++) {

                // Crea una fecha de nacimento que tenga sentido dentro del sistema
                String fecha_nacimiento = generarFechaDeNacimiento();
                String fecha_inicio = generarFechaDeInicio();

                String puesto = "";

                if (cont < pivote * 7)
                    puesto = generarTipoAsistente(tipos, pivote);
                else
                    puesto = generarTipoAsistenteOffset(tipos.keySet().toArray(new String[tipos.keySet().size()]));

                tipos.replace(puesto, tipos.get(puesto) + 1);

                String queryPersona = "INSERT INTO persona " +
                        "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento)" +
                        "VALUES(?, ?, ?, ?, ?)";

                String queryEmpleado = "INSERT INTO empleado" +
                        "(numero_pasaporte, nacionalidad, fecha_inicio_puesto, tipo_empleado, tipo_especifico_empleado, codigo_fifa_equipo)" +
                        "VALUES(?, ?, ?, ?, ?, ?)";

                statement = conexion.prepareStatement(queryPersona);
                statement.setLong(1, pasaporte);
                statement.setString(2, "nombreAsi" + codigo + i);
                statement.setString(3, "apellido1Asi" + codigo + i);
                statement.setString(4, "apellido2Asi" + codigo + i);
                statement.setDate(5, Date.valueOf(fecha_nacimiento));

                statementAsistente = conexion.prepareStatement(queryEmpleado);
                statementAsistente.setLong(1, pasaporte);
                statementAsistente.setString(2, "Prueba");
                statementAsistente.setDate(3, Date.valueOf(fecha_inicio));
                statementAsistente.setString(4, "Asistente");
                statementAsistente.setString(5, puesto);
                statementAsistente.setString(6, codigo);

                // Genera una transaccion donde se deben actualizar primero la tabla persona y luego la tabla jugador
                // Ambas deben ser exitosas y sino se hacer un rollback

                if(!conexion.isValid(500))
                    Controller.iniciarConexion();

                int resultadoPersona = statement.executeUpdate();

                if (resultadoPersona == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un asistente en el equipo: " + codigo);
                }

                int resultadoEmpleado = statementAsistente.executeUpdate();

                if (resultadoEmpleado == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un asistente en el equipo: " + codigo);
                }

                statement.close();
                statementAsistente.close();
                pasaporte++;
                cont++;
            }
        }catch (SQLException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            if(statement != null)
                statement.close();
            if(statementAsistente != null)
                statementAsistente.close();
        }
        return pasaporte;
    }

    private static int inicializarDatosFederativos(String codigo, int pasaporte) throws Exception{

        // Genera una lista con los puestos federativos
        String[] puestosFederativos = {"Presidente" , "Vicepresidente", "Secretario", "Tesorero", "Fiscal", "Vocal"};

        PreparedStatement statement = null;
        PreparedStatement statementFederativo = null;

        try{

            for (int i = 1; i <= 6; i++) {

                // Crea una fecha de nacimento que tenga sentido dentro del sistema
                String fecha_nacimiento = generarFechaDeNacimiento();
                String fecha_inicio = generarFechaDeInicio();

                String puesto = puestosFederativos[i-1];

                String queryPersona = "INSERT INTO persona" +
                        "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento) " +
                        "VALUES(?, ?, ?, ?, ?)";

                String queryFederativo = "INSERT INTO empleado" +
                        "(numero_pasaporte, nacionalidad, fecha_inicio_puesto, tipo_empleado, tipo_especifico_empleado, codigo_fifa_equipo) " +
                        "VALUES(?, ?, ?, ?, ?, ?)";

                statement = conexion.prepareStatement(queryPersona);
                statement.setLong(1, pasaporte);
                statement.setString(2, "nombreFed" + codigo + i);
                statement.setString(3, "apellido1Fed" + codigo + i);
                statement.setString(4, "apellido2Fed" + codigo + i);
                statement.setDate(5, Date.valueOf(fecha_nacimiento));

                statementFederativo = conexion.prepareStatement(queryFederativo);
                statementFederativo.setLong(1, pasaporte);
                statementFederativo.setString(2, "Prueba");
                statementFederativo.setDate(3, Date.valueOf(fecha_inicio));
                statementFederativo.setString(4, "Federativo");
                statementFederativo.setString(5, puesto);
                statementFederativo.setString(6, codigo);

                // Genera una transaccion donde se deben actualizar primero la tabla persona y luego la tabla jugador
                // Ambas deben ser exitosas y sino se hacer un rollback

                if(!conexion.isValid(500))
                    Controller.iniciarConexion();

                int resultadoPersona = statement.executeUpdate();

                if (resultadoPersona == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un federativo en el equipo: " + codigo);
                }

                int resultadoEmpleado = statementFederativo.executeUpdate();

                if (resultadoEmpleado == 0) {
                    conexion.rollback();
                    throw new Exception("No se ha podido insertar un federativo en el equipo: " + codigo);
                }

                statement.close();
                statementFederativo.close();

                pasaporte++;
            }
        }catch (SQLException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            if(statement != null)
                statement.close();
            if(statementFederativo != null)
                statementFederativo.close();
        }
        return pasaporte;
    }

    private static int inicializarDatosEntrenador(String codigo, int pasaporte) throws Exception{

        PreparedStatement statement = null;
        PreparedStatement statementEntrenador = null;

        try{
            // Crea una fecha de nacimento que tenga sentido dentro del sistema
            String fecha_nacimiento = generarFechaDeNacimiento();
            String fecha_inicio = generarFechaDeInicio();

            String queryPersona = "INSERT INTO persona" +
                    "(numero_pasaporte, nombreP, apellido1, apellido2, fecha_nacimiento) " +
                    "VALUES(?, ?, ?, ?, ?)";

            String queryEntrenador = "INSERT INTO empleado" +
                    "(numero_pasaporte, nacionalidad, fecha_inicio_puesto, tipo_empleado, tipo_especifico_empleado, codigo_fifa_equipo)" +
                    "VALUES(?, ?, ?, ?, ?, ?)";

            statement = conexion.prepareStatement(queryPersona);
            statement.setLong(1, pasaporte);
            statement.setString(2, "nombreEnt" + codigo);
            statement.setString(3, "apellido1Ent" + codigo);
            statement.setString(4, "apellido2Ent" + codigo);
            statement.setDate(5, Date.valueOf(fecha_nacimiento));

            statementEntrenador = conexion.prepareStatement(queryEntrenador);
            statementEntrenador.setLong(1, pasaporte);
            statementEntrenador.setString(2, "Prueba");
            statementEntrenador.setDate(3, Date.valueOf(fecha_inicio));
            statementEntrenador.setString(4, "Tecnico");
            statementEntrenador.setNull(5, Types.VARCHAR);
            statementEntrenador.setString(6, codigo);

            // Genera una transaccion donde se deben actualizar primero la tabla persona y luego la tabla jugador
            // Ambas deben ser exitosas y sino se hacer un rollback

            if(!conexion.isValid(500))
                Controller.iniciarConexion();

            int resultadoPersona = statement.executeUpdate();

            if (resultadoPersona == 0) {
                conexion.rollback();
                throw new Exception("No se ha podido insertar el tecnico en el equipo: " + codigo);
            }

            int resultadoEmpleado = statementEntrenador.executeUpdate();

            if (resultadoEmpleado == 0) {
                conexion.rollback();
                throw new Exception("No se ha podido insertar el tecnico en el equipo: " + codigo);
            }

            statement.close();
            statementEntrenador.close();
        }
        catch (SQLException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            if(statement != null)
                statement.close();
            if(statementEntrenador != null)
                statement.close();
        }

        pasaporte++;
        return pasaporte;
    }

    private static String generarPuestoParaJugador(HashMap<String, Integer> jugadoresPorPuesto){
        int valor = new Random().nextInt(4);
        switch (valor){
            case 0:
                if(jugadoresPorPuesto.get("Portero") < 3)
                    return "Portero";
                else
                    return generarPuestoParaJugador(jugadoresPorPuesto);
            case 1:
                if(jugadoresPorPuesto.get("Defensa") < 8)
                    return "Defensa";
                else
                    return generarPuestoParaJugador(jugadoresPorPuesto);
            case 2:
                if(jugadoresPorPuesto.get("Mediocampista") < 8)
                    return "Mediocampista";
                else
                    return generarPuestoParaJugador(jugadoresPorPuesto);
            default:
                if(jugadoresPorPuesto.get("Delantero") < 4)
                    return "Delantero";
                else
                    return generarPuestoParaJugador(jugadoresPorPuesto);
        }
    }

    private static String generarTipoAsistente(HashMap<String, Integer> tiposDeAsistente, int pivote){

        int valor = new Random().nextInt(7);

        switch (valor){
            case 0:
                if(tiposDeAsistente.get("Tecnico") < pivote)
                    return "Tecnico";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            case 1:
                if(tiposDeAsistente.get("Preparador Fisico") < pivote)
                    return "Preparador Fisico";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            case 2:
                if(tiposDeAsistente.get("Medico") < pivote)
                    return "Medico";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            case 3:
                if(tiposDeAsistente.get("Psicologo") < pivote)
                    return "Psicologo";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            case 4:
                if(tiposDeAsistente.get("Nutricionista") < pivote)
                    return "Nutricionista";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            case 5:
                if(tiposDeAsistente.get("Administrativo") < pivote)
                    return "Administrativo";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
            default:
                if(tiposDeAsistente.get("Delegado") < pivote)
                    return "Delegado";
                else
                    return generarTipoAsistente(tiposDeAsistente, pivote);
        }
    }

    private static String generarTipoAsistenteOffset(String[] tipos){
        return tipos[new Random().nextInt(tipos.length)];
    }

    private static String generarFechaDeInicio(){
        Random generador = new Random();

        // Crea una fecha de nacimento que tenga sentido dentro del sistema
        int dia = generador.nextInt(28) + 1;
        int mes = generador.nextInt(12) + 1;
        int ano = 2018 - generador.nextInt(10);
        String fecha_nacimiento = ano + "-" + mes + "-" + dia;

        return  fecha_nacimiento;
    }

    private static String generarFechaDeNacimiento(){
        Random generador = new Random();

        // Crea una fecha de nacimento que tenga sentido dentro del sistema
        int dia = generador.nextInt(28) + 1;
        int mes = generador.nextInt(12) + 1;
        int ano = 2018 - (20 + generador.nextInt(15));
        String fecha_nacimiento = ano + "-" + mes + "-" + dia;

        return  fecha_nacimiento;
    }

    private static void inicializarDatosSedes(){

        HashMap<String, String> sedes = new HashMap<>();
        Random generador = new Random();

        sedes.put("Estadio Luzhniki", "Moscu");
        sedes.put("Otkrytie Arena", "Moscu");
        sedes.put("Estadio de San Petersbrugo", "San Petersburgo");
        sedes.put("Estadio de Kaliningrado", "Kaliningrado");
        sedes.put("Kazan Arena", "Kazan");
        sedes.put("Estadio de Nizhni Novgorod", "Nizhni Novgorod");
        sedes.put("Samara Arena", "Samara");
        sedes.put("Estadio de Volgogrado", "Volgogrado");
        sedes.put("Mordovia Arena", "Saransk");
        sedes.put("Estadio Fisht", "Sochi");
        sedes.put("Ekaterimburgo Arena", "Ekaterimburgo");
        sedes.put("Rostov Arena", "Rostov Del Don");

        for(String estadio : sedes.keySet()){
            String ciudad = sedes.get(estadio);
            int capacidad = 45000 + generador.nextInt(30000);

            String query = "INSERT INTO sede(" +
                    estadio + ", " + ciudad + capacidad + ")";

            try{
                Statement statement = conexion.createStatement();
                statement.execute(query);
            }catch(SQLException e){

            }catch(Exception e){

            }
        }

    }

    // Backtracking para borrar tablas
    public static void borrarTablas(ArrayList<String> tablas){

        if(conexion != null)
        {
            try{
                String query = "SELECT table_name FROM user_tables";
                Statement statement = conexion.createStatement();
                ResultSet result = statement.executeQuery(query);

                if(tablas == null){
                    tablas = new ArrayList<>();
                    while(result.next())
                        tablas.add(result.getString("table_name"));
                }

                int indice = new Random().nextInt(tablas.size());

                String queryBorrado = "DROP TABLE " + tablas.get(indice);
                statement.execute(queryBorrado);
                tablas.remove(tablas.get(indice));

                if(tablas.size() > 0)
                    borrarTablas(tablas);
                else
                    new MessageBox(Alert.AlertType.INFORMATION, "Se han borrado con exito las tablas");

            }catch(SQLException e){
                borrarTablas(tablas);
            }catch (Exception e){
                borrarTablas(tablas);
            }
        }
    }

    /* Metodos de borrado */

    public static void borrarConfederacion(String codigo) throws Exception{
        try{

            String query = "DELETE FROM confederacion WHERE codigo_confederacion = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, codigo);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception();
        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarEquipo(String codigo) throws Exception{
        try{
            String query = "DELETE FROM equipo WHERE codigo_fifa_equipo = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, codigo);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception();
        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarGrupo(String codigo) throws Exception{
        try{
            String query = "DELETE FROM grupo WHERE nombre_grupo = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, codigo);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception();
        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarJugador(long numero) throws Exception{
        try{
            conexion.setAutoCommit(false);

            String query = "DELETE FROM jugador WHERE numero_pasaporte = ?";
            String queryParent = "DELETE FROM personas WHERE numero_pasaporte = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);

            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryParent);
            statement.setLong(1, numero);

            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarArbitro(long numero) throws Exception{
        try{
            conexion.setAutoCommit(false);

            String query = "DELETE FROM arbitro WHERE numero_pasaporte = ?";
            String queryParent = "DELETE FROM persona WHERE numero_pasaporte = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryParent);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarEntrenador(long numero) throws Exception{
        try{
            conexion.setAutoCommit(false);

            String query = "DELETE FROM empleado WHERE numero_pasaporte = ?";
            String queryParent = "DELETE FROM persona WHERE numero_pasaporte = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryParent);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarAsistente(long numero) throws Exception{
        try{
            conexion.setAutoCommit(false);

            String query = "DELETE FROM empleado WHERE numero_pasaporte = ?";
            String queryParent = "DELETE FROM persona WHERE numero_pasaporte = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryParent);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarFederativo(long numero) throws Exception{
        try{
            conexion.setAutoCommit(false);

            String query = "DELETE FROM empleado WHERE numero_pasaporte = ?";
            String queryParent = "DELETE FROM persona WHERE numero_pasaporte = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryParent);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarPartido(long numero) throws Exception{
        try{

            String query = "DELETE FROM partido WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception();

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarSede(String codigo) throws Exception{
        try{
            String query = "DELETE FROM sede WHERE nombre_sede = ?";

            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, codigo);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception();

        }catch(Exception e){
            throw new Exception("No se pudo realizar la operacion.\nRazon: " + e.getMessage());
        }
    }

    public static void borrarDelegacion(long numero) throws Exception{
        try {
            conexion.setAutoCommit(false);

            String queryTitulares = "DELETE FROM titulares WHERE numero_partido = ?";
            String querySuplentes = "DELETE FROM suplentes WHERE numero_partido = ?";
            String queryAsistentes = "DELETE FROM asistentes_en_partidos WHERE numero_partido = ?";
            String queryMedico = "DELETE FROM medico_en_partidos WHERE numero_partido = ?";
            String queryDelegados = "DELETE FROM delegados_en_partidos WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(querySuplentes);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryAsistentes);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryMedico);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            statement = conexion.prepareStatement(queryDelegados);
            statement.setLong(1, numero);
            statement.executeUpdate();
            statement.close();

            conexion.commit();
            conexion.setAutoCommit(true);

        }catch (SQLException e){
            throw e;
        }
    }

    public static void borrarCuerpoArbitral(long numero) throws Exception{
        try {
            String queryTitulares = "DELETE FROM cuerpo_arbitral WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception("No hubo resultados");

        }catch (SQLException e){
            throw e;
        }
    }

    public static void borrarGoles(long numero) throws Exception{
        try {
            String queryTitulares = "DELETE FROM gol WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception("No hubo resultados");

        }catch (SQLException e){
            throw e;
        }
    }

    public static void borrarPenales(long numero) throws Exception{
        try {
            String queryTitulares = "DELETE FROM penales WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception("No hubo resultados");

        }catch (SQLException e){
            throw e;
        }
    }

    public static void borrarReposicion(long numero) throws Exception{
        try {
            String queryTitulares = "DELETE FROM reposicion WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception("No hubo resultados");

        }catch (SQLException e){
            throw e;
        }
    }

    public static void borrarTarjetas(long numero) throws Exception{
        try {
            String queryTitulares = "DELETE FROM tarjeta WHERE numero_partido = ?";

            PreparedStatement statement = conexion.prepareStatement(queryTitulares);
            statement.setLong(1, numero);
            int resultado = statement.executeUpdate();
            statement.close();

            if(resultado == 0)
                throw new Exception("No hubo resultados");

        }catch (SQLException e){
            throw e;
        }
    }

    /* Metodo de actualizacion y creacion */
    public static Connection obtenerConexion(){
        return conexion;
    }

    // Funcion que crea alertas con base en excepciones
    public static void manejarExcepcion(Exception e){
        MessageBox alerta = new MessageBox(Alert.AlertType.ERROR, e.getMessage());
        e.printStackTrace();
    }

    // Funcion auxiliar que liga javafx con java swing
    public static void createSwingContent(final SwingNode swingNode, ResultSet resultado) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DataModel modelo = new DataModel(resultado);
                swingNode.setContent(new JTable(modelo));
            }
        });
    }

    public static <T> T crearPopup(Class<T> tipoDeResultado, String mensajeTitulo)throws Exception{
        try{

            FXMLLoader loaderPopup = new FXMLLoader(ControllerVentanaPrincipal.class.getResource("/interfaz/popup/popupPrimaryKey.fxml"));
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

}
