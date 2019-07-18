package alumnos.model;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class TaskEntrega extends Task<String> {
	private final getAlumnosData d;
	private final String periodo;
	private final File folder;
	private String tipo;
	StringBuilder problems = new StringBuilder("");
	
    public TaskEntrega(getAlumnosData d, String periodo, String tipo, File folder) {
        this.d = d;
        this.periodo = periodo;
        this.folder = folder;
        this.tipo = tipo;        
    }
    
	@Override
	protected String call() throws Exception {
        
		if (this.tipo.equals("ST1 - PEC2") || this.tipo.equals("ST2")) entregaPDF();
		if (this.tipo.equals("ST1 - PEC1")) entregaPEC1();

		return this.problems.toString();
	}
	
	private void entregaPDF() {
		// PDF form only PECs: get PDF files from folder 
		FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
        File[] pecs = this.folder.listFiles(pdfFilter);

        // loop through the PEC files
        int iter = 1;
        int total = pecs.length;
        for (File pec : pecs) {
        	if (pec.isFile()) {
                String n = pec.getName();		// get info from file name
                String dni = n.substring(n.lastIndexOf("_") + 1, n.indexOf(".pdf"));	// dni
                String curso = n.substring(n.indexOf("_") + 1, n.lastIndexOf("_"));		// curso
                
                // open pdf form, check for problems & HONOR
                try {
    				PDDocument pdf = PDDocument.load(pec);
    			    PDAcroForm form = pdf.getDocumentCatalog().getAcroForm();
    			    String producer = pdf.getDocumentInformation().getProducer();		// get form producer
    			    		        			    
                    if (producer.toUpperCase().contains("LibreOffice".toUpperCase()) ||
                            form.getFields().size()>0) {
                        //get honor field
                        PDCheckBox honor = (PDCheckBox) form.getField("HONOR");
                        
                        EntPEC p = new EntPEC();
                        p.setCurso(curso);
                        p.setPeriodo(periodo);
                        p.setDNI(dni);
                        p.setNPEC((this.tipo.equals("ST1 - PEC2") ? 2 : 1));
                        p.setHonor(honor.isChecked());

                        this.d.entregaPEC(p);
                    } else {
                    	// if the producer is not LibreOffice or there are no fields, the PDF file may be corrupted
                        problems.append(dni + " ; " + producer + "\n");      // there may be problems with the pdf 
                    }
                    pdf.close();		// close pdf form
                    pdf = null;
                    form = null;
                    
                    updateProgress(iter,total);
                    iter = iter + 1;
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.showAndWait();
                }
        	}
        }	
	}
	
	private void entregaPEC1() {
		// ST1 PEC1: get folders from working folder
        File[] folders = this.folder.listFiles();
        // loop through the PEC files
        int iter = 1;
        int total = folders.length;
        for (File folder : folders) {
            if (folder.isDirectory()) {
            	String dni = folder.getName();
            	
                // get list of files for the dni and confirm PEC1 elements
            	boolean foundMDB = false;
            	boolean foundPDF = false;
                boolean honor = false;
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                    	// file extension
                        String ext = file.getName().toLowerCase().substring(file.getName().lastIndexOf(".")+1); 
                        
                        // there's a database
                        if (ext.equals("mdb") || ext.equals("accdb") || ext.equals("odb")) foundMDB = true;
                        // there's a pdf form file
                        if (ext.equals("pdf")) {
                        	foundPDF = true;
                            // open pdf file & get honor
                            try {
                            	// get honor field
                				PDDocument pdf = PDDocument.load(file);
                			    PDAcroForm form = pdf.getDocumentCatalog().getAcroForm();
                                if (form.getFields().size()>0) {
                                    PDCheckBox cb = (PDCheckBox) form.getField("HONOR");
                                    honor = cb.isChecked();
                                }
                                pdf.close();		// close pdf form
                                pdf = null;
                                form = null;
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                                alert.showAndWait();
                            }
                        }
                    }
                }
                // entrega PEC
                EntPEC p = new EntPEC();
                p.setCurso("ST1");
                p.setPeriodo(periodo);
                p.setDNI(dni);
                p.setNPEC(1);
                p.setHonor(honor);
                p.setMDB(foundMDB);
                p.setPDF(foundPDF);
                this.d.entregaPEC(p);

                updateProgress(iter,total);
                iter = iter + 1;
            }
        }
	}
}
