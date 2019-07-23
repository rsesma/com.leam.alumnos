package alumnos.model;


public class Pregunta {
    private String periodo;
    private String curso;
    private String pregunta;
    private int tipo;
    private String rescor;
    private float w;
    private Integer numopc;
    private int accion;
    private String datos;
    private String extra;
    private boolean changed;
            
    public enum TipoPregunta {
        LIBRE(1, "Libre"), CERRADA(2, "Cerrada"), NUM(3, "Numérica");
    	
        private int code;
        private String descrip;
    	
        TipoPregunta(int code, String descrip) {
            this.code = code;
            this.descrip = descrip;
        }
        
        public int getCode() {
            return this.code;					// getter for code
        }
        
        public String getDescrip() {
            return this.descrip;				// getter for descrip
        }
        
        public static TipoPregunta getByCode(int tipoCode) {
            for (TipoPregunta t : TipoPregunta.values()) {
                if (t.getCode() == tipoCode) {
                    return t;
                }
            }
            return null;
        }
        
        public static String getDescripByCode(int tipoCode) {
            for (TipoPregunta t : TipoPregunta.values()) {
                if (t.getCode() == tipoCode) {
                    return t.getDescrip();
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return this.descrip;
        }
    }

    public enum Accion {
    	LEER(1, "Leer"), TEST(2, "Test"), DATOS(3, "Datos"), IGNORAR(4, "Ignorar");
    	
        private int code;
        private String descrip;
    	
        Accion(int code, String descrip) {
            this.code = code;
            this.descrip = descrip;
        }
        
        public int getCode() {
            return this.code;					// getter for code
        }
        
        public String getDescrip() {
            return this.descrip;				// getter for descrip
        }
        
        public static Accion getByCode(int acCode) {
            for (Accion a : Accion.values()) {
                if (a.getCode() == acCode) {
                    return a;
                }
            }
            return null;
        }
        
        public static String getDescripByCode(int acCode) {
            for (Accion a : Accion.values()) {
                if (a.getCode() == acCode) {
                    return a.getDescrip();
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return this.descrip;
        }
    }

    public Pregunta() {
    	this.setChanged(false);
    }
    
	public String getPeriodo() {
		return periodo;
	}


	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}


	public String getCurso() {
		return curso;
	}


	public void setCurso(String curso) {
		this.curso = curso;
	}


	public String getPregunta() {
		return pregunta;
	}


	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public int getTipo() {
		return this.tipo;
	}
	
	public String getTipoAsDescrip() {
		return TipoPregunta.getDescripByCode(this.tipo);
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public String getRescor() {
		return rescor;
	}


	public void setRescor(String rescor) {
		this.rescor = rescor;
	}


	public float getW() {
		return w;
	}


	public void setW(float w) {
		this.w = w;
	}


	public Integer getNumopc() {
		return numopc;
	}


	public void setNumopc(Integer numopc) {
		this.numopc = numopc;
	}


	public int getAccion() {
		return accion;
	}


	public void setAccion(int accion) {
		this.accion = accion;
	}


	public String getDatos() {
		return datos;
	}


	public void setDatos(String datos) {
		this.datos = datos;
	}


	public String getExtra() {
		return extra;
	}


	public void setExtra(String extra) {
		this.extra = extra;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
