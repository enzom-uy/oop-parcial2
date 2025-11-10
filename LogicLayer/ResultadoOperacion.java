package LogicLayer;

public class ResultadoOperacion {
    private boolean exito;
    private String mensaje;
    private double nuevoSaldo;

    public ResultadoOperacion(boolean exito, String mensaje,
            double nuevoSaldo) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nuevoSaldo = nuevoSaldo;
    }

    public ResultadoOperacion(boolean exito, String mensaje) {
        this(exito, mensaje, 0.0);
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public double getNuevoSaldo() {
        return nuevoSaldo;
    }

    public static ResultadoOperacion exitoso(String mensaje) {
        return new ResultadoOperacion(true, mensaje);
    }

    public static ResultadoOperacion exitoso(String mensaje,
            double nuevoSaldo) {
        return new ResultadoOperacion(true, mensaje, nuevoSaldo);
    }

    public static ResultadoOperacion fallido(String mensaje) {
        return new ResultadoOperacion(false, mensaje);
    }

    public static ResultadoOperacion fallido(String mensaje,
            double saldoActual) {
        return new ResultadoOperacion(false, mensaje, saldoActual);
    }

    @Override
    public String toString() {
        String estado = exito ? "✓ ÉXITO" : "✗ ERROR";
        String info = estado + "\n" + mensaje;

        if (nuevoSaldo > 0 || !exito) {
            info += "\nSaldo: $" + String.format("%.2f", nuevoSaldo);
        }

        return info;
    }
}
