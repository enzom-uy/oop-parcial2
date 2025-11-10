package LogicLayer;

import java.util.LinkedList;

public class GestorUsuarios {
    private LinkedList<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new LinkedList<>();
        inicializarUsuariosPorDefecto();
    }

    private void inicializarUsuariosPorDefecto() {
        usuarios.add(new Usuario("María García", "empleado1", "admin", Rol.EMPLEADO));
        usuarios.add(new Usuario("Admin Sistema", "admin", "admin123", Rol.ADMINISTRADOR));
    }

    public Usuario autenticar(String usuario, String contrasena) {
        if (usuario == null || contrasena == null) {
            return null;
        }

        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario) && u.validarContrasena(contrasena)) {
                return u;
            }
        }

        return null;
    }

    public boolean registrarUsuario(String nombre, String usuario, String contrasena, Rol rol) {
        if (nombre == null || usuario == null || contrasena == null || rol == null) {
            return false;
        }

        if (nombre.trim().isEmpty() || usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return false;
        }

        if (buscarUsuario(usuario) != null) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(nombre, usuario, contrasena, rol);
        usuarios.add(nuevoUsuario);
        return true;
    }

    public Usuario buscarUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return null;
        }

        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario)) {
                return u;
            }
        }

        return null;
    }

    public LinkedList<Usuario> obtenerTodosUsuarios() {
        return usuarios;
    }

    public int contarUsuarios() {
        return usuarios.size();
    }

    public boolean crearEmpleado(String nombre, String usuario, String contrasena) {
        return registrarUsuario(nombre, usuario, contrasena, Rol.EMPLEADO);
    }

    public LinkedList<Usuario> obtenerEmpleados() {
        LinkedList<Usuario> empleados = new LinkedList<>();
        for (Usuario u : usuarios) {
            if (u.getRol() == Rol.EMPLEADO) {
                empleados.add(u);
            }
        }
        return empleados;
    }
}
