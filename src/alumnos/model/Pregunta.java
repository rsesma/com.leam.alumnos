package alumnos.model;

public class Pregunta {
    private String periodo;
    private String curso;
    private String pregunta;
    private int tipo;
    private String rescor;
    private float w;
    private int numopc;
    private int accion;
    private String datos;
    private String extra;
            
    public enum TipoPregunta {
        LIBRE(1), CERRADA(2), NUM(3), DEFAULT;
    	
        private int value;				// int value
    	
        TipoPregunta() {}				// empty constructor for default value
        
        TipoPregunta(int value) {
            this.value = value;			// constructor with value
        }
        
        public int getValue() {
            return value;				// getter for value
        }
        
        public static TipoPregunta forValue(int value) {
        	// utility method to retrieve instance by int value
            // iterating values
            for (TipoPregunta n: values()) {
                if (n.getValue() == value) return n;		// matches argument
            }
            return DEFAULT;			// no match, returning DEFAULT
        }
    }

    public enum AccionPregunta {
        IGNORAR(0), DATOS(1), TEST(2), LEER(3), DEFAULT;
    	
        private int value;				// int value
    	
        AccionPregunta() {}				// empty constructor for default value
        
        AccionPregunta(int value) {
            this.value = value;			// constructor with value
        }
        
        public int getValue() {
            return value;				// getter for value
        }
        
        public static AccionPregunta forValue(int value) {
        	// utility method to retrieve instance by int value
            // iterating values
            for (AccionPregunta n: values()) {
                if (n.getValue() == value) return n;		// matches argument
            }
            return DEFAULT;			// no match, returning DEFAULT
        }
    }

    public Pregunta() {}
    
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
		return tipo;
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


	public int getNumopc() {
		return numopc;
	}


	public void setNumopc(int numopc) {
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
}
