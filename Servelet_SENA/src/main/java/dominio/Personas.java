package dominio;

public class Personas {
    private int idPersona;
    private String nombre;
    private String email;
    private String password;

    public Personas(int idPersona, String nombre, String email, String password) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public Personas(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public Personas(int idPersona) {
        this.idPersona = idPersona;
    }

    public Personas() {

    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Personas{" +
                "idPersona=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
