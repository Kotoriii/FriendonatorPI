package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 10/30/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public final String INTERESES = "intereses";
    public final String DATOS_PERSONALES = "datos_us";
    public final String IMAGEN_PERFIL = "imagen_perfil";
    public final String TEXTOS = "textos";
    public final String HISTORIAL = "historial";

    private static SQLiteHelper sInstance;

    public static SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context, "BaseFriendonator", null, 1);
        }
        return sInstance;
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    String createAuth = "CREATE TABLE limbo (" +
            "idUsuario INTEGER," +
            "password VARCHAR," +
            "PRIMARY KEY (idUsuario)" +
            ")";

    String createSync = "CREATE TABLE sync (" +
            "descripcion VARCHAR," +
            "cambiado INTEGER," +
            "PRIMARY KEY (descripcion)" +
            ")";

    String createMACBT = "CREATE TABLE mac_bt (" +
            "idUsuario INTEGER," +
            "MAC VARCHAR," +
            "PRIMARY KEY (idUsuario, MAC)" +
            ")";


    //diria que esto es un poco overkill jaja
    String createNombreBluetooth = "CREATE TABLE nombreB (" +
            "nombre VARCHAR," +
            "PRIMARY KEY (nombre)" +
            ")";

    String createHist = "CREATE TABLE historial (" +
            "idMatch INTEGER," +
            "idUsuario INTEGER," +
            "matchPerc INTEGER," +
            "matchName VARCHAR," +
            "latitud VARCHAR," +
            "longitud VARCHAR," +
            "fecha VARCHAR," +
            "PRIMARY KEY (idMatch, idUsuario)" +
            "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario)" +
            ")";

    String createInter = "CREATE TABLE intereses (" +
            "idIntereses INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idSuperInteres INTEGER," +
            "Descripcion VARCHAR," +
            "FOREIGN KEY(idSuperInteres) REFERENCES superinteres(idSuperInteres)" +
            ")";

    String createSuperinter = "CREATE TABLE superinteres (" +
            "idSuperInteres INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Descripcion VARCHAR" +
            ")";

    String createUsuario = "CREATE TABLE usuario (" +
            "idUsuario INTEGER PRIMARY KEY," +
            "nombre VARCHAR," +
            "fecha_de_nacimiento VARCHAR," +
            "correo VARCHAR," +
            //agregado password hasta resolver asunto ese
            "password VARCHAR," +
            "numero_telefono VARCHAR," +
            "facebook VARCHAR," +
            "google_p VARCHAR," +
            "Twitter VARCHAR," +
            "modo_favorito VARCHAR," +
            "foto_perfil VARCHAR," +
            "match_percentage VARCHAR" +
            ")";

    String createUserinter = "CREATE TABLE usuariointereses (" +
            "idInteres INTEGER," +
            "idUsuario INTEGER," +
            "PRIMARY KEY (idInteres,idUsuario)" +
            "FOREIGN KEY(idInteres) REFERENCES intereses(idIntereses), " +
            "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario)" +
            ")";

    String createTextointer = "CREATE TABLE textointeres (" +
            "idSuperInteres INTEGER," +
            "idUsuario INTEGER," +
            "texto VARCHAR," +
            "PRIMARY KEY (idSuperInteres,idUsuario)" +
            "FOREIGN KEY(idSuperInteres) REFERENCES superinteres(idSuperInteres), " +
            "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario))";

    String createContacto = "CREATE TABLE contacto (" +
            "idContacto INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idUsuario INTEGER," +
            "descripcion VARCHAR," +
            "modofavorito INTEGER," +
            "activo INTEGER," +
            "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario))";

    String createConfig = "CREATE TABLE config (" +
            "idUsuario INTEGER PRIMARY KEY," +
            "minmatch INTEGER," +
            "notific VARCHAR," +
            "sound VARCHAR," +
            "vibration VARCHAR," +
            "interval VARCHAR," +
            "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAuth);
        db.execSQL(createUsuario);
        db.execSQL(createHist);
        db.execSQL(createSuperinter);
        db.execSQL(createInter);
        db.execSQL(createUserinter);
        db.execSQL(createTextointer);
        db.execSQL(createContacto);
        db.execSQL(createConfig);
        db.execSQL(createSync);
        db.execSQL(createNombreBluetooth);
        db.execSQL(createMACBT);

        //se inicializan los observadores de cambios
        db.execSQL("insert into sync (descripcion, cambiado) VALUES ('" + this.INTERESES + "',0)");
        db.execSQL("insert into sync (descripcion, cambiado) VALUES ('" + this.IMAGEN_PERFIL + "',0)");
        db.execSQL("insert into sync (descripcion, cambiado) VALUES ('" + this.DATOS_PERSONALES + "',0)");
        db.execSQL("insert into sync (descripcion, cambiado) VALUES ('" + this.TEXTOS + "',0)");
        db.execSQL("insert into sync (descripcion, cambiado) VALUES ('" + this.HISTORIAL + "',0)");

        //inicializa nombreBluetooth.
        db.execSQL("insert into nombreB (nombre) VALUES ('banana')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void salvarNombreBluetooth(String nuevo_nombre){
        if(this.getNombreBluetooth().equals(nuevo_nombre)){
            return;
        }else{
            this.getWritableDatabase().execSQL(
                    "update nombreB set nombre='"+nuevo_nombre+"'"
            );
        }
    }
    public String getNombreBluetooth(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nombreB", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("nombre"));
    }

    public String getUserIDFROMMAC(String MAC){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mac_bt where MAC='"+MAC+"'", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("idUsuario"));
    }

    public String getMACFROMID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mac_bt where idUsuario="+id, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("MAC"));
    }

    public void insertMAC_BT(int id_us, String Address){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM mac_bt where idUSuario="+id_us
                , null);
        //si existe
        if(cursor.moveToFirst()){
            db.execSQL(
                    "update mac_bt set MAC='"+Address+"' where idUsuario="+id_us
            );
        }else{
            db.execSQL(
                    "insert into mac_bt (idUsuario, MAC) VALUES" +
                            " ("+id_us+",'"+Address+"')"
            );
        }
    }


    public void updateSync(String descripcion, int cambiado) {
        getWritableDatabase().execSQL("update sync set cambiado=" + cambiado + " where descripcion='" + descripcion + "'");
    }

    public boolean textosCambiaron() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        Cursor cursor = db.rawQuery("SELECT * FROM sync where descripcion='" + this.TEXTOS + "'", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("cambiado")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean interesesCambiaron() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        Cursor cursor = db.rawQuery("SELECT * FROM sync where descripcion='" + this.INTERESES + "'", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("cambiado")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean datosPCambiaron() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        Cursor cursor = db.rawQuery("SELECT * FROM sync where descripcion='" + this.DATOS_PERSONALES + "'", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("cambiado")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean imgPerfCambiaron() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        Cursor cursor = db.rawQuery("SELECT * FROM sync where descripcion='" + this.IMAGEN_PERFIL + "'", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("cambiado")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean historialCambiaron() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        Cursor cursor = db.rawQuery("SELECT * FROM sync where descripcion='" + this.HISTORIAL + "'", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("cambiado")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkDataBase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean emptyTable = true;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM superinteres", null);
            if (cursor.moveToFirst())
                emptyTable = false;
            cursor.close();
        } catch (SQLiteException sqlE) {
            emptyTable = false;
        }

        return emptyTable;
    }

    /**
     * retorna el usuario que es el propietario del telefono
     *
     * @return
     */
    public Usuario getLimbo1() {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = new Usuario();

        Cursor cursor = db.rawQuery("select * from limbo", null);

            if (cursor.moveToFirst()) {
                usuario.setId(cursor.getString(cursor.getColumnIndex("idUsuario")));
                usuario.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            }


        cursor.close();

        return usuario;
    }

    /**
     * retorna el usuario que es el propietario del telefono
     *
     * @return
     */
    public boolean hay_algo_en_limbo() {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("select * from limbo", null);
        //si moveToFirst quiere decir q hay algo en limbo
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else
            cursor.close();
        return false;
    }

    public void insertTexto(TextoInteres text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idSuperInteres", text.getIdSuperInteres());
        values.put("idUsuario", text.getUsuario());
        values.put("texto", text.getTexto());

        db.insert("textointeres", null, values);

    }

    public void insertAuth(Limbo auth) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", auth.getId());
        values.put("password", auth.getPassword());

        db.insert("limbo", null, values);

    }

    public void insertHistorial(Historial hist) { // ToDo change this to match the new table values
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idMatch", hist.getIdMatch());
        values.put("idUsuario", hist.getIdusuario());
        values.put("matchPerc", hist.getMatchPerc());
        values.put("matchName", hist.getMatchName());
        values.put("fecha", hist.getFecha());
        values.put("latitud", hist.getLatitud());
        values.put("longitud", hist.getLongitud());

        db.insert("historial", null, values);

    }

    public void insertInteres(Intereses inter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idSuperInteres", inter.getIdsuperinteres());
        values.put("Descripcion", inter.getDescripcion());

        db.insert("intereses", null, values);

    }

    public void insertInteresCust(Intereses inter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idSuperInteres", inter.getIdsuperinteres());
        values.put("idIntereses", inter.getId());
        values.put("Descripcion", inter.getDescripcion());

        db.insert("intereses", null, values);

    }

    public void insertSuperinter(Superinteres superint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idSuperInteres", superint.getId());
        values.put("Descripcion", superint.getDescripcion());

        //db.insert("superinteres", null, values);
        db.execSQL("insert into superinteres (idSuperInteres, Descripcion) " +
                "VALUES ("+superint.getId()+",'"+superint.getDescripcion()+"')");

    }

    public void insertUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", user.getId());
        values.put("nombre", user.getNombre());
        values.put("fecha_de_nacimiento", user.getDob());
        values.put("correo", user.getCorreo());
        //values.put("password", user.getPassword());
        values.put("numero_telefono", user.getNum());
        values.put("facebook", user.getFb());
        values.put("google_p", user.getGplus());
        values.put("Twitter", user.getTwitter());
        values.put("modo_favorito", user.getModfav());
        values.put("foto_perfil", user.getFoto());
        values.put("match_percentage", user.getMatchp());

        db.insert("usuario", null, values);

    }

    public void insertUserint(Usuariointereses userint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idInteres", userint.getIdinteres());
        values.put("idUsuario", userint.getIdusuario());

        db.execSQL("insert into usuariointereses " +
                "(idInteres, idUsuario) VALUES ("+userint.getIdinteres()+", "+userint.getIdusuario()+") ");
        //db.insert("usuariointereses", null, values);

    }

    public void insertContacto(Contacto contacto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", contacto.getIdUsuario());
        values.put("descripcion", contacto.getDescripcion());
        values.put("modofavorito", contacto.getModofavorito());
        values.put("activo", contacto.getActivo());
        db.insert("contacto", null, values);

    }

    public void insertConfig(Configuracion config) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", config.getIdUsuario());
        values.put("minmatch", config.getMinmatch());
        values.put("notific", config.getNotific());
        values.put("sound", config.getSound());
        values.put("vibration", config.getVibration());
        values.put("interval", config.getInterval());
        db.insert("config", null, values);

    }

    public int updateHistorial(Historial hist) { // ToDo change this to match the new table values
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("matchPerc", hist.getMatchPerc());
        values.put("fecha", hist.getFecha());
        values.put("latitud", hist.getLatitud());
        values.put("longitud", hist.getLongitud());

        return db.update("historial", values, "idMatch=?", new String[]{hist.getIdMatch()});
    }


    public int updateInteres(Intereses intereses) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Descripcion", intereses.getDescripcion());

        return db.update("intereses", values, "idIntereses=?", new String[]{intereses.getId()});
    }

    public int updateSuperinteres(Superinteres superint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Descripcion", superint.getDescripcion());

        return db.update("superinteres", values, "idSuperInteres=?", new String[]{superint.getId()});
    }

    public int updateTextointeres(TextoInteres text) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idTexto", text.getIdSuperInteres());
        values.put("idUsuario", text.getUsuario());
        values.put("texto", text.getTexto());

        return db.update("textointeres", values, "idUsuario=?", new String[]{text.getUsuario()});
    }

    public int updateUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", user.getNombre());
        values.put("fecha_de_nacimiento", user.getDob());
        values.put("correo", user.getCorreo());
        values.put("password", user.getPassword());
        values.put("numero_telefono", user.getNum());
        values.put("facebook", user.getFb());
        values.put("google_p", user.getGplus());
        values.put("Twitter", user.getTwitter());
        values.put("modo_favorito", user.getModfav());
        values.put("foto_perfil", user.getFoto());
        values.put("match_percentage", user.getMatchp());

        return db.update("usuario", values, "idUsuario=?", new String[]{user.getId()});
    }

    public int updateUserProfileName(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", user.getNombre());

        return db.update("usuario", values, "idUsuario=?", new String[]{user.getId()});
    }

    public int updateContacto(Contacto contacto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", contacto.getIdUsuario());
        values.put("descripcion", contacto.getDescripcion());
        values.put("modofavorito", contacto.getModofavorito());
        values.put("activo", contacto.getActivo());

        return db.update("contacto", values, "idContacto=?", new String[]{contacto.getIdContacto()});
    }

    public int updateConfig(Configuracion config) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("minmatch", config.getMinmatch());
        values.put("notific", config.getNotific());
        values.put("sound", config.getSound());
        values.put("vibration", config.getVibration());
        values.put("interval", config.getInterval());
        db.insert("config", null, values);


        return db.update("config", values, "idUsuario=?", new String[]{config.getIdUsuario()});
    }

    public List<Contacto> getAllContactos(int idUser) {
        List<Contacto> contactoList = new ArrayList<Contacto>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM contacto WHERE activo=?", new String[]{String.valueOf(idUser)});
        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto();
                contacto.setIdContacto(cursor.getString(0));
                contacto.setIdUsuario(cursor.getString(1));
                contacto.setDescripcion(cursor.getString(2));
                contacto.setModofavorito(cursor.getString(3));
                contacto.setActivo(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return contactoList;

    }

    public List<Historial> getAllHistorial() { // ToDo change this to match the new table values
        List<Historial> historialList = new ArrayList<Historial>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM historial", null);

        if (cursor.moveToFirst()) {
            do {
                Historial historial = new Historial();
                historial.setIdMatch(cursor.getString(0));
                historial.setIdusuario(cursor.getString(1));
                historial.setMatchPerc(cursor.getString(2));
                historial.setMatchName(cursor.getString(3));
                historial.setLatitud(cursor.getString(4));
                historial.setLongitud(cursor.getString(5));
                historial.setFecha(cursor.getString(6));
                historialList.add(historial);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return historialList;
    }

    public List<Superinteres> getAllSuperinter() {
        List<Superinteres> superinterList = new ArrayList<Superinteres>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM superinteres", null);
        if (cursor.moveToFirst()) {
            do {
                Superinteres superinteres = new Superinteres();
                superinteres.setId(cursor.getString(cursor.getColumnIndex("idSuperInteres")));
                superinteres.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
                superinterList.add(superinteres);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return superinterList;
    }

    public List<Intereses> getAllUserInterests(int idUser) {
        List<Intereses> interests = new ArrayList<Intereses>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM intereses, usuariointereses " +
                "WHERE intereses.idIntereses = usuariointereses.idInteres AND usuariointereses.idUsuario=?" +
                "GROUP BY intereses.idIntereses", new String[]{String.valueOf(idUser)});

        if (cursor.moveToFirst()) {
            do {
                Intereses intereses = new Intereses();
                intereses.setId(cursor.getString(0));
                intereses.setIdsuperinteres(cursor.getString(1));
                intereses.setDescripcion(cursor.getString(2));
                interests.add(intereses);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return interests;
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarioList = new ArrayList<Usuario>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getString(cursor.getColumnIndex("idUsuario")));
                usuario.setDob(cursor.getString(cursor.getColumnIndex("fecha_de_nacimiento")));
                usuario.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                usuario.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                usuario.setNum(cursor.getString(cursor.getColumnIndex("numero_telefono")));
                usuario.setFb(cursor.getString(cursor.getColumnIndex("facebook")));
                usuario.setGplus(cursor.getString(cursor.getColumnIndex("google_p")));
                usuario.setTwitter(cursor.getString(cursor.getColumnIndex("Twitter")));
                usuario.setModfav(cursor.getString(cursor.getColumnIndex("modo_favorito")));
                usuario.setFoto(cursor.getString(cursor.getColumnIndex("foto_perfil")));
                usuario.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                usuario.setMatchp(cursor.getString(cursor.getColumnIndex("match_percentage")));
                usuarioList.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return usuarioList;
    }

    public List<TextoInteres> getAllInterestTexts(int idUser) {
        List<TextoInteres> textList = new ArrayList<TextoInteres>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM textointeres WHERE idUsuario=?", new String[]{String.valueOf(idUser)});

        if (cursor.moveToFirst()) {
            do {
                TextoInteres text = new TextoInteres();
                text.setIdSuperInteres(cursor.getString(0));
                text.setUsuario(cursor.getString(1));
                text.setTexto(cursor.getString(2));
                textList.add(text);
            } while (cursor.moveToNext());
        }

        return textList;
    }

    public Usuario getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = new Usuario();

        Cursor cursor = db.query("usuario", null, "correo=?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()) {
            usuario.setId(cursor.getString(cursor.getColumnIndex("idUsuario")));
            usuario.setDob(cursor.getString(cursor.getColumnIndex("fecha_de_nacimiento")));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
            usuario.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            usuario.setNum(cursor.getString(cursor.getColumnIndex("numero_telefono")));
            usuario.setFb(cursor.getString(cursor.getColumnIndex("facebook")));
            usuario.setGplus(cursor.getString(cursor.getColumnIndex("google_p")));
            usuario.setTwitter(cursor.getString(cursor.getColumnIndex("Twitter")));
            usuario.setModfav(cursor.getString(cursor.getColumnIndex("modo_favorito")));
            usuario.setFoto(cursor.getString(cursor.getColumnIndex("foto_perfil")));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            usuario.setMatchp(cursor.getString(cursor.getColumnIndex("match_percentage")));
        }

        cursor.close();

        return usuario;
    }

    public Configuracion getConfig(String idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Configuracion config = new Configuracion();

        Cursor cursor = db.query("config", null, "idUsuario=?", new String[]{idUsuario}, null, null, null);
        if (cursor.moveToFirst()) {
            config.setIdUsuario(cursor.getString(cursor.getColumnIndex("idUsuario")));
            config.setMinmatch(cursor.getString(cursor.getColumnIndex("minmatch")));
            config.setNotific(cursor.getString(cursor.getColumnIndex("notific")));
            config.setSound(cursor.getString(cursor.getColumnIndex("sound")));
            config.setVibration(cursor.getString(cursor.getColumnIndex("vibration")));
            config.setInterval(cursor.getString(cursor.getColumnIndex("interval")));
        }

        cursor.close();

        return config;
    }

    public Usuario getUserByID(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = new Usuario();

        Cursor cursor = db.query("usuario", null, "idUsuario=?", new String[]{String.valueOf(userId)}, null, null, null);

            if (cursor.moveToFirst()) {
                usuario.setId(cursor.getString(cursor.getColumnIndex("idUsuario")));
                usuario.setDob(cursor.getString(cursor.getColumnIndex("fecha_de_nacimiento")));
                usuario.setCorreo(cursor.getString(cursor.getColumnIndex("correo")));
                usuario.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                usuario.setNum(cursor.getString(cursor.getColumnIndex("numero_telefono")));
                usuario.setFb(cursor.getString(cursor.getColumnIndex("facebook")));
                usuario.setGplus(cursor.getString(cursor.getColumnIndex("google_p")));
                usuario.setTwitter(cursor.getString(cursor.getColumnIndex("Twitter")));
                usuario.setModfav(cursor.getString(cursor.getColumnIndex("modo_favorito")));
                usuario.setFoto(cursor.getString(cursor.getColumnIndex("foto_perfil")));
                usuario.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                usuario.setMatchp(cursor.getString(cursor.getColumnIndex("match_percentage")));
            }


        cursor.close();

        return usuario;
    }

    public int deleteUserInterestData(String idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("usuariointereses", "idUsuario=?", new String[]{idUser});
    }

    public int deleteUserTextData(String idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("textointeres", "idUsuario=?", new String[]{idUser});
    }

    public TextoInteres getTexto(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        TextoInteres texto = new TextoInteres();

        Cursor cursor = db.query("textointeres", null, " idTexto=?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            texto.setIdSuperInteres(cursor.getString(0));
            texto.setUsuario(cursor.getString(1));
            texto.setTexto(cursor.getString(2));
        }
        cursor.close();

        return texto;
    }

    public int updateUserContacts(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("numero_telefono", user.getNum());
        values.put("facebook", user.getFb());
        values.put("google_p", user.getGplus());
        values.put("Twitter", user.getTwitter());

        return db.update("usuario", values, "idUsuario=?", new String[]{user.getId()});
    }

    public void insertlimbo(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", usuario.getId());
        values.put("password", usuario.getPassword());

        db.insert("limbo", null, values);

    }
}
