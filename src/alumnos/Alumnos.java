package alumnos;
	
import alumnos.model.getAlumnosData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;


public class Alumnos extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
	        Boolean test = false;
	        Boolean ok = true;
	        getAlumnosData d = null;
	        
	        if (!test) {
	            // Launch login window
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLlogin.fxml")); 
	            Stage stage0 = new Stage(); 
	            stage0.initModality(Modality.WINDOW_MODAL);
	            stage0.setTitle("Registro");
	            stage0.setScene(new Scene((Parent) loader.load()));
	            FXMLloginController login = loader.<FXMLloginController>getController();
	            stage0.showAndWait();
	            
	            ok = login.ok;
	            if (ok) d = login.d;
	        } else {
	            d = new getAlumnosData(true);
	        }
	        
	        if (ok) {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION, "login correcto");
	            alert.showAndWait();

/*	            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLalumnos.fxml"));
	            Parent root = (Parent) fxml.load();
	            FXMLalumnosController alumnos = fxml.<FXMLalumnosController>getController();
	            alumnos.SetData(d);

	            Scene scene = new Scene(root);
	            stage.setTitle("Alumnos cursos Stata");
	            stage.setScene(scene);
	            stage.show();*/
	        }
		} catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
