
package modelo;
import java.util.*;

public class Bus   {
    // Atributos
    private String patente, marca, modelo;
    private int nroAsientos;
    private Empresa empresa;

    private ArrayList<Viaje> viajes = new ArrayList<>();

    public Bus(String patente, int nroAsientos, Empresa empresa) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.marca="";
        this.modelo="";
        this.empresa = empresa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPatente() {
        return this.patente;
    }

    public String getModelo() {
        return this.modelo;
    }

    public String getMarca() {
        return this.marca;
    }

    public int getNroAsientos() {
        return this.nroAsientos;
    }

    public void addViaje(Viaje viaje) {
        this.viajes.add(viaje);
    }

    public Viaje[] getViajes() {
        Viaje[] viajes = new Viaje[this.viajes.size()];
        for (int contador = 0; contador < this.viajes.size(); contador++) {
            viajes[contador] = this.viajes.get(contador);
        }
        return viajes;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }
}