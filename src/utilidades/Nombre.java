package utilidades;

public class Nombre  {
    // Atributos

    private String nombres, apellidoPaterno, apellidoMaterno;
    private Tratamiento tratamiento;

    // Constructores

    public Nombre(String nombre, String apellidoPaterno, String apellidoMaterno, Tratamiento tratamiento) {
        this.nombres = nombre;
        this.apellidoMaterno = apellidoMaterno;
        this.apellidoPaterno = apellidoPaterno;
        this.tratamiento = tratamiento;
    }

    public void setNombres(String nombre) {
        this.nombres = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getNombres() {
        return this.nombres;
    }

    public String getApellidoPaterno() {
        return this.apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return this.apellidoMaterno;
    }

    public Tratamiento getTratamiento() {
        return this.tratamiento;
    }

    public String toString() {
        return (this.tratamiento +" " +this.nombres +" " +this.apellidoPaterno +" " +this.apellidoMaterno);
    }

    public boolean equals(Nombre otro) {
        if ((this.nombres.equals(otro.nombres)) && (this.apellidoPaterno.equals(otro.apellidoPaterno)) && (this.apellidoMaterno.equals(otro.apellidoMaterno))) {
            return true;
        } else {
            return false;
        }
    }
}

