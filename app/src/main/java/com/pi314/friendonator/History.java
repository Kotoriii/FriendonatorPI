package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Carlos", "78" + "%", 78));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Mack", "87" + "%", 4));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Tupini", "80" + "%", 34));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Alejandro", "75" + "%", 67));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Fernando", "82" + "%", 89));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Warren", "90" + "%", 23));

        datos.add(new ListaEntrada_History(R.drawable.ic_launcher, "Vicky", "69" + "%", 45));


        final ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
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
                }
            }
        });
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 int id1 = datos.get(position).getId();
                 //ToastCostumizado(""+id1);
                Intent intent = new Intent(History.this, MatchProfileActivity.class);
                Bundle b = new Bundle();
                b.putInt("ID", id1);
                intent.putExtras(b);
                startActivity(intent);
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

                ToastCostumizado("Pressed the option order Match");

                clearAdapter();
                orderbyMatch();
                return true;
            case R.id.menu_save:

                ToastCostumizado("Pressed the option order Name");

                clearAdapter();
                orderByName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ToastCostumizado (String mensaje){
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(mensaje);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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
                }
            }
        });
    }


    public void clearAdapter()
    {
        ListView historyList = (ListView) findViewById(R.id.ListView_listado_history);
        historyList.setAdapter(null);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */

}
