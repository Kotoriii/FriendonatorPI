package misc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;
import com.pi314.interests.InterestsMethods;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Database.Historial;
import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
import Database.Usuario;

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
    public Person loginConServidor(String correo, String password, Activity act) {
        //obtenemos el objeto json correspondiente al correo
        //para mas seguridad usamos post
        String url = "http://tupini07.pythonanywhere.com/api/webServices/login_usuario/?correo=" + correo + "&pass=" + password;
        int id_us = 0;
        /*---Esto solo si se implementa con POST
            List<NameValuePair> datos = new ArrayList<NameValuePair>(2);
            datos.add(new BasicNameValuePair("correo", correo));
            datos.add(new BasicNameValuePair("pass", password));
        */
        SQLiteHelper sqlHelper = SQLiteHelper.getInstance(act);
        JSONObject json = getRESTJSON(url);
        Person persona = new Person();
        try {
            //Inserta cosas en el servidor
            JSONArray jsonArray = json.getJSONArray("objects");
            json = jsonArray.getJSONObject(0); //en teoria solo hay una entrada asi que esto es seguro.
            if (correo.equals(json.getString("correo"))) {

                id_us = json.getInt("id");//obtiene id para operaciones futuras

                //si es null entonces inserta el usuario en la base de datos,
                //quiere decir que esta persona nunca ha iniciado sesion.
                if (sqlHelper.getUser(correo).getId() == null) {
                    //llenar la bd con los interes y eso del servidor
                    //si nunca se ha hecho login a la aplicacion
                    salvarInteresesDelServidorABDLocal(act);

                    Bitmap img_serv = this.getUserImage(id_us);
                    persona.setId(id_us);
                    persona.setEmail(correo);
                    persona.setDataBaseInterest(this.getInteresesUsuario(id_us));
                    persona.setName(json.getString("nombre"));

                    //todo, persona.setGetTextFieldInfo();.. primero hay que hacerlo en servidor
                    // o podriamos quitarlo :D



                    //Obtenemos la fecha
                    String json_fecha = json.getString("fecha_de_nacimiento");
                    int anno = Integer.parseInt(json_fecha.substring(0, 4));
                    int mes = Integer.parseInt(json_fecha.substring(5, 7));
                    int dia = Integer.parseInt(json_fecha.substring(8));
                    persona.setFecha_de_nacimiento(new Date(anno, mes, dia));
                    persona.setFoto_perfil(this.saveUserBitmapFromUrl(act, id_us));

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
                    usuario.setFoto(this.saveBitmap(act, img_serv));
                    usuario.setModfav(json.getString("modo_de_cont_favorito"));
                    usuario.setPassword(""); // <- para evitar inconsistencias

                    this.insertPerson(act, persona, usuario);

                    //en teoria no deberia de llegar aqui si hay algo en limbo.
                    // por lo tanto no se ponen checks. sin ebargo LoginActivity deberia
                    // de revisar que no haya nada en limbo
                    usuario.setPassword(password);
                    sqlHelper.insertlimbo(usuario);


                }

            }

        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "error de lecctura de json. " + e.getStackTrace());
        }
        //luego de insertar tanto usuario como persona se recrea una instancia limpia de la persona.
        //o en el caso de que no sea una nueva persona entonces se obtiene la persona de la BD
        return (new InterestsMethods()).createPerson(act, id_us);
    }

    private String saveUserBitmapFromUrl(Activity act, int id_us) {
        return this.saveBitmap(act, this.getUserImage(id_us));
    }

    /**
     * Retorna un HashMap<Integer, List<Integer>> para meter en el nuevo objeto
     * Person. Casi que solo se usa en el login.
     * ************* Es necesaria una conexion a internet
     *
     * @param id_us
     * @return
     */
    public HashMap<Integer, List<Integer>> getInteresesUsuario(int id_us) {
        HashMap<Integer, List<Integer>> lstIntereses = new HashMap<Integer, List<Integer>>();
        JSONObject json = getRESTJSON("http://tupini07.pythonanywhere.com/api/webServices/intereses_usuario/?id_us=" + id_us);
        try {
            Integer supHolder = null;
            List<Integer> interesHolder = new ArrayList<Integer>();
            JSONObject supIntJson = null;
            boolean added_checker = false;
            Iterator<String> iter = json.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                supHolder = Integer.parseInt(key);
                JSONArray arrayHolder = json.getJSONArray(key);
                int intrsInmdt = 0;
                for (int i = 0; i < arrayHolder.length(); i++) {
                    try {
                        intrsInmdt = arrayHolder.getInt(i);
                        interesHolder.add(intrsInmdt);
                    } catch (IndexOutOfBoundsException e) {
                        Log.e(getClass().getSimpleName(), "out of bound en obtener intereses usuario, revisar");
                    }
                }

                lstIntereses.put(supHolder, interesHolder);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstIntereses;
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
     * /data/data/com.pi314.friendonator/app_Friendonator/Pictures
     *
     * @param url
     * @return
     */
    public Bitmap getImageFromURL(String url) {
        //TODO el request sigue ejecutandose incluso si no esta conectado a internet
        mBitmapHolder = null; //limpiamos
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
        return mBitmapHolder;
    }

    /**
     * Salva el bitmap path default. Devuelve el path
     * a la imagen.
     *
     * @param act
     * @param img_serv
     * @return El string donde se guardo.
     */
    public String saveBitmap(Activity act, Bitmap img_serv) {
        Bitmap btmp = img_serv;


        Random rndm = new Random();
        boolean salvada = false;
        while (!salvada) {
            try {
                OutputStream fOut = null;
                // save image
                String name = String.valueOf(rndm.nextLong());
                ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("Pictures", Context.MODE_PRIVATE);
                File file = new File(directory, name + ".jpg"); // the File to save to
                fOut = new FileOutputStream(file);
                btmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                salvada = true;
                fOut.flush();
                fOut.close(); // do not forget to close the stream
                Log.v(getClass().getSimpleName(), "image directory path " + file.getAbsolutePath());
                return file.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;

    }

    /**
     * Devuelve el bitmap con la imagen del usuario identificado con el id, estaimagen se saca desde el
     * servidor.
     *
     * @param user_id
     * @return
     */
    public Bitmap getUserImage(int user_id) {
        JSONObject json = getRESTJSON("http://tupini07.pythonanywhere.com/api/webServices/get_imagen_usuario/?id_usuario=" + user_id);

        try {
            String url = "http://tupini07.pythonanywhere.com" + json.getString("url_foto");
            return getImageFromURL(url);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Devuelve la imagen q se encuentra en cierto path..
     * no sabia donde poner el metodo :) pero por el momento se queda aqui
     *
     * @param path
     * @return
     */
    public Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        try {
            File f = new File(path, "profile.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * salva los intereses y superintereses del servidor a la base de datos local
     * @return true si fue exitoso o false si hubo un problema
     */
    public boolean salvarInteresesDelServidorABDLocal(Activity act){
        SQLiteHelper Helper = SQLiteHelper.getInstance(act);
        try {
            HashMap<Superinteres, List<Intereses>> hmap = this.getIntereses();
            for (Superinteres sup : hmap.keySet()) {
                Helper.insertSuperinter(sup);
                for (Intereses in : hmap.get(sup)) {
                    Helper.insertInteresCust(in);
                }
            }
            return true;
        }catch (SQLiteException e) {
            return false;
        }
    }

    private JSONObject getRESTJSON(String URL) {
        mResult = null; //reiniciamos el holder
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
        wifi.setWifiEnabled(true); // true or false to activate/deactivate wifi
    }

    private void insertPerson(Context context, Person person, Usuario usuario) {
        // Insert received person via bluetooth into Data Base
        InterestsMethods mths = new InterestsMethods();
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());

        db.insertUsuario(usuario);
        mths.insertInterests(context, person);

        mths.insertText(context, person);

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