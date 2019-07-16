/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import alumnos.model.EntPEC;
import alumnos.model.getAlumnosData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author R
 */
public class FXMLproblemasController implements Initializable {

    @FXML
    TableView<EntPEC> table;
    @FXML
    private TableColumn<EntPEC,String> periodoCol;
    @FXML
    private TableColumn<EntPEC,String> cursoCol;
    @FXML
    private TableColumn<EntPEC,String> grupoCol;
    @FXML
    private TableColumn<EntPEC,Integer> npecCol;
    @FXML
    private TableColumn<EntPEC,String> dniCol;
    @FXML
    private TableColumn<EntPEC,String> nombreCol;
    @FXML
    private TableColumn<EntPEC,Boolean> honorCol;
    @FXML
    private TableColumn<EntPEC,Boolean> mdbCol;
    @FXML
    private TableColumn<EntPEC,Boolean> pdfCol;
    @FXML
    private TableColumn<EntPEC,String> emailCol;
    @FXML
    TextField search;
    @FXML
    Button btSearch;
    @FXML
    Button btClean;

    final ObservableList<EntPEC> data = FXCollections.observableArrayList();
    
    getAlumnosData d;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
        this.table.setItems(this.data);
        this.table.setEditable(true);
        
        // Set up the alumnos table
        this.periodoCol.setCellValueFactory(new PropertyValueFactory<>("Periodo"));
        this.cursoCol.setCellValueFactory(new PropertyValueFactory<>("Curso"));
        this.grupoCol.setCellValueFactory(new PropertyValueFactory<>("Grupo"));
        this.npecCol.setCellValueFactory(new PropertyValueFactory<>("NPEC"));
        this.dniCol.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        this.nombreCol.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));

        // checkbox Extra
        this.mdbCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntPEC, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EntPEC, Boolean> param) {
                EntPEC p = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(p.getMDB());
                // when column change
                booleanProp.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) { 
                        p.setMDB(newValue);
                        p.setChanged(true);
                    }
                });
                return booleanProp;
            }
        });
        this.mdbCol.setCellFactory(new Callback<TableColumn<EntPEC, Boolean>, TableCell<EntPEC, Boolean>>() {
            @Override
            public TableCell<EntPEC, Boolean> call(TableColumn<EntPEC, Boolean> p) {
                CheckBoxTableCell<EntPEC, Boolean> cell = new CheckBoxTableCell<EntPEC, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        // checkbox PDF
        this.pdfCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntPEC, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EntPEC, Boolean> param) {
                EntPEC p = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(p.getPDF());
                // when column change
                booleanProp.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) { 
                        p.setPDF(newValue);
                        p.setChanged(true);
                    }
                });
                return booleanProp;
            }
        });
        this.pdfCol.setCellFactory(new Callback<TableColumn<EntPEC, Boolean>, TableCell<EntPEC, Boolean>>() {
            @Override
            public TableCell<EntPEC, Boolean> call(TableColumn<EntPEC, Boolean> p) {
                CheckBoxTableCell<EntPEC, Boolean> cell = new CheckBoxTableCell<EntPEC, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        // checkbox HONOR
        this.honorCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntPEC, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EntPEC, Boolean> param) {
                EntPEC p = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(p.getHonor());
                // when column change
                booleanProp.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) { 
                        p.setHonor(newValue);
                        p.setChanged(true);
                    }
                });
                return booleanProp;
            }
        });
        this.honorCol.setCellFactory(new Callback<TableColumn<EntPEC, Boolean>, TableCell<EntPEC, Boolean>>() {
            @Override
            public TableCell<EntPEC, Boolean> call(TableColumn<EntPEC, Boolean> p) {
                CheckBoxTableCell<EntPEC, Boolean> cell = new CheckBoxTableCell<EntPEC, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
    }
    
    @FXML
    public void pbSearch(ActionEvent event) {
        String filter = "";
        if (!this.search.getText().trim().isEmpty()) {
            filter = "Periodo = '".concat(this.search.getText()).concat("'");
            filter = filter.concat("OR Curso = '").concat(this.search.getText()).concat("'");
            filter = filter.concat("OR Grupo = '").concat(this.search.getText()).concat("'");
            filter = filter.concat("OR DNI LIKE '%").concat(this.search.getText()).concat("%'");
            filter = filter.concat("OR nom LIKE '%").concat(this.search.getText()).concat("%'");
            this.data.removeAll(this.data);
            LoadProblemasTable(filter);
        }
    }

    @FXML
    public void pbClean(ActionEvent event) {
        this.data.removeAll(this.data);
        this.search.setText("");
        LoadProblemasTable("");
    }

    @FXML
    public void pbCopiarMail(ActionEvent event) {
        StringBuilder mails = new StringBuilder("");
        this.data.forEach((p) -> { 
            mails.append(p.getEmail().concat(", "));
        });
        
        Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .setContents(new StringSelection(mails.toString()),null);
    }
    
    @FXML
    public void pbGrabar(ActionEvent event) {
        this.data.forEach((p) -> { 
            if (p.getChanged()) this.d.updateEntregaPEC(p);
        });
        this.data.removeAll(this.data);
        LoadProblemasTable("");
    }
    
    public void SetData(getAlumnosData d) {
        this.d = d;
        LoadProblemasTable("");
    }
    
    public void LoadProblemasTable(String filter) {
        try{
            ResultSet rs = this.d.getProblemasPEC(filter);
            while(rs.next()){
                EntPEC p = new EntPEC();
                p.setPeriodo(rs.getString("Periodo"));
                p.setCurso(rs.getString("Curso"));
                p.setGrupo(rs.getString("Grupo"));
                p.setNPEC(rs.getInt("npec"));
                p.setDNI(rs.getString("DNI"));
                p.setNombre(rs.getString("nom"));
                p.setHonor(rs.getBoolean("honor"));
                p.setEmail(rs.getString("email"));
                if (p.isMultiple()) {
	                p.setMDB(rs.getBoolean("mdb"));
	                p.setPDF(rs.getBoolean("pdf"));
                } else {
	                p.setMDB(false);
	                p.setPDF(false);                	
                }
                
                this.data.add(p);
            }
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
    
}
