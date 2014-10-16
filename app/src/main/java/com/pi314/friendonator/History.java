package com.pi314.friendonator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class History extends Activity {

    //Se crea un arrylist con los datos que quieren que se muestren en el perfil
    static List<ListaEntrada_History> datos = new ArrayList<ListaEntrada_History>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_history);

        //Se crean ls personajes
        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Carlos", "78" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Mack", "87" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Tupini", "80" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Alejandro", "75" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Fernando", "82" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Warren", "90" + "%"));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Vicky", "69" + "%"));

        //Ordenacion de testeo
     /*Collections.sort(datos, new Comparator<ListaEntrada_History>() {
            @Override
            public int compare(ListaEntrada_History L1, ListaEntrada_History L2) {
                // Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
                return new Integer(L2.getPercentageInt()).compareTo(new Integer(L1.getPercentageInt()));
            }
        });*/
        //Termina logica de ordenaacion de testeo


        ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
        historyList.setAdapter(new Lista_History(this, R.layout.entrada_history, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView txtName = (TextView) view.findViewById(R.id.textView_superior);
                    if (txtName != null)
                        txtName.setText(((ListaEntrada_History) entrada).getName());

                    TextView txtPercentage = (TextView) view.findViewById(R.id.textView_inferior);
                    if (txtPercentage != null)
                        txtPercentage.setText(((ListaEntrada_History) entrada).getPercentage());

                    ImageView imgProfile = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imgProfile != null)
                        imgProfile.setImageResource(((ListaEntrada_History) entrada).getImage());

                    ProgressBar progreesMatch = (ProgressBar) findViewById(R.id.progressBarPercentage);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        switch (item.getItemId()) {
            case R.id.menu_new:
                Toast.makeText(History.this,
                        "Presionado la opcion order Match",
                        Toast.LENGTH_SHORT).show();
                clearAdapter();
                orderbyMatch();
                return true;
            case R.id.menu_save:
                Toast.makeText(History.this,
                        "Presionado la opcion order name",
                        Toast.LENGTH_SHORT).show();
                clearAdapter();
                orderByName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void orderByName () {

        Collections.sort(datos, new OrderArrayListByName());

        //de nueva la logica para llenar el listView una vez con la lista ordenada :D
        ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
        historyList.setAdapter(new Lista_History(this, R.layout.entrada_history, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView txtName = (TextView) view.findViewById(R.id.textView_superior);
                    if (txtName != null)
                        txtName.setText(((ListaEntrada_History) entrada).getName());

                    TextView txtPercentage = (TextView) view.findViewById(R.id.textView_inferior);
                    if (txtPercentage != null)
                        txtPercentage.setText(((ListaEntrada_History) entrada).getPercentage());

                    ImageView imgProfile = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imgProfile != null)
                        imgProfile.setImageResource(((ListaEntrada_History) entrada).getImage());

                    ProgressBar progreesMatch = (ProgressBar) findViewById(R.id.progressBarPercentage);
                }
            }
        });
    }

    public void orderbyMatch(){

        Collections.sort(datos, new OrderArrayListByMatch());

        //de nueva la logica para llenar el listView una vez con la lista ordenada :D
        ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
        historyList.setAdapter(new Lista_History(this, R.layout.entrada_history, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView txtName = (TextView) view.findViewById(R.id.textView_superior);
                    if (txtName != null)
                        txtName.setText(((ListaEntrada_History) entrada).getName());

                    TextView txtPercentage = (TextView) view.findViewById(R.id.textView_inferior);
                    if (txtPercentage != null)
                        txtPercentage.setText(((ListaEntrada_History) entrada).getPercentage());

                    ImageView imgProfile = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imgProfile != null)
                        imgProfile.setImageResource(((ListaEntrada_History) entrada).getImage());

                    ProgressBar progreesMatch = (ProgressBar) findViewById(R.id.progressBarPercentage);
                }
            }
        });
    }


    public void clearAdapter()
    {
        ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
        historyList.setAdapter(null);
    }
}
