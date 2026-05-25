// 2do avance: Joaquin Vicente Roca Mendoza

package controlador;

import excepciones.SistemaVentaPasajesException;
import modelo.*;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public class ControladorEmpresa implements java.io.Serializable {
    private static ControladorEmpresa instancia;
    private final ArrayList<Empresa> empresas = new ArrayList<>();
    private final ArrayList<Terminal> terminales = new ArrayList<>();
    private final ArrayList<Bus> buses = new ArrayList<>();
    private final ArrayList<Tripulante> tripulantes = new ArrayList<>();

    private ControladorEmpresa() {
    }

    public static ControladorEmpresa getInstance() {
        if (instancia == null) {
            instancia = new ControladorEmpresa();
        }
        return instancia;
    }

    public void createEmpresa(Rut rut, String nombre, String url) throws SistemaVentaPasajesException {
        if (findEmpresa(rut).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe una empresa con el RUT dado.");
        }
        Empresa nuevaEmpresa = new Empresa(rut, nombre);
        nuevaEmpresa.setUrl(url);
        empresas.add(nuevaEmpresa);
    }

    public void createBus(String patente, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesException {
        Empresa emp;

        if (findEmpresa(rutEmp).isPresent()) {
            emp = findEmpresa(rutEmp).get();
        } else {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        if (findBus(patente.toUpperCase()).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada");
        }

        Bus bus = new Bus(patente, nroAsientos, emp);
        bus.setMarca(marca);
        bus.setModelo(modelo);

        emp.addBus(bus);
        this.buses.add(bus);
    }

    public void createTerminal(String nombre, Direccion direccion) throws SistemaVentaPasajesException {
        if (findTerminal(nombre).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe un terminal con el nombre indicado: " + nombre);
        }
        if (findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe un terminal en la comuna indicada: " + direccion.getComuna());
        }
        terminales.add(new Terminal(nombre, direccion));
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nombre, Direccion direccion) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);
        if (empresa.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el RUT indicado.");
        }

        if (!empresa.get().addConductor(id, nombre, direccion)) {
            throw new SistemaVentaPasajesException("Ya está contratado un conductor/auxiliar con el ID dado en la empresa señalada.");
        }

        tripulantes.add(new Conductor(id, nombre, direccion, empresa.get()));
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nombre, Direccion direccion) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);
        if (empresa.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el RUT indicado.");
        }

        if (!empresa.get().addAuxiliar(id, nombre, direccion)) {
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el ID dado en la empresa señalada.");
        }

        tripulantes.add(new Auxiliar(id, nombre, direccion, empresa.get()));
    }

    public String[][] listEmpresas() {
        if (empresas.isEmpty()) {
            return new String[0][0];
        }

        String[][] datos = new String[empresas.size()][6];

        IntStream.range(0, empresas.size()).forEach(e -> {
            datos[e][0] = empresas.get(e).getRut().toString();
            datos[e][1] = empresas.get(e).getNombre();
            datos[e][2] = empresas.get(e).getUrl();
            datos[e][3] = String.valueOf(empresas.get(e).getTripulantes().length);
            datos[e][4] = String.valueOf(empresas.get(e).getBuses().length);
            datos[e][5] = String.valueOf(empresas.get(e).getVentas().length);
        });
        return datos;
    }

    public String[][] listLlegadasSalidasTerminal(String nombreTerminal, LocalDate fecha) throws SistemaVentaPasajesException {
        if (findTerminal(nombreTerminal).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe terminal con el nombre indicado");
        }

        Terminal terminal = findTerminal(nombreTerminal).get();

        Viaje[] viajesLlegada = Arrays.stream(terminal.getLlegadas())
                .filter(e -> e.getFecha().equals(fecha))
                .toArray(Viaje[]::new);

        Viaje[] viajesSalida = Arrays.stream(terminal.getSalidas())
                .filter(e -> e.getFecha().equals(fecha))
                .toArray(Viaje[]::new);

        String[][] listLlegadasSalidas = new String[viajesLlegada.length + viajesSalida.length][5];

        // Llegadas: la hora es la de término del viaje (getFechaHoraTermino)
        IntStream.range(0, viajesLlegada.length).forEach(i -> {
            listLlegadasSalidas[i][0] = "Llegada";
            listLlegadasSalidas[i][1] = viajesLlegada[i].getFechaHoraTermino()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
            listLlegadasSalidas[i][2] = viajesLlegada[i].getBus().getPatente();
            listLlegadasSalidas[i][3] = viajesLlegada[i].getBus().getEmpresa().getNombre();
            listLlegadasSalidas[i][4] = String.valueOf(viajesLlegada[i].getListaPasajeros().length);
        });

        // Salidas: la hora es la de salida del terminal (getHora)
        IntStream.range(0, viajesSalida.length).forEach(i -> {
            int index = viajesLlegada.length + i;
            listLlegadasSalidas[index][0] = "Salida";
            listLlegadasSalidas[index][1] = viajesSalida[i].getHora()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
            listLlegadasSalidas[index][2] = viajesSalida[i].getBus().getPatente();
            listLlegadasSalidas[index][3] = viajesSalida[i].getBus().getEmpresa().getNombre();
            listLlegadasSalidas[index][4] = String.valueOf(viajesSalida[i].getListaPasajeros().length);
        });

        return listLlegadasSalidas;
    }

    public String[][] listVentasEmpresa(Rut rut) throws SistemaVentaPasajesException {
        if (findEmpresa(rut).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        Empresa empresa = findEmpresa(rut).get();
        Venta[] listVentas = empresa.getVentas();

        String[][] listVentasEmpresa = new String[listVentas.length][5];

        IntStream.range(0, listVentas.length).forEach(v -> {
            listVentasEmpresa[v][0] = listVentas[v].getIdDocumento();
            listVentasEmpresa[v][1] = listVentas[v].getTipo().toString();
            listVentasEmpresa[v][2] = listVentas[v].getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            listVentasEmpresa[v][3] = String.valueOf(listVentas[v].getMontoPagado());
            listVentasEmpresa[v][4] = listVentas[v].getTipoPago();
        });

        return listVentasEmpresa;
    }

    // Finds

    protected Optional<Empresa> findEmpresa(Rut rut) {
        return this.empresas.stream().filter(e -> e.getRut().equals(rut)).findFirst();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        return this.terminales.stream().filter(t -> t.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        return this.terminales.stream().filter(t -> t.getDireccion().getComuna().equalsIgnoreCase(comuna)).findFirst();
    }

    protected Optional<Bus> findBus(String patente) {
        return this.buses.stream().filter(b -> b.getPatente().equalsIgnoreCase(patente)).findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        Empresa emp;
        if (findEmpresa(rutEmpresa).isPresent()) {
            emp = findEmpresa(rutEmpresa).get();
        } else {
            emp = null;
        }

        Optional<Tripulante> trip = tripulantes.stream()
                .filter(t -> t instanceof Conductor
                        && Arrays.stream(emp.getTripulantes()).toList().contains(t)
                        && t.getIdPersona().equals(id))
                .findFirst();

        return trip.map(t -> (Conductor) t);
    }

    protected Optional<Auxiliar> findAuxiliar(IdPersona id, Rut rutEmpresa) {
        Empresa emp;
        if (findEmpresa(rutEmpresa).isPresent()) {
            emp = findEmpresa(rutEmpresa).get();
        } else {
            emp = null;
        }

        Optional<Tripulante> trip = tripulantes.stream()
                .filter(t -> t instanceof Auxiliar
                        && Arrays.stream(emp.getTripulantes()).toList().contains(t)
                        && t.getIdPersona().equals(id))
                .findFirst();

        return trip.map(t -> (Auxiliar) t);
    }
}