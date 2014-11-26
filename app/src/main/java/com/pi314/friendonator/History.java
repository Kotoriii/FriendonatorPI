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

import com.pi314.interests.InterestsMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Database.Historial;
import Database.SQLiteHelper;


public class History extends Activity {

    //Se crea un arrylist con los datos que quieren que se muestren en el perfil
    List<ListaEntrada_History> datos = new ArrayList<ListaEntrada_History>();

    Person person;
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_history);

        // Get object person from intent extras
        getSetPerson();

        // Set Data Base
        db = SQLiteHelper.getInstance(getApplicationContext());

        // Get history list from Data Base
        List<Historial> historialList = db.getAllHistorial();

        if (historialList.isEmpty()) {
            // Testing inserting match and history
            testMatchStuff();
        }

        historialList = db.getAllHistorial();

        if (!historialList.isEmpty()) {
            int count = 2; // Integer.parseInt(h.getIdMatch())
            for (Historial h : historialList) {
                datos.add(new ListaEntrada_History(R.drawable.ic_launcher, h.getMatchName(), getResources().getString(R.string.matchPercentage) + h.getMatchPerc() + " %", count));
                count ++;
            }
        }

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

                // Create the Intent element
                Intent intent = new Intent(History.this, MatchProfileActivity.class);

                // Send ID to match profile activity
                Bundle b = new Bundle();
                b.putInt("ID", id1);
                intent.putExtras(b);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                startActivity(intent);

                // Finish activity
                finish();

                // Slide animation
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
           }
        });

    }

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    public void testMatchStuff() {
        InterestsMethods test = new InterestsMethods();

        Person matchPerson = new Person();

        HashMap<Integer, List<Integer>> interests = new HashMap<Integer, List<Integer>>();
        List<Integer> genres = new ArrayList<Integer>();
        genres.add(0);
        genres.add(1);
        genres.add(2);
        genres.add(3);

        List<Integer> genres2 = new ArrayList<Integer>();
        genres2.add(40);
        genres2.add(43);
        genres2.add(44);
        genres2.add(47);

        List<Integer> genres3 = new ArrayList<Integer>();
        genres3.add(18);
        genres3.add(22);

        interests.put(1, genres);
        interests.put(3, genres3);
        interests.put(6, genres2);

        matchPerson.setDataBaseInterest(interests);
        matchPerson.setName("Female Doge");
        matchPerson.setId("2");

        matchPerson.fillTextFieldInfo(getResources().getString(R.string.selectInterestMusic), "Such music, many artist, wow");
        matchPerson.fillTextFieldInfo(getResources().getString(R.string.selectInterestMovies), "Cute puppy");
        matchPerson.fillTextFieldInfo(getResources().getString(R.string.selectInterestSports), "Must shape body");

        HashMap<String, String> contactedBy = new HashMap<String, String>();
        contactedBy.put("Cellphone", "8649-5984");
        contactedBy.put("Facebook", "www.facebook.com/female.doge");
        contactedBy.put("Twitter", "@SuchClass");

        matchPerson.setGetContactedByList(contactedBy);

        int percentage = (int) Math.floor(test.getMatchPercentage(person, matchPerson));
        test.insertReceivedPerson(History.this, matchPerson, person.getId(), percentage);

        Person matchPerson2 = new Person();

        HashMap<Integer, List<Integer>> interests2 = new HashMap<Integer, List<Integer>>();
        List<Integer> genres4 = new ArrayList<Integer>();
        genres4.add(10);
        genres4.add(11);
        genres4.add(12);

        List<Integer> genres5 = new ArrayList<Integer>();
        genres5.add(27);
        genres5.add(28);
        genres5.add(29);

        List<Integer> genres6 = new ArrayList<Integer>();
        genres6.add(49);
        genres6.add(50);

        interests2.put(2, genres4);
        interests2.put(4, genres5);
        interests2.put(8, genres6);

        matchPerson2.setDataBaseInterest(interests2);
        matchPerson2.setName("Doge");
        matchPerson2.setId("3");

        matchPerson2.fillTextFieldInfo(getResources().getString(R.string.selectInterestLiterature), "Kevin Bacon Guardian, Many Galaxies, Such Mix!");
        matchPerson2.fillTextFieldInfo(getResources().getString(R.string.selectInterestArt), "Doge Lisa");
        matchPerson2.fillTextFieldInfo(getResources().getString(R.string.selectInterestLookingFor), "Many friends, wow");

        HashMap<String, String> contactedBy2 = new HashMap<String, String>();
        contactedBy2.put("Cellphone", "8321-8495");
        contactedBy2.put("Twitter", "@MuchFood");

        matchPerson2.setGetContactedByList(contactedBy2);

        int percentage2 = (int) Math.floor(test.getMatchPercentage(person, matchPerson2));
        test.insertReceivedPerson(History.this, matchPerson2, person.getId(), percentage2);
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

    @Override
    public void onBackPressed() {
        // Create intent to open get contacted by activity
        Intent intent = new Intent(History.this, HomeActivity.class);

        // Set bundle inside intent
        intent.putExtra("PERSON", person);

        // Start change to a new layout
        startActivity(intent);

        // Finish activity
        this.finish();
    }

}
