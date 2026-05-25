
package utilidades;
import java.util.Objects;

public class Direccion  {
    private String calle, comuna;
    private int numero;

    public Direccion(String calle, int num, String comuna) {
        this.numero = num;
        this.calle = calle;
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public String getComuna() {
        return comuna;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "calle='" + calle + '\'' +
                ", comuna='" + comuna + '\'' +
                ", numero=" + numero +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return numero == direccion.numero && Objects.equals(calle, direccion.calle) && Objects.equals(comuna, direccion.comuna);
    }
}