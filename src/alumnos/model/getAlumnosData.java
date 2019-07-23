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

    public ResultSet getRS(String fields, String table, String filter, String order) throws SQLException {
    	String sql = "SELECT " + fields + " FROM " + table;
    	if (filter.length()>0) sql = sql + " WHERE " + filter;
    	if (order.length()>0) sql = sql + " ORDER BY " + order;
    	return conn.prepareStatement(sql).executeQuery();
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
    
    public void entregaPEC(EntPEC p) {
        try {
        	String sql = "INSERT INTO entregahonor (DNI, Curso, Periodo, npec, entregada, honor" +
        			(p.isMultiple() ? ", mdb, pdf" : "") +
        			") VALUES(?, ?, ?, ?, ?, ?" +
        			(p.isMultiple() ? ", ?, ?) " : ") ");
            PreparedStatement q;
            q = conn.prepareStatement(sql);
            q.setString(1, p.getDNI());
            q.setString(2, p.getCurso());
            q.setString(3, p.getPeriodo());
            q.setInt(4, p.getNPEC());
            q.setBoolean(5, true);
            q.setBoolean(6, p.getHonor());
            if (p.isMultiple()) {
            	// load extra fields: MDB & PDF
            	q.setBoolean(7, p.getMDB());
            	q.setBoolean(8, p.getPDF());
            }
            q.executeUpdate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }
    
    public void updateEntregaPEC(EntPEC p) {
        try {
        	String sql = "UPDATE entregahonor SET honor = ?, mdb = ?, pdf = ? " + 
        			" WHERE DNI = ? AND Curso = ? AND Periodo = ? AND npec = ?";
            PreparedStatement q;
            q = conn.prepareStatement(sql);
            q.setBoolean(1, p.getHonor());
            q.setBoolean(2, (p.isMultiple() ? p.getMDB() : null));
            q.setBoolean(3, (p.isMultiple() ? p.getPDF() : null));
            q.setString(4, p.getDNI());
            q.setString(5, p.getCurso());
            q.setString(6, p.getPeriodo());
            q.setInt(7, p.getNPEC());
            q.executeUpdate();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }
}
