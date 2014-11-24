package Database;

/**
 * Created by Christian on 10/30/2014.
 */
public class Historial {

    String idMatch;
    String idusuario;
    String matchPerc;
    String latitud;
    String longitud;
    String fecha;


    public Historial() {
    }

    public String getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(String idMatch) {
        this.idMatch = idMatch;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getMatchPerc() {
        return matchPerc;
    }

    public void setMatchPerc(String matchPerc) {
        this.matchPerc = matchPerc;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}


