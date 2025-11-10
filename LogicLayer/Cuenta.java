package LogicLayer;

public class Cuenta {

    private String numeroCuenta;
    private String titular;
    private double saldo;
    private String tipoCuenta;
    private boolean aprobada;

    private static int contadorCuentas = 1000;

    public Cuenta(String titular, double saldoInicial, String tipoCuenta) {
        this.numeroCuenta = generarNumeroCuenta();
        this.titular = titular;
        this.saldo = saldoInicial;
        this.tipoCuenta = tipoCuenta;
        this.aprobada = false;
    }

    public Cuenta(String titular, double saldoInicial) {
        this(titular, saldoInicial, "Ahorro");
    }

    private String generarNumeroCuenta() {
        contadorCuentas++;
        return String.valueOf(contadorCuentas);
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public boolean aprobada() {
        return aprobada;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    @Override
    public String toString() {
        String estado = aprobada ? "APROBADA" : "PENDIENTE DE APROBACIÓN";
        return "INFORMACIÓN DE CUENTA\n" +
                "Número de Cuenta: " + numeroCuenta + "\n" +
                "Titular: " + titular + "\n" +
                "Tipo: " + tipoCuenta + "\n" +
                "Estado: " + estado + "\n" +
                "Saldo: $" + String.format("%.2f", saldo);
    }
}
