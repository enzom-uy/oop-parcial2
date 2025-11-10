package UserLayer;

import LogicLayer.GestorCuenta;
import LogicLayer.GestorUsuarios;
import LogicLayer.Usuario;
import javax.swing.JOptionPane;

public class MenuEmpleado {

    private final Usuario usuarioActual;
    private final GestorCuenta gestorCuentas;
    private final GestorUsuarios gestorUsuarios;
    private final MenuOperacionesCuenta menuCuentas;

    public MenuEmpleado(Usuario usuarioActual, GestorCuenta gestorCuentas, GestorUsuarios gestorUsuarios,
            MenuOperacionesCuenta menuCuentas) {
        this.usuarioActual = usuarioActual;
        this.gestorCuentas = gestorCuentas;
        this.gestorUsuarios = gestorUsuarios;
        this.menuCuentas = menuCuentas;
    }

    public int mostrarMenu() {
        String[] opciones = {
                "Crear Cuenta",
                "Ver Todas las Cuentas",
                "Buscar Cuenta",
                "Consultar Saldo de Cuenta",
                "Ver Todos los Usuarios",
                "Cerrar Sesión",
                "Salir del Sistema"
        };

        String mensajeMenu = "MENÚ EMPLEADO - " + usuarioActual.getNombre() + "\n\n" +
                "Total de cuentas: " + gestorCuentas.contarCuentas() + "\n" +
                "Total de usuarios: " + gestorUsuarios.contarUsuarios() + "\n";

        int seleccion = JOptionPane.showOptionDialog(
                null,
                mensajeMenu,
                "Sistema Bancario - Menú Empleado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

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
                menuCuentas.menuCrearCuenta();
                break;
            case 1:
                menuCuentas.mostrarTodasCuentas();
                break;
            case 2:
                menuCuentas.menuBuscarCuenta();
                break;
            case 3:
                menuCuentas.menuConsultarSaldoCuenta();
                break;
            case 4:
                mostrarTodosUsuarios();
                break;
        }
    }

    private void mostrarTodosUsuarios() {
        if (gestorUsuarios.contarUsuarios() == 0) {
            Utilidades.mostrarError("No hay usuarios registrados en el sistema");
            return;
        }

        String listado = "USUARIOS REGISTRADOS\n\n" +
                "Total de usuarios: " + gestorUsuarios.contarUsuarios() + "\n\n";

        for (Usuario usuario : gestorUsuarios.obtenerTodosUsuarios()) {
            listado += "Usuario: " + usuario.getUsuario() + "\n" +
                    "Nombre: " + usuario.getNombre() + "\n" +
                    "Rol: " + usuario.getRol().getNombre() + "\n" +
                    "------------------------\n";
        }

        JOptionPane.showMessageDialog(
                null,
                listado,
                "Listado de Usuarios",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
