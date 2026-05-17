// Joaquin Castro, Marcos Lillo, Maximiliano Sandoval y Jose Millan

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class SistemaVentaPasajes {

    private ArrayList<Cliente> clientes;
    private ArrayList<Pasajero> pasajeros;
    private ArrayList<Bus> buses;
    private ArrayList<Viaje> viajes;
    private ArrayList<Venta> ventas;

    public SistemaVentaPasajes() {
        clientes = new ArrayList<>();
        pasajeros = new ArrayList<>();
        buses = new ArrayList<>();
        viajes = new ArrayList<>();
        ventas = new ArrayList<>();
    }

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if(findCliente(id) != null)
            return false;

        Cliente c = new Cliente(id, nom, email, fono);
        clientes.add(c);
        return true;
    }

    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {

        if(findPasajero(id) != null)
            return false;

        Pasajero p = new Pasajero(id, nom, fono, nomContacto.toString(), fonoContacto);

        pasajeros.add(p);
        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {

        if(findBus(patente) != null)
            return false;

        Bus b = new Bus(patente, marca, modelo, nroAsientos);
        buses.add(b);
        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {

        Bus b = findBus(patBus);

        if(b == null)
            return false;

        if(findViaje(fecha, hora, patBus) != null)
            return false;

        Viaje v = new Viaje(fecha, hora, precio, b);
        b.addViaje(v);
        viajes.add(v);
        return true;
    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, IdPersona idCliente) {

        if(findVenta(idDoc, tipo) != null)
            return false;

        Cliente cli = findCliente(idCliente);

        if(cli == null)
            return false;

        Venta v = new Venta(idDoc, tipo, fechaVenta, cli
        );

        ventas.add(v);
        return true;
    }

    public boolean vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora, String patBus,
                               int asiento,
                               IdPersona idPasajero) {

        Venta venta = findVenta(idDoc, tipo);
        Viaje viaje = findViaje(fecha, hora, patBus);
        Bus bus = findBus(patBus);
        Pasajero pasajero = findPasajero(idPasajero);

        if(venta == null || viaje == null || bus == null || pasajero == null)
            return false;

        venta.createPasaje(asiento, viaje, pasajero);
        viaje.addPasaje(pasajero, asiento);
        return true;
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {

        Venta v = findVenta(idDocumento, tipo);

        if(v == null)
            return 0;

        return v.getMonto();
    }

    public String getNombrePasajero(IdPersona idPasajero) {

        Pasajero p = findPasajero(idPasajero);

        if(p == null)
            return null;


        return p.getNombreCompleto().toString();
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {

        ArrayList<String[]> datos = new ArrayList<>();

        for(Viaje v : viajes) {


            if(v.getFecha().equals(fechaViaje) && v.existeDisponibilidad()) {

                String[] fila = {
                        v.getBus().getPatente(),
                        v.getHora().toString(),
                        String.valueOf(v.getPrecio()),
                        String.valueOf(v.getNroAsientosDisponibles())
                };

                datos.add(fila);
            }
        }

        return datos.toArray(new String[0][0]);
    }

    public int[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {

        Viaje v = findViaje(fecha, hora, patBus);

        if(v == null)
            return new int[0][0];

        return v.getAsientosInt();
    }

    public String[][] listVentas(LocalDate fecha) {

        ArrayList<String[]> datos = new ArrayList<>();

        for(Venta v : ventas) {

            if(v.getFecha().equals(fecha)) {

                String[] fila = {
                        v.getIdDocumento(),
                        v.getTipo().toString(),
                        v.getFecha().toString(),
                        v.getCliente().getIdPersona().toString(),
                        v.getCliente().getNombreCompleto().toString(),
                        String.valueOf(v.getPasajes().length),
                        String.valueOf(v.getMonto())
                };

                datos.add(fila);
            }
        }

        return datos.toArray(new String[0][0]);
    }

    public String[][] listViajes() {

        String[][] datos = new String[viajes.size()][5];

        for(int i = 0; i < viajes.size(); i++) {

            Viaje v = viajes.get(i);

            datos[i][0] = v.getFecha().toString();
            datos[i][1] = v.getHora().toString();
            datos[i][2] = String.valueOf(v.getPrecio());
            datos[i][3] = String.valueOf(v.getNroAsientosDisponibles());
            datos[i][4] = v.getBus().getPatente();
        }

        return datos;
    }

    public String[][] listPasajeros(LocalDate fecha,
                                    LocalTime hora,
                                    String patBus) {

        Viaje v = findViaje(fecha, hora, patBus);

        if(v == null)
            return new String[0][0];

        return v.getListaPasajeros();
    }

    private Cliente findCliente(IdPersona id) {

        for(Cliente c : clientes) {

            if(c.getIdPersona().equals(id))
                return c;
        }

        return null;
    }

    private Venta findVenta(String idDocumento,
                            TipoDocumento tipoDocumento) {

        for(Venta v : ventas) {

            if(v.getIdDocumento().equals(idDocumento)
                    && v.getTipo() == tipoDocumento)
                return v;
        }

        return null;
    }

    private Bus findBus(String patente) {

        for(Bus b : buses) {

            if(b.getPatente().equalsIgnoreCase(patente))
                return b;
        }

        return null;
    }

    private Viaje findViaje(LocalDate fecha,
                            LocalTime hora,
                            String patenteBus) {

        for(Viaje v : viajes) {

            if(v.getFecha().equals(fecha)
                    && v.getHora().equals(hora)
                    && v.getBus().getPatente().equalsIgnoreCase(patenteBus))
                return v;
        }

        return null;
    }

    private Pasajero findPasajero(IdPersona idPersona) {

        for(Pasajero p : pasajeros) {

            if(p.getIdPersona().equals(idPersona))
                return p;
        }

        return null;
    }
}