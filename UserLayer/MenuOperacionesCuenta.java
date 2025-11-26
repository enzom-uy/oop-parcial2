package UserLayer;

import LogicLayer.Cuenta;
import LogicLayer.GestorCuenta;
import LogicLayer.Movimiento;
import LogicLayer.ResultadoOperacion;
import LogicLayer.Rol;
import LogicLayer.Usuario;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class MenuOperacionesCuenta {

    private final GestorCuenta gestorCuentas;
    private final Usuario usuarioActual;
    private Cuenta cuentaActiva;

    public MenuOperacionesCuenta(GestorCuenta gestorCuentas, Usuario usuarioActual) {
        this.gestorCuentas = gestorCuentas;
        this.usuarioActual = usuarioActual;
        this.cuentaActiva = null;
    }

    public void setCuentaActiva(Cuenta cuenta) {
        this.cuentaActiva = cuenta;
    }

    public Cuenta getCuentaActiva() {
        return cuentaActiva;
    }

    public void menuCrearCuenta() {
        String titular;

        if (usuarioActual.getRol() == Rol.USUARIO) {
            titular = usuarioActual.getNombre();
        } else {
            titular = JOptionPane.showInputDialog(
                    null,
                    "Ingrese el nombre del titular:",
                    "Crear Cuenta",
                    JOptionPane.QUESTION_MESSAGE);

            if (titular == null) {
                return;
            }

            if (titular.trim().isEmpty()) {
                Utilidades.mostrarError("El nombre del titular no puede estar vacío");
                return;
            }
        }

        String saldoStr = JOptionPane.showInputDialog(
                null,
                "Ingrese el saldo inicial:",
                "Crear Cuenta",
                JOptionPane.QUESTION_MESSAGE);

        if (saldoStr == null) {
            return;
        }

        double saldoInicial;
        try {
            saldoInicial = Double.parseDouble(saldoStr);
        } catch (NumberFormatException e) {
            Utilidades.mostrarError("El saldo debe ser un número válido");
            return;
        }

        if (saldoInicial < 0) {
            Utilidades.mostrarError("El saldo inicial no puede ser negativo");
            return;
        }

        String[] tiposCuenta = { "Ahorro", "Corriente" };
        int tipoSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de cuenta:",
                "Crear Cuenta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposCuenta,
                tiposCuenta[0]);

        if (tipoSeleccionado == -1) {
            return;
        }

        String tipoCuenta = tiposCuenta[tipoSeleccionado];

        Cuenta nuevaCuenta = gestorCuentas.crearCuenta(titular, saldoInicial, tipoCuenta);

        if (nuevaCuenta != null) {
            Utilidades.mostrarMensaje(
                    "Cuenta creada exitosamente\n\n" +
                            "Número de Cuenta: " + nuevaCuenta.getNumeroCuenta() + "\n" +
                            "Titular: " + nuevaCuenta.getTitular() + "\n" +
                            "Tipo: " + nuevaCuenta.getTipoCuenta() + "\n" +
                            "Saldo Inicial: $" + String.format("%.2f", nuevaCuenta.getSaldo()));
        } else {
            Utilidades.mostrarError("Error al crear la cuenta");
        }
    }

    public void menuSeleccionarCuentaActiva() {
        // si no hay cuentas registradas en general
        if (gestorCuentas.contarCuentas() == 0) {
            Utilidades.mostrarError("No hay cuentas registradas. Cree una cuenta primero.");
            return;
        }

        String listaCuentas = "Seleccione el número de cuenta a activar:\n\n";

        int cuentasDelUsuario = 0;
        for (Cuenta cuenta : gestorCuentas.obtenerTodasCuentas()) {
            // no agrego la cuenta a la lista si no es del mismo usuario
            if (usuarioActual.getRol() == Rol.USUARIO && !cuenta.getTitular().equals(usuarioActual.getNombre())) {
                continue;
            }
            listaCuentas += "Cuenta N°: " + cuenta.getNumeroCuenta() + "\n" +
                    "Titular: " + cuenta.getTitular() + "\n" +
                    "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                    "Saldo: $" + String.format("%.2f", cuenta.getSaldo()) + "\n" +
                    "------------------------\n";
            cuentasDelUsuario++;
        }

        // si no hay cuentas registradas a nombre del usuario
        if (usuarioActual.getRol() == Rol.USUARIO && cuentasDelUsuario == 0) {
            Utilidades.mostrarError("No tiene cuentas registradas a su nombre. Cree una cuenta primero.");
            return;
        }

        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                listaCuentas + "\nIngrese el número de cuenta:",
                "Seleccionar Cuenta Activa",
                JOptionPane.QUESTION_MESSAGE);

        // checkeo que el usuario haya escrito algo
        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        // prevengo que el usuario seleccione como cuenta activa una cuenta que no es
        // suya
        if (usuarioActual.getRol() == Rol.USUARIO && !cuenta.getTitular().equals(usuarioActual.getNombre())) {
            Utilidades.mostrarError("No puede seleccionar una cuenta que no es suya");
            return;
        }

        cuentaActiva = cuenta;

        Utilidades.mostrarMensaje(
                "Cuenta activada exitosamente\n\n" +
                        "Número de Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
                        "Titular: " + cuenta.getTitular() + "\n" +
                        "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuenta.getSaldo()));
    }

    public void menuDepositar() {
        if (cuentaActiva == null) {
            Utilidades.mostrarError("No hay cuenta activa seleccionada.\nPor favor, seleccione una cuenta primero.");
            return;
        }

        String montoStr = JOptionPane.showInputDialog(
                null,
                "Cuenta Activa: " + cuentaActiva.getTitular() + " (N°: " + cuentaActiva.getNumeroCuenta() + ")\n" +
                        "Saldo actual: $" + String.format("%.2f", cuentaActiva.getSaldo()) + "\n\n" +
                        "Ingrese el monto a depositar:",
                "Depositar",
                JOptionPane.QUESTION_MESSAGE);

        if (montoStr == null) {
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            Utilidades.mostrarError("El monto debe ser un número válido");
            return;
        }

        ResultadoOperacion resultado = gestorCuentas.depositar(cuentaActiva, monto);

        if (resultado.isExito()) {
            Utilidades.mostrarMensaje(resultado.getMensaje() + "\n" +
                    "Nuevo saldo: $" + String.format("%.2f", resultado.getNuevoSaldo()));
        } else {
            Utilidades.mostrarError(resultado.getMensaje());
        }
    }

    public void menuRetirar() {
        if (cuentaActiva == null) {
            Utilidades.mostrarError("No hay cuenta activa seleccionada.\nPor favor, seleccione una cuenta primero.");
            return;
        }

        String montoStr = JOptionPane.showInputDialog(
                null,
                "Cuenta Activa: " + cuentaActiva.getTitular() + " (N°: " + cuentaActiva.getNumeroCuenta() + ")\n" +
                        "Saldo actual: $" + String.format("%.2f", cuentaActiva.getSaldo()) + "\n\n" +
                        "Ingrese el monto a retirar:",
                "Retirar",
                JOptionPane.QUESTION_MESSAGE);

        if (montoStr == null) {
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            Utilidades.mostrarError("El monto debe ser un número válido");
            return;
        }

        ResultadoOperacion resultado = gestorCuentas.retirar(cuentaActiva, monto);

        if (resultado.isExito()) {
            Utilidades.mostrarMensaje(resultado.getMensaje() + "\n" +
                    "Nuevo saldo: $" + String.format("%.2f", resultado.getNuevoSaldo()));
        } else {
            Utilidades.mostrarError(resultado.getMensaje());
        }
    }

    public void menuTransferir() {
        if (gestorCuentas.contarCuentas() < 2) {
            Utilidades.mostrarError("Se necesitan al menos 2 cuentas para realizar una transferencia");
            return;
        }

        String numeroCuentaOrigen = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de cuenta ORIGEN:",
                "Transferir",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuentaOrigen == null) {
            return;
        }

        Cuenta cuentaOrigen = gestorCuentas.buscarCuenta(numeroCuentaOrigen);

        if (cuentaOrigen == null) {
            Utilidades.mostrarError("Cuenta de origen no encontrada");
            return;
        }

        String numeroCuentaDestino = JOptionPane.showInputDialog(
                null,
                "Cuenta Origen: " + cuentaOrigen.getTitular() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuentaOrigen.getSaldo()) + "\n\n" +
                        "Ingrese el número de cuenta DESTINO:",
                "Transferir",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuentaDestino == null) {
            return;
        }

        Cuenta cuentaDestino = gestorCuentas.buscarCuenta(numeroCuentaDestino);

        if (cuentaDestino == null) {
            Utilidades.mostrarError("Cuenta de destino no encontrada");
            return;
        }

        String montoStr = JOptionPane.showInputDialog(
                null,
                "De: " + cuentaOrigen.getTitular() + " (Cta: " + cuentaOrigen.getNumeroCuenta() + ")\n" +
                        "Saldo: $" + String.format("%.2f", cuentaOrigen.getSaldo()) + "\n\n" +
                        "Para: " + cuentaDestino.getTitular() + " (Cta: " + cuentaDestino.getNumeroCuenta() + ")\n\n" +
                        "Ingrese el monto a transferir:",
                "Transferir",
                JOptionPane.QUESTION_MESSAGE);

        if (montoStr == null) {
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            Utilidades.mostrarError("El monto debe ser un número válido");
            return;
        }

        ResultadoOperacion resultado = gestorCuentas.transferir(cuentaOrigen, cuentaDestino, monto);

        if (resultado.isExito()) {
            Utilidades.mostrarMensaje(resultado.getMensaje() + "\n\n" +
                    "Nuevo saldo cuenta origen: $" + String.format("%.2f", resultado.getNuevoSaldo()));
        } else {
            Utilidades.mostrarError(resultado.getMensaje());
        }
    }

    public void menuConsultarSaldo() {
        if (gestorCuentas.contarCuentas() == 0) {
            Utilidades.mostrarError("No hay cuentas registradas. Cree una cuenta primero.");
            return;
        }

        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de cuenta:",
                "Consultar Saldo",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        Utilidades.mostrarMensaje(
                "INFORMACIÓN DE CUENTA\n\n" +
                        "Número de Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
                        "Titular: " + cuenta.getTitular() + "\n" +
                        "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuenta.getSaldo()));
    }

    public void mostrarTodasCuentas() {
        if (gestorCuentas.contarCuentas() == 0) {
            Utilidades.mostrarError("No hay cuentas registradas en el sistema");
            return;
        }

        String listado = "CUENTAS REGISTRADAS\n\n" +
                "Total de cuentas: " + gestorCuentas.contarCuentas() + "\n\n";

        for (Cuenta cuenta : gestorCuentas.obtenerTodasCuentas()) {
            listado += "Cuenta N°: " + cuenta.getNumeroCuenta() + "\n" +
                    "Titular: " + cuenta.getTitular() + "\n" +
                    "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                    "Saldo: $" + String.format("%.2f", cuenta.getSaldo()) + "\n" +
                    "------------------------\n";
        }

        JOptionPane.showMessageDialog(
                null,
                listado,
                "Listado de Cuentas",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void menuBuscarCuenta() {
        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de cuenta a buscar:",
                "Buscar Cuenta",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        Utilidades.mostrarMensaje(
                "CUENTA ENCONTRADA\n\n" +
                        "Número de Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
                        "Titular: " + cuenta.getTitular() + "\n" +
                        "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuenta.getSaldo()));
    }

    public void menuConsultarSaldoCuenta() {
        if (gestorCuentas.contarCuentas() == 0) {
            Utilidades.mostrarError("No hay cuentas registradas en el sistema");
            return;
        }

        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                "Total de cuentas: " + gestorCuentas.contarCuentas() + "\n\n" +
                        "Ingrese el número de cuenta a consultar:",
                "Consultar Saldo",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        Utilidades.mostrarMensaje(
                "INFORMACIÓN DE CUENTA\n\n" +
                        "Número de Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
                        "Titular: " + cuenta.getTitular() + "\n" +
                        "Tipo: " + cuenta.getTipoCuenta() + "\n" +
                        "Saldo: $" + String.format("%.2f", cuenta.getSaldo()));
    }

    public void menuVerHistorial() {
        if (gestorCuentas.contarCuentas() == 0) {
            Utilidades.mostrarError("No hay cuentas registradas en el sistema");
            return;
        }

        String numeroCuenta = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de cuenta para ver su historial:",
                "Historial de Movimientos",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroCuenta == null) {
            return;
        }

        Cuenta cuenta = gestorCuentas.buscarCuenta(numeroCuenta);

        if (cuenta == null) {
            Utilidades.mostrarError("Cuenta no encontrada");
            return;
        }

        if (usuarioActual.getRol() == Rol.USUARIO && !cuenta.getTitular().equals(usuarioActual.getNombre())) {
            Utilidades.mostrarError("No puede ver el historial de una cuenta que no es suya");
            return;
        }

        LinkedList<Movimiento> movimientos = gestorCuentas.obtenerHistorialCuenta(numeroCuenta);

        if (movimientos.isEmpty()) {
            Utilidades.mostrarMensaje("No hay movimientos registrados para esta cuenta");
            return;
        }

        String historial = "HISTORIAL DE MOVIMIENTOS\n\n" +
                "Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
                "Titular: " + cuenta.getTitular() + "\n" +
                "Total de movimientos: " + movimientos.size() + "\n\n";

        historial += "========================\n\n";

        for (Movimiento movimiento : movimientos) {
            historial += movimiento.toString() + "\n";
        }

        JOptionPane.showMessageDialog(
                null,
                historial,
                "Historial de Movimientos",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
