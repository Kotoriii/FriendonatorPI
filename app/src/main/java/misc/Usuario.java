package misc;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by andrea on 17/10/14.
 */
public class Usuario implements Serializable {

    //variables estaticas para saber cual modo es su favorito
    public static final int MODO_TELEFONO = 1;
    public static final int MODO_CORREO = 2;
    public static final int MODO_FACEBOOK = 3;
    public static final int MODO_GPLUS = 4;
    public static final int MODO_TWITTER = 5;

    //Atributos del usuario
    private String nombre = null;
    private String password = null;
    private Date fecha_de_nacimiento = null;
    private String correo = null;
    private String numero_telefono = null;
    private String facebookID = null;
    private String googleP_id = null;
    private String twitter_id = null;
    private int modo_de_cont_favorito;
    private Bitmap foto_perfil = null;
    private List<String> intereses = null;
    private List<Usuario> historial = null;

    public Usuario(String nombre, String password, Date fecha_de_nacimiento, String correo,
                   String numero_telefono, String facebookID, String googleP_id, String twitter_id,
                   int modo_de_cont_favorito, Bitmap foto_perfil, List<String> intereses,
                   List<Usuario> historial) {
        this.nombre = nombre;
        this.password = password;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.correo = correo;
        this.numero_telefono = numero_telefono;
        this.facebookID = facebookID;
        this.googleP_id = googleP_id;
        this.twitter_id = twitter_id;
        this.modo_de_cont_favorito = modo_de_cont_favorito;
        this.foto_perfil = foto_perfil;
        this.intereses = intereses;
        this.historial = historial;
    }


    public static Usuario newTestUsuario(){
        return new Usuario("nombre", "contra", (new Date()), "correo", "numeroTel", "FacebbokID", "googlePID", "TwitterD", 1, null, new ArrayList<String>(), new ArrayList<Usuario>());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFecha_de_nacimiento() {
        return fecha_de_nacimiento;
    }

    public void setFecha_de_nacimiento(Date fecha_de_nacimiento) {
        this.fecha_de_nacimiento = fecha_de_nacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getGoogleP_id() {
        return googleP_id;
    }

    public void setGoogleP_id(String googleP_id) {
        this.googleP_id = googleP_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public int getModo_de_cont_favorito() {
        return modo_de_cont_favorito;
    }

    public void setModo_de_cont_favorito(int modo_de_cont_favorito) {
        this.modo_de_cont_favorito = modo_de_cont_favorito;
    }

    public Bitmap getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(Bitmap foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public List<String> getIntereses() {
        return intereses;
    }

    public void addIntereses(String interes) {
        this.intereses.add(interes);
    }

    public List<Usuario> getHistorial() {
        return historial;
    }

    public void addHistorial(Usuario usuario) {
        this.historial.add(usuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", fecha_de_nacimiento=" + fecha_de_nacimiento +
                ", correo='" + correo + '\'' +
                ", numero_telefono='" + numero_telefono + '\'' +
                ", facebookID='" + facebookID + '\'' +
                ", googleP_id='" + googleP_id + '\'' +
                ", twitter_id='" + twitter_id + '\'' +
                ", modo_de_cont_favorito=" + modo_de_cont_favorito +
                ", foto_perfil=" + foto_perfil +
                ", intereses=" + intereses +
                ", historial=" + historial +
                '}';
    }
}
