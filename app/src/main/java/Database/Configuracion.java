package Database;

/**
 * Created by laboratorio on 03/12/2014.
 */
public class Configuracion {

    String idUsuario;
    String minmatch;
    String notific;
    String sound;
    String vibration;
    String interval;

    public Configuracion() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getNotific() {
        return notific;
    }

    public void setNotific(String notific) {
        this.notific = notific;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getMinmatch() {
        return minmatch;
    }

    public void setMinmatch(String minmatch) {
        this.minmatch = minmatch;
    }
}
