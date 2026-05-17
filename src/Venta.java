//Marcos Lillo
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta   {
    private  String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cli;
    private List<Pasaje> pasajes;

    public Venta(String idDocumento, TipoDocumento tipo, LocalDate fecha, Cliente cli) {
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cli = cli;
        this.pasajes = new ArrayList<>();
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
    public Cliente getCliente(){
        return cli;

    }
    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }
    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero){
        Pasaje nuevo = new Pasaje(asiento, viaje, pasajero, this);
        this.pasajes.add(nuevo);
    }

    public int getMonto() {
        int total = 0;
        for (Pasaje p : pasajes) {
            total += p.getViaje().getPrecio();
        }
        return total;
    }

//https://www.youtube.com/watch?v=oUMsNjCDT8I
}
