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
    
    final ObservableList<Alumno> data = FXCollections.observableArrayList();
    
    getAlumnosData d;
    
/*    
  	private static final String CORREGIRPECS = "/CorregirPECs/";
    private static final String PEC1_comprimidas = "/CorregirPECs/ST1/PEC1/comprimidas";
    private static final String PEC1_originales = "/CorregirPECs/ST1/PEC1/originales";
    private static final String PEC2_originales = "/CorregirPECs/ST1/PEC2/originales";
    private final File home = new File(System.getProperty("user.home"));
*/
    
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
/*        String filter = "";
        if (!this.search.getText().trim().isEmpty()) {
            filter = "Periodo = '".concat(this.search.getText()).concat("'");
            filter = filter.concat("OR Grupo = '").concat(this.search.getText()).concat("'");
            filter = filter.concat("OR DNI LIKE '%").concat(this.search.getText()).concat("%'");
            filter = filter.concat("OR nom LIKE '%").concat(this.search.getText()).concat("%'");
            this.data.removeAll(this.data);
            LoadAlumnosTable(filter);
        }*/
    }

    @FXML
    public void pbClean(ActionEvent event) {
/*        this.data.removeAll(this.data);
        this.search.setText("");
        LoadAlumnosTable("");*/
    }

    @FXML
    public void mnuImportar(ActionEvent event) {
/*        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir archivo de datos");
        chooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Indicar periodo");
            dialog.setHeaderText("Indicar periodo");
            dialog.setContentText("Periodo:");
            Optional<String> periodo = dialog.showAndWait();
            if (periodo.isPresent()){
                try {
                    FileInputStream input = new FileInputStream(file.getAbsolutePath());
                    HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(input));
                    HSSFSheet sheet = wb.getSheetAt(0);
                    org.apache.poi.ss.usermodel.Row row;
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        row = sheet.getRow(i);
                        this.d.importExcelRow(row, periodo.get());
                    }
                    input.close();
                    
                    String filter = "";
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Importación finalizada");
                    alert.setHeaderText(null);
                    alert.setContentText("Importación finalizada.\n¿Visualizar el periodo?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) filter = "Periodo = '" + periodo.get() + "'";
                    
                    this.data.removeAll(this.data);
                    LoadAlumnosTable(filter);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.showAndWait();
                }
            }
        }*/
    }
        
    @FXML
    public void mnuDescomprimirPEC1(ActionEvent event) {
/*        DirectoryChooser chooser;
        File def;
        
        // get zip folder
        chooser = new DirectoryChooser();
        chooser.setTitle("Escoger carpeta PEC1 comprimidas");
        def = new File(this.home, PEC1_comprimidas);
        chooser.setInitialDirectory(def);
        File zips = chooser.showDialog(null);
        
        if (zips != null) {
            // get unzip folder
            chooser = new DirectoryChooser();
            chooser.setTitle("Escoger carpeta PEC1 originales");
            def = new File(this.home, PEC1_originales);
            chooser.setInitialDirectory(def);
            File unzip = chooser.showDialog(null);
            if (unzip != null) {
                // get all zip files
                FilenameFilter pdfFilter;
                pdfFilter = (File dir1, String name) -> {
                    String lowercaseName = name.toLowerCase();
                    return lowercaseName.endsWith(".zip");
                };
                File[] listOfFiles = zips.listFiles(pdfFilter);

                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        byte[] buffer = new byte[1024];
                        
                        // get DNI from file name
                        String n = file.getName();
                        String dni = n.substring(n.lastIndexOf("_")+1,n.lastIndexOf("."));
                        
                        // create output directory if it doesn't exists
                        File dir = new File(unzip.getAbsolutePath(), dni);
                        if (!dir.exists()) dir.mkdir();
                        
                        try {
                            // get the zip file content
                            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
                            //get the zipped file list entry
                            ZipEntry ze = zis.getNextEntry();
                            while (ze!=null) {
                                String fileName = ze.getName();
                                File newFile = new File(dir + File.separator + fileName);

                                // create all non exists folders else you will hit FileNotFoundException for compressed folder
                                new File(newFile.getParent()).mkdirs();

                                FileOutputStream fos = new FileOutputStream(newFile);             
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                                fos.close();
                                
                                ze = zis.getNextEntry();
                            }
                            zis.closeEntry();
                            zis.close();
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                            alert.showAndWait();
                        }
                    }
                }
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proceso finalizado");
                alert.showAndWait();
            }
        }*/
    }
    
    @FXML
    public void mnuEntregaPEC1(ActionEvent event) {
/*        // get PECs folder
        DirectoryChooser chooser = new DirectoryChooser();
        File def = new File(this.home, PEC1_originales);
        chooser.setTitle("Escoger carpeta PEC1 comprimidas");
        chooser.setInitialDirectory(def);
        File dir = chooser.showDialog(null);
        if (dir != null) {
            // get PEC1 folders
            File[] folders = dir.listFiles();
            for (File folder : folders) {
                if (folder.isDirectory()) {
                    String dni = folder.getName();
            
                    // get list of files for the dni and confirm PEC1 elements
                    boolean foundMdb = false;
                    boolean foundPdf = false;
                    boolean honor = false;
                    File[] listOfFiles = folder.listFiles();
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            String ext = file.getName().toLowerCase().substring(file.getName().lastIndexOf(".")+1);     //file extension
                            
                            // there's a database
                            if (ext.equals("mdb") || ext.equals("accdb") || ext.equals("odb")) foundMdb = true;
                            
                            // there's a pdf form file
                            if (ext.equals("pdf")) {
                                foundPdf = true;                                
                                // open pdf file
                                try {
                                    PdfReader reader = new PdfReader(file.getAbsolutePath());
                                    AcroFields form = reader.getAcroFields();
                                    // get honor field
                                    if (form.getFields().size()>0) honor = (form.getField("HONOR").equalsIgnoreCase("yes"));
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                    
                    this.d.entregaPEC1(dni, foundMdb, foundPdf, honor);
                }
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proceso finalizado");
            alert.showAndWait();
        }*/
    }
    
    @FXML
    public void mnuEntregaPEC(ActionEvent event) {
/*        // get periodo
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Indicar periodo");
        dialog.setHeaderText("Indicar periodo");
        dialog.setContentText("Periodo:");
        Optional<String> periodo = dialog.showAndWait();
        if (periodo.isPresent()){
            System.out.println(periodo);
            
            // get PECs folder
            DirectoryChooser chooser = new DirectoryChooser();
            File def = new File(this.home, PEC2_originales);
            chooser.setTitle("Escoger carpeta PECs");
            chooser.setInitialDirectory(def);
            File dir = chooser.showDialog(null);
            if (dir != null) {
                // get the PEC files of dir
                FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
                File[] pecs = dir.listFiles(pdfFilter);

                // loop through the PEC files
                StringBuilder problems = new StringBuilder("");
                boolean lproblems = false;
                for (File pec : pecs) {
                    if (pec.isFile()) {
                        String n = pec.getName();
                        String dni = n.substring(n.lastIndexOf("_")+1,n.lastIndexOf(".pdf"));      //student's dni
                        String curso = n.substring(5,8);

                        try {
                            boolean honor = false;
                            PdfReader reader = new PdfReader(pec.getAbsolutePath());
                            AcroFields form = reader.getAcroFields();
                            String prod = reader.getInfo().get("Producer");
                            if (prod.toUpperCase().contains("LibreOffice".toUpperCase()) |
                                    form.getFields().size()>0) {
                                honor = (form.getField("HONOR").equalsIgnoreCase("yes"));   //get honor field
                                this.d.entregaPEC(dni, curso, periodo.get(), honor);
                            } else {
                                lproblems = true;
                                problems.append(dni + " ; " + prod + "\n");      //the pdf is not readable
                            }
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage());
                            alert.showAndWait();
                        }
                    }
                }
                
                if (lproblems) {
                    Toolkit.getDefaultToolkit()
                        .getSystemClipboard()
                        .setContents(new StringSelection(problems.toString()),null);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, problems.toString());
                    alert.setTitle("Problemas");
                    alert.setHeaderText("Se encontraron PECs con problemas (copiado al portapapeles)");
                    alert.showAndWait();
                }
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proceso finalizado");
                alert.showAndWait();
            }
        }*/
    }

    @FXML
    public void mnuProblemasPEC(ActionEvent event) {
/*        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLproblemas.fxml"));
            Parent r = (Parent) fxml.load();            
            Stage stage = new Stage(); 
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(new Scene(r));
            stage.setTitle("Problemas PEC");
            FXMLproblemasController probl = fxml.<FXMLproblemasController>getController();
            probl.SetData(this.d,false);
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    
    @FXML
    public void mnuProblemasPEC1(ActionEvent event) {
/*        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLproblemas.fxml"));
            Parent r = (Parent) fxml.load();            
            Stage stage = new Stage(); 
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(new Scene(r));
            stage.setTitle("Problemas PEC1");
            FXMLproblemasController probl = fxml.<FXMLproblemasController>getController();
            probl.SetData(this.d,true);
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    
    @FXML
    public void mnuSintaxis(ActionEvent event) {
/*        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLsintaxis.fxml"));
            Parent r = (Parent) fxml.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(r));
            stage.setTitle("Definir Exportación de Sintaxis");
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    
    @FXML
    public void mnuCorregir(ActionEvent event) {
 /*       try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLcorregir.fxml"));
            Parent r = (Parent) fxml.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(r));
            stage.setTitle("Corregir");
            FXMLcorregirController corr = fxml.<FXMLcorregirController>getController();
            corr.SetData(this.d);
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }*/
    }

    @FXML
    public void mnuCorregirPEC1(ActionEvent event) {
/*        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXMLcorregirPEC1.fxml"));
            Parent r = (Parent) fxml.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(r));
            stage.setTitle("Corregir PEC1");
            FXMLcorregirPEC1Controller corr = fxml.<FXMLcorregirPEC1Controller>getController();
            corr.SetData(this.d);
            stage.showAndWait();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    
    @FXML
    public void mnuExport(ActionEvent event) {
/*        // get periodo
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Indicar periodo");
        dialog.setHeaderText("Indicar periodo");
        dialog.setContentText("Periodo:");
        Optional<String> periodo = dialog.showAndWait();
        if (periodo.isPresent()){
            // get PECs folder
            DirectoryChooser chooser = new DirectoryChooser();
            File def = new File(this.home, CORREGIRPECS);
            chooser.setTitle("Escoger carpeta PECs");
            chooser.setInitialDirectory(def);
            File dir = chooser.showDialog(null);
            if (dir != null) {
                // get the PEC files of dir
                FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
                File[] pecs = dir.listFiles(pdfFilter);

                // get curso from first pdf file
                String curso = "";
                for (File pec : pecs) {
                    if (pec.isFile()) {
                        curso = pec.getName().substring(5,8);
                        break;
                    }
                }
                
                // loop through the PEC files
                try {
                    ResultSet rs = this.d.getPreguntasRs(periodo.get(),curso);
                    List<String> lines = new ArrayList<>();
                    for (File pec : pecs) {
                        if (pec.isFile()) {
                            String n = pec.getName();
                            String dni = n.substring(pec.getName().lastIndexOf("_")+1,n.lastIndexOf(".pdf"));      //student's dni

                            PdfReader reader = new PdfReader(pec.getAbsolutePath());
                            AcroFields form = reader.getAcroFields();
                            //Header with identification data
                            String c = "'" + form.getField("APE1") + "','" + form.getField("APE2") + "','" + 
                                    form.getField("NOMBRE") + "','" + dni + "'";

                            rs.beforeFirst();
                            while(rs.next()){
                                c = c + ",'" + form.getField("P"+rs.getString("pregunta")).replace(".", ",") + "'";
                            }
                            lines.add(c);
                            reader.close();
                        }
                    }
                    rs.close();
                    
                    Path fdata = Paths.get(dir + "/datos_pecs.txt");
                    Files.write(fdata, lines, Charset.forName("UTF-8"));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage());
                    alert.showAndWait();
                }
            }
                
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proceso finalizado");
            alert.showAndWait();
        }*/
    }
    
    public void SetData(getAlumnosData d) {
        this.d = d;
        LoadAlumnosTable("");
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
