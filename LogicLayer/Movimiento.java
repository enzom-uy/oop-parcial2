package LogicLayer;

// vendría a ser como el "ticket" de una transacción
public class Movimiento {

    private String numeroCuenta;
    private String tipoMovimiento;
    private double monto;
    private String fecha;
    private String descripcion;
    private double saldoAnterior;
    private double saldoNuevo;

    public Movimiento(String numeroCuenta, String tipoMovimiento, double monto, String descripcion,
            double saldoAnterior, double saldoNuevo) {
        this.numeroCuenta = numeroCuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.fecha = obtenerFechaActual();
        this.descripcion = descripcion;
        this.saldoAnterior = saldoAnterior;
        this.saldoNuevo = saldoNuevo;
    }

    private String obtenerFechaActual() {
        java.util.Date fechaActual = new java.util.Date();
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formato.format(fechaActual);
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public double getSaldoNuevo() {
        return saldoNuevo;
    }

    @Override
    public String toString() {
        String signo = "";
        if (tipoMovimiento.equals("DEPOSITO") || tipoMovimiento.equals("TRANSFERENCIA RECIBIDA")) {
            signo = "+";
        } else if (tipoMovimiento.equals("RETIRO") || tipoMovimiento.equals("TRANSFERENCIA ENVIADA")) {
            signo = "-";
        }

        return "Fecha: " + fecha + "\n" +
                "Tipo: " + tipoMovimiento + "\n" +
                "Monto: " + signo + "$" + String.format("%.2f", monto) + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Saldo anterior: $" + String.format("%.2f", saldoAnterior) + "\n" +
                "Saldo nuevo: $" + String.format("%.2f", saldoNuevo) + "\n" +
                "------------------------";
    }
}
