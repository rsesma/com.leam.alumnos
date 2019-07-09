/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    final ObservableList<Alumno> data = FXCollections.observableArrayList();
    
    getAlumnosData d;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
}
