package UserLayer;

import LogicLayer.Cuenta;
import LogicLayer.Usuario;
import javax.swing.JOptionPane;

public class MenuUsuario {

    private final Usuario usuarioActual;
    private final MenuOperacionesCuenta menuCuentas;

    public MenuUsuario(Usuario usuarioActual, MenuOperacionesCuenta menuCuentas) {
        this.usuarioActual = usuarioActual;
        this.menuCuentas = menuCuentas;
    }

    public int mostrarMenu() {
        String[] opciones = {
                "Crear Cuenta",
                "Seleccionar Cuenta Activa",
                "Depositar",
                "Retirar",
                "Transferir",
                "Consultar Saldo",
                "Ver Todas las Cuentas",
                "Ver Historial de Movimientos",
                "Cerrar Sesión",
                "Salir del Sistema"
        };

        Cuenta cuentaActiva = menuCuentas.getCuentaActiva();
        String mensajeMenu = "MENÚ USUARIO - " + usuarioActual.getNombre() + "\n\n";

        if (cuentaActiva != null) {
            mensajeMenu += "Cuenta Activa: " + cuentaActiva.getNumeroCuenta() + "\n" +
                    "Titular: " + cuentaActiva.getTitular() + "\n" +
                    "Saldo: $" + String.format("%.2f", cuentaActiva.getSaldo()) + "\n";
        } else {
            mensajeMenu += "No hay cuenta activa seleccionada\n";
        }

        int seleccion = JOptionPane.showOptionDialog(
                null,
                mensajeMenu,
                "Sistema Bancario - Menú Usuario",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == -1 || seleccion == 9) {
            return -1;
        } else if (seleccion == 8) {
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
                menuCuentas.menuSeleccionarCuentaActiva();
                break;
            case 2:
                menuCuentas.menuDepositar();
                break;
            case 3:
                menuCuentas.menuRetirar();
                break;
            case 4:
                menuCuentas.menuTransferir();
                break;
            case 5:
                menuCuentas.menuConsultarSaldo();
                break;
            case 6:
                menuCuentas.mostrarTodasCuentas();
                break;
            case 7:
                menuCuentas.menuVerHistorial();
                break;
        }
    }
}
