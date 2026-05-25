package utilidades;

import java.text.DecimalFormat;

public class Rut implements IdPersona {
    private int numero;
    private char dv;

    public Rut(int numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public Rut(String rut) {
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static Rut of(String rutConDv){
        String[] partes = rutConDv.split("-");
        int numero = Integer.parseInt(partes[0]);
        char dv = partes[1].charAt(0);
        return new Rut(numero, dv);
    }

    @Override

    public String toString() {

        DecimalFormat formatter;
        if (numero >= 10000000) {

            formatter = new DecimalFormat("##,###,###");
        } else {

            formatter = new DecimalFormat("#,###,###");
        }

        String numeroFormateado = formatter.format(numero).replace(",", ".");

        return numeroFormateado + "-" + dv;
    }

    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rut rut = (Rut) o;
        return numero == rut.numero && dv == rut.dv;
    }

}