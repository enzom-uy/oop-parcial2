package LogicLayer;

public enum Rol {
    USUARIO("Usuario"),
    EMPLEADO("Empleado"),
    ADMINISTRADOR("Administrador");

    private String nombre;

    Rol(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
