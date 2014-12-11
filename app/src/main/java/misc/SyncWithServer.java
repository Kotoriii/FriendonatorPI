package misc;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.pi314.friendonator.Person;
import com.pi314.interests.InterestsMethods;

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
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Database.SQLiteHelper;

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

    public boolean sync_image() {
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
            responseS = response.toString();

        return responseS;
    }
}
