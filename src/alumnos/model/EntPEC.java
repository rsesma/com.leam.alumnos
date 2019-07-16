/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos.model;

/**
 *
 * @author R
 */
public class EntPEC {
	private String curso;
	private String periodo;
	private String grupo;
    private String dni;
    private int npec;
    private Boolean mdb;
    private Boolean pdf;
    private Boolean honor;
    private String nombre;
    private String email;
    private Boolean changed;
    
    public EntPEC() {
        this.changed = false;
        this.mdb = null;
        this.pdf = null;
    }
    
    public String getCurso() {
        return this.curso;
    }

    public void setCurso(String c) {
        this.curso = c;
    }
    
    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String c) {
        this.periodo = c;
    }
    
    public int getNPEC() {
        return this.npec;
    }

    public void setNPEC(int n) {
        this.npec = n;
    }
    
    public String getGrupo() {
        return this.grupo;
    }

    public void setGrupo(String c) {
        this.grupo = c;
    }
        
    public String getDNI() {
        return this.dni;
    }

    public void setDNI(String c) {
        this.dni = c;
    }
    
    public Boolean getMDB() {
        return this.mdb;
    }

    public void setMDB(Boolean l) {
        this.mdb = l;
    }
    
    public Boolean getPDF() {
        return this.pdf;
    }

    public void setPDF(Boolean l) {
        this.pdf = l;
    }

    public Boolean getHonor() {
        return this.honor;
    }

    public void setHonor(Boolean l) {
        this.honor = l;
    }
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String c) {
        this.nombre = c;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String c) {
        this.email = c;
    }

    public Boolean getChanged() {
        return this.changed;
    }

    public void setChanged(Boolean l) {
        this.changed = l;
    }

    public Boolean isMultiple() {
        if (this.curso.equals("ST1") && this.npec==1) return true;
        else return false;
    }
}
