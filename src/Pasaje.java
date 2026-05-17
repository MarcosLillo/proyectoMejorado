//Marcos Lillo
public class Pasaje {
    private long numero;
    private int asiento;
    private Pasajero pasajero;
    private Viaje viaje;
    private Venta venta;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.asiento = asiento;
        this.viaje = viaje;
        this.pasajero = pasajero;
        this.venta = venta;
        this.numero = generarNumero();
    }
    //metodo para generar numeros no duplicados
    public long generarNumero(){
        return (long) (Math.random() * 10000000000000L);
    }
    //Consultar sobre si es int o long, //Yo lo deje en Long
    public long getNumero() {
        return numero;
    }

    public int getAsiento() {
        return asiento;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }
}
