package alumnos.model;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import alumnos.model.Pregunta.Accion;
import alumnos.model.Pregunta.TipoPregunta;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class TaskSintaxis extends Task<Void> {
	private final ArrayList<Pregunta> p;
	private final File orig;
	private final File sint;
	private final File cd;

	private static final String STATA_EXEC = "C:\\Program Files\\Stata16\\StataSE-64.exe";
	
	public TaskSintaxis(ArrayList<Pregunta> p, File orig, File sint, File cd) {
        this.p = p;
        this.orig = orig;
        this.sint = sint;
        this.cd = cd;
    }
	
	@Override
	protected Void call() throws Exception {
		// PDF form only PECs: get PDF files from folder 
		FilenameFilter pdfFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".pdf");
        File[] pecs = this.orig.listFiles(pdfFilter);

        // loop through the PEC files
        int iter = 1;
        int total = pecs.length;
        for (File pec : pecs) {
        	if (pec.isFile()) {
                String n = pec.getName();		// get info from file name
                String dni = n.substring(n.lastIndexOf("_") + 1, n.indexOf(".pdf"));	// dni
                List<String> lines = new ArrayList<>();
                lines.add("*** " + dni);
                lines.add("cd \"" + this.cd.getAbsolutePath() + "\"");
                
                // open pdf form and get sintaxis
                try {				
                	PDDocument pdf = PDDocument.load(pec);
                	PDAcroForm form = pdf.getDocumentCatalog().getAcroForm();

					for (Pregunta pr : this.p) {
						if (pr.getTipoPregunta() == TipoPregunta.LIBRE) {
							if (pr.getAccionAsEnum() != Accion.IGNORAR) {
								lines.add("\n** Pregunta " + pr.getPregunta());
								if (pr.getAccionAsEnum() == Accion.DATOS) {
									String file = pr.getDatos();
									String ext = file.substring(file.indexOf(".")+1);
									if (ext.equalsIgnoreCase("XLSX")) {
										lines.add("qui import excel \"" + file + "\", sheet(\"" + pr.getExtra() + "\") firstrow clear");
									}
									if (ext.equalsIgnoreCase("DTA")) {
										lines.add("qui use \"" + file + "\",  clear");
									}
								} else if (pr.getAccionAsEnum() == Accion.LEER || pr.getAccionAsEnum() == Accion.TEST) {
									PDTextField ed = (PDTextField) form.getField(pr.getPreguntaAsMemo());
									if (pr.getAccionAsEnum() == Accion.TEST) lines.add("quietly{");
									lines.add(ed.getValue());
									if (pr.getAccionAsEnum() == Accion.TEST) lines.add("}");
									if (pr.getAccionAsEnum() == Accion.TEST) {
										String dta = pr.getDatos().split(";")[0];		// dta is first token of Datos
										String id = pr.getDatos().split(";")[1];		// id is second token of Datos
										lines.add("\ncf3 " + pr.getExtra() + " using " + dta + ", id(" + id + ")");
									}
								} else if (pr.getAccionAsEnum() == Accion.GRAFICO) {
									PDTextField ed = (PDTextField) form.getField(pr.getPreguntaAsMemo());
									lines.add("quietly{");
									lines.add(ed.getValue());
									lines.add("}");
									lines.add("graph export \"" + sint.getAbsolutePath() + "/" + dni + ".png\", as(png)");
									lines.add("graph close _all");
								}
							}
						}
	            	}
					
                    // close pdf form
                    pdf.close();
                    pdf = null;
                    form = null;
                    
                    // write sintax file
                    Files.write(Paths.get(sint.getAbsolutePath() + "/" + dni + ".do"), lines, Charset.forName("UTF-8"));
                    
                    // execute syntax file and obtain results
            		String dofile = new File(sint,dni+".do").getAbsolutePath();
            		String cmd = String.format("\"%s\" /e /q do \"%s\", nostop", STATA_EXEC, dofile);
            		try {
            			Runtime rt = Runtime.getRuntime();
            			Process pr = rt.exec(cmd,null,sint);
            			pr.waitFor();
            		} catch(Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,e.toString());
                        alert.showAndWait();
            		}
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.showAndWait();
                }
                
                updateProgress(iter,total);
                iter = iter + 1;
        	}
        }

		// Return null at the end of a Task of type Void
		return null;
	}
	

}
