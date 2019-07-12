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
    	
    	if (load) getConnection("rsesma","Amsesr.1977","localhost");
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
    
    public ResultSet getProblemasPEC(String filter) throws SQLException {
        if (filter.length()>0) {
            return conn.prepareStatement("SELECT * FROM problemaspec WHERE " + filter).executeQuery();
        }
        else {
            return conn.prepareStatement("SELECT * FROM problemaspec").executeQuery();            
        }
    }

    public ResultSet getProblemasPEC1(String filter) throws SQLException {
        if (filter.length()>0) {
            return conn.prepareStatement("SELECT * FROM problemaspec1 WHERE " + filter).executeQuery();
        }
        else {
            return conn.prepareStatement("SELECT * FROM problemaspec1").executeQuery();            
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
    
    public void entregaPEC(String dni, String curso, String periodo, Boolean honor) {
        try {
            PreparedStatement q;
            q = conn.prepareStatement("INSERT INTO entregahonor (DNI, Curso, Periodo, entregada, honor) VALUES(?, ?, ?, ?, ?)");
            q.setString(1, dni);
            q.setString(2, curso);
            q.setString(3, periodo);
            q.setBoolean(4, true);
            q.setBoolean(5, honor);
            q.executeUpdate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }
    
    public void updateEntregaPEC(Problema p, boolean pec1) {
        try {
            PreparedStatement q;
            if (pec1) {
                q = conn.prepareStatement("UPDATE entregahonorpec1 SET mdb = ?, pdf = ?, honor = ? WHERE DNI = ?");
                q.setBoolean(1,p.getMDB());
                q.setBoolean(2,p.getPDF());
                q.setBoolean(3,p.getHonor());
                q.setString(4,p.getDNI());                
            } else {
                q = conn.prepareStatement("UPDATE entregahonor SET honor = ? WHERE DNI = ? AND CURSO = ?");
                q.setBoolean(1,p.getHonor());
                q.setString(2,p.getDNI());
                q.setString(3,p.getGrupo().substring(0,3));
            }
            q.executeUpdate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }


}
