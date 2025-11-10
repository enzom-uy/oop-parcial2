package UserLayer;

import javax.swing.JOptionPane;

public class Utilidades {

    public static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Sistema Bancario",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
