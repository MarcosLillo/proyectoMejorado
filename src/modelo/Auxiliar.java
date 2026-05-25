// Creada por: Matías Cáceres Ceballos

package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.*;

public class Auxiliar extends Tripulante  {
    private List<Viaje> viajes;

    public Auxiliar(IdPersona idPersona, Nombre nombreCompleto, Direccion direccion, Empresa empresa) {
        super(idPersona, nombreCompleto, direccion, empresa);

        this.viajes = new ArrayList<>();
    }

    public void addViaje(Viaje viaje) {
        boolean existe = false;

        for (int contador = 0; contador < viajes.size(); contador++) {
            if (viaje.equals(viajes.get(contador))) {
                existe = true;
            }
        }

        if (existe) {
            System.out.println("Este tripulante ya pertenece a este viaje.");
        } else {
            viajes.add(viaje);
        }
    }

    public int getNroViajes() {
        return viajes.size();
    }
}