package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

public class Pasajero extends Persona  {
    private String fonoContacto;
    private Nombre nomContacto;


    public Pasajero(IdPersona idPersona, Nombre nombreCompleto) {
        super(idPersona, nombreCompleto);

        this.fonoContacto = "";
        this.nomContacto = null;
    }

    public void setNomContacto(Nombre nom) {
        this.nomContacto = nom;
    }

    public Nombre getNomContacto() {
        return this.nomContacto;
    }

    public void setFonoContacto(String fono) {
        this.fonoContacto = fono;
    }

    public String getFonoContacto() {
        return this.fonoContacto;
    }
}