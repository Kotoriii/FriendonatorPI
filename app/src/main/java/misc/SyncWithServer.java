package misc;

import android.app.Activity;
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
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public SyncWithServer(Activity mAct) {
        this.mAct = mAct;
        mP = this.obtain_user();
    }

    public boolean sync_user_upstream(){
        String url = super.urlDomain + "api/webServices/actualizar_usuario/";
        List<NameValuePair> data = new ArrayList<NameValuePair>(9);
        data.add(new BasicNameValuePair("id_usuario",mP.getId()));//id_usuario
        data.add(new BasicNameValuePair("nombre",mP.getName()));//nombre
        data.add(new BasicNameValuePair("dob",mP.getFecha_de_nacimiento().toString()));//fecha de nacimiento
        data.add(new BasicNameValuePair("correo",mP.getEmail()));//correo
        data.add(new BasicNameValuePair("numero_tel",mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblCellphone))));//numero de telefono
        data.add(new BasicNameValuePair("facebook",mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblFacebook))));//facebookID
        data.add(new BasicNameValuePair("google_p",mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblGoogle))));//googlePid
        data.add(new BasicNameValuePair("twitter",mP.getGetContactedByList().get(mAct.getApplicationContext().getResources().getString(R.string.lblTwitter))));//twitterID
        data.add(new BasicNameValuePair("modo_fav",String.valueOf(mP.getModo_de_cont_favorito())));//modo contacto fav
        try{
            this.post(url, data);
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sync_interests_upstream(){
        String url = super.urlDomain + "api/webServices/actualizar_intereses/";
        List<NameValuePair> data = new ArrayList<NameValuePair>();

        data.add(new BasicNameValuePair("id_us", String.valueOf(mP.getId())));

        for(List<Integer> lstIntereses : mP.getDataBaseInterest().values()){
            for(Integer interes : lstIntereses){
                data.add(new BasicNameValuePair("intereses[]", String.valueOf(interes)));
            }
        }

        try {
            this.post(url, data);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean sync_textos_upstream(){
        String url = super.urlDomain + "api/webServices/actualizar_textos/";
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        HashMap<String, String> textos_super = mP.getTextFieldInfo();

        SQLiteHelper helper = SQLiteHelper.getInstance(mAct);
        for(Superinteres sup : helper.getAllSuperinter()){
            for(String desc : textos_super.keySet()){
                if(sup.getDescripcion().equals(desc)){
                    data.add(new BasicNameValuePair(
                            "sup"+String.valueOf(sup.getId()),
                            textos_super.get(desc)
                            ));
                }
            }
        }

        try{
            this.post(url, data);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sync_image_upstream() {
        //Bitmap foto_usuario = super.loadImageFromStorage(mP.getFoto_perfil());
        String url = super.urlDomain + "/api/webServices/upload_image_usuario/";
        List<NameValuePair> data = new ArrayList<NameValuePair>(2);
        data.add(new BasicNameValuePair("id_usuario",mP.getId()));
        data.add(new BasicNameValuePair("imagen",mP.getFoto_perfil()));
        String pop;
        try{
            pop = this.post(url, data);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    /**
     * Saca al usuario de la base de datos
     * @return el objeto usuario
     */
    private Person obtain_user() {
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

    public String post(String url, List<NameValuePair> nameValuePairs) throws IOException {
        String responseS = "";
        HttpClient httpClient = AndroidHttpClient.newInstance("Android");
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);


            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("imagen")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
                }else{
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity_resp = response.getEntity();
            responseS = EntityUtils.toString(entity_resp, "UTF-8");
            if(responseS.equals("0")){
                throw  new IOException();
            }
        return responseS;
    }
}
