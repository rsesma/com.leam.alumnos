package alumnos;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import alumnos.model.Alumno;
import alumnos.model.getAlumnosData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;


public class FXMLcorregirController implements Initializable {
    
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
    TableColumn<Alumno, Boolean> entregaPEC1Col;
    @FXML
    TableColumn<Alumno, String> pec1Col;
    @FXML
    TableColumn<Alumno, Boolean> entregaPECCol;
    @FXML
    TableColumn<Alumno, String> pecCol;
    @FXML
    TableColumn<Alumno, String> notaCol;
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
    
    final ObservableList<Alumno> data = FXCollections.observableArrayList();
    
    private static final String PEC1_descomprimidas = "/PEC1/descomprimidas/";
    private static final String PEC1_corregidas = "/PEC1/corregidas/";
    
    String tipo = "";
    String periodo = "";
    File folder = null; 
    getAlumnosData d;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {    	
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
        
        // checkbox entregaPEC1
        this.entregaPEC1Col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Alumno, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Alumno, Boolean> param) {
                Alumno a = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(a.getEntregaPEC1());                
                return booleanProp;
            }
        });
        this.entregaPEC1Col.setCellFactory(new Callback<TableColumn<Alumno, Boolean>, TableCell<Alumno, Boolean>>() {
            public TableCell<Alumno, Boolean> call(TableColumn<Alumno, Boolean> p) {
                CheckBoxTableCell<Alumno, Boolean> cell = new CheckBoxTableCell<Alumno, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        // checkbox entregaPEC
        this.entregaPECCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Alumno, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Alumno, Boolean> param) {
                Alumno a = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(a.getEntregaPEC());                
                return booleanProp;
            }
        });
        this.entregaPECCol.setCellFactory(new Callback<TableColumn<Alumno, Boolean>, TableCell<Alumno, Boolean>>() {
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
    void mnuAbrir(ActionEvent event) {
    	
    }

    @FXML
    void mnuCorregir(ActionEvent event) {
    	
    }
    
    @FXML
    void mnuAbrirPEC1(ActionEvent event) {
        File orig = null;
        File dest = null;
        if (Desktop.isDesktopSupported()) {
            Alumno a = this.table.getItems().get(this.table.getSelectionModel().getSelectedIndex());
            if (a != null) {
                try {
                	orig = new File(this.folder,PEC1_descomprimidas.concat(a.getDNI()));
                	if (orig.exists()) {
                		dest = new File(this.folder,PEC1_corregidas.concat(a.getDNI()));
                		// copy to corregidas dir
                		FileUtils.copyDirectory(orig, dest);
                		// open files
                		File[] listOfFiles = dest.listFiles();
                        for (File file : listOfFiles) {
                            if (file.isFile()) {
                            	// file extension
                                String ext = file.getName().toLowerCase().substring(file.getName().lastIndexOf(".")+1);                                
                                // there's a database or a pdf form file
                                if (ext.equals("mdb") || ext.equals("accdb") || ext.equals("odb") || ext.equals("pdf")) {
                                	Desktop.getDesktop().open(file);
                                }
                            }
                        }
                	}
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    alert.showAndWait();
                }
            }
        }

    }

    @FXML
    void mnuCorregirPEC1(ActionEvent event) {
    	
    }

    @FXML
    public void pbSearch(ActionEvent event) {
    	
    }

    public void SetData(getAlumnosData d, String p, String t, File f) {
        this.d = d;
        this.periodo = p;
        this.tipo = t;
        this.folder = f;
        
        String filter = "Periodo = '" + this.periodo + "'" +
        		" AND Curso = '" + this.tipo.substring(0,3) + "'";
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
                a.setEntregaPEC1(rs.getBoolean("entregaPEC1"));
                a.setEntregaPEC(rs.getBoolean("entregaPEC"));
                
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
