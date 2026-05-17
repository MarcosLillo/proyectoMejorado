// Joaquin Castro, Marcos Lillo, Maximiliano Sandoval y Jose Millan

import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private SistemaVentaPasajes sistema = new SistemaVentaPasajes();

    public static void main(String[] args) {
        new Main().menu();
    }

    private void menu() {
        int opcion = 0;

        while (opcion != 9) {
            System.out.println("============================");
            System.out.println("...::: Menu principal :::...");
            System.out.println("1) Crear cliente");
            System.out.println("2) Crear bus");
            System.out.println("3) Crear viaje ");
            System.out.println("4) Vender pasaje");
            System.out.println("5) Lista de pasajeros");
            System.out.println("6) Lista de ventas");
            System.out.println("7) Lista de viajes");
            System.out.println("8) Consulta Viajes disponibles por fecha");
            System.out.println("9) Salir");
            System.out.println("-------------");
            System.out.print("Ingrese opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    createCliente();
                    break;
                case 2:
                    createBus();
                    break;
                case 3:
                    createViaje();
                    break;
                case 4:
                    vendePasajes();
                    break;
                case 5:
                    listPasajerosViaje();
                    break;
                case 6:
                    listVentas();
                    break;
                case 7:
                    listViajes();
                    break;
                case 8:
                    consultaViajesDisponibles();
                    break;
                case 9:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }
    }

    private void createCliente() {
        System.out.println("...:::Crear nuevo cliente:::...");
        System.out.println("-------------------------------");
        System.out.println("Rut[1] o Pasaporte[2]: ");
        int tipoDoc = Integer.parseInt(sc.nextLine());
        IdPersona id = null;

        if (tipoDoc == 1) {
            System.out.print("R.U.T : ");

            String input = sc.nextLine();
            String[] partes = input.split("-");

            int numero = Integer.parseInt(partes[0]);
            char dv = Character.toUpperCase(partes[1].charAt(0));

            id = Rut.of(numero, dv);

            if (id == null) {
                System.out.println("RUT inválido");
                return;
            }

        } else if (tipoDoc == 2) {
            System.out.print("Pasaporte (numero): ");
            String num = sc.nextLine();

            System.out.print("Nacionalidad: ");
            String nac = sc.nextLine();

            id = Pasaporte.of(num, nac);

            if (id == null) {
                System.out.println("Pasaporte invalido");
                return;
            }

        } else {
            System.out.println("Tipo de documento invalido");
            return;
        }

        System.out.print("Sr.[1] o Sra. [2] : ");
        int genero = Integer.parseInt(sc.nextLine());
        Tratamiento titulo = (genero == 1) ? Tratamiento.SR : Tratamiento.SRA;

        System.out.print("Nombres : ");
        String nombres = sc.nextLine();
        System.out.print("Apellido Paterno : ");
        String apellidoPaterno = sc.nextLine();
        System.out.print("Apellido Materno : ");
        String apellidoMaterno = sc.nextLine();
        System.out.print("Telefono movil : ");
        String telefono = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();

        Nombre nombreCompleto = new Nombre(titulo, nombres, apellidoPaterno, apellidoMaterno);

        if (sistema.createCliente(id, nombreCompleto, telefono, email)) {
            System.out.println("...:::: Cliente guardado exitosamente ::::...");
        } else {
            System.out.println("Error: El cliente ya existe.");
        }
    }

    private void createBus() {
        System.out.println("...::::Creacion de un nuevo BUS::::...");
        System.out.println("--------------------------------------");
        System.out.println("Patente: ");
        String pat = sc.nextLine();
        System.out.println("Marca: ");
        String marca = sc.nextLine();
        System.out.println("Modelo: ");
        String modelo = sc.nextLine();
        System.out.println("Numero de asientos: ");
        int asientos = Integer.parseInt(sc.nextLine());
        if (sistema.createBus(pat, marca, modelo, asientos)) {
            System.out.println("...:::: Bus guardado exitosamente ::::...");
        } else {
            System.out.println("Error: Patente ya registrada.");
        }

    }

    private void createViaje() {
        System.out.println("...:::: Creacion de un nuevo Viaje ::::...");
        System.out.print("Fecha [dd/MM/yyyy]: ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Hora [HH:mm]: ");
        LocalTime hora = LocalTime.parse(sc.nextLine());
        System.out.print("Precio: ");
        int precio = Integer.parseInt(sc.nextLine());
        System.out.print("Patente Bus: ");
        String pat = sc.nextLine();

        if (sistema.createViaje(fecha, hora, precio, pat)) {
            System.out.println("...:::: Viaje guardado exitosamente ::::...");
        } else {
            System.out.println("Error: Bus no existe o viaje ya registrado en ese horario.");
        }
    }

    private void vendePasajes() {
        System.out.println("...:::: Venta de pasajes ::::....");

        System.out.print("ID Documento : ");
        String idDoc = sc.nextLine();
        System.out.print("Tipo [1] Boleta [2] Factura : ");
        TipoDocumento tipo = (Integer.parseInt(sc.nextLine()) == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;
        System.out.print("Fecha venta [dd/MM/yyyy] : ");
        LocalDate fVenta = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("RUT Cliente : ");
        String inputCli = sc.nextLine();
        String[] partesCli = inputCli.split("-");

        int numeroCli = Integer.parseInt(partesCli[0]);
        char dvCli = Character.toUpperCase(partesCli[1].charAt(0));

        IdPersona idCli = Rut.of(numeroCli, dvCli);

        if (idCli == null) {
            System.out.println("RUT invalido.");
            return;
        }

        if (!sistema.iniciaVenta(idDoc, tipo, fVenta, idCli)) {
            System.out.println("Venta no permitida (Cliente no existe o ID duplicado).");
            return;
        }

        System.out.print("Cantidad de pasajes : ");
        int cant = Integer.parseInt(sc.nextLine());
        System.out.print("Fecha viaje [dd/MM/yyyy] : ");
        LocalDate fViaje = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String[][] disp = sistema.getHorariosDisponibles(fViaje);
        for (int i = 0; i < disp.length; i++) {
            System.out.println((i + 1) + ") Bus: " + disp[i][0] + " | Salida: " + disp[i][1] + " | Libres: " + disp[i][3]);
        }

        System.out.print("Seleccione viaje: ");
        int sel = Integer.parseInt(sc.nextLine()) - 1;
        String patBus = disp[sel][0];
        LocalTime hViaje = LocalTime.parse(disp[sel][1]);

        for (int i = 0; i < cant; i++) {
            System.out.print("Asiento para pasajero " + (i + 1) + ": ");
            int numAsiento = Integer.parseInt(sc.nextLine());

            System.out.print("RUT Pasajero: ");
            String inputPas = sc.nextLine();
            String[] partesPas = inputPas.split("-");

            int numeroPas = Integer.parseInt(partesPas[0]);
            char dvPas = Character.toUpperCase(partesPas[1].charAt(0));

            IdPersona idPas = Rut.of(numeroPas, dvPas);

            if (idPas == null) {
                System.out.println("RUT invalido.");
                return;
            }

            if (sistema.getNombrePasajero(idPas) == null) {
                System.out.print("Nombre pasajero nuevo: ");
                Nombre nom = new Nombre(Tratamiento.SR, sc.nextLine(), "ApP", "ApM");
                System.out.print("Telefono: ");
                String fono = sc.nextLine();
                System.out.print("Nombre contacto: ");
                Nombre nomContact = new Nombre(Tratamiento.SR, sc.nextLine(), "ApP", "ApM");
                System.out.print("Telefono contacto: ");
                String fonoContact = sc.nextLine();
                sistema.createPasajero(idPas, nom, fono, nomContact, fonoContact);
            }

            sistema.vendePasaje(idDoc, tipo, fViaje, hViaje, patBus, numAsiento, idPas);
        }

        System.out.println("Venta finalizada. Monto total: $" + sistema.getMontoVenta(idDoc, tipo));
    }

    private void listPasajerosViaje() {
        System.out.println("...::::Listado de pasajeros de un viaje::::...");
        System.out.println("Fecha del viaje [dd/MM/yyyy]");
        LocalDate f = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Hora del viaje[HH:mm]");
        LocalTime h = LocalTime.parse(sc.nextLine());
        System.out.println("Patente bus: ");
        String p = sc.nextLine();
        String[][] lista = sistema.listPasajeros(f, h, p);

        if (lista.length == 0) {
            System.out.println("No hay pasajeros en este viaje.");
            return;
        }

        System.out.printf("| %-13s| %-13s| %-13s| %-13s| %-13s|%n",
                "ASIENTO", "RUT/PASS", "PASAJERO", "CONTACTO", "TELEFONO");
        for (String[] fila : lista) {
            if (fila[0] != null) {
                System.out.printf("| %-13s| %-13s| %-13s| %-13s| %-13s|%n",
                        fila[0], fila[1], fila[2], fila[3], fila[4]);
            }
        }
    }

    private void listVentas() {
        System.out.println("...::::Listado de ventas::::...");
        System.out.print("Ingrese fecha [dd/MM/yyyy]: ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String[][] ventas = sistema.listVentas(fecha);

        if (ventas.length == 0) {
            System.out.println("No existen ventas registradas para esa fecha.");
            return;
        }
        System.out.printf("| %-13s| %-13s| %-13s| %-13s| %-13s| %-13s| %-13s|%n",
                "ID DOCUMENTO", "TIPO DOCU", "FECHA", "RUT/PASS", "CLIENTE", "CANT BOLETOS", "TOTAL VENTA");
        for (String[] v : ventas) {
            System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | $%-10s |%n",
                    v[0], v[1], v[2], v[3], v[4], v[5], v[6]);
        }
    }

    private void listViajes() {
        System.out.println("...::::Listado de viajes::::...");
        String[][] viajes = sistema.listViajes();
        if (viajes.length == 0) {
            System.out.println("No existen viajes registrados.");
            return;
        }
        System.out.printf("| %-13s| %-13s| %-13s| %-13s| %-13s|%n",
                "FECHA", "HORA", "PRECIO", "DISPONIBLES", "PATENTE");
        for (String[] v : viajes) {
            System.out.printf("| %-10s | %-10s | $%-10s | %-10s | %-10s |%n", v[0], v[1], v[2], v[3], v[4]);
        }
    }
    private void consultaViajesDisponibles() {
        System.out.println("...::::Consulta de Viajes Disponibles por Fecha::::...");
        System.out.println("------------------------------------------------------");
        System.out.print("Ingrese fecha [dd/MM/yyyy]: ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[][] viajes = sistema.getHorariosDisponibles(fecha);

        if (viajes.length == 0) {
            System.out.println(" No existen viajes disponibles para la fecha: " + fecha);
            System.out.println("Por favor, intente con otra fecha.");
            return;
        }

        System.out.println("\n Se encontraron " + viajes.length + " viaje(s) disponible(s) para: " + fecha);
        System.out.println("------------------------------------------------------\n");
        System.out.printf("| %-10s | %-10s | %-10s | %-15s |%n",
                "PATENTE BUS", "SALIDA", "PRECIO", "ASIENTOS LIBRES");
        System.out.println("|-----------+-----------+-----------+------------------|");

        for (int i = 0; i < viajes.length; i++) {
            System.out.printf("| %-10s | %-10s | $%-10s | %-15s |%n",
                    viajes[i][0],     // Patente del bus
                    viajes[i][1],     // Hora de salida
                    viajes[i][2],     // Precio
                    viajes[i][3]);    // Asientos disponibles
        }

    }
}