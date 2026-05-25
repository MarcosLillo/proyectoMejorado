package modelo;

import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Pasaje  {
    private long numero;
    private int asiento;

    private Viaje viaje;
    private Pasajero pasajero;

    private Venta venta;

    // Constructor
    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.asiento = asiento;
        this.venta = venta;

        this.numero = generarNumeroPasajeUnico();

        this.pasajero = pasajero;
        this.viaje = viaje;

        // Se supone que los datos son correctos, por tanto no es necesario validar nada
    }

    public int getAsiento() {
        return this.asiento;
    }

    public long getNumero() {
        return this.numero;
    }

    public Pasajero getPasajero() {
        return this.pasajero;
    }

    public Viaje getViaje() {
        return this.viaje;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public String toString() {
        return String.format(
                "%-90s%n" +

                        "%-25s%-40s%n" +
                        "%-25s%-40s%n" +

                        "%-40s%-20s%n" +
                        "%-40s%-20s%n" +

                        "%-20s%-20s%-20s%n" +
                        "%-20s%-20s%-20s%n" +

                        "%-30s%-30s%-20s%-10s%n" +
                        "%-30s%-30s%-20s%-10s%n" +

                        "%-90s%n%n",


                "-------------------------------------- PASAJE ELECTRONICO --------------------------------------",

                "Nombre Empresa", "Número de Pasaje",
                this.getViaje().getBus().getEmpresa().getNombre(), this.getNumero(),
                "Nombre Pasajero", "RUT/Pasaporte",
                this.getPasajero().getNombreCompleto().toString(), this.pasajero.getIdPersona().toString(),
                "Patente Bus", "Asiento", "Valor Pagado",
                this.getViaje().getBus().getPatente(), this.getAsiento(), this.getViaje().getPrecio(),
                "Terminal Origen", "Terminal destino", "Fecha", "Hora",
                this.getViaje().getTerminalSalida().getNombre(), this.getViaje().getTerminalLlegada().getNombre(), this.getViaje().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), this.getViaje().getHora().format(DateTimeFormatter.ofPattern("HH:mm")),

                "------------------------------------------------------------------------------------------------"
        );


    }

    // Metodo original
    private int generarNumeroPasajeUnico() {
        Random random = new Random();
        return random.nextInt(999999); // Genera un número aleatorio de hasta 6 dígitos
    }
}