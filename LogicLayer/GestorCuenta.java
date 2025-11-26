package LogicLayer;

import java.util.LinkedList;

public class GestorCuenta {

    private LinkedList<Cuenta> cuentas;
    private GestorMovimientos gestorMovimientos;

    public GestorCuenta() {
        this.cuentas = new LinkedList<>();
        this.gestorMovimientos = new GestorMovimientos();
    }

    public Cuenta crearCuenta(String titular, double saldoInicial, String tipoCuenta) {
        if (titular == null || titular.trim().isEmpty()) {
            return null;
        }

        if (saldoInicial < 0) {
            return null;
        }

        Cuenta nuevaCuenta = new Cuenta(titular, saldoInicial, tipoCuenta);
        cuentas.add(nuevaCuenta);

        return nuevaCuenta;
    }

    public ResultadoOperacion depositar(Cuenta cuenta, double monto) {
        if (cuenta == null) {
            return ResultadoOperacion.fallido("La cuenta no existe");
        }

        if (!cuenta.aprobada()) {
            return ResultadoOperacion
                    .fallido("La cuenta no está aprobada. Debe esperar la aprobación del administrador.");
        }

        if (monto <= 0) {
            return ResultadoOperacion.fallido(
                    "El monto debe ser mayor a cero",
                    cuenta.getSaldo());
        }

        double saldoAnterior = cuenta.getSaldo();
        double nuevoSaldo = saldoAnterior + monto;
        cuenta.setSaldo(nuevoSaldo);

        gestorMovimientos.registrarMovimiento(
                cuenta.getNumeroCuenta(),
                "DEPOSITO",
                monto,
                "Depósito en cuenta",
                saldoAnterior,
                nuevoSaldo);

        return ResultadoOperacion.exitoso(
                "Depósito de $" + String.format("%.2f", monto) + " realizado correctamente",
                nuevoSaldo);
    }

    public ResultadoOperacion retirar(Cuenta cuenta, double monto) {
        if (cuenta == null) {
            return ResultadoOperacion.fallido("La cuenta no existe");
        }

        if (!cuenta.aprobada()) {
            return ResultadoOperacion
                    .fallido("La cuenta no está aprobada. Debe esperar la aprobación del administrador.");
        }

        if (monto <= 0) {
            return ResultadoOperacion.fallido(
                    "El monto debe ser mayor a cero",
                    cuenta.getSaldo());
        }

        if (cuenta.getSaldo() < monto) {
            return ResultadoOperacion.fallido(
                    "Saldo insuficiente. Saldo disponible: $" +
                            String.format("%.2f", cuenta.getSaldo()),
                    cuenta.getSaldo());
        }

        double saldoAnterior = cuenta.getSaldo();
        double nuevoSaldo = saldoAnterior - monto;
        cuenta.setSaldo(nuevoSaldo);

        gestorMovimientos.registrarMovimiento(
                cuenta.getNumeroCuenta(),
                "RETIRO",
                monto,
                "Retiro de efectivo",
                saldoAnterior,
                nuevoSaldo);

        return ResultadoOperacion.exitoso(
                "Retiro de $" + String.format("%.2f", monto) + " realizado correctamente",
                nuevoSaldo);
    }

    public ResultadoOperacion transferir(Cuenta origen, Cuenta destino, double monto) {
        if (origen == null) {
            return ResultadoOperacion.fallido("La cuenta de origen no existe");
        }

        if (!origen.aprobada()) {
            return ResultadoOperacion.fallido("La cuenta de origen no está aprobada.");
        }

        if (destino == null) {
            return ResultadoOperacion.fallido("La cuenta de destino no existe");
        }

        if (!destino.aprobada()) {
            return ResultadoOperacion.fallido("La cuenta de destino no está aprobada.");
        }

        if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta())) {
            return ResultadoOperacion.fallido(
                    "No puede transferir a la misma cuenta",
                    origen.getSaldo());
        }
        if (monto <= 0) {
            return ResultadoOperacion.fallido(
                    "El monto debe ser mayor a cero",
                    origen.getSaldo());
        }

        if (origen.getSaldo() < monto) {
            return ResultadoOperacion.fallido(
                    "Saldo insuficiente en cuenta origen. Saldo disponible: $" +
                            String.format("%.2f", origen.getSaldo()),
                    origen.getSaldo());
        }

        double saldoAnteriorOrigen = origen.getSaldo();
        double saldoAnteriorDestino = destino.getSaldo();
        double nuevoSaldoOrigen = saldoAnteriorOrigen - monto;
        double nuevoSaldoDestino = saldoAnteriorDestino + monto;

        origen.setSaldo(nuevoSaldoOrigen);
        destino.setSaldo(nuevoSaldoDestino);

        gestorMovimientos.registrarMovimiento(
                origen.getNumeroCuenta(),
                "TRANSFERENCIA ENVIADA",
                monto,
                "Transferencia a " + destino.getTitular() + " (Cta: " + destino.getNumeroCuenta() + ")",
                saldoAnteriorOrigen,
                nuevoSaldoOrigen);

        gestorMovimientos.registrarMovimiento(
                destino.getNumeroCuenta(),
                "TRANSFERENCIA RECIBIDA",
                monto,
                "Transferencia de " + origen.getTitular() + " (Cta: " + origen.getNumeroCuenta() + ")",
                saldoAnteriorDestino,
                nuevoSaldoDestino);

        return ResultadoOperacion.exitoso(
                "Transferencia de $" + String.format("%.2f", monto) +
                        " realizada correctamente\n" +
                        "De: " + origen.getTitular() + " (Cta: " + origen.getNumeroCuenta() + ")\n" +
                        "Para: " + destino.getTitular() + " (Cta: " + destino.getNumeroCuenta() + ")",
                nuevoSaldoOrigen);
    }

    public double consultarSaldo(Cuenta cuenta) {
        if (cuenta == null) {
            return -1;
        }
        return cuenta.getSaldo();
    }

    public Cuenta buscarCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            return null;
        }

        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }

        return null;
    }

    public LinkedList<Cuenta> obtenerTodasCuentas() {
        return cuentas;
    }

    public int contarCuentas() {
        return cuentas.size();
    }

    public LinkedList<Cuenta> obtenerCuentasPendientes() {
        LinkedList<Cuenta> pendientes = new LinkedList<>();
        for (Cuenta cuenta : cuentas) {
            if (!cuenta.aprobada()) {
                pendientes.add(cuenta);
            }
        }
        return pendientes;
    }

    public boolean aprobarCuenta(String numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            return false;
        }
        cuenta.setAprobada(true);
        return true;
    }

    public boolean rechazarCuenta(String numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            return false;
        }
        cuentas.remove(cuenta);
        return true;
    }

    public LinkedList<Movimiento> obtenerHistorialCuenta(String numeroCuenta) {
        return gestorMovimientos.obtenerMovimientosPorCuenta(numeroCuenta);
    }

    public LinkedList<Movimiento> obtenerUltimosMovimientos(String numeroCuenta, int cantidad) {
        return gestorMovimientos.obtenerUltimosMovimientos(numeroCuenta, cantidad);
    }

    public int contarMovimientos(String numeroCuenta) {
        return gestorMovimientos.contarMovimientos(numeroCuenta);
    }
}
