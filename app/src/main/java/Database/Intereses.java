package Database;

/**
 * Created by Christian on 10/30/2014.
 */
public class Intereses {

    String id;
    String idsuperinteres;
    String descripcion;


    public Intereses() {
    }

    public Intereses(String id, String descripcion,  String idsuperinteres) {
        this.id = id;
        this.idsuperinteres = idsuperinteres;
        this.descripcion = descripcion;
    }

    public Intereses(String idsuperinteres, String descripcion) {
        this.idsuperinteres = idsuperinteres;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdsuperinteres() {
        return idsuperinteres;
    }

    public void setIdsuperinteres(String idsuperinteres) {
        this.idsuperinteres = idsuperinteres;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
