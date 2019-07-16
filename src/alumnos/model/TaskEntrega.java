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

    public TaskEntrega(getAlumnosData d, String periodo, String tipo, File folder) {
        this.d = d;
        this.periodo = periodo;
        this.folder = folder;
        this.tipo = tipo;        
    }
    
	@Override
	protected String call() throws Exception {
        int iter = 1;
        StringBuilder problems = new StringBuilder("");
        
		if (this.tipo.equals("ST1 - PEC2") || 
				this.tipo.equals("ST2")) {
			// ST1 PEC2 & ST2: PDF form only PECs
			// get PDF files from folder
			FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
	        File[] pecs = this.folder.listFiles(pdfFilter);

	        // loop through the PEC files
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
	                        if (this.tipo.equals("ST1 - PEC2")) p.setNPEC(2);
	                        if (this.tipo.equals("ST2")) p.setNPEC(1);
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

		return problems.toString();
	}
}
