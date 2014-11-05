package Database;

/**
 * Created by Christian on 10/30/2014.
 */
public class Usuariointereses {

    String idinteres;
    String idusuario;

    public Usuariointereses() {
    }

    public Usuariointereses(String idinteres, String idusuario) {
        this.idinteres = idinteres;
        this.idusuario = idusuario;
    }

    public String getIdinteres() {
        return idinteres;
    }

    public void setIdinteres(String idinteres) {
        this.idinteres = idinteres;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }
}
