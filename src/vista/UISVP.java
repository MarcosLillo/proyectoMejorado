package vista;

import controlador.ControladorEmpresa;
import controlador.SistemaVentaPasajes;
import excepciones.SistemaVentaPasajesException;
import modelo.TipoDocumento;
import utilidades.*;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

//Singleton
public class UISVP {

    //Lo que carateriza a un singleton
    private static UISVP instance;
    private final SistemaVentaPasajes svp = SistemaVentaPasajes.getInstance();
    private final ControladorEmpresa controladorEmpresa = ControladorEmpresa.getInstance();

    private final Scanner sc;

    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("\r\n|[\n\r\u2028\u2029\u0085,;\t]");
    }

    //Esto asegura que se cree una instancia si no la hay, si la hay, que se mantenga igual osea que se reutilize, es caracteristico de los singleton
    public static UISVP getInstance() {
        if (instance == null) {
            instance = new UISVP();
        }
        return instance;
    }

    //Lo que se vera como menu so...
    public void menu() {
        boolean salir = false;

        while (salir != true) { //(!salir)

            System.out.println("===============================================");
            System.out.println("          ...::: Menú principal :::...         ");
            System.out.println("1)  Crear empresa");
            System.out.println("2)  Contratar tripulante");
            System.out.println("3)  Crear terminal");
            System.out.println("4)  Crear cliente");
            System.out.println("5)  Crear bus");
            System.out.println("6)  Crear viaje");
            System.out.println("7)  Vender pasajes");
            System.out.println("8)  Listar ventas");
            System.out.println("9)  Listar viajes");
            System.out.println("10) Listar pasajeros de viaje");
            System.out.println("11) Listar empresas");
            System.out.println("12) Listar llegadas/salidas de terminal");
            System.out.println("13) Listar ventas de empresa");
            System.out.println("14) Salir");
            System.out.println("------------------------------------------------");
            System.out.print("..:: Ingrese número de opción: ");
            int opcion;
            try {
                opcion = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opcion elegida invalida, ingresela nuevamente: ");
                sc.next();
                continue;
            }

            switch (opcion) {
                case 1:
                    createEmpresa();
                    break;
                case 2:
                    contrataTripulante();
                    break;
                case 3:
                    createTerminal();
                    break;
                case 4:
                    createCliente();
                    break;
                case 5:
                    createBus();
                    break;
                case 6:
                    createViaje();
                    break;
                case 7:
                    vendePasajes();
                    break;
                case 8:
                    listVentas();
                    break;
                case 9:
                    listViajes();
                    break;
                case 10:
                    listPasajerosViaje();
                    break;
                case 11:
                    listEmpresas();
                    break;
                case 12:
                    listLlegadasSalidasTerminal();
                    break;
                case 13:
                    listVentasEmpresa();
                    break;
                case 14:
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion equivocada, muy mal, reflexiona ante tus acciones");
            }
        }
    }

    //Crear empresa (1)
    private void createEmpresa() {
        try {
            System.out.println("   ...::::: Creando una nueva Empresa :::::...");
            String rutStr = obtenerRutStr("R.U.T: ");
            Rut rut = parseRut(rutStr);
            if (rut == null) return;

            String nombre = obtenerTexto("Nombre: ");
            String url    = obtenerTexto("url: ");

            controladorEmpresa.createEmpresa(rut, nombre, url);
            System.out.println("   ...::::: Empresa guardada exitosamente :::::..");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    //Contratar tripulante
    private void contrataTripulante() {
        try {
            System.out.println("   ...:::::: Contratando un nuevo Tripulante :::::...");

            System.out.println(":::: Dato de la Empresa");
            String rutEmpStr = obtenerRutStr("R.U.T: ");
            Rut rutEmp = parseRut(rutEmpStr);
            if (rutEmp == null) return;

            System.out.println(":::: Datos tripulante");
            int tipoTripulante = obtenerNumero("Auxiliar[1] o Conductor[2]: ", 1, 2);

            int eleccionId = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
            IdPersona id = obtenerIdPersona(eleccionId);

            int eleccionTrat = obtenerNumero("Sr.[1] o Sra.[2]: ", 1, 2);
            Tratamiento tratamiento = (eleccionTrat == 1) ? Tratamiento.SR : Tratamiento.SRA;

            String nombre   = obtenerTexto("Nombres: ");
            String apePat   = obtenerTexto("Apellido Paterno: ");
            String apeMat   = obtenerTexto("Apellido Materno: ");
            Nombre nombreTripulante = new Nombre(nombre, apePat, apeMat, tratamiento);

            String calle  = obtenerTexto("Calle: ");
            int numero    = obtenerNumero("Numero: ", 0, 999999999);
            String comuna = obtenerTexto("Comuna: ");
            Direccion direccion = new Direccion(calle, numero, comuna);

            if (tipoTripulante == 1) {
                controladorEmpresa.hireAuxiliarForEmpresa(rutEmp, id, nombreTripulante, direccion);
                System.out.println("   ...::::: Auxiliar contratado exitosamente :::::..");
            } else {
                controladorEmpresa.hireConductorForEmpresa(rutEmp, id, nombreTripulante, direccion);
                System.out.println("   ...::::: Conductor contratado exitosamente :::::..");
            }
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    //Crear terminal
    private void createTerminal() {
        try {
            System.out.println("   ...:::::: Creando un nuevo Terminal :::::...");
            String nombre = obtenerTexto("Nombre: ");
            String calle  = obtenerTexto("Calle: ");
            int numero    = obtenerNumero("Numero: ", 0, 999999999);
            String comuna = obtenerTexto("Comuna: ");

            Direccion direccion = new Direccion(calle, numero, comuna);
            controladorEmpresa.createTerminal(nombre, direccion);
            System.out.println("   ...::::: Terminal guardado exitosamente :::::..");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error al crear el terminal: " + e.getMessage() + " ***");
        }
    }

    //Crear el cliente (4)
    private void createCliente() {
        try {
            System.out.println(":::: Datos cliente");

            int eleccion = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
            IdPersona id = obtenerIdPersona(eleccion);

            String nombre    = obtenerTexto("Nombre: ");
            String apellidoP = obtenerTexto("Apellido Paterno: ");
            String apellidoM = obtenerTexto("Apellido Materno: ");

            eleccion = obtenerNumero("Sr.[1] o Sra.[2]: ", 1, 2);
            Tratamiento tratamiento = (eleccion == 1) ? Tratamiento.SR : Tratamiento.SRA;

            Nombre nombreCompleto = new Nombre(nombre, apellidoP, apellidoM, tratamiento);

            String fono  = obtenerTexto("Telefono movil: ");
            String email = obtenerTexto("Email: ");

            svp.createCliente(id, nombreCompleto, fono, email);
            System.out.println("   ...::::: Cliente creado exitosamente :::::..");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error al crear el cliente: " + e.getMessage() + " ***");
        }
    }

    //Crear el bus (5)
    private void createBus() {
        try {
            System.out.println("   ...:::::: Creando un nuevo Bus :::::...");
            String patente  = obtenerTexto("Patente: ");
            String marca    = obtenerTexto("Marca: ");
            String modelo   = obtenerTexto("Modelo: ");
            int nroAsientos = obtenerNumero("Numero de asientos: ", 1, 999);

            System.out.println(":::: Dato de la empresa");
            String rutEmpStr = obtenerRutStr("R.U.T: ");
            Rut rutEmp = parseRut(rutEmpStr);
            if (rutEmp == null) return;

            controladorEmpresa.createBus(patente, marca, modelo, nroAsientos, rutEmp);
            System.out.println("   ...::::: Bus guardado exitosamente :::::..");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error al crear el bus: " + e.getMessage() + " ***");
        }
    }

    //Crear el viaje (6)
    private void createViaje() {
        try {
            System.out.println("   ...:::::: Creando un nuevo Viaje :::::...");

            LocalDate fecha  = obtenerFecha("Fecha [dd/MM/yyyy]: ");
            LocalTime hora   = obtenerHora("Hora [HH:mm]: ");
            int precio       = obtenerNumero("Precio: ", 1, 999999999);
            int duracion     = obtenerNumero("Duración (minutos): ", 1, 999999999);
            String patBus    = obtenerTexto("Patente Bus: ");

            int cantidadCond = obtenerNumero("Nro. de conductores: ", 1, 2);

            IdPersona[] idTripulantes = new IdPersona[3];

            System.out.println("\t:: Id Conductor ::");
            int eleccionId = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
            idTripulantes[0] = obtenerIdPersona(eleccionId);

            if (cantidadCond == 2) {
                System.out.println("\t:: Id Conductor 2 ::");
                eleccionId = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
                idTripulantes[1] = obtenerIdPersona(eleccionId);
            } else {
                idTripulantes[1] = null;
            }

            System.out.println("\t:: Id Auxiliar ::");
            eleccionId = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
            idTripulantes[2] = obtenerIdPersona(eleccionId);

            String[] comunas = new String[2];
            comunas[0] = obtenerTexto("Nombre comuna salida: ");
            comunas[1] = obtenerTexto("Nombre comuna llegada: ");

            svp.createViaje(fecha, hora, precio, duracion, patBus, idTripulantes, comunas);
            System.out.println("   ...::::: Viaje guardado exitosamente :::::..");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error al crear el viaje: " + e.getMessage() + " ***");
        }
    }

    //Venta de pasajes (7)
    private void vendePasajes() {
        try {
            int eleccionTipo, eleccionID;
            String idDoc;
            TipoDocumento tipo;
            Tratamiento tratamiento;
            LocalDate fecha;
            IdPersona id;
            int nroPasajes;
            String comunaLlegada, comunaSalida;

            System.out.println("...::::: Venta de pasajes :::::..");
            System.out.println(":::: Datos de la Venta");

            idDoc = obtenerTexto("ID Documento: ");
            eleccionTipo = obtenerNumero("Tipo documento: [1] Boleta [2] Factura: ", 1, 2);
            tipo = (eleccionTipo == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;

            fecha = obtenerFecha("Fecha de viaje[dd/MM/yyyy]: ");
            comunaSalida  = obtenerTexto("Origen (comuna): ");
            comunaLlegada = obtenerTexto("Destino (comuna): ");

            System.out.println(":::: Datos del cliente");
            eleccionID = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
            id = obtenerIdPersona(eleccionID);

            System.out.println(":::: Pasajes a vender");
            nroPasajes = obtenerNumero("Cantidad de pasajes: ", 1, 999999999);

            svp.iniciaVenta(idDoc, tipo, fecha, comunaSalida, comunaLlegada, id, nroPasajes);

            // Horarios disponibles
            String[][] horarios = svp.getHorariosDisponibles(fecha, comunaLlegada, comunaSalida, nroPasajes);

            System.out.println(":::: Listado de horarios disponibles");
            System.out.println("   *---------*---------*----------*----------*");
            System.out.println("   | BUS     | SALIDA  | VALOR    | ASIENTOS |");
            System.out.println("   |---------+---------+----------+----------|");

            IntStream.range(0, horarios.length).forEach(e -> {
                System.out.printf("%2d | %-7s | %-7s | %-8s | %-8s |%n",
                        (e + 1), horarios[e][0], horarios[e][1], horarios[e][2], horarios[e][3]);
                System.out.println("   |---------+---------+----------+----------|");
            });

            int eleccion = obtenerNumero("Seleccione viaje [1.." + horarios.length + "]: ", 1, horarios.length) - 1;

            // Asientos
            String[] listAsientos = svp.listAsientosDeViaje(fecha, parseHora(horarios[eleccion][1]), horarios[eleccion][0]);

            System.out.println(":::: Asientos disponibles para el viaje seleccionado");
            System.out.println("*---*---*---*---*---*");

            try {
                for (int cont = 0; cont < listAsientos.length; cont += 4) {
                    System.out.printf("| %2s | %2s |   | %2s | %2s |%n",
                            listAsientos[cont], listAsientos[cont + 1],
                            listAsientos[cont + 3], listAsientos[cont + 2]);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            System.out.println("*---*---*---*---*---*");

            // Selección de asientos
            String[] asiSeleccionados;
            boolean ocupado;

            do {
                ocupado = false;
                System.out.print("Seleccione sus asientos [separe por ,]: ");
                asiSeleccionados = sc.useDelimiter("\n").next().split(",");

                while (asiSeleccionados.length != nroPasajes) {
                    System.out.println("Cantidad de asientos inválida [seleccionados: "
                            + asiSeleccionados.length + " ;; requeridos: " + nroPasajes + "]");
                    System.out.print("Seleccione sus asientos [separe por ,]: ");
                    asiSeleccionados = sc.useDelimiter("\n").next().split(",");
                }

                for (String asi : asiSeleccionados) {
                    if (listAsientos[Integer.parseInt(asi.trim()) - 1].equals("**")) {
                        System.out.println("El asiento seleccionado ya está ocupado.");
                        ocupado = true;
                        break;
                    }
                }
            } while (ocupado);

            final String[] finalAsiSeleccionados = asiSeleccionados;
            int[] asientosSeleccionados = new int[asiSeleccionados.length];
            IntStream.range(0, asiSeleccionados.length).forEach(e ->
                    asientosSeleccionados[e] = Integer.parseInt(finalAsiSeleccionados[e].trim()));

            // Datos de pasajeros
            for (int e = 0; e < nroPasajes; e++) {
                System.out.println(":::: Datos pasajeros " + (e + 1));
                try {
                    eleccionID = obtenerNumero("Rut[1] o Pasaporte[2]: ", 1, 2);
                    id = obtenerIdPersona(eleccionID);
                    svp.vendePasaje(idDoc, tipo, fecha, parseHora(horarios[eleccion][1]),
                            horarios[eleccion][0], asientosSeleccionados[e], id);
                    System.out.println(":::: Pasaje agregado exitosamente");

                } catch (SistemaVentaPasajesException i) {
                    if (i.getMessage().equals("No existe pasajero con el id indicado")) {
                        System.out.println("..:: " + i.getMessage() + " ::..");
                        System.out.println(":::: Creación de pasajero ::::");

                        String nombre  = obtenerTexto("Nombre: ");
                        String apePat  = obtenerTexto("Apellido paterno: ");
                        String apeMat  = obtenerTexto("Apellido materno: ");
                        eleccionTipo   = obtenerNumero("Sr[1] o Sra[2]: ", 1, 2);
                        tratamiento    = (eleccionTipo == 1) ? Tratamiento.SR : Tratamiento.SRA;
                        Nombre nombrePasajero = new Nombre(nombre, apePat, apeMat, tratamiento);
                        String fono = obtenerTexto("Fono: ");

                        String nomC    = obtenerTexto("Nombre contacto: ");
                        String apePatC = obtenerTexto("Apellido paterno del contacto: ");
                        String apeMatC = obtenerTexto("Apellido materno del contacto: ");
                        eleccionTipo   = obtenerNumero("Sr[1] o Sra[2]: ", 1, 2);
                        tratamiento    = (eleccionTipo == 1) ? Tratamiento.SR : Tratamiento.SRA;
                        Nombre nomContacto = new Nombre(nomC, apePatC, apeMatC, tratamiento);
                        String fonoContacto = obtenerTexto("Fono de contacto: ");

                        svp.createPasajero(id, nombrePasajero, fono, nomContacto, fonoContacto);
                        svp.vendePasaje(idDoc, tipo, fecha, parseHora(horarios[eleccion][1]),
                                horarios[eleccion][0], asientosSeleccionados[e], id);
                        System.out.println(":::: Pasaje agregado exitosamente");
                    }
                }
            }

            // Pago
            System.out.println(":::: Monto total de la venta: $" + (Integer.parseInt(horarios[eleccion][2]) * nroPasajes));
            System.out.println(":::: Pago de la venta");
            int tipoPago = obtenerNumero("Efectivo[1] o Tarjeta[2]: ", 1, 2);

            if (tipoPago == 1) {
                svp.pagaVenta(idDoc, tipo);
            } else {
                long nroTarjeta = obtenerLong("Numero de tarjeta: ");
                svp.pagaVenta(idDoc, tipo, nroTarjeta);
            }

            System.out.println("   ...::::: Venta realizada exitosamente :::::..");

        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error al realizar la venta: " + e.getMessage() + " ***");
        }
    }

    //Lista de ventas (8)
    private void listVentas() {
        String[][] ventas = svp.listVentas();
        if (ventas.length == 0) {
            System.out.println("..:: No hay ventas registradas ::..");
        } else {
            System.out.println("\n...::::: Listado de ventas :::::..");
            System.out.println("*----------------*----------*------------*--------------------------*-----------*--------------*");
            System.out.println("| ID Documento   | Tipo     | Fecha      | Cliente ID               | N° Pasajes | Monto       |");
            System.out.println("*----------------*----------*------------*--------------------------*-----------*--------------*");
            for (String[] venta : ventas) {
                System.out.printf("| %-14s | %-8s | %-10s | %-24s | %-9s | %-12s |%n",
                        venta[0], venta[1], venta[2], venta[3], venta[5], venta[6]);
                System.out.println("*----------------*----------*------------*--------------------------*-----------*--------------*");
            }
        }
    }

    //Lista de viajes (9)
    private void listViajes() {
        String[][] viajes = svp.listViajes();
        if (viajes.length == 0) {
            System.out.println("..:: No hay viajes registrados ::..");
        } else {
            System.out.println("\n...::::: Listado de viajes :::::..");
            System.out.println("*------------*-----------*------------*--------*----------------*-----------*--------------*---------------*");
            System.out.println("| FECHA      | HORA SALE | HORA LLEGA | PRECIO | ASIENTOS DISP. | PATENTE   | ORIGEN       | DESTINO       |");
            System.out.println("*------------*-----------*------------*--------*----------------*-----------*--------------*---------------*");
            IntStream.range(0, viajes.length).forEach(v -> {
                System.out.printf("| %-10s | %-9s | %-10s | %-6s | %-14s | %-9s | %-12s | %-13s |%n",
                        viajes[v][0], viajes[v][1], viajes[v][2], viajes[v][3],
                        viajes[v][4], viajes[v][5], viajes[v][6], viajes[v][7]);
                System.out.println("*------------*-----------*------------*--------*----------------*-----------*--------------*---------------*");
            });
        }
    }

    //Lista de pasajeros en el viaje (10)
    private void listPasajerosViaje() {
        try {
            LocalDate fecha = obtenerFecha("Fecha [dd/MM/yyyy]: ");
            LocalTime hora  = obtenerHora("Hora [HH:mm]: ");
            String patenteBus = obtenerTexto("Patente: ");

            while (comprobarMatricula(patenteBus)) {
                System.out.println("Patente inválida.");
                patenteBus = obtenerTexto("Patente: ");
            }

            String[][] pasajeros = svp.listPasajerosViaje(fecha, hora, patenteBus);

            if (pasajeros.length == 0) {
                System.out.println("..:: No hay pasajeros registrados en este viaje ::..");
            } else {
                System.out.println("\n:::: Listado de Pasajeros en el Viaje ::::");
                System.out.println("*------------*--------------------------*--------------------------*----------------*");
                System.out.println("| ID         | Nombre                   | Contacto de Emergencia   | Tel. Contacto  |");
                System.out.println("*------------*--------------------------*--------------------------*----------------*");
                for (String[] pasajero : pasajeros) {
                    System.out.printf("| %-10s | %-24s | %-24s | %-14s |%n",
                            pasajero[1], pasajero[2], pasajero[3], pasajero[4]);
                    System.out.println("*------------*--------------------------*--------------------------*----------------*");
                }
            }
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    //Lista de empresas (11)
    private void listEmpresas() {
        try {
            String[][] empresas = controladorEmpresa.listEmpresas();
            if (empresas.length == 0) {
                System.out.println("\t\t..:: No hay empresas registradas ::..");
            } else {
                System.out.println("\n...:::: Listado de empresas ::::...");
                System.out.println("*---------------*-------------------------*---------------------------------------------*-------------------*--------------*--------------*");
                System.out.println("| RUT EMPRESA   | NOMBRE                  | URL                                         | NRO. TRIPULANTES  | NRO. BUSES   | NRO. VENTAS  |");
                System.out.println("*---------------*-------------------------*---------------------------------------------*-------------------*--------------*--------------*");

                IntStream.range(0, empresas.length).forEach(e -> {
                    System.out.printf("| %-13s | %-23s | %-43s | %-17s | %-12s | %-12s |\n",
                            empresas[e][0], empresas[e][1], empresas[e][2], empresas[e][3], empresas[e][4], empresas[e][5]
                    );
                    System.out.println("*---------------*-------------------------*---------------------------------------------*-------------------*--------------*--------------*");
                });
            }
        } catch (SistemaVentaPasajesException e) {
            System.out.println("\t\t..:: " + e.getMessage() + " ::..");
        }
    }

    //Lista de llegada / salida (12)
    private void listLlegadasSalidasTerminal() {
        try {
            String nombreTerminal = obtenerTexto("Nombre terminal: ");
            LocalDate fecha = obtenerFecha("Fecha[dd/MM/yyyy]: ");

            System.out.println("\n...::::: Listado de llegadas y salidas de un terminal :::::..");
            System.out.printf("         Nombre terminal : %s%n", nombreTerminal);
            System.out.printf("         Fecha[dd/MM/yyyy] : %s%n", fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            String[][] llegadasSalidas = controladorEmpresa.listLlegadasSalidasTerminal(nombreTerminal, fecha);

            if (llegadasSalidas.length == 0) {
                System.out.println("..:: No hay llegadas ni salidas registradas para esa fecha ::..");
            } else {
                System.out.println("*----------------*--------*-------------*-------------------------*-----------------*");
                System.out.println("| LLEGADA/SALIDA | HORA   | PATENTE BUS | NOMBRE EMPRESA          | NRO. PASAJEROS  |");
                System.out.println("*----------------*--------*-------------*-------------------------*-----------------*");
                for (String[] registro : llegadasSalidas) {
                    System.out.printf("| %-14s | %-6s | %-11s | %-23s | %-15s |%n",
                            registro[0], registro[1], registro[2], registro[3], registro[4]);
                    System.out.println("*----------------*--------*-------------*-------------------------*-----------------*");
                }
            }
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    //(13)
    private void listVentasEmpresa() {
        try {
            System.out.println("...::::: Listado de ventas de una empresa :::::..");
            String rutStr = obtenerRutStr("R.U.T: ");
            Rut rut = parseRut(rutStr);
            if (rut == null) return;

            String[][] ventas = controladorEmpresa.listVentasEmpresa(rut);

            if (ventas.length == 0) {
                System.out.println("..:: La empresa no registra ventas ::..");
            } else {
                System.out.println("*-----------*----------*---------------*----------------*");
                System.out.println("| FECHA     | TIPO     | MONTO PAGADO  | TIPO PAGO      |");
                System.out.println("*-----------*----------*---------------*----------------*");
                for (String[] venta : ventas) {
                    System.out.printf("| %-9s | %-8s | %-13s | %-14s |%n",
                            venta[2], venta[1], venta[3], venta[4]);
                    System.out.println("*-----------*----------*---------------*----------------*");
                }
            }
        } catch (SistemaVentaPasajesException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    //Metodos necesarios
    private String obtenerTexto(String mensaje) {
        String input;
        do {
            System.out.printf("%40s", mensaje);
            input = sc.next().trim();
            if (input.isEmpty()) {
                System.out.println("La entrada no puede estar vacía.");
            }
        } while (input.isEmpty());
        return input;
    }

    private int obtenerNumero(String mensaje, int min, int max) {
        while (true) {
            System.out.printf("%40s", mensaje);
            try {
                int numero = sc.nextInt();
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.printf("Ingrese un número entre %d y %d.%n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Debe ser un número.");
                sc.next();
            }
        }
    }

    private LocalDate obtenerFecha(String mensaje) {
        while (true) {
            System.out.printf("%40s", mensaje);
            try {
                String fechaStr = sc.next().trim();
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                System.out.println("Formato de fecha no válido. Debe ser dd/MM/yyyy.");
            }
        }
    }

    private LocalTime obtenerHora(String mensaje) {
        while (true) {
            System.out.printf("%40s", mensaje);
            try {
                String horaStr = sc.next().trim();
                return LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                System.out.println("Formato de hora no válido. Debe ser HH:mm.");
            }
        }
    }

    private long obtenerLong(String msg) {
        while (true) {
            System.out.printf("%40s", msg);
            try {
                return sc.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Debe ser un número.");
                sc.next();
            }
        }
    }

    private boolean comprobarMatricula(String patente) {
        return (!patente.matches("^[a-zA-Z]{2}\\d{4}$")) && (!patente.matches("^[a-zA-Z]{4}\\d{2}$"));
    }

    private String obtenerRutStr(String mensaje) {
        String rut;
        do {
            rut = obtenerTexto(mensaje);
            if ((!rut.matches("\\d{7,8}-[0-9Kk]")) && (!rut.matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]"))) {
                System.out.println("Formato de RUT inválido [XX.XXX.XXX-X o XXXXXXXX-X].");
                rut = null;
            }
        } while (rut == null);
        return rut;
    }

    private Rut parseRut(String rut) {
        if (rut.matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Kk]")) {
            String[] partes = rut.split("[.-]");
            return new Rut(Integer.parseInt(partes[0] + partes[1] + partes[2]), partes[3].charAt(0));
        } else if (rut.matches("\\d{7,8}-[0-9Kk]")) {
            String[] partes = rut.split("-");
            return new Rut(Integer.parseInt(partes[0]), partes[1].charAt(0));
        }
        System.out.println("Formato de RUT inválido.");
        return null;
    }

    private Pasaporte obtenerPasaporte(String msg) {
        while (true) {
            String pasaporte = obtenerTexto(msg);
            if (pasaporte.matches("[A-Za-z0-9]+")) {
                String nacionalidad = obtenerTexto(">> Nacionalidad: ");
                return new Pasaporte(pasaporte, nacionalidad);
            }
            System.out.println("Formato de pasaporte inválido.");
        }
    }

    private IdPersona obtenerIdPersona(int tipo) {
        if (tipo == 1) {
            return parseRut(obtenerRutStr(">> Rut: "));
        } else {
            return obtenerPasaporte(">> Pasaporte: ");
        }
    }

    private LocalTime parseHora(String hor) {
        String[] partes = hor.split(":");
        return LocalTime.of(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
    }

}