package alumnos.model;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class EntregaTask extends Task<String> {
	private final getAlumnosData d;
	private final String periodo;
	private final File folder;

    public EntregaTask(getAlumnosData d, String periodo, File folder) {
        this.d = d;
        this.periodo = periodo;
        this.folder = folder;
    }
    
	@Override
	protected String call() throws Exception {
        // get PDF files from folder
		FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
        File[] pecs = folder.listFiles(pdfFilter);
        
        // loop through the PEC files
        int iter = 0;
        int total = pecs.length;
        StringBuilder problems = new StringBuilder("");
        for (File pec : pecs) {
        	if (pec.isFile()) {
                String n = pec.getName();
                String dni = n.substring(n.lastIndexOf("_")+1,n.indexOf(".pdf"));      //student's dni
                String curso = n.substring(n.indexOf("_")+1,n.lastIndexOf("_"));
                
                // open pdf form, check for problems & HONOR
                try {
    				PDDocument pdf = PDDocument.load(pec);
    			    PDAcroForm form = pdf.getDocumentCatalog().getAcroForm();
    			    String producer = pdf.getDocumentInformation().getProducer();		// get form producer
    			    		        			    
                    if (producer.toUpperCase().contains("LibreOffice".toUpperCase()) ||
                            form.getFields().size()>0) {
                        //get honor field
                        PDCheckBox honor = (PDCheckBox) form.getField("HONOR");
                        this.d.entregaPEC(dni, curso, periodo, honor.isChecked());
                    } else {
                    	// if the producer is not LibreOffice or there are no fields, the PDF file may be corrupted
                        problems.append(dni + " ; " + producer + "\n");      // there may be problems with the pdf 
                    }
                    
                    updateMessage("Iteración " + iter);
                    updateProgress(iter,total);
                    
                    // close pdf form
                    pdf.close();
                    pdf = null;
                    form = null;
                    
                    iter = iter + 1;
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.showAndWait();
                }
        	}
        }

		return problems.toString();
	}
}
