//Joaquin Vicente Roca Mendoza
package modelo;
import utilidades.Rut;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Direccion;

import java.util.ArrayList;
import java.util.Arrays;

public class Empresa  {
    private final Rut rut;
    private final String nombre;
    private String url;

    private final ArrayList<Bus> buses = new ArrayList<>();
    private final ArrayList<Conductor> conductores = new ArrayList<>();
    private final ArrayList<Auxiliar> auxiliares = new ArrayList<>();
    private ArrayList<Venta> ventas  = new ArrayList<>();

    // Constructor
    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public Rut getRut() {
        return this.rut;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus) {
        buses.add(bus);
    }

    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }

    public boolean addConductor(IdPersona id, Nombre nombre, Direccion direccion) {

        for (Conductor conductor : conductores) {
            if (conductor.getIdPersona().equals(id)) {
                return false;
            }
        }
        for (Auxiliar auxiliar : auxiliares) {
            if (auxiliar.getIdPersona().equals(id)) {
                return false;
            }
        }

        conductores.add(new Conductor(id, nombre, direccion, this));
        return true;
    }

    public boolean addAuxiliar(IdPersona id, Nombre nombre, Direccion direccion) {
        for (Conductor conductor : conductores) {
            if (conductor.getIdPersona().equals(id)) {
                return false;
            }
        }
        for (Auxiliar auxiliar : auxiliares) {
            if (auxiliar.getIdPersona().equals(id)) {
                return false;
            }
        }

        auxiliares.add(new Auxiliar(id, nombre, direccion, this));
        return true;
    }


    public Tripulante[] getTripulantes() {
        ArrayList<Tripulante> tripulantes = new ArrayList<>();
        tripulantes.addAll(conductores);
        tripulantes.addAll(auxiliares);
        return tripulantes.toArray(new Tripulante[0]);
    }

    public Venta[] getVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();

        for (Bus bus : buses) {
            for (Viaje viaje : bus.getViajes()) {
                ventas.addAll(Arrays.asList(viaje.getVentas()));
            }
        }

        return ventas.toArray(new Venta[0]);
    }
}