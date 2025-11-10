package UserLayer;

import LogicLayer.GestorCuenta;
import LogicLayer.GestorUsuarios;
import LogicLayer.Rol;
import LogicLayer.Usuario;

public class MenuPrincipal {

    private final GestorCuenta gestorCuentas;
    private final GestorUsuarios gestorUsuarios;
    private Usuario usuarioActual;

    public MenuPrincipal() {
        this.gestorCuentas = new GestorCuenta();
        this.gestorUsuarios = new GestorUsuarios();
        this.usuarioActual = null;
    }

    public void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            if (!autenticarUsuario()) {
                Utilidades.mostrarMensaje("Saliendo del sistema...");
                return;
            }

            boolean continuarSesion = true;

            while (continuarSesion) {
                int resultado = ejecutarMenuSegunRol();

                if (resultado == 0) {
                    continuarSesion = false;
                } else if (resultado == -1) {
                    salir = true;
                    continuarSesion = false;
                }
            }

            if (salir == true) {
                usuarioActual = null;
                Utilidades.mostrarMensaje("Sesión cerrada correctamente");
            }
        }

        Utilidades.mostrarMensaje("Gracias por usar el Sistema Bancario");
    }

    private boolean autenticarUsuario() {
        MenuAutenticacion menuAuth = new MenuAutenticacion(gestorUsuarios, gestorCuentas);
        int opcion = menuAuth.mostrarMenuInicio();

        if (opcion == -1) {
            return false;
        }

        if (opcion == 0) {
            usuarioActual = menuAuth.iniciarSesion();
        } else if (opcion == 1) {
            usuarioActual = menuAuth.registrarseYIniciar();
        }

        return usuarioActual != null;
    }

    private int ejecutarMenuSegunRol() {
        MenuOperacionesCuenta menuCuentas = new MenuOperacionesCuenta(gestorCuentas, usuarioActual);

        if (usuarioActual.getRol() == Rol.ADMINISTRADOR) {
            MenuAdministrador menuAdmin = new MenuAdministrador(usuarioActual, gestorCuentas, gestorUsuarios,
                    menuCuentas);
            return menuAdmin.mostrarMenu();
        } else if (usuarioActual.getRol() == Rol.EMPLEADO) {
            MenuEmpleado menuEmpleado = new MenuEmpleado(usuarioActual, gestorCuentas, gestorUsuarios, menuCuentas);
            return menuEmpleado.mostrarMenu();
        } else {
            MenuUsuario menuUsuario = new MenuUsuario(usuarioActual, menuCuentas);
            return menuUsuario.mostrarMenu();
        }
    }

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.mostrarMenuPrincipal();
    }
}
