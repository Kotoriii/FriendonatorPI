package misc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Database.Intereses;
import Database.Superinteres;

/**
 * Created by andrea on 18/11/14.
 * Esta clase solo se encarga de ejecutar los requests, pero no se encarga de validar si el dispositivo
 * esta conectado a internet o etc. Eso le toca a la clase que lo llama (en teoria solo deberia de ser llamado
 * desde un activity). Aunque si va a proveer un metodo ('isConnected') para ver si esta conectado a internet.
 * Tambien tiene una clase que sirve para pedirle al usuario que prenda el internet (en caso de que este apagado)
 * Ver doc.
 */
public class ApiWrapper {
    private String mResult = null; //Resultado del request, para request asincronicas
    private List<NameValuePair> mPostData = null; //datos a mandar durante el HttpAsyncPOSTTask
    private Bitmap mBitmapHolder = null;

    /**
     * Devuelve True su el login es exitoso y false si no lo es.
     * Este login solo lo puede accesar la aplicacion con dicho api_key.
     *
     * @param correo
     * @param password
     * @return si son datos correctos o no
     */
    public boolean loginConServidor(String correo, String password) {
        //obtenemos el objeto json correspondiente al correo
        String url = "http://tupini07.pythonanywhere.com/api/webServices/login_usuario/correo=" + correo + "&pass=" + password;

        //TODO talves implementar login con post en ves de GET o poner encripcion
        /*---Esto solo si se implementa con POST
            List<NameValuePair> datos = new ArrayList<NameValuePair>(2);
            datos.add(new BasicNameValuePair("correo", correo));
            datos.add(new BasicNameValuePair("pass", password));
        */

        JSONObject json = getRESTJSON(url);
        try {
            JSONArray jsonArray = json.getJSONArray("objects");
            json = jsonArray.getJSONObject(0); //en teoria solo hay una entrada asi que esto es seguro.
            if (password.equals(json.getString("password")))
                return true;

        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    /**
     * Retorna la lista de superintereses que se encuentran en el servidor
     *
     * @return
     */
    public List<Superinteres> getSuperIntereses() {
        List<Superinteres> lstSupInt = new ArrayList<Superinteres>();
        JSONObject json = getRESTJSON("http://tupini07.pythonanywhere.com/api/webServices/SuperIntereses/?format=json");
        try {
            JSONArray jsonArray = json.getJSONArray("objects");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                lstSupInt.add(new Superinteres(row.getString("id"), row.getString("descripcion")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstSupInt;
    }

    /**
     * Devuelve un HashMap<Superinteres, List<Intereses>> con todos los intereses del servidor
     *
     * @return lstIntereses
     */
    public HashMap<Superinteres, List<Intereses>> getIntereses() {
        HashMap<Superinteres, List<Intereses>> lstIntereses = new HashMap<Superinteres, List<Intereses>>();
        JSONObject json = getRESTJSON("http://tupini07.pythonanywhere.com/api/webServices/Intereses/?format=json");
        try {
            JSONArray jsonArray = json.getJSONArray("objects");
            Superinteres supHolder = null;
            Intereses interesHolder = null;
            JSONObject supIntJson = null;
            boolean added_checker = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                supIntJson = row.getJSONObject("super_interes");

                //holders para la actual linea de codigo
                interesHolder = new Intereses(row.getString("id"), row.getString("descripcion"), supIntJson.getString("id"));
                supHolder = new Superinteres(supIntJson.getString("id"), supIntJson.getString("descripcion"));

                for (Superinteres su : lstIntereses.keySet()) {
                    if (su.getId() == supHolder.getId()) {
                        lstIntereses.get(su).add(interesHolder);
                        added_checker = true;
                        break;
                    }
                }
                if (!added_checker) {
                    List<Intereses> intLSTHOlder = new ArrayList<Intereses>();
                    intLSTHOlder.add(interesHolder);
                    lstIntereses.put(supHolder, intLSTHOlder);
                }
                added_checker = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstIntereses;
    }

    /**
     * Devuelve la imagen don dicho url y la guarda en:
     *      /data/data/com.pi314.friendonator/app_Friendonator/Pictures
     * @param url
     * @param act
     * @return
     */
    public Bitmap getImage(String url, Activity act){

        new HttpGetImage().execute(url);
        int ss = 0;
        while (mBitmapHolder == null) { //necesitamos esperar por la respuesta
            try {
                synchronized (this) {
                    this.wait(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ss++;
        }
        Log.d("ApiWrapper", "waited " + ss + " cycles before response");
        Bitmap btmp = null;
        try {
            btmp = mBitmapHolder;

            Random rndm = new Random();
            boolean salvada = false;
            while(!salvada)
            {
                try {
                    OutputStream fOut = null;
                    // save image
                    ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("Pictures", Context.MODE_PRIVATE);
                    File file = new File(directory, rndm.nextLong()+".jpg"); // the File to save to
                    fOut = new FileOutputStream(file);
                    btmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    salvada = true;
                    fOut.flush();
                    fOut.close(); // do not forget to close the stream
                    Log.v(getClass().getSimpleName(), "image directory path " + directory.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return btmp;
    }

    /**
     * Devuelve la imagen q se encuentra en cierto path..
     * no sabia donde poner el metodo :) pero por el momento se queda aqui
     * @param path
     * @return
     */
    public Bitmap loadImageFromStorage(String path){
        Bitmap b = null;
        try {
            File f=new File(path, "profile.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    private JSONObject getRESTJSON(String URL) {
        new HttpAsyncGETTask().execute(URL);
        int ss = 0;
        while (mResult == null) { //necesitamos esperar por la respuesta
            try {
                synchronized (this) {
                    this.wait(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ss++;
        }
        Log.d("ApiWrapper", "waited " + ss + " cycles before response");
        JSONObject json = null;
        try {
            json = new JSONObject(mResult);


        } catch (JSONException e) {
            try {
                json = new JSONObject("{\"error\": \"not a valid json object\"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return json;
    }

    private JSONObject postRESTJSON(String URL, List<NameValuePair> data) {
        mPostData = data;
        new HttpAsyncPOSTTask().execute(URL);
        int ss = 0;
        while (mResult == null) { //necesitamos esperar por la respuesta
            try {
                synchronized (this) {
                    this.wait(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ss++;
        }
        mPostData = null; //liberamos la memoria.

        Log.d("ApiWrapper", "waited " + ss + " cycles before response");
        JSONObject json = null;
        try {
            json = new JSONObject(mResult);


        } catch (JSONException e) {
            try {
                json = new JSONObject("{\"error\": \"not a valid json object\"}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return json;
    }

    private String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String POST(String url, List<NameValuePair> data) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        InputStream inputStream = null;
        String result = "";
        try {
            httppost.setEntity(new UrlEncodedFormEntity(data));
            // Execute HTTP Post Request
            HttpResponse httpResponse = httpclient.execute(httppost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (ClientProtocolException e) {
            result = "Did not work!";
        } catch (IOException e) {
            result = "Did not work!";
        }
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;


    }

    /**
     * Retorna si esta conectado o no a internet
     *
     * @param context
     * @return
     */
    public boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Activa el wifi
     *
     * @param context
     */
    public void activateWifi(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false); // true or false to activate/deactivate wifi
    }

    private class HttpAsyncGETTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            mResult = null; // Si el resutado es null entonces aun no se a completado el AsyncTask
            mResult = GET(urls[0]);
            return mResult;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("ApiWrapper", "** POST execute HttpAsyncTask ** \n" + result);
        }
    }

    private class HttpAsyncPOSTTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            mResult = null; // Si el resutado es null entonces aun no se a completado el AsyncTask
            mResult = POST(urls[0], mPostData);
            return mResult;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("ApiWrapper", "** POST execute HttpAsyncPOSTTask ** \n" + result);
        }
    }

    private class HttpGetImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            mBitmapHolder = null;
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            mBitmapHolder = mIcon11;
            return mBitmapHolder;
        }

        protected void onPostExecute(Bitmap result) {
            Log.v("ApiWrapper", "** POST execute HTTPGETIMAGE ** \n");
        }
    }

}