// Creada por: Matías Cáceres Ceballos

package modelo;
import java.util.*;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

public class Conductor extends Tripulante  {
    private List<Viaje> viajes;

    public Conductor(IdPersona idPersona, Nombre nombreCompleto, Direccion direccion, Empresa empresa) {
        super(idPersona, nombreCompleto, direccion, empresa);
        this.viajes = new ArrayList<>();
    }

    public void addViaje(Viaje viaje) {
        viajes.add(viaje);
        viaje.addConductor(this);
    }

    public int getNroViajes() {
        return viajes.size();
    }
}