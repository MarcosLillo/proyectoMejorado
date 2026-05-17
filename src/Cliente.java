import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona{
    //Solucionando el problema de los 2 Clientes
    private String email;
    private List<Venta> ventas;

    //Tenia que arreglar primero la clase Persona, en especifico el Nombre nombreCompleto
    public Cliente(IdPersona id, Nombre nom, String email, String telefono) {
        super(id, nom, telefono); //Kwea
        this.email = email;
        this.ventas = new ArrayList<>();

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public void addVenta(Venta venta) {

        this.ventas.add(venta);

    }

    public Venta[] getVentas() {

        return ventas.toArray(new Venta[0]);

    }

}
