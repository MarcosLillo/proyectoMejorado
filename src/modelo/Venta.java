package modelo;
//Joaquin Vicente Roca Mendoza

import java.time.LocalDate;
import java.util.ArrayList;
public class Venta  {
    private final String idDocumento;
    private final TipoDocumento tipo;
    private final LocalDate fecha;
    private Pago pago;

    private final Cliente cliente;
    private final ArrayList<Pasaje> pasajes = new ArrayList<>();

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {

        this.tipo = tipo;
        this.fecha = fec;

        this.cliente = cli;
        this.idDocumento = id;

        this.pago = null;
        cli.addVenta(this);
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }


    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        if (viaje.addPasaje(new Pasaje(asiento, viaje, pasajero, this), asiento)) {
            Pasaje nuevoPasaje = new Pasaje(asiento, viaje, pasajero, this);
            pasajes.add(nuevoPasaje);
        } else {
            System.out.println("No se pudo agregar el pasaje: asiento ya ocupado o inválido.");
        }
    }


    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }

    public int getMonto() {
        return (this.getPasajes()[0].getViaje().getPrecio() * this.getPasajes().length);
    }

    public boolean pagaMonto() {
        if (this.pago != null) {
            return false;
        }
        this.pago = new PagoEfectivo(getMonto());

        return true;
    }

    public boolean pagaMonto(long nroTarjeta) {
        if (this.pago != null) {
            return false;
        }

        this.pago = new PagoTarjeta(getMonto(), nroTarjeta);

        return true;
    }
    public int getMontoPagado() {
        if (this.pago != null) {
            return this.pago.getMonto();
        }
        return 0;
    }
    public String getTipoPago() {
        if (this.pago != null) {
            if (this.pago instanceof PagoEfectivo) {
                return "Efectivo";
            } else if (this.pago instanceof PagoTarjeta) {
                return "Con Tarjeta";
            }
        }
        return null;
    }
}