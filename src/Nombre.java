//Quejas a Joaquin Castro

public class Nombre {

    //Atributos
    private Tratamiento tratamiento; //Señor, señora
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    //Con o sin constructor, supondre que si
    public Nombre(Tratamiento tratamiento, String nombres, String apellidoPaterno, String apellidoMaterno) {

        this.tratamiento = tratamiento;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;

    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String toString() {
        return this.tratamiento + " " + this.nombres + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
    }

    public boolean equals(Object Otro) {

        //Modifique mi propio codigo por que no inclui esto
        if (this == Otro) return true;
        if (Otro == null || getClass() != Otro.getClass()) return false;
        //Osea la cosa basica

        Nombre persona = (Nombre) Otro;

        return this.nombres.equalsIgnoreCase(persona.nombres) && this.apellidoPaterno.equalsIgnoreCase(persona.apellidoPaterno) && this.apellidoMaterno.equalsIgnoreCase(persona.apellidoMaterno);
    }



}