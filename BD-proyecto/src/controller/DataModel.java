package controller;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * @author gmc_2
 */

public class DataModel extends AbstractTableModel{
	
        ArrayList<String> headers;
        ArrayList<ArrayList<Object>> objetos;
        ArrayList<String> clasesPorColumna;
        
        public DataModel(ResultSet res){
            this.headers = new ArrayList<>();
            this.objetos = new ArrayList<>();
            this.clasesPorColumna = new ArrayList<>();
            
            ResultSetMetaData metaData = null;
            
            try{
                metaData = res.getMetaData();
                
                for(int i = 1; i <= metaData.getColumnCount(); i++){
                    headers.add(metaData.getColumnLabel(i));
                    clasesPorColumna.add(metaData.getColumnClassName(i));
                }
                
                while(res.next()){
                    ArrayList<Object> filaDeObjetos = new ArrayList<>();
                    
                    for(int i = 1; i <= headers.size(); i++)
                        filaDeObjetos.add(res.getObject(i));
                    
                    objetos.add(filaDeObjetos);
                }      
            }
            catch(Exception e){
                System.out.println("No se ha podido obtener los metadatos");
            }
            
        }
        
        @Override
        public int getRowCount(){
            return objetos.size();
        }
        
        @Override
        public int getColumnCount(){
           return headers.size();
        }
        
        @Override
        public Object getValueAt(int filaIndex, int columnaIndex){
            return objetos.get(filaIndex).get(columnaIndex);
        }
        
        @Override
        public Class<?> getColumnClass(int columna){
            try{
                return Class.forName(clasesPorColumna.get(columna));
            }catch(Exception e){
                System.out.println("No se pudo contruir la clase, porque no se ha encontrado");
            }
            return null;
        }
        
        @Override
        public String getColumnName(int columna){
            return headers.get(columna);
        }
    }