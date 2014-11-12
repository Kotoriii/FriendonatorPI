package Database;

/**
 * Created by laboratorio on 12/11/2014.
 */
public class Contacto {

    String idContacto;
    String idUsuario;
    String descripcion;
    String modofavorito;
    String activo;

    public Contacto() {
    }

    public String getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(String idContacto) {
        this.idContacto = idContacto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModofavorito() {
        return modofavorito;
    }

    public void setModofavorito(String modofavorito) {
        this.modofavorito = modofavorito;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
