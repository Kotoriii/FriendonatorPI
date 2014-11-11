package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 10/30/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

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

    String createHist = "CREATE TABLE historial (" +
            "idUsuario INTEGER," +
            "latitud VARCHAR," +
            "longitud VARCHAR," +
            "fecha VARCHAR," +
            "PRIMARY KEY (idUsuario)" +
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
            "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAuth);
        db.execSQL(createUsuario);
        db.execSQL(createHist);
        db.execSQL(createSuperinter);
        db.execSQL(createInter);
        db.execSQL(createUserinter);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public void insertAuth(Limbo auth) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUsuario", auth.getId());
        values.put("password", auth.getPassword());

        db.insert("limbo", null, values);
        db.close();
    }

    public void insertHistorial(Historial hist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idUsuario", hist.getIdusuario());
        values.put("fecha", hist.getFecha());
        values.put("latitud", hist.getLatitud());
        values.put("longitud", hist.getLongitud());

        db.insert("historial", null, values);
        db.close();
    }

    public void insertInteres(Intereses inter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idIntereses", inter.getId());
        values.put("idSuperInteres", inter.getIdsuperinteres());
        values.put("Descripcion", inter.getDescripcion());

        db.insert("intereses", null, values);
        db.close();
    }

    public void insertSuperinter(Superinteres superint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idSuperInteres", superint.getId());
        values.put("Descripcion", superint.getDescripcion());

        db.insert("superinteres", null, values);
        db.close();
    }

    public void insertUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idUsuario", user.getId());
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

        db.insert("usuario", null, values);
        db.close();
    }

    public void insertUserint(Usuariointereses userint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idInteres", userint.getIdinteres());
        values.put("idUsuario", userint.getIdusuario());

        db.insert("usuariointereses", null, values);
        db.close();
    }

    public int updateHistorial(Historial hist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha", hist.getFecha());
        values.put("latitud", hist.getLatitud());
        values.put("longitud", hist.getLongitud());

        return db.update("historial", values, "idUsuario=?", new String[] { hist.getIdusuario() });
    }

    public int updateInteres(Intereses intereses) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idSuperInteres", intereses.getIdsuperinteres());
        values.put("Descripcion", intereses.getDescripcion());

        return db.update("intereses", values, "idIntereses=?", new String[] { intereses.getId() });
    }

    public int updateSuperinteres(Superinteres superint) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Descripcion", superint.getDescripcion());

        return db.update("superinteres", values, "idSuperInteres=?", new String[] { superint.getId() });
    }

    public int updateUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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

        return db.update("usuario", values, "idUsuario=?", new String[] { user.getId() });
    }


    public List<Historial> getAllHistorial() {
        List<Historial> historialList = new ArrayList<Historial>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM historial", null);

        if(cursor.moveToFirst()) {
            do {
                Historial historial = new Historial();
                historial.setIdusuario(cursor.getString(0));
                historial.setFecha(cursor.getString(1));
                historial.setLatitud(cursor.getString(2));
                historial.setLongitud(cursor.getString(3));
                historialList.add(historial);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return historialList;
    }

    public List<Intereses> getAllIntereses() {
        List<Intereses> interesesList = new ArrayList<Intereses>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM intereses", null);

        if(cursor.moveToFirst()) {
            do {
                Intereses intereses = new Intereses();
                intereses.setId(cursor.getString(0));
                intereses.setIdsuperinteres(cursor.getString(1));
                intereses.setDescripcion(cursor.getString(2));
                interesesList.add(intereses);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return interesesList;
    }

    public List<Superinteres> getAllSuperinter() {
        List<Superinteres> superinterList = new ArrayList<Superinteres>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM superinteres", null);
        if(cursor.moveToFirst()) {
            do {
                Superinteres superinteres = new Superinteres();
                superinteres.setId(cursor.getString(0));
                superinteres.setDescripcion(cursor.getString(1));
                superinterList.add(superinteres);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return superinterList;
    }

    public List<Usuariointereses> getAllUsuarioIntereses() {
        List<Usuariointereses> intereses = new ArrayList<Usuariointereses>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM usuariointereses", null);

        if(cursor.moveToFirst()) {
            do {
                Usuariointereses uInteres = new Usuariointereses();
                uInteres.setIdinteres(cursor.getString(0));
                uInteres.setIdusuario(cursor.getString(1));
                intereses.add(uInteres);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return intereses;
    }

    public List<Intereses> getAllUserInterests(int idUser) {
        List<Intereses> interests = new ArrayList<Intereses>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM intereses, usuariointereses " +
                                    "WHERE intereses.idIntereses = usuariointereses.idInteres AND usuariointereses.idUsuario=?" +
                                    "GROUP BY intereses.idIntereses", new String[] {String.valueOf(idUser)});

        if(cursor.moveToFirst()) {
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

        if(cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getString(0));
                usuario.setDob(cursor.getString(1));
                usuario.setCorreo(cursor.getString(2));
                usuario.setPassword(cursor.getString(3));
                usuario.setNum(cursor.getString(4));
                usuario.setFb(cursor.getString(5));
                usuario.setGplus(cursor.getString(6));
                usuario.setTwitter(cursor.getString(7));
                usuario.setModfav(cursor.getString(8));
                usuario.setFoto(cursor.getString(9));
                usuario.setMatchp(cursor.getString(10));
                usuarioList.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return usuarioList;
    }

    public Usuario getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = new Usuario();

        Cursor cursor=db.query("usuario", null, "correo=?", new String[]{email}, null, null, null);

        if(cursor.moveToFirst()){
            usuario.setId(cursor.getString(0));
            usuario.setDob(cursor.getString(1));
            usuario.setCorreo(cursor.getString(2));
            usuario.setPassword(cursor.getString(3));
            usuario.setNum(cursor.getString(4));
            usuario.setFb(cursor.getString(5));
            usuario.setGplus(cursor.getString(6));
            usuario.setTwitter(cursor.getString(7));
            usuario.setModfav(cursor.getString(8));
            usuario.setFoto(cursor.getString(9));
            usuario.setMatchp(cursor.getString(10));
        }

        cursor.close();

        return usuario;
    }

    public int deleteUserInterestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("usuariointereses", null, null);
    }

}
