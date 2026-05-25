package utilidades;

import java.util.Objects;

public class Pasaporte implements IdPersona{
    private String numero;
    private String nacionalidad;


    public Pasaporte(String numero, String nacionalidad) {
        this.numero = numero;
        this.nacionalidad = nacionalidad;
    }

    public static Pasaporte of(String numero, String nacionalidad) {

        numero = numero.trim();
        nacionalidad = nacionalidad.trim();

        if (!isValidPasaporte(numero) || !isValidNacionalidad(nacionalidad)) {
            return null;
        }

        return new Pasaporte(numero, nacionalidad);
    }

    private static boolean isValidPasaporte(String numero) {
        return numero != null && numero.matches("[A-Za-z0-9]+");
    }

    private static boolean isValidNacionalidad(String nacionalidad) {
        return nacionalidad != null && nacionalidad.matches("[A-Za-z ]+");
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    @Override
    public String toString() {

        return "utilidades.Pasaporte: " + numero + " Nacionalidad: " + nacionalidad;
    }

    @Override
    public boolean equals(Object otro) {

        if (this == otro) return true;


        if (otro == null || getClass() != otro.getClass()) return false;

        Pasaporte pasaporte = (Pasaporte) otro;

        return numero.equals(pasaporte.numero) && nacionalidad.equals(pasaporte.nacionalidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, nacionalidad);
    }
}