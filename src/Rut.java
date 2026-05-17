public class Rut implements IdPersona {

    private int numero;
    private char dv;

    /*Como el constructor es privado, es necesario un of, pero no es la razon principal, la principal es para
    verificar los datos ingresados, osea rut y el digito verificador, por verificar significa ingresar numeros
    que no sean negativos o que simplemente no tengan sentido*/
    private Rut(int numero, char dv) {

        this.numero = numero;
        this.dv = dv;

    }

    public int getNumero() {

        return numero;

    }

    public char getDv() {

        return dv;

    }
    //IdPersona
    @Override
    public String toString() {
        return numero + "-" + dv;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Rut r = (Rut) obj;
        return this.numero == r.numero && this.dv == r.dv;
    }

    public static Rut of(int numero, char dv) {

        if (numero <= 0) {

            System.out.println("Su rut debe ser positivo");
            return null;

        }

        if (!((dv >= '0' && dv <= '9') || dv == 'K')) {

            System.out.println("Digito verificador invalido");
            return null;

        }

        return new Rut(numero, dv);

    }
}