package misc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;
import com.pi314.interests.InterestsMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Database.Configuracion;
import Database.Historial;
import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
import Database.Usuario;

/**
 * Created by andrea on 11/12/14.
 */
public class SyncWithServer extends ApiWrapper {
    private Activity mAct;
    private Person mP;
    private HttpClient httpClient = AndroidHttpClient.newInstance("Android");

    public SyncWithServer(Activity Act) {
        super(Act);
        this.mAct = Act;
        mP = this.obtain_user();
    }

    public boolean sync_user_downstream() {
        SQLiteHelper helper = SQLiteHelper.getInstance(mAct);
        String password = helper.getLimbo1().getPassword();
        int id_us;
        String url =  super.urlDomain + "api/webServices/login_usuario/?correo=" + mP.getEmail() + "&pass=" + password;
        JSONObject json = getRESTJSONObject(url);
        Person persona = new Person();
        try {
            JSONArray jsonArray = json.getJSONArray("objects");
            json = jsonArray.getJSONObject(0); //en teoria solo hay una entrada asi que esto es seguro.

            id_us = json.getInt("id");//obtiene id para operaciones futuras

            Bitmap img_serv = this.getUserImage(id_us);
            persona.setId(id_us);
            persona.setEmail(json.getString("correo"));
            persona.setDataBaseInterest(this.getInteresesUsuario(id_us));
            persona.setName(json.getString("nombre"));
            persona.setGetTextFieldInfo(this.get_texto_Intereses_us(id_us));


            //Obtenemos la fecha
            String json_fecha = json.getString("fecha_de_nacimiento");
            int anno = Integer.parseInt(json_fecha.substring(0, 4));
            int mes = Integer.parseInt(json_fecha.substring(5, 7));
            int dia = Integer.parseInt(json_fecha.substring(8));
            persona.setFecha_de_nacimiento(new Date(anno, mes, dia));
            persona.setFoto_perfil(this.saveUserBitmapFromUrl(mAct, id_us));

            //cosas especificas de usuario
            Usuario usuario = new Usuario();
            usuario.setCorreo(persona.getEmail());
            usuario.setMatchp("");
            usuario.setId(persona.getId());
            usuario.setDob(persona.getFecha_de_nacimiento().getTime());
            usuario.setNombre(persona.getName());
            usuario.setNum(json.getString("numero_telefono"));
            usuario.setGplus(json.getString("googleP_id"));
            usuario.setFb(json.getString("facebookID"));
            usuario.setTwitter(json.getString("twitter_id"));
            usuario.setFoto(this.saveBitmap(mAct, img_serv));
            usuario.setModfav(json.getString("modo_de_cont_favorito"));
            usuario.setPassword(""); // <- para evitar inconsistencias

            this.updatePerson(mAct, persona, usuario);
            return true;
        } catch (JSONException e) {
            return false;
        }

    } //funciona

