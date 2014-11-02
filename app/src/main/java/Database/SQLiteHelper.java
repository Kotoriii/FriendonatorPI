package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 10/30/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    private static SQLiteHelper sInstance;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    String createAuth = "CREATE TABLE autenticacion (" +
            "passwordUS varchar(50) NOT NULL," +
            "PRIMARY KEY (passwordUS)" +
            ")";

    String createHist = "CREATE TABLE historial (" +
            "idUsuario integer NOT NULL," +
            "latitud varchar(45)," +
            "longitud varchar(45)," +
            "PRIMARY KEY (idUsuario)" +
            ")";

    String createInter = "CREATE TABLE intereses (" +
            "idIntereses integer NOT NULL," +
            "idSuperInteres integer NOT NULL," +
            "Descripcion varchar(45)," +
            "PRIMARY KEY (idIntereses)" +
            ")";

    String createSuperinter = "CREATE TABLE superinteres (" +
            "idSuperInteres integer NOT NULL," +
            "Descripcion varchar(45)," +
            "PRIMARY KEY (idSuperInteres)" +
            ")";

    String createUsuario = "CREATE TABLE usuario (" +
            "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "fecha_de_nacimiento varchar(45)," +
            "correo varchar(45) NOT NULL UNIQUE," +
            //agregado password hasta resolver asunto ese
            "password varchar(45) NOT NULL," +
            "numero_telefono varchar(45)," +
            "facebook varchar(45)," +
            "google_p varchar(45)," +
            "Twitter varchar(45)," +
            "modo_favorito varchar(45)," +
            "foto_perfil varchar(45)," +
            ")";

    String createUserinter = "CREATE TABLE usuariointereses (" +
            "idInteres integer NOT NULL," +
            "idUsuario integer NOT NULL," +
            "PRIMARY KEY (idInteres,idUsuario)" +
            ")";



    // ALTERS



    String alterHist = "ALTER TABLE historial" +
            "ADD FOREIGN KEY (idUsuario) " +
            "REFERENCES usuario (idUsuario)";

    String alterInter = "ALTER TABLE intereses" +
            "ADD FOREIGN KEY (idSuperInteres) " +
            "REFERENCES superinteres (idSuperInteres)";

    String alterUsuariointer = "ALTER TABLE usuariointereses" +
            "ADD FOREIGN KEY (idInteres)" +
            "REFERENCES intereses (idIntereses)";



    public static SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context, "BaseFriendonator", null, 1);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAuth);
        db.execSQL(createHist);
        db.execSQL(createInter);
        db.execSQL(createSuperinter);
        db.execSQL(createUserinter);
        db.execSQL(createUsuario);
        db.execSQL(alterHist);
        db.execSQL(alterInter);
        db.execSQL(alterUsuariointer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertAuth(Autenticacion auth) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("passwordUS", auth.getPasswordUS());

        db.insert("autenticacion", null, values);
        db.close();
    }

    public void insertHistorial(Historial hist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idUsuario", hist.getIdusuario());
        values.put("latitud", hist.getLatitud());
        values.put("longitud", hist.getLongitud());

        db.insert("historial", null, values);
        db.close();
    }

    public void insertInteres(Intereses inter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("idIntereses", inter.getId());
        //values.put("idSuperInteres", inter.getIdsuperinteres());
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
                historial.setLatitud(cursor.getString(1));
                historial.setLongitud(cursor.getString(2));
                historialList.add(historial);
            } while (cursor.moveToNext());
        }

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

        return superinterList;
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
                usuarioList.add(usuario);
            } while (cursor.moveToNext());
        }

        return usuarioList;
    }





}
