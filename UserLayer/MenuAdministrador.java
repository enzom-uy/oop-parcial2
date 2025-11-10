package UserLayer;

import LogicLayer.Cuenta;
import LogicLayer.GestorCuenta;
import LogicLayer.GestorUsuarios;
import LogicLayer.Usuario;
import javax.swing.JOptionPane;

public class MenuAdministrador {

    private final Usuario usuarioActual;
    private final GestorCuenta gestorCuentas;
    private final GestorUsuarios gestorUsuarios;
    private final MenuOperacionesCuenta menuCuentas;

    public MenuAdministrador(Usuario usuarioActual, GestorCuenta gestorCuentas, GestorUsuarios gestorUsuarios,
            MenuOperacionesCuenta menuCuentas) {
        this.usuarioActual = usuarioActual;
        this.gestorCuentas = gestorCuentas;
        this.gestorUsuarios = gestorUsuarios;
        this.menuCuentas = menuCuentas;
    }

    public int mostrarMenu() {
        String[] opciones = {
                "Crear Empleado",
                "Aprobar Cuentas",
                "Ver Cuentas Pendientes",
                "Ver Todas las Cuentas",
                "Ver Todos los Empleados",
                "Cerrar Sesión",
                "Salir del Sistema"
        };

        String mensajeMenu = "MENÚ ADMINISTRADOR - " + usuarioActual.getNombre() + "\n\n" +
                "Total de cuentas: " + gestorCuentas.contarCuentas() + "\n" +
                "Cuentas pendientes: " + gestorCuentas.obtenerCuentasPendientes().size() + "\n" +
                "Total de empleados: " + gestorUsuarios.obtenerEmpleados().size() + "\n";

        int seleccion = JOptionPane.showOptionDialog(
                null,
                mensajeMenu,
                "Sistema Bancario - Menú Administrador",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        // -1 es si el usuario cierra la ventana del menú no te olvides
        // los valores del returns se usan en MenuPrincipal, línea 31
        if (seleccion == -1 || seleccion == 6) {
            return -1;
        } else if (seleccion == 5) {
            return 0;
        } else {
            ejecutarOpcion(seleccion);
            return 1;
        }
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 0:
                menuCrearEmpleado();
                break;
            case 1:
                menuAprobarCuentas();
                break;
            case 2:
                mostrarCuentasPendientes();
                break;
            case 3:
                menuCuentas.mostrarTodasCuentas();
                break;
            case 4:
                mostrarTodosEmpleados();
                break;
        }
    }

    private void menuCrearEmpleado() {
        String nombre = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre completo del empleado:",
                "Crear Empleado",
                JOptionPane.QUESTION_MESSAGE);

        if (nombre == null) {
            return;
        }

        if (nombre.trim().isEmpty()) {
            Utilidades.mostrarError("El nombre no puede estar vacío");
            return;
        }

        String usuario = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre de usuario:",
                "Crear Empleado",
                JOptionPane.QUESTION_MESSAGE);

        if (usuario == null) {
            return;
        }

        if (usuario.trim().isEmpty()) {
            Utilidades.mostrarError("El nombre de usuario no puede estar vacío");
            return;
        }

        if (gestorUsuarios.buscarUsuario(usuario) != null) {
            Utilidades.mostrarError("El nombre de usuario ya existe. Intente con otro.");
            return;
        }

        String contrasena = JOptionPane.showInputDialog(
                null,
                "Ingrese la contraseña para el empleado:",
                "Crear Empleado",
                JOptionPane.QUESTION_MESSAGE);

        if (contrasena == null) {
            return;
        }

        if (contrasena.length() < 4) {
            Utilidades.mostrarError("La contraseña debe tener al menos 4 caracteres");
            return;
        }

        if (gestorUsuarios.crearEmpleado(nombre, usuario, contrasena)) {
            Utilidades.mostrarMensaje("Empleado creado exitosamente\n\n" +
                    "Nombre: " + nombre + "\n" +
                    "Usuario: " + usuario + "\n" +
                    "Rol: Empleado");
        } else {
            Utilidades.mostrarError("Error al crear el empleado");
        }
    }

    private void menuAprobarCuentas() {
        if (gestorCuentas.obtenerCuentasPendientes().isEmpty()) {
            Utilidades.mostrarError("No hay cuentas pendientes de aprobación");
            return;
        }

        String listado = "CUENTAS PENDIENTES DE APROBACIÓN\n\n";
        for (Cuenta cuenta : gestorCuentas.obtenerCuentasPendientes()) {
            listado += "Cuenta N°: " + cuenta.getNumeroCuenta() + "\n" +
                    "Titular: " + cuenta.getTitular() + "\n" +
                    "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                    "Saldo: $" + String.format("%.2f", cuenta.getSaldo()) + "\n" +
                    "------------------------\n";
        }

        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                listado + "\nIngrese el número de cuenta a aprobar o rechazar:",
                "Aprobar Cuentas",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        if (cuenta.aprobada()) {
            Utilidades.mostrarError("Esta cuenta ya está aprobada");
            return;
        }

        String[] opciones = { "Aprobar", "Rechazar", "Cancelar" };
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Cuenta N°: " + cuenta.getNumeroCuenta() + "\n" +
                        "Titular: " + cuenta.getTitular() + "\n" +
                        "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuenta.getSaldo()) + "\n\n" +
                        "¿Qué desea hacer con esta cuenta?",
                "Aprobar/Rechazar Cuenta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) {
            if (gestorCuentas.aprobarCuenta(numeroCuenta)) {
                Utilidades.mostrarMensaje("Cuenta aprobada exitosamente");
            } else {
                Utilidades.mostrarError("Error al aprobar la cuenta");
            }
        } else if (seleccion == 1) {
            if (gestorCuentas.rechazarCuenta(numeroCuenta)) {
                Utilidades.mostrarMensaje("Cuenta rechazada y eliminada del sistema");
            } else {
                Utilidades.mostrarError("Error al rechazar la cuenta");
            }
        }
    }

    private void mostrarCuentasPendientes() {
        if (gestorCuentas.obtenerCuentasPendientes().isEmpty()) {
            Utilidades.mostrarError("No hay cuentas pendientes de aprobación");
            return;
        }

        String listado = "CUENTAS PENDIENTES DE APROBACIÓN\n\n" +
                "Total: " + gestorCuentas.obtenerCuentasPendientes().size() + "\n\n";

        for (Cuenta cuenta : gestorCuentas.obtenerCuentasPendientes()) {
            listado += "Cuenta N°: " + cuenta.getNumeroCuenta() + "\n" +
                    "Titular: " + cuenta.getTitular() + "\n" +
                    "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                    "Saldo: $" + String.format("%.2f", cuenta.getSaldo()) + "\n" +
                    "------------------------\n";
        }

        JOptionPane.showMessageDialog(
                null,
                listado,
                "Cuentas Pendientes",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarTodosEmpleados() {
        if (gestorUsuarios.obtenerEmpleados().isEmpty()) {
            Utilidades.mostrarError("No hay empleados registrados en el sistema");
            return;
        }

        String listado = "EMPLEADOS REGISTRADOS\n\n" +
                "Total de empleados: " + gestorUsuarios.obtenerEmpleados().size() + "\n\n";

        for (Usuario empleado : gestorUsuarios.obtenerEmpleados()) {
            listado += "Usuario: " + empleado.getUsuario() + "\n" +
                    "Nombre: " + empleado.getNombre() + "\n" +
                    "------------------------\n";
        }

        JOptionPane.showMessageDialog(
                null,
                listado,
                "Listado de Empleados",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
