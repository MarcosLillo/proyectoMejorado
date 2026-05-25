//Joaquin Vicente Roca Mendoza
package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.ArrayList;

public class Cliente extends Persona   {
    private String email;
    private final ArrayList<Venta> ventas;
    public Cliente(IdPersona idPersona, Nombre nombreCompleto, String telefono, String email) {
        super(idPersona, nombreCompleto);
        setTelefono(telefono);
        this.email = email;
        this.ventas = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString() + ", Email: " + email;
    }
    public void addVenta(Venta venta){
        ventas.add(venta);
    }
    public Venta[] getVentas(){
        return ventas.toArray(new Venta[0]);
    }
}