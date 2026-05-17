//Marcos Lillo
public class Pasaporte implements IdPersona {
    //atributos
    private String numero;
    private String nacionalidad;
    //constructor
    public Pasaporte(String numero, String nacionalidad) {
        this.numero = numero;
        this.nacionalidad = nacionalidad;
    }
    //getters
    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    @Override
    public String toString() {
        return this.numero + " " + this.nacionalidad;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Pasaporte o = (Pasaporte) obj;
        return this.numero.equals(o.numero) && this.nacionalidad.equals(o.nacionalidad);

    }
    public static Pasaporte of(String num, String nacionalidad){
        if (num == null || nacionalidad == null) {
            return null;
        }
        return new Pasaporte(num, nacionalidad);
    }
}