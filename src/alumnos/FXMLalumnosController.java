/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import alumnos.model.Alumno;
import alumnos.model.EntregaTask;
import alumnos.model.ImportTask;
import alumnos.model.getAlumnosData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class FXMLalumnosController implements Initializable {
    
    @FXML
    TableView<Alumno> table;
    @FXML
    TableColumn<Alumno, String> periodoCol;
    @FXML
    TableColumn<Alumno, String> cursoCol;
    @FXML
    TableColumn<Alumno, String> grupoCol;
    @FXML
    TableColumn<Alumno, String> DNICol;
    @FXML
    TableColumn<Alumno, String> PCCol;
    @FXML
    TableColumn<Alumno, Boolean> fijoCol;
    @FXML
    TableColumn<Alumno, String> nameCol;
    @FXML
    TableColumn<Alumno, String> claseCol;
    @FXML
    TableColumn<Alumno, String> pec1Col;
    @FXML
    TableColumn<Alumno, String> pecCol;
    @FXML
    TableColumn<Alumno, String> notaCol;
    @FXML
    TableColumn<Alumno, String> comentCol;
    @FXML
    TableColumn<Alumno, Boolean> copiaCol;
    @FXML
    TableColumn<Alumno, String> idCopiaCol;    
    @FXML
    Label ntotal;
    @FXML
    TextField search;
    @FXML
    Button btSearch;
    @FXML
    Button btClean;
    @FXML
    Label pbLabel;
    @FXML
    ProgressBar pb;
    @FXML
    TextField folder;
    @FXML
    TextField periodo;
    
    final ObservableList<Alumno> data = FXCollections.observableArrayList();
    private static final String CORREGIRPECS = "/CorregirPECs/";
    private static final String ST1_PEC2_originales = "/ST1/PEC2/originales";

    private final File home = new File(System.getProperty("user.home"));
    
    getAlumnosData d;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.pb.setVisible(false);
    	this.pbLabel.setVisible(false);
    	
    	this.folder.setText(new File(this.home, CORREGIRPECS).getAbsolutePath());
    	
		ImageView imgSearch = new ImageView(new Image(getClass().getResourceAsStream("/fxml/search.png")));
		imgSearch.setFitWidth(15);
		imgSearch.setFitHeight(15);
        this.btSearch.setGraphic(imgSearch);
        
		ImageView imgClean = new ImageView(new Image(getClass().getResourceAsStream("/fxml/no_filter.png")));
		imgClean.setFitWidth(15);
		imgClean.setFitHeight(15);
        this.btClean.setGraphic(imgClean);
        
        this.table.setEditable(false);
        
        // Set up the alumnos table
        this.periodoCol.setCellValueFactory(new PropertyValueFactory<>("Periodo"));
        this.cursoCol.setCellValueFactory(new PropertyValueFactory<>("Curso"));
        this.grupoCol.setCellValueFactory(new PropertyValueFactory<>("Grupo"));
        this.DNICol.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.pec1Col.setCellValueFactory(new PropertyValueFactory<>("PEC1"));
        this.pecCol.setCellValueFactory(new PropertyValueFactory<>("PEC"));
        this.notaCol.setCellValueFactory(new PropertyValueFactory<>("NOTA"));
        this.PCCol.setCellValueFactory(new PropertyValueFactory<>("PC"));
        this.comentCol.setCellValueFactory(new PropertyValueFactory<>("Coment"));
        this.claseCol.setCellValueFactory(new PropertyValueFactory<>("Clase"));
        this.idCopiaCol.setCellValueFactory(new PropertyValueFactory<>("IDCopia"));
        
        // checkbox Fijo
        this.fijoCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Alumno, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Alumno, Boolean> param) {
                Alumno a = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(a.getFijo());                
                return booleanProp;
            }
        });
        this.fijoCol.setCellFactory(new Callback<TableColumn<Alumno, Boolean>, TableCell<Alumno, Boolean>>() {
            public TableCell<Alumno, Boolean> call(TableColumn<Alumno, Boolean> p) {
                CheckBoxTableCell<Alumno, Boolean> cell = new CheckBoxTableCell<Alumno, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        // checkbox Copia
        this.copiaCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Alumno, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Alumno, Boolean> param) {
                Alumno a = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(a.getCopia());
                return booleanProp;
            }
        });
        this.copiaCol.setCellFactory(new Callback<TableColumn<Alumno, Boolean>, TableCell<Alumno, Boolean>>() {
            public TableCell<Alumno, Boolean> call(TableColumn<Alumno, Boolean> p) {
                CheckBoxTableCell<Alumno, Boolean> cell = new CheckBoxTableCell<Alumno, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        table.setItems(this.data);
    }
    
    @FXML
    void mnuImportar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir archivo de datos");
        chooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
        File file = chooser.showOpenDialog(null);
        if (file != null) {
        	String p = this.periodo.getText(); 
            if (!p.isEmpty()){
	        	this.pb.setVisible(true);
	            this.pb.setProgress(0);
	            this.pbLabel.setVisible(true);
	            this.pbLabel.setText("Importando datos...");
	        	
	            ImportTask importTask = new ImportTask(this.d, this.periodo.getText(), file.getAbsolutePath());
	            this.pb.progressProperty().unbind();
	            this.pb.progressProperty().bind(importTask.progressProperty());
	            
	            // When completed tasks
	            importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
	            		new EventHandler<WorkerStateEvent>() { 
	            	@Override
                    public void handle(WorkerStateEvent t) {
	    	        	pb.setVisible(false);
	    	            pbLabel.setVisible(false);
	    	            pbLabel.setText("");
	            		
	                    String filter = "";
	                    Alert alert = new Alert(AlertType.CONFIRMATION);
	                    alert.setTitle("Importación finalizada");
	                    alert.setHeaderText(null);
	                    alert.setContentText("Importación finalizada.\n¿Visualizar el periodo?");
	                    Optional<ButtonType> result = alert.showAndWait();
	                    if (result.get() == ButtonType.OK) filter = "Periodo = '" + p + "'";
	                    
	                    data.removeAll(data);
	                    LoadAlumnosTable(filter);
	            	}
	            });
	            
	            new Thread(importTask).start();
            }
        }
    }

    @FXML
    void mnuCorregir(ActionEvent event) {

    }

    @FXML
    void mnuCorregirPEC1(ActionEvent event) {

    }

    @FXML
    void mnuEntregaPEC(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Entrega PECs");
        alert.setContentText(null);

        // check default dir
        String dir = this.folder.getText();    	
    	if (dir.isEmpty()) {
    		alert.setHeaderText("Indique una carpeta CorregirPECs");
    		alert.showAndWait();
    	} else {
    		File def = new File(dir);
    		if (!def.exists()) {
        		alert.setHeaderText("La carpeta CorregirPECs indicada no existe");
        		alert.showAndWait();    			
    		} else { 
	            // get periodo
    			String p = this.periodo.getText(); 
	            if (p.isEmpty()){
	        		alert.setHeaderText("Indique un periodo");
	        		alert.showAndWait();
	            } else {
		        	this.pb.setVisible(true);
		            this.pb.setProgress(0);
		            this.pbLabel.setVisible(true);
		            this.pbLabel.setText("Revisando entrega...");
		        	
		            EntregaTask entregaTask = new EntregaTask(this.d, p, new File(def,ST1_PEC2_originales));
		            this.pb.progressProperty().unbind();
		            this.pb.progressProperty().bind(entregaTask.progressProperty());
		            
		            // When completed tasks
		            entregaTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
		            		new EventHandler<WorkerStateEvent>() { 
		            	@Override
	                    public void handle(WorkerStateEvent t) {
		    	        	pb.setVisible(false);
		    	            pbLabel.setVisible(false);
		    	            pbLabel.setText("");
		            		
		    	            String prbl = entregaTask.getValue();
                			alert.setAlertType(AlertType.INFORMATION);
		                    alert.setHeaderText("Proceso finalizado");
			                if (!prbl.isEmpty()) {
			                    // show pane with problems
			                    VBox dialogPaneContent = new VBox();
			                    Label label = new Label("Se encontraron problemas:");
			                    TextArea textArea = new TextArea();
			                    textArea.setText(prbl);
			             
			                    // set content for Dialog Pane
			                    dialogPaneContent.getChildren().addAll(label, textArea);	             
			                    alert.getDialogPane().setContent(dialogPaneContent);	             
			                }
			                alert.showAndWait();
		            	}
		            });
		            
		            new Thread(entregaTask).start();
	            }
    		}
    	}
    	
    }

    @FXML
    void mnuProblemasPEC(ActionEvent event) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/FXMLproblemas.fxml"));
            Parent r = (Parent) fxml.load();            
            FXMLproblemasController probl = fxml.<FXMLproblemasController>getController();
            probl.SetData(this.d,false);
            
            Stage stage = new Stage(); 
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(new Scene(r));
            stage.setTitle("Problemas PEC");
            stage.showAndWait();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void mnuDescomprimirPEC1(ActionEvent event) {

    }

    @FXML
    void mnuEntregaPEC1(ActionEvent event) {

    }

    @FXML
    void mnuProblemasPEC1(ActionEvent event) {

    }

    @FXML
    void mnuSintaxis(ActionEvent event) {

    }

    @FXML
    void mnuExport(ActionEvent event) {

    }
    
    @FXML
    public void pbSearch(ActionEvent event) {
        String filter = "";
        if (!this.search.getText().trim().isEmpty()) {
            filter = "Periodo = '".concat(this.search.getText()).concat("'");
            filter = filter.concat("OR Grupo = '").concat(this.search.getText()).concat("'");
            filter = filter.concat("OR DNI LIKE '%").concat(this.search.getText()).concat("%'");
            filter = filter.concat("OR nom LIKE '%").concat(this.search.getText()).concat("%'");
            this.data.removeAll(this.data);
            LoadAlumnosTable(filter);
        }
    }

    @FXML
    public void pbClean(ActionEvent event) {
        this.data.removeAll(this.data);
        this.search.setText("");
        LoadAlumnosTable("");
    }
    
    @FXML
    public void pbFolder(ActionEvent event) {
        // get CorregirPECs folder
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escoger carpeta CorregirPECs");
        chooser.setInitialDirectory(this.home);
        File dir = chooser.showDialog(null);
        if (dir != null) {
        	this.folder.setText(dir.getAbsolutePath());
        }
    }
    
    public void SetData(getAlumnosData d, String filter) {
        this.d = d;
        LoadAlumnosTable(filter);
    }
    
    public void LoadAlumnosTable(String filter) {
        int count = 0;        
        try{
            ResultSet rs = this.d.getAlumnosRs(filter);
            while(rs.next()){
                Alumno a = new Alumno();
                a.setPeriodo(rs.getString("Periodo"));
                a.setCurso(rs.getString("Curso"));
                a.setGrupo(rs.getString("Grupo"));
                a.setDNI(rs.getString("DNI"));
                a.setPC(rs.getString("PC"));
                a.setFijo(rs.getBoolean("Fijo"));
                a.setName(rs.getString("nom"));
                a.setClase(rs.getString("NCLASE"));
                a.setPEC1(rs.getString("PEC1"));
                a.setPEC(rs.getString("PEC"));
                a.setNOTA(rs.getString("NOTA"));
                a.setCopia(rs.getBoolean("Copia"));
                a.setIDCopia(rs.getString("IDCopia"));
                a.setComent(rs.getString("coment"));
                
                this.data.add(a);
                
                count++;
            }
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
        this.ntotal.setText(count + " registros");
    }
    
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) this.search.getScene().getWindow();
        stage.close();
    }    

}
