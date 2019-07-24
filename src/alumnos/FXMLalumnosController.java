package alumnos;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import alumnos.model.Alumno;
import alumnos.model.TaskEntrega;
import alumnos.model.TaskExtract;
import alumnos.model.TaskImport;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private static final String ST1_PEC1_comprimidas = "/ST1/PEC1/comprimidas";
    private static final String ST1_PEC1_descomprimidas = "/ST1/PEC1/descomprimidas";
    private static final String ST1_PEC2_originales = "/ST1/PEC2/originales";
    private static final String ST2_originales = "/ST2/originales";

    private static final String ABRIR_ARCHIVO = "Abrir archivo de datos";
    private static final String IMPORTANDO_DATOS = "Importando datos...";
    private static final String PROCESO_FINALIZADO = "Proceso finalizado";
    private static final String IMPORTACION_FINALIZADA = "Importaci�n finalizada.\n�Visualizar el periodo?";
    private static final String ENTREGA_PECS = "Entrega PECs";
    private static final String INDIQUE_CARPETA = "Indique una carpeta CorregirPECs";
    private static final String CARPETA_NO_EXISTE = "La carpeta CorregirPECs indicada no existe";
    private static final String INDIQUE_PERIODO = "Indique un periodo";
    private static final String CURSO_PECS = "Curso del que se entregan las PECs";
    private static final String CURSO = "Curso:";
    private static final String REVISANDO_ENTREGA = "Revisando entrega...";
    private static final String ENCONTRARON_PROBLEMAS = "Se encontraron problemas:";
    private static final String EXTRAYENDO_PECS = "Extrayendo PECs...";
    
    private final File home = new File(System.getProperty("user.home"));
    
    getAlumnosData d;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.pb.setVisible(false);
    	this.pbLabel.setVisible(false);
    	
    	this.folder.setText(new File(this.home, CORREGIRPECS).getAbsolutePath());
    	
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
        chooser.setTitle(ABRIR_ARCHIVO);
        chooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
        File file = chooser.showOpenDialog(null);
        if (file != null) {
        	String p = this.periodo.getText(); 
            if (!p.isEmpty()){
	        	this.pb.setVisible(true);
	            this.pb.setProgress(0);
	            this.pbLabel.setVisible(true);
	            this.pbLabel.setText(IMPORTANDO_DATOS);
	        	
	            TaskImport importTask = new TaskImport(this.d, this.periodo.getText(), file.getAbsolutePath());
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
	                    alert.setTitle(PROCESO_FINALIZADO);
	                    alert.setHeaderText(null);
	                    alert.setContentText(IMPORTACION_FINALIZADA);
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
		String stata = "C:\\Program Files (x86)\\Stata15\\Stata-64.exe";
		String dofile = "C:\\Users\\tempo\\Desktop\\test.do";
		File dir = new File("C:\\Users\\tempo\\Desktop");
		String cmd = String.format("\"%s\" /e /q do \"%s\", nostop", stata, dofile);
		// System.out.println(cmd);
		
		try {
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmd,null,dir);
			pr.waitFor();
			// System.out.println("Exited with error code " + exitCode);
			// assert exitCode == 0;
		} catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
		}

    }

    @FXML
    void mnuCorregirPEC1(ActionEvent event) {

    }
    
    @FXML
    void mnuEstructuraPEC(ActionEvent event) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/FXMLestructura.fxml"));
            Parent r = (Parent) fxml.load();
            FXMLestructuraController estruc = fxml.<FXMLestructuraController>getController();
            estruc.SetData(this.d);
            
            Stage stage = new Stage(); 
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(new Scene(r));
            stage.setTitle("Estructura PEC");
            stage.showAndWait();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void mnuEntregaPEC(ActionEvent event) {
    	
    	if (checkCarpetaPeriodo(true)) {
    		Optional<String> tipo = getTipoPEC();
        	if (tipo.isPresent()){
        		TaskEntrega entregaTask = new TaskEntrega(this.d, 
        			this.periodo.getText(), 
    				tipo.get(), getFolderFromTipo(tipo,true));
        		
	            this.pb.progressProperty().unbind();
	            this.pb.progressProperty().bind(entregaTask.progressProperty());
	            this.pb.setVisible(true);
	            this.pbLabel.setVisible(true);
	            this.pbLabel.setText(REVISANDO_ENTREGA);

	            // When completed tasks
	            entregaTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
	            		new EventHandler<WorkerStateEvent>() { 
	            	@Override
                    public void handle(WorkerStateEvent t) {
	    	        	pb.setVisible(false);
	    	            pbLabel.setVisible(false);
	    	            pbLabel.setText("");
	            		
	    	            String prbl = entregaTask.getValue();
	    	            Alert alert = new Alert(AlertType.INFORMATION);
	                    alert.setHeaderText(PROCESO_FINALIZADO);
	                    alert.setContentText(null);
		                if (!prbl.isEmpty()) {
		                    // show pane with problems
		                    VBox dialogPaneContent = new VBox();
		                    Label label = new Label(ENCONTRARON_PROBLEMAS);
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

    @FXML
    void mnuProblemasPEC(ActionEvent event) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/FXMLproblemas.fxml"));
            Parent r = (Parent) fxml.load();            
            FXMLproblemasController probl = fxml.<FXMLproblemasController>getController();
            probl.SetData(this.d);
            
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
    void mnuDescomprimirPEC(ActionEvent event) {
    	if (checkCarpetaPeriodo(false)) {
    		Optional<String> tipo = getTipoPEC();
        	if (tipo.isPresent()){
        		TaskExtract extractTask = new TaskExtract(getFolderFromTipo(tipo,false), getExtractFolderFromTipo(tipo));
            		
	            this.pb.progressProperty().unbind();
	            this.pb.progressProperty().bind(extractTask.progressProperty());
	            this.pb.setVisible(true);
	            this.pbLabel.setVisible(true);
	            this.pbLabel.setText(EXTRAYENDO_PECS);

	            // When completed tasks
	            extractTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
	            		new EventHandler<WorkerStateEvent>() { 
	            	@Override
                    public void handle(WorkerStateEvent t) {
	    	        	pb.setVisible(false);
	    	            pbLabel.setVisible(false);
	    	            pbLabel.setText("");
	    	            
	    	            Alert alert = new Alert(AlertType.INFORMATION);
	                    alert.setHeaderText(PROCESO_FINALIZADO);
	                    alert.setContentText(null);
		                alert.showAndWait();
	            	}
	            });
	            
	            new Thread(extractTask).start();
        	}
    	}
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
            ResultSet rs = this.d.getRS("*","nota_fin",filter,"");
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
    
    public Boolean checkCarpetaPeriodo(Boolean checkPeriodo) {
    	Boolean ok = false;
    	String error = "";
    	
        String dir = this.folder.getText();
        String p = this.periodo.getText();
    	if (dir.isEmpty()) {
    		error = INDIQUE_CARPETA;			// default dir is not empty
    	} else {
            File def = new File(dir);
    		if (!def.exists()) {
    			error = CARPETA_NO_EXISTE;		// default dir exists
    		} else {
    			if (checkPeriodo) {
	    			if (p.isEmpty()){
	    				error = INDIQUE_PERIODO;	// periodo exists
	    			} else {
	    				ok = true;
	    			}
    			} else {
    				ok = true;
    			}
    		}
		}
    	
        if (!ok) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ENTREGA_PECS);
            alert.setContentText(null);
    		alert.setHeaderText(error);
        	alert.showAndWait();
        }
        
    	return ok;
    }
    
    public Optional<String> getTipoPEC() {
    	List<String> choices = new ArrayList<>();
    	choices.add("ST1 - PEC1");
    	choices.add("ST1 - PEC2");
    	choices.add("ST2");

    	ChoiceDialog<String> dlg = new ChoiceDialog<>("ST1 - PEC2", choices);
    	dlg.setTitle(ENTREGA_PECS);
    	dlg.setHeaderText(CURSO_PECS);
    	dlg.setContentText(CURSO);
    	
    	return dlg.showAndWait();
    }
    
    public File getFolderFromTipo(Optional<String> tipo, boolean entrega) {
    	File folder = null;
    	File def = new File(this.home,CORREGIRPECS);
    	if (tipo.get().equals("ST1 - PEC1")) {
    		if (entrega) folder = new File(def, ST1_PEC1_descomprimidas);
    		else folder = new File(def, ST1_PEC1_comprimidas);
    	} else if (tipo.get().equals("ST1 - PEC2")) {
    		folder = new File(def, ST1_PEC2_originales);
    	} else if (tipo.get().equals("ST2")) {
    		folder = new File(def, ST2_originales);
    	}
    	
    	return folder;
    }

    public File getExtractFolderFromTipo(Optional<String> tipo) {
    	File folder = null;
    	File def = new File(this.home,CORREGIRPECS);
    	if (tipo.get().equals("ST1 - PEC1")) {
    		folder = new File(def, ST1_PEC1_descomprimidas);
    	}
    	
    	return folder;
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) this.search.getScene().getWindow();
        stage.close();
    }    

}
