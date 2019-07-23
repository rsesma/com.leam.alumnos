package alumnos;


import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import alumnos.model.Pregunta;
import alumnos.model.Pregunta.Accion;
import alumnos.model.getAlumnosData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class FXMLestructuraController implements Initializable {

    @FXML
    private TableView<Pregunta> table;
    @FXML
    private TableColumn<Pregunta, String> periodoCol;
    @FXML
    private TableColumn<Pregunta, String> cursoCol;
    @FXML
    private TableColumn<Pregunta, String> preguntaCol;
    @FXML
    private TableColumn<Pregunta, String> tipoCol;
    @FXML
    private TableColumn<Pregunta, String> rescorCol;
    @FXML
    private TableColumn<Pregunta, Integer> nopcCol;
    @FXML
    private TableColumn<Pregunta, Float> wCol;
    @FXML
    private TableColumn<Pregunta, Accion> accionCol;
	@FXML
    private TableColumn<Pregunta, String> datosCol;
	@FXML
    private TableColumn<Pregunta, String> extraCol;
    @FXML
    private TextField periodo;
    @FXML
    private TextField curso;

    final ObservableList<Pregunta> data = FXCollections.observableArrayList();
    getAlumnosData d;
    
    private static final String ATENCION = "Atención";
    private static final String ERROR = "Error";
    private static final String INDICAR_PERIODO_CURSO = "Indicar periodo y curso";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.table.setEditable(true);
        
        // Set up the alumnos table
        this.periodoCol.setCellValueFactory(new PropertyValueFactory<>("Periodo"));
        this.cursoCol.setCellValueFactory(new PropertyValueFactory<>("Curso"));
        this.preguntaCol.setCellValueFactory(new PropertyValueFactory<>("Pregunta"));
        this.tipoCol.setCellValueFactory(new PropertyValueFactory<>("TipoAsDescrip"));
        this.nopcCol.setCellValueFactory(new PropertyValueFactory<>("Numopc"));
        this.wCol.setCellValueFactory(new PropertyValueFactory<>("W"));
        
        // respuesta correcta is editable
        this.rescorCol.setCellValueFactory(new PropertyValueFactory<>("Rescor"));
        this.rescorCol.setCellFactory(TextFieldTableCell.<Pregunta> forTableColumn());
        // respuesta correcta: On Cell edit commit
        this.rescorCol.setOnEditCommit((CellEditEvent<Pregunta, String> event) -> {
            int row = event.getTablePosition().getRow();
            Pregunta p = event.getTableView().getItems().get(row); 
            p.setRescor(event.getNewValue());
            p.setChanged(true);
        });
        
        // datos is editable
        this.datosCol.setCellValueFactory(new PropertyValueFactory<>("Datos"));
        this.datosCol.setCellFactory(TextFieldTableCell.<Pregunta> forTableColumn());
        // datos: On Cell edit commit
        this.datosCol.setOnEditCommit((CellEditEvent<Pregunta, String> event) -> {
            int row = event.getTablePosition().getRow();
            Pregunta p = event.getTableView().getItems().get(row); 
            p.setDatos(event.getNewValue());
            p.setChanged(true);
        });
        
        // extra is editable
        this.extraCol.setCellValueFactory(new PropertyValueFactory<>("Extra"));
        this.extraCol.setCellFactory(TextFieldTableCell.<Pregunta> forTableColumn());
        // extra: On Cell edit commit
        this.extraCol.setOnEditCommit((CellEditEvent<Pregunta, String> event) -> {
            int row = event.getTablePosition().getRow();
            Pregunta p = event.getTableView().getItems().get(row); 
            p.setExtra(event.getNewValue());
            p.setChanged(true);
        });
        
        // accion is editable: combo box
        // load combo box values
        ObservableList<Accion> acList = FXCollections.observableArrayList(Accion.values());
        this.accionCol.setCellValueFactory(new Callback<CellDataFeatures<Pregunta, Accion>, ObservableValue<Accion>>() {        	 
            @Override
            public ObservableValue<Accion> call(CellDataFeatures<Pregunta, Accion> param) {
                Pregunta p = param.getValue();
                // 0,1,2,3 values
                int acCode = p.getAccion();
                Accion a = Accion.getByCode(acCode);
                return new SimpleObjectProperty<Accion>(a);
            }
        });
        this.accionCol.setCellFactory(ComboBoxTableCell.forTableColumn(acList));
        // on edit commit
        this.accionCol.setOnEditCommit((CellEditEvent<Pregunta, Accion> event) -> {
        	int row = event.getTablePosition().getRow();
            Accion a = event.getNewValue();
            Pregunta p = event.getTableView().getItems().get(row);
            p.setAccion(a.getCode());
            p.setChanged(true);
        });
        
        
        table.setItems(this.data);
    }
    
    @FXML
    void pbImportar(ActionEvent event) {
    	String per =this.periodo.getText().trim();
    	String cur =this.curso.getText().trim();
    	if (per.length()>0 && cur.length()>0) {
	    	// get the txt definition file
	    	FileChooser fc = new FileChooser();
	    	fc.setInitialDirectory(new File(System.getProperty("user.home")));
	    	fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        File file = fc.showOpenDialog(null);
	        if (file != null) {
	      	    // read txt data
	        	List<String> lines = Collections.emptyList();
	    	    try { 
	    	    	lines = Files.readAllLines(Paths.get(file.getAbsolutePath()),StandardCharsets.UTF_8); 
	    	    } catch (Exception e) { 
	    	    	this.showAlert(Alert.AlertType.ERROR, ERROR, e.getMessage());
	    	    }
	    	    
	    	    this.data.removeAll(this.data);
	    	    // load data to tableview
	    	    Iterator<String> itr = lines.iterator();
	    	    itr.next();
	    	    while (itr.hasNext()) { 
		    		String[] tokens = itr.next().split(",");
		    		
		    		Pregunta p = new Pregunta();
		    		p.setPeriodo(per);
		    		p.setCurso(cur);
		    		p.setPregunta(tokens[1].replace("'","").replace("P",""));
		    		p.setTipo(Integer.parseInt(tokens[2]));
		    		p.setW(Float.parseFloat(tokens[4]));
		    		if (!tokens[5].equals("null")) p.setNumopc(Integer.parseInt(tokens[5]));
		    		p.setChanged(true);
		    		
		    		this.data.add(p);
	    	    } 
	        }
    	} else {
        	this.showAlert(Alert.AlertType.WARNING, ATENCION, INDICAR_PERIODO_CURSO);
        }
    }

    @FXML
    void pbBuscar(ActionEvent event) {
    	String p =this.periodo.getText().trim();
    	String c =this.curso.getText().trim();
    	if (p.length()>0 && c.length()>0) {
	        this.data.removeAll(this.data);
	        LoadTable("Periodo = '" + p + "' AND Curso = '" + c + "'");
        } else {
        	this.showAlert(Alert.AlertType.WARNING, ATENCION, INDICAR_PERIODO_CURSO);
        }
    }

    @FXML
    void pbLimpiar(ActionEvent event) {
        this.data.removeAll(this.data);
        LoadTable("");
    }
    
    @FXML
    void pbGrabar(ActionEvent event) {
        this.data.forEach((p) -> { 
            if (p.isChanged()) {
            	System.out.println(p.getPregunta());
            }
        });
    }
    
    
    public void SetData(getAlumnosData d) {
        this.d = d;
        LoadTable("");
    }
    
    public void LoadTable(String filter) {
        try{
            ResultSet rs = this.d.getRS("*", "pec_estructura", filter, "Periodo, Curso, pregunta");
            while(rs.next()){
                Pregunta p = new Pregunta();
                p.setPeriodo(rs.getString("Periodo"));
                p.setCurso(rs.getString("Curso"));
                p.setPregunta(rs.getString("pregunta"));
                p.setTipo(rs.getInt("tipo"));
                p.setRescor(rs.getString("rescor"));
                p.setW(rs.getFloat("w"));
                p.setNumopc(rs.getInt("numopc"));
                p.setAccion(rs.getInt("accion"));
                p.setDatos(rs.getString("datos"));
                p.setExtra(rs.getString("extra"));
                
                this.data.add(p);
            }
        } catch(SQLException e){
        	this.showAlert(Alert.AlertType.ERROR, ERROR, e.getMessage());
        }
    }
    
    public void showAlert(Alert.AlertType t, String title, String text) {
    	Alert alert = new Alert(t);
        alert.setTitle(title);
        alert.setContentText(null);
		alert.setHeaderText(text);
    	alert.showAndWait();        	    	
    }
}
