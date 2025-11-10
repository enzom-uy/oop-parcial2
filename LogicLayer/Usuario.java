package LogicLayer;

public class Usuario {
    private String nombre;
    private String usuario;
    private String contrasena;
    private Rol rol;

    public Usuario(String nombre, String usuario, String contrasena, Rol rol) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    @Override
    public String toString() {
        return "Usuario: " + usuario + " | Nombre: " + nombre + " | Rol: " + rol.getNombre();
    }
}
