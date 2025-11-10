package UserLayer;

import LogicLayer.GestorCuenta;
import LogicLayer.GestorUsuarios;
import LogicLayer.Rol;
import LogicLayer.Usuario;
import javax.swing.JOptionPane;

public class MenuAutenticacion {

    private GestorUsuarios gestorUsuarios;
    private GestorCuenta gestorCuentas;

    public MenuAutenticacion(GestorUsuarios gestorUsuarios, GestorCuenta gestorCuentas) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorCuentas = gestorCuentas;
    }

    public int mostrarMenuInicio() {
        String[] opciones = {
                "Iniciar Sesión",
                "Registrarse",
                "Salir"
        };

        String mensajeMenu = "SISTEMA BANCARIO\n\n" +
                "Bienvenido/a\n\n" +
                "Seleccione una opción:";

        int seleccion = JOptionPane.showOptionDialog(
                null,
                mensajeMenu,
                "Sistema Bancario",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == -1 || seleccion == 2) {
            return -1;
        }

        return seleccion;
    }

    public Usuario iniciarSesion() {
        String usuario = JOptionPane.showInputDialog(
                null,
                "INICIO DE SESIÓN\n\n" +
                        "Usuarios de prueba:\n" +
                        "Admin: admin / admin123\n" +
                        "Empleado: empleado1 / admin\n\n" +
                        "Ingrese su nombre de usuario:",
                "Inicio de Sesión",
                JOptionPane.QUESTION_MESSAGE);

        if (usuario == null) {
            return null;
        }

        if (usuario.trim().isEmpty()) {
            Utilidades.mostrarError("El nombre de usuario no puede estar vacío");
            return iniciarSesion();
        }

        String contrasena = JOptionPane.showInputDialog(
                null,
                "Usuario: " + usuario + "\n\n" +
                        "Ingrese su contraseña:",
                "Inicio de Sesión",
                JOptionPane.QUESTION_MESSAGE);

        if (contrasena == null) {
            return null;
        }

        if (contrasena.trim().isEmpty()) {
            Utilidades.mostrarError("La contraseña no puede estar vacía");
            return iniciarSesion();
        }

        Usuario usuarioActual = gestorUsuarios.autenticar(usuario, contrasena);

        if (usuarioActual == null) {
            Utilidades.mostrarError("Usuario o contraseña incorrectos.\nPor favor, intente nuevamente.");
            return iniciarSesion();
        }

        Utilidades.mostrarMensaje("Bienvenido/a, " + usuarioActual.getNombre() + "!\n" +
                "Rol: " + usuarioActual.getRol().getNombre());

        return usuarioActual;
    }

    public Usuario registrarseYIniciar() {
        String nombre = JOptionPane.showInputDialog(
                null,
                "REGISTRO DE USUARIO\n\n" +
                        "Ingrese su nombre completo:",
                "Registro",
                JOptionPane.QUESTION_MESSAGE);

        if (nombre == null) {
            return null;
        }

        if (nombre.trim().isEmpty()) {
            Utilidades.mostrarError("El nombre no puede estar vacío");
            return registrarseYIniciar();
        }

        String usuario = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + "\n\n" +
                        "Ingrese un nombre de usuario\n" +
                        "(será usado para iniciar sesión):",
                "Registro",
                JOptionPane.QUESTION_MESSAGE);

        if (usuario == null) {
            return null;
        }

        if (usuario.trim().isEmpty()) {
            Utilidades.mostrarError("El nombre de usuario no puede estar vacío");
            return registrarseYIniciar();
        }

        if (gestorUsuarios.buscarUsuario(usuario) != null) {
            Utilidades.mostrarError("El nombre de usuario ya está en uso.\nPor favor, elija otro.");
            return registrarseYIniciar();
        }

        String contrasena = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + "\n" +
                        "Usuario: " + usuario + "\n\n" +
                        "Ingrese una contraseña:",
                "Registro",
                JOptionPane.QUESTION_MESSAGE);

        if (contrasena == null) {
            return null;
        }

        if (contrasena.trim().isEmpty()) {
            Utilidades.mostrarError("La contraseña no puede estar vacía");
            return registrarseYIniciar();
        }

        if (contrasena.length() < 4) {
            Utilidades.mostrarError("La contraseña debe tener al menos 4 caracteres");
            return registrarseYIniciar();
        }

        String confirmarContrasena = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + "\n" +
                        "Usuario: " + usuario + "\n\n" +
                        "Confirme su contraseña:",
                "Registro",
                JOptionPane.QUESTION_MESSAGE);

        if (confirmarContrasena == null) {
            return null;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Utilidades.mostrarError("Las contraseñas no coinciden.\nPor favor, intente nuevamente.");
            return registrarseYIniciar();
        }

        boolean registrado = gestorUsuarios.registrarUsuario(nombre, usuario, contrasena, Rol.USUARIO);

        if (!registrado) {
            Utilidades.mostrarError("Error al registrar el usuario.\nPor favor, intente nuevamente.");
            return registrarseYIniciar();
        }

        Usuario usuarioActual = gestorUsuarios.autenticar(usuario, contrasena);

        Utilidades.mostrarMensaje("Registro exitoso!\n\n" +
                "Bienvenido/a, " + nombre + "!\n" +
                "Usuario: " + usuario);

        int opcion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea crear una cuenta bancaria ahora?",
                "Crear Cuenta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION && usuarioActual != null) {
            MenuOperacionesCuenta menuCuentas = new MenuOperacionesCuenta(gestorCuentas, usuarioActual);
            menuCuentas.menuCrearCuenta();
        }

        return usuarioActual;
    }
}
