package misc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private JSONObject getRESTJSON(String URL) {
        new HttpAsyncTask().execute(URL);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
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
}