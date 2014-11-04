package Database;

/**
 * Created by Christian on 10/30/2014.
 */
public class Usuario {

    String id;
    String dob;
    String correo;
    String num;
    String fb;
    String twitter;
    String gplus;
    String modfav;
    String foto;
    String password;
    String matchp;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGplus() {
        return gplus;
    }

    public void setGplus(String gplus) {
        this.gplus = gplus;
    }

    public String getModfav() {
        return modfav;
    }

    public void setModfav(String modfav) {
        this.modfav = modfav;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMatchp() {
        return matchp;
    }

    public void setMatchp(String matchp) {
        this.matchp = matchp;
    }
}
