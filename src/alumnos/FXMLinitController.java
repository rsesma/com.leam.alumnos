package alumnos;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import alumnos.model.ImportTask;
import alumnos.model.getAlumnosData;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLinitController implements Initializable {
    @FXML
    private TextField periodo;
    @FXML
    private TextField curso;
    @FXML
    private TextField grupo;
    @FXML
    private TextField nombre;
    @FXML
    private TextField dni;
    @FXML
    private ProgressBar pbImportar;
    @FXML
    private Button btImportar;
    
    getAlumnosData d;

    @FXML
    void pbMostrar(ActionEvent event) {
        String p = this.periodo.getText().trim();
        String c = this.curso.getText().trim();
        String g = this.grupo.getText().trim();
        String d = this.dni.getText().trim();
        String n = this.nombre.getText().trim();
    	String filter = "";
    	if (!p.isEmpty()) {
	    	filter = (!p.isEmpty() ? "Periodo = '" + p + "'" : "");
	    	filter = filter + (!c.isEmpty() ? " AND Curso = '" + c + "'" : "");
    	}
    	if (!g.isEmpty()) filter = ("Grupo = '" + g + "'");
    	if (!d.isEmpty()) filter = ("DNI LIKE '%" + d + "%'");
    	if (!n.isEmpty()) filter = ("nom LIKE '%" + n + "%'");
        
    	try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/FXMLalumnos.fxml"));
            Parent r = (Parent) fxml.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(r));
            stage.setTitle("Alumnos");
            FXMLalumnosController alumnos = fxml.<FXMLalumnosController>getController();
            alumnos.SetData(this.d, filter);
            stage.showAndWait();            
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    public void pbImportar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Importar");
        alert.setContentText(null);

        String periodo = this.periodo.getText().trim();
        if (!periodo.isEmpty()){
        	FileChooser chooser = new FileChooser();
	        chooser.setTitle("Abrir archivo de datos");
	        chooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
	        File file = chooser.showOpenDialog(null);
	        if (file != null) {
	        	btImportar.setDisable(true);
	        	pbImportar.setVisible(true);
	            pbImportar.setProgress(0);
	        	
	            ImportTask importTask = new ImportTask(this.d, periodo, file.getAbsolutePath());
	            pbImportar.progressProperty().unbind();
	            pbImportar.progressProperty().bind(importTask.progressProperty());
	            
	            // When completed tasks
	            importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
	            		new EventHandler<WorkerStateEvent>() { 
	            	@Override
                    public void handle(WorkerStateEvent t) {
	            		btImportar.setDisable(false);;
	            		pbImportar.setVisible(false);
	            		
	                    alert.setHeaderText("Importación finalizada");
	                    alert.showAndWait();
	            	}
	            });
	            
	            new Thread(importTask).start();
	        }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText("Falta el período");
            alert.showAndWait();
        }
    }


 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		pbImportar.setVisible(false);
	}

    public void SetData(getAlumnosData d) {
        this.d = d;
    }

}