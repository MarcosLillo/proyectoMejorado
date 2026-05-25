package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

public abstract class Tripulante extends Persona  {
    private Direccion direccion;
    private Empresa empresa;

    public Tripulante(IdPersona idPersona, Nombre nombreCompleto, Direccion direccion, Empresa empresa) {
        super(idPersona, nombreCompleto);
        this.direccion = direccion;

        this.empresa = empresa;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);
    public abstract int getNroViajes();
}