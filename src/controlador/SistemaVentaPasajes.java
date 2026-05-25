
package controlador;

import excepciones.SistemaVentaPasajesException;
import modelo.*;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class SistemaVentaPasajes  {
    private static SistemaVentaPasajes svp;
    private static final ControladorEmpresa controladorEmpresa = ControladorEmpresa.getInstance();

    private List<Cliente> clientes = new ArrayList<>();
    private List<Viaje> viajes = new ArrayList<>();
    private List<Venta> ventas = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();

    private static final DateTimeFormatter fTime = DateTimeFormatter.ofPattern("HH:mm");

    public static SistemaVentaPasajes getInstance() {
        if (svp == null) {
            svp = new SistemaVentaPasajes();
        }
        return svp;
    }

    public SistemaVentaPasajes() {
    }

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SistemaVentaPasajesException {
        this.clientes.forEach(e -> {
            if (e.getIdPersona().equals(id)) {
                throw new SistemaVentaPasajesException("Ya existe un cliente con esta ID");
            }
        });

        Cliente cliente = new Cliente(id, nom, fono, email);
        this.clientes.add(cliente);
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) throws SistemaVentaPasajesException {
        this.pasajeros.forEach(e -> {
            if (e.getIdPersona().equals(id)) {
                throw new SistemaVentaPasajesException("Ya existe un pasajero con esta ID");
            }
        });

        Pasajero pasajero = new Pasajero(id, nom);
        pasajero.setNomContacto(nomContacto);
        pasajero.setFonoContacto(fonoContacto);
        pasajero.setTelefono(fono);
        this.pasajeros.add(pasajero);
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion, String patBus,
                            IdPersona[] idTripulantes, String[] nomComunas) throws SistemaVentaPasajesException {
        Empresa emp;
        Auxiliar aux;
        Bus bus;
        Conductor cond;
        Conductor cond2 = null;
        Terminal llegada;
        Terminal salida;

        if (controladorEmpresa.findBus(patBus.toUpperCase()).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe un bus con la patente indicada");
        } else {
            bus = controladorEmpresa.findBus(patBus.toUpperCase()).get();
            emp = bus.getEmpresa();
        }

        if (controladorEmpresa.findConductor(idTripulantes[0], emp.getRut()).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe conductor con el id indicado en la empresa con el rut indicado");
        } else {
            cond = controladorEmpresa.findConductor(idTripulantes[0], emp.getRut()).get();
        }

        if (idTripulantes[1] != null) {
            if (controladorEmpresa.findConductor(idTripulantes[1], emp.getRut()).isEmpty()) {
                throw new SistemaVentaPasajesException("No existe conductor con el id indicado en la empresa con el rut indicado");
            } else {
                // Corrección de bug: usaba idTripulantes[0] en lugar de idTripulantes[1]
                cond2 = controladorEmpresa.findConductor(idTripulantes[1], emp.getRut()).get();
            }
        }

        if (controladorEmpresa.findAuxiliar(idTripulantes[2], emp.getRut()).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe auxiliar con el id indicado en la empresa con el rut indicado");
        } else {
            aux = controladorEmpresa.findAuxiliar(idTripulantes[2], emp.getRut()).get();
        }

        this.viajes.forEach(e -> {
            if ((e.getHora().equals(hora)) && (e.getFecha().equals(fecha)) && (e.getBus().getPatente().equals(patBus.toUpperCase()))) {
                throw new SistemaVentaPasajesException("Ya existe un viaje con la fecha, hora y bus indicados");
            }
        });

        if (controladorEmpresa.findTerminalPorComuna(nomComunas[0]).isPresent()) {
            salida = controladorEmpresa.findTerminalPorComuna(nomComunas[0]).get();
        } else {
            throw new SistemaVentaPasajesException("No existe un terminal de salida en esta comuna");
        }

        if (controladorEmpresa.findTerminalPorComuna(nomComunas[1]).isPresent()) {
            llegada = controladorEmpresa.findTerminalPorComuna(nomComunas[1]).get();
        } else {
            throw new SistemaVentaPasajesException("No existe un terminal de llegada en esta comuna");
        }

        Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus, aux, cond, salida, llegada);

        if (cond2 != null) {
            viaje.addConductor(cond2);
        }

        this.viajes.add(viaje);
    }

    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fecha, String comunaSalida,
                            String comunaLlegada, IdPersona idPasajero, int nroPasajes) throws SistemaVentaPasajesException {
        Cliente cli;

        this.ventas.forEach(e -> {
            if ((e.getIdDocumento().equals(idDoc)) && (e.getTipo() == tipo)) {
                throw new SistemaVentaPasajesException("Ya existe venta con el id y tipo de documento indicados");
            }
        });

        if (findCliente(idPasajero).isPresent()) {
            cli = findCliente(idPasajero).get();
        } else {
            throw new SistemaVentaPasajesException("No existe cliente con id indicado");
        }

        if ((controladorEmpresa.findTerminalPorComuna(comunaSalida).isPresent())
                && (controladorEmpresa.findTerminalPorComuna(comunaLlegada).isPresent())) {
            Optional<Viaje> optionalViaje = this.viajes.stream().filter(v ->
                    (v.getFecha().equals(fecha)
                            && v.getTerminalSalida().equals(controladorEmpresa.findTerminalPorComuna(comunaSalida).get())
                            && v.getTerminalLlegada().equals(controladorEmpresa.findTerminalPorComuna(comunaLlegada).get())
                            && v.getNroAsientosDisponibles() >= nroPasajes)
            ).findFirst();

            if (optionalViaje.isEmpty()) {
                throw new SistemaVentaPasajesException("No existen viajes disponibles en la fecha y con terminales en las comunas de \nsalida y llegada indicados");
            }
        } else {
            throw new SistemaVentaPasajesException("No existen viajes disponibles en la fecha y con terminales en las comunas de \nsalida y llegada indicados");
        }

        Venta venta = new Venta(idDoc, tipo, fecha, cli);
        this.ventas.add(venta);
    }

    public String[][] getHorariosDisponibles(LocalDate fecha, String nomComunaLlegada,
                                             String nomComunaSalida, int nroPasajes) {
        Viaje[] arrayViajes;

        if (controladorEmpresa.findTerminalPorComuna(nomComunaLlegada).isPresent()
                && controladorEmpresa.findTerminalPorComuna(nomComunaSalida).isPresent()) {

            arrayViajes = this.viajes.stream().filter(v ->
                    (v.getFecha().equals(fecha))
                            && (v.getTerminalLlegada().equals(controladorEmpresa.findTerminalPorComuna(nomComunaLlegada).get()))
                            && (v.getTerminalSalida().equals(controladorEmpresa.findTerminalPorComuna(nomComunaSalida).get()))
                            && (v.getNroAsientosDisponibles() >= nroPasajes)
            ).toArray(Viaje[]::new);

            String[][] datos = new String[arrayViajes.length][4];

            IntStream.range(0, arrayViajes.length).forEach(e -> {
                datos[e][0] = arrayViajes[e].getBus().getPatente();
                datos[e][1] = arrayViajes[e].getHora().format(fTime);
                datos[e][2] = String.valueOf(arrayViajes[e].getPrecio());
                datos[e][3] = String.valueOf(arrayViajes[e].getNroAsientosDisponibles());
            });
            return datos;
        }
        return new String[0][0];
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        if (controladorEmpresa.findBus(patBus.toUpperCase()).isPresent()) {
            if (findViaje(fecha, hora, patBus.toUpperCase()).isPresent()) {
                Viaje viaje = findViaje(fecha, hora, patBus.toUpperCase()).get();
                return viaje.getAsientos();
            }
        }
        return new String[0];
    }

    public Optional<String> getNombrePasajero(IdPersona id) {
        if (findPasajero(id).isPresent()) {
            return Optional.of(findPasajero(id).get().getNombreCompleto().toString());
        }
        return Optional.empty();
    }

    public Optional<Integer> getMontoVenta(String idDoc, TipoDocumento tipo) {
        if (findVenta(idDoc, tipo).isPresent()) {
            return Optional.of(findVenta(idDoc, tipo).get().getMontoPagado());
        }
        return Optional.empty();
    }

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora,
                            String patBus, int asiento, IdPersona id) throws SistemaVentaPasajesException {
        if (findVenta(idDoc, tipo).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el id y tipo de documento indicados");
        }
        if (findPasajero(id).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe pasajero con el id indicado");
        }
        if (findViaje(fecha, hora, patBus).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicados");
        }

        Venta venta = findVenta(idDoc, tipo).get();
        Pasajero pasa = findPasajero(id).get();
        Viaje viaje = findViaje(fecha, hora, patBus).get();

        venta.createPasaje(asiento, viaje, pasa);
    }

    public void pagaVenta(String idDoc, TipoDocumento tipo) throws SistemaVentaPasajesException {
        if (findVenta(idDoc, tipo).isPresent()) {
            Venta venta = findVenta(idDoc, tipo).get();
            if (!venta.pagaMonto()) {
                throw new SistemaVentaPasajesException("La venta ya fue pagada");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe venta con el id y tipo de documento indicados\n");
        }
    }

    public void pagaVenta(String idDoc, TipoDocumento tipo, long nroTarjeta) throws SistemaVentaPasajesException {
        if (findVenta(idDoc, tipo).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe venta con el id y tipo de documento indicados\n");
        }
        Venta venta = findVenta(idDoc, tipo).get();
        if (!venta.pagaMonto(nroTarjeta)) {
            throw new SistemaVentaPasajesException("La venta ya fue pagada");
        }
    }

    public String[][] listVentas() {
        String[][] ventasArray = new String[this.ventas.size()][7];

        IntStream.range(0, this.ventas.size()).forEach(e -> {
            ventasArray[e][0] = this.ventas.get(e).getIdDocumento();
            ventasArray[e][1] = this.ventas.get(e).getTipo().toString();
            ventasArray[e][2] = this.ventas.get(e).getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            ventasArray[e][3] = this.ventas.get(e).getCliente().getIdPersona().toString();
            ventasArray[e][4] = this.ventas.get(e).getCliente().getNombreCompleto().toString();
            ventasArray[e][5] = String.valueOf(this.ventas.get(e).getPasajes().length);
            ventasArray[e][6] = String.valueOf(this.ventas.get(e).getMontoPagado());
        });

        return ventasArray;
    }

    public String[][] listViajes() {
        String[][] viajesArray = new String[this.viajes.size()][8];

        IntStream.range(0, this.viajes.size()).forEach(e -> {
            viajesArray[e][0] = this.viajes.get(e).getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            viajesArray[e][1] = this.viajes.get(e).getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
            viajesArray[e][2] = this.viajes.get(e).getFechaHoraTermino().format(DateTimeFormatter.ofPattern("HH:mm"));
            viajesArray[e][3] = String.valueOf(this.viajes.get(e).getPrecio());
            viajesArray[e][4] = String.valueOf(this.viajes.get(e).getNroAsientosDisponibles());
            viajesArray[e][5] = this.viajes.get(e).getBus().getPatente();
            viajesArray[e][6] = this.viajes.get(e).getTerminalSalida().getDireccion().getComuna();
            viajesArray[e][7] = this.viajes.get(e).getTerminalLlegada().getDireccion().getComuna();
        });

        return viajesArray;
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SistemaVentaPasajesException {
        Viaje viaje;

        if (findViaje(fecha, hora, patBus).isPresent()) {
            viaje = findViaje(fecha, hora, patBus).get();
        } else {
            throw new SistemaVentaPasajesException("No existe viaje con la fecha, hora y patente de bus indicados");
        }

        String[][] listPasajeros = new String[viaje.getListaPasajeros().length][5];

        IntStream.range(0, viaje.getListaPasajeros().length).forEach(v -> {
            listPasajeros[v][0] = String.valueOf(v);              // NroAsiento
            listPasajeros[v][1] = viaje.getListaPasajeros()[v][0]; // IdPersona
            listPasajeros[v][2] = viaje.getListaPasajeros()[v][1]; // NombreCompleto
            listPasajeros[v][3] = viaje.getListaPasajeros()[v][2]; // NombreContacto
            listPasajeros[v][4] = viaje.getListaPasajeros()[v][3]; // FonoContacto
        });

        return listPasajeros;
    }

    // Finds

    protected Optional<Pasajero> findPasajero(IdPersona id) {
        return this.pasajeros.stream().filter(p -> p.getIdPersona().equals(id)).findFirst();
    }

    protected Optional<Viaje> findViaje(LocalDate fecha, LocalTime hora, String patBus) {
        return this.viajes.stream().filter(v ->
                v.getHora().equals(hora)
                        && v.getFecha().equals(fecha)
                        && v.getBus().getPatente().equals(patBus)
        ).findFirst();
    }

    protected Optional<Cliente> findCliente(IdPersona id) {
        return this.clientes.stream().filter(c -> c.getIdPersona().equals(id)).findFirst();
    }

    protected Optional<Venta> findVenta(String idDoc, TipoDocumento tipo) {
        return this.ventas.stream().filter(v ->
                v.getTipo() == tipo && v.getIdDocumento().equals(idDoc)
        ).findFirst();
    }
}