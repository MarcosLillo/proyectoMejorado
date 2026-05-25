
package modelo;
import java.util.*;
import utilidades.Direccion;

public class Terminal  {
    private String nombre;
    private Direccion dir;

    private List<Viaje> viajesLlegada, viajesSalida;

    public Terminal(String nombre, Direccion dir) {
        this.nombre = nombre;
        this.dir = dir;

        this.viajesLlegada = new ArrayList<>();
        this.viajesSalida = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Direccion getDireccion() {
        return dir;
    }

    public void setDireccion(Direccion dir) {
        this.dir = dir;
    }

    public void addLlegada(Viaje viaje) {
        boolean existe = false;

        for (int contador = 0; contador < viajesLlegada.size(); contador++) {
            if (viajesLlegada.get(contador).equals(viaje)) {
                existe = true;
            }
        }

        if (existe) {
            System.out.println("Ya existe este viaje de llegada");
        } else {
            viajesLlegada.add(viaje);
        }
    }

    public void addSalida(Viaje viaje) {
        boolean existe = false;

        for (int contador = 0; contador < viajesSalida.size(); contador++) {
            if (viajesSalida.get(contador).equals(viaje)) {
                existe = true;
            }
        }

        if (existe) {
            System.out.println("Ya existe este viaje de llegada");
        } else {
            viajesSalida.add(viaje);
        }
    }

    public Viaje[] getLlegadas(){
        Viaje viajeLlegada[] = new Viaje[viajesLlegada.size()];

        for (int contador = 0; contador < viajesLlegada.size(); contador++) {
            viajeLlegada[contador] = viajesLlegada.get(contador);
        }

        return viajeLlegada;
    }

    public Viaje[] getSalidas(){
        Viaje viajeSalida[] = new Viaje[viajesSalida.size()];

        for (int contador = 0; contador < viajesSalida.size(); contador++) {
            viajeSalida[contador] = viajesSalida.get(contador);
        }
        return viajeSalida;
    }
}