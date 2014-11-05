package Database;

/**
 * Created by Christian on 10/30/2014.
 */
public class Superinteres {

    String id;
    String descripcion;

    public Superinteres() {
    }

    public Superinteres(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