    public boolean sync_user_upstream() {
        String url = super.urlDomain + "api/webServices/actualizar_usuario/";
        List<NameValuePair> data = new ArrayList<NameValuePair>(9);
        data.add(new BasicNameValuePair("id_usuario", mP.getId()));//id_usuario
        data.add(new BasicNameValuePair("nombre", mP.getName()));//nombre
        data.add(new BasicNameValuePair("dob", String.valueOf(mP.getFecha_de_nacimiento().getYear())+"-"+String.valueOf(mP.getFecha_de_nacimiento().getMonth())+"-"+String.valueOf(mP.getFecha_de_nacimiento().getDay()) ));//fecha de nacimiento
        data.add(new BasicNameValuePair("correo", mP.getEmail()));//correo
        data.add(new BasicNameValuePair("numero_tel", mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblCellphone))));//numero de telefono
        data.add(new BasicNameValuePair("facebook", mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblFacebook))));//facebookID
        data.add(new BasicNameValuePair("google_p", mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblGoogle))));//googlePid
        data.add(new BasicNameValuePair("twitter", mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblTwitter))));//twitterID
        data.add(new BasicNameValuePair("modo_fav", String.valueOf(mP.getModo_de_cont_favorito())));//modo contacto fav
        try {
            this.post(url, data);
        } catch (IOException e) {
            return false;
        }
        return true;
    } //funciona

    public boolean sync_interests_upstream() {
        String url = super.urlDomain + "api/webServices/actualizar_intereses/";
        List<NameValuePair> data = new ArrayList<NameValuePair>();

        SQLiteHelper h = SQLiteHelper.getInstance(mAct);

        data.add(new BasicNameValuePair("id_us", String.valueOf(mP.getId())));

        for (List<Integer> lstIntereses : mP.getDataBaseInterest().values()) {
            for (Integer interes : lstIntereses) {
                data.add(new BasicNameValuePair("intereses[]", String.valueOf(interes)));
            }
        }

        try {
            this.post(url, data);
        } catch (IOException e) {
            return false;
        }

        return true;
    } //funciona

    public boolean sync_textos_upstream() {
        String url = super.urlDomain + "api/webServices/actualizar_textos/";
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        HashMap<String, String> textos_super = mP.getTextFieldInfo();
        data.add(new BasicNameValuePair("id_us", mP.getId()));//id_usuario
        SQLiteHelper helper = SQLiteHelper.getInstance(mAct);
        for (Superinteres sup : helper.getAllSuperinter()) {
            for (String desc : textos_super.keySet()) {
                if (sup.getDescripcion().equals(desc)) {
                    data.add(new BasicNameValuePair(
                            "sup" + String.valueOf(sup.getId()),
                            textos_super.get(desc)
                    ));
                }
            }
        }

        try {
            this.post(url, data);
        } catch (IOException e) {
            return false;
        }
        return true;
    } //funciona

    public boolean sync_image_upstream() {
        //Bitmap foto_usuario = super.loadImageFromStorage(mP.getFoto_perfil());
        String url = super.urlDomain + "api/webServices/upload_image_usuario/";
        List<NameValuePair> data = new ArrayList<NameValuePair>(2);
        data.add(new BasicNameValuePair("id_usuario", mP.getId()));
        data.add(new BasicNameValuePair("imagen", mP.getFoto_perfil()));
        String pop;
        try {
            pop = this.post(url, data);
        } catch (IOException e) {
            return false;
        }
        return true;
    } //funciona

    public boolean sync_history_upstream() {
        String url = super.urlDomain + "api/webServices/actualizar_historial/";
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("id_us", mP.getId()));//id_usuario

        SQLiteHelper helper = SQLiteHelper.getInstance(mAct);
        List<Historial> historialList = helper.getAllHistorial();
        for(Historial h : historialList){
            data.add(new BasicNameValuePair("meets[]", h.getIdMatch()+","+h.getLatitud()+","+h.getLongitud()+","+h.getFecha()+","+h.getMatchPerc()));
        }

        try {
            this.post(url, data);
       } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Saca al usuario de la base de datos
     *
     * @return el objeto usuario
     */
    public Person obtain_user() {
        SQLiteHelper pop = SQLiteHelper.getInstance(mAct);
        Person pers;
        InterestsMethods mthl = new InterestsMethods();

        pers = mthl.createPerson(mAct, Integer.valueOf(pop.getLimbo1().getId()));
        InterestsMethods fillPerson = new InterestsMethods();
        pers.setDataBaseInterest(fillPerson.getInterestFromDataBase(mAct, Integer.parseInt(pers.getId())));
        pers.setGetTextFieldInfo(fillPerson.getTextsFromDataBase(mAct, Integer.parseInt(pers.getId())));
        pers.setGetContactedByList(fillPerson.getContactedByFromDataBase(mAct, pop.getUser(pers.getEmail())));


        return pers;
    }

    private String post(String url, List<NameValuePair> nameValuePairs) throws IOException {
        String responseS = "";

        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);


        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (int index = 0; index < nameValuePairs.size(); index++) {
            if (nameValuePairs.get(index).getName().equalsIgnoreCase("imagen")) {
                // If the key equals to "image", we use FileBody to transfer the data
                entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
            } else {
                // Normal string data
                entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
            }
        }

        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost, localContext);
        HttpEntity entity_resp = response.getEntity();
        responseS = EntityUtils.toString(entity_resp, "UTF-8");
        if (responseS.equals("0")) {
            throw new IOException();
        }
        return responseS;
    }

    private void updatePerson(Activity act, Person person, Usuario usuario){
        // Insert received person via bluetooth into Data Base
        InterestsMethods mths = new InterestsMethods();
        SQLiteHelper db = SQLiteHelper.getInstance(act.getApplicationContext());

        db.updateUsuario(usuario);
        db.getWritableDatabase().execSQL("delete from usuariointereses where idUsuario=" + person.getId());
        mths.insertOnLoginIntereses(act, person);


        db.getWritableDatabase().execSQL("delete from textointeres where idUsuario=" + person.getId());
        mths.insertText(act, person);

    }


}
