package modelo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.IntStream;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private int duracion;

    private Bus bus;
    private Auxiliar auxiliar;
    private Conductor conductor;
    private Terminal terminalSalida;
    private Terminal terminalLlegada;

    private List<Pasaje> pasajes;
    private boolean[] asientosOcupados;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracion, Bus bus, Auxiliar auxiliar, Conductor conductor, Terminal salida, Terminal llegada) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.duracion = duracion;
        this.bus = bus;
        this.auxiliar = auxiliar;
        this.conductor = conductor;
        this.terminalSalida = salida;
        this.terminalLlegada = llegada;

        this.pasajes = new ArrayList<>();
        this.asientosOcupados = new boolean[bus.getNroAsientos()];

        salida.addSalida(this);
        llegada.addLlegada(this);
        auxiliar.addViaje(this);
        conductor.addViaje(this);
    }
    public LocalDateTime getFechaHoraTermino() {
        return LocalDateTime.of(fecha, hora).plusMinutes(duracion);
    }


    public boolean addPasaje(Pasaje pasaje, int numeroAsiento) {
        for (Pasaje pas : pasajes) {
            if (pas.getAsiento() == numeroAsiento) {
                return false;
            }
        }

        pasajes.add(pasaje);
        return true;
    }

    public int getNroAsientosDisponibles() {
        return (this.getBus().getNroAsientos() - pasajes.size());
    }

    public boolean existeDisponibilidad(int nroAsientos) {
        return getNroAsientosDisponibles() >= nroAsientos;
    }

    public String[] getAsientos() {
        ArrayList<Integer> asiList = new ArrayList<>();

        IntStream.range(0, pasajes.size()).forEach(i -> asiList.add(pasajes.get(i).getAsiento()));

        String[] asientos = new String[this.getBus().getNroAsientos()];

        IntStream.range(0, (this.getBus().getNroAsientos())).forEach(e -> {
            if (asiList.contains(e+1)) {
                asientos[e] = "**";
            } else {
                asientos[e] = String.valueOf(e+1);
            }
        });
        return asientos;
    }

    public int getPrecio() {
        return this.precio;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public Bus getBus() {
        return this.bus;
    }

    public String[][] getListaPasajeros() {
        String[][] pasajerosArray = new String[pasajes.size()][4];

        for (int i = 0; i < pasajes.size(); i++) {
            Pasaje pasaje = pasajes.get(i);
            Pasajero pasajero = pasaje.getPasajero();

            pasajerosArray[i][0] = String.valueOf(pasaje.getAsiento());
            pasajerosArray[i][1] = pasajero.getIdPersona().toString();
            pasajerosArray[i][2] = pasajero.getNombreCompleto().toString();
            pasajerosArray[i][3] = pasajero.getFonoContacto();
        }

        return pasajerosArray;
    }
    public void addConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public Venta[] getVentas() {
        Set<Venta> ventasUnicas = new HashSet<>();
        for (Pasaje pasaje : pasajes) {
            ventasUnicas.add(pasaje.getVenta());
        }
        return ventasUnicas.toArray(new Venta[0]);
    }


    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }


    public Terminal getTerminalSalida() {
        return terminalSalida;
    }

    public Terminal getTerminalLlegada() {
        return terminalLlegada;
    }

}