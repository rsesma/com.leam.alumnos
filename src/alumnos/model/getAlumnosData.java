package alumnos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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

}
