package alumnos.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class TaskExtract extends Task<Void> {
    
	private final File zips;
	private final File extract;
	
	public TaskExtract(File zips, File extract) {
		this.zips = zips;
		this.extract = extract;
    }
    
	@Override
	protected Void call() throws Exception {
        int iter = 1;

		FilenameFilter zipFilter = (File dir1, String name) -> name.toLowerCase().endsWith(".zip");
        File[] pecs = this.zips.listFiles(zipFilter);
		FileSystem FS = FileSystems.getDefault();
        
        int total = pecs.length;
        for (File pec : pecs) {
        	if (pec.isFile()) {
        		ZipFile file;
        		try {
        			// create unzip folder to extract
        			String n = pec.getName();
                    String dni = n.substring(n.lastIndexOf("_")+1,n.lastIndexOf("."));
                    File unzip = new File(this.extract, dni);
                    if (!unzip.exists()) unzip.mkdir();
                    
                    // extract zip contents
        			file = new ZipFile(pec);
                    Enumeration<? extends ZipEntry> entries = file.entries();		//Get file entries
                    while (entries.hasMoreElements()) {
                    	ZipEntry entry = entries.nextElement();
                    	if (!entry.isDirectory()) {
	                    	String name = entry.getName();
	                    	name = name.substring(name.lastIndexOf("/") + 1);
	                    	if (!name.startsWith(".")) {
	                            BufferedInputStream bis = new BufferedInputStream(file.getInputStream(entry));
	                            File extractFile = new File(unzip,name);
	                            Files.createFile(FS.getPath(extractFile.getAbsolutePath()));
	                            FileOutputStream fileOutput = new FileOutputStream(extractFile.getAbsolutePath());
	                            while (bis.available() > 0) {
	                                fileOutput.write(bis.read());
	                            }
	                            fileOutput.close();
	                    	}
                    	}                    	
        	        }
                
	                updateProgress(iter,total);
	                iter = iter + 1;
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    alert.showAndWait();
                }
        	}
        }
		
        // Return null at the end of a Task of type Void
        return null;
	}
}
