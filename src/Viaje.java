// Joaquin Castro, Marcos Lillo, Maximiliano Sandoval y Jose Millan

import java.time.LocalDate;
import java.time.LocalTime;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private String[][] asientos; //Vacío o Ocupado
    private String[][] listaPasajeros;
    private int nroAsientosDisponibles;


    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        int capacidad = bus.getNroAsientos(); //Para resumir
        this.asientos = new String[capacidad][1];
        this.listaPasajeros = new String[capacidad][4];
        this.nroAsientosDisponibles = capacidad;
        for (int i = 0; i < capacidad; i++) {
            asientos[i][0] = "V";
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Bus getBus() {
        return bus;
    }

    public String[][] getAsientos() {
        String[][] resultado = new String[asientos.length][2];
        for (int i = 0; i < asientos.length; i++) {
            resultado[i][0] = String.valueOf(i + 1);
            resultado[i][1] = asientos[i][0];
        }
        return resultado;
    }

    public int[][] getAsientosInt() {
        int[][] resultado = new int[asientos.length][2];
        for (int i = 0; i < asientos.length; i++) {
            resultado[i][0] = i + 1;                      // Número del asiento
            resultado[i][1] = asientos[i][0].equals("V") ? 0 : 1;  // 0=libre, 1=ocupado
        }
        return resultado;
    }

    public void addPasaje(Pasajero pasajero, int numAsiento) {
        int index = numAsiento - 1;
        if (index < 0 || index >= asientos.length) {
            System.out.println("Asiento no valido");
            return;
        }
        if (!asientos[index][0].equals("V")) {
            System.out.println("Asiento ocupado");
            return;
        }
        asientos[index][0] = "O"; //Ocupado
        listaPasajeros[index][0] = pasajero.getIdPersona().toString();
        listaPasajeros[index][1] = pasajero.getNombreCompleto().toString();
        listaPasajeros[index][2] = pasajero.getNomContacto();
        listaPasajeros[index][3] = pasajero.getFonoContacto();
        nroAsientosDisponibles--;
    }

    public String[][] getListaPasajeros() {
        return listaPasajeros;
    }

    public boolean existeDisponibilidad() {
        return nroAsientosDisponibles > 0;
    }

    public int getNroAsientosDisponibles() {
        return nroAsientosDisponibles;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "fecha=" + fecha +
                ", hora=" + hora +
                ", precio=" + precio +
                ", bus=" + bus.getPatente() +
                ", disponibles=" + nroAsientosDisponibles +
                '}';
    }
}