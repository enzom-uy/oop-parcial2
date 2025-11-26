package LogicLayer;

import java.util.LinkedList;

public class GestorMovimientos {

    private LinkedList<Movimiento> movimientos;

    public GestorMovimientos() {
        this.movimientos = new LinkedList<>();
    }

    public void registrarMovimiento(String numeroCuenta, String tipoMovimiento, double monto, String descripcion,
            double saldoAnterior, double saldoNuevo) {
        Movimiento nuevoMovimiento = new Movimiento(numeroCuenta, tipoMovimiento, monto, descripcion, saldoAnterior,
                saldoNuevo);
        movimientos.add(nuevoMovimiento);
    }

    public LinkedList<Movimiento> obtenerMovimientosPorCuenta(String numeroCuenta) {
        LinkedList<Movimiento> movimientosCuenta = new LinkedList<>();

        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            return movimientosCuenta;
        }

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getNumeroCuenta().equals(numeroCuenta)) {
                movimientosCuenta.add(movimiento);
            }
        }

        return movimientosCuenta;
    }

    public LinkedList<Movimiento> obtenerUltimosMovimientos(String numeroCuenta, int cantidad) {
        LinkedList<Movimiento> movimientosCuenta = obtenerMovimientosPorCuenta(numeroCuenta);
        LinkedList<Movimiento> ultimosMovimientos = new LinkedList<>();

        if (movimientosCuenta.isEmpty()) {
            return ultimosMovimientos;
        }

        int inicio = Math.max(0, movimientosCuenta.size() - cantidad);

        for (int i = movimientosCuenta.size() - 1; i >= inicio; i--) {
            ultimosMovimientos.add(movimientosCuenta.get(i));
        }

        return ultimosMovimientos;
    }

    public int contarMovimientos(String numeroCuenta) {
        return obtenerMovimientosPorCuenta(numeroCuenta).size();
    }

    public LinkedList<Movimiento> obtenerTodosMovimientos() {
        return movimientos;
    }
}
