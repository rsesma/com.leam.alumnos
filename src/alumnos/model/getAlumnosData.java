package alumnos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import javafx.scene.control.Alert;

public class getAlumnosData {
    private static final String C_DRIVER = "jdbc:mariadb";
    private static Connection conn;
    
    public getAlumnosData(Boolean load) {
    	getAlumnosData.setConn(null);
    	
    	if (load) getConnection("rsesma","Amsesr.1977","192.168.1.10");
    }
    
    public Boolean getConnection(String user, String pswd, String server) {
        try {
            String url = C_DRIVER + "://" + server + ":3306/alumnos";
            Properties info = new Properties();
            info.setProperty("user", user);
            info.setProperty("password", pswd);
            getAlumnosData.setConn(DriverManager.getConnection(url, info));
            return true;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de registro");
            alert.setHeaderText(e.getMessage());
            alert.setContentText(null);
            alert.showAndWait();
            return false;
        }
    }

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		getAlumnosData.conn = conn;
	}

    public ResultSet getAlumnosRs(String filter) throws SQLException {
        if (filter.length()>0) {
            return conn.prepareStatement("SELECT * FROM nota_fin WHERE " + filter).executeQuery();
        }
        else {
            return conn.prepareStatement("SELECT * FROM nota_fin").executeQuery();            
        }
    }

    public void importExcelRow(org.apache.poi.ss.usermodel.Row row, String periodo) {
        try {
        	Cell c = row.getCell(5);		// check PC value to discard rows with no PC
        	
        	if (c == null || c.getCellType() == CellType.BLANK) {
        		// do nothing: cell is void
        	} else {
	            PreparedStatement q;
	            q = conn.prepareStatement("INSERT INTO alumnos " +
	            						  "(Periodo,Curso,DNI,Grupo,nombre,ape1,ape2,PC,Fijo," +
	            						  "CLASE,Comentario,provincia,poblacion,trabajo,email) " +
	                                      "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	            q.setString(1,periodo);														// Periodo
	            q.setString(2,row.getCell(0).getStringCellValue().substring(0,3));			// Curso
	            q.setString(3,row.getCell(1).getStringCellValue());							// DNI
	            q.setString(4,row.getCell(0).getStringCellValue());							// Grupo
	            q.setString(5,row.getCell(2).getStringCellValue());							// nombre
	            q.setString(6,row.getCell(3).getStringCellValue());							// ape1
	            q.setString(7,row.getCell(4).getStringCellValue());							// ape2
	            q.setInt(8, (int) row.getCell(5).getNumericCellValue());					// PC
	            q.setBoolean(9,row.getCell(6).getStringCellValue().equalsIgnoreCase("Si"));	// Fijo
	            q.setInt(10, (int) row.getCell(7).getNumericCellValue());					// CLASE
	            q.setString(11,row.getCell(8).getStringCellValue());						// Comentario
	            q.setString(12,row.getCell(9).getStringCellValue());						// provincia
	            q.setString(13,row.getCell(10).getStringCellValue());						// poblacion
	            q.setString(14,row.getCell(11).getStringCellValue());						// trabajo
	            q.setString(15,row.getCell(12).getStringCellValue());						// email
	            q.executeUpdate();
        	}
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }

}
