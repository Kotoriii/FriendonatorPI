package com.pi314.friendonator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pi314.interests.InterestsMethods;

import java.io.File;
import java.util.ArrayList;

import Bluetooth.BluetoothHandler;
import Database.SQLiteHelper;
import Database.Usuario;

/**
 * Created by Christian on 10/12/2014.
 */
public class HomeActivity extends Activity {

    private Spinner spnEvent;
    ImageView iv;
    Bitmap image;
    ProgressDialog pd;
    Person person;
    TextView lblprofilename;
    SQLiteHelper db;
    int eventSelected;
    ImageView viewImage;

    //Elementos del menu
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;

    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        iv = (ImageView) findViewById(R.id.imgviewEventpic);
        final String txtSpinner = spnEvent.getSelectedItem().toString();

        db = SQLiteHelper.getInstance(getApplicationContext());

        //addItemsOnEventSpinner();
        lblprofilename = (TextView) findViewById(R.id.lblProfileName);

        // Get object person from intent extras
        getSetPerson();

        // Set user name
        textName();

        final TextView btnprofile = (TextView) findViewById(R.id.lblProfileName);

        btnprofile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent element
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                startActivity(intent);

                // Finish activity
                finish();
            }
        });

        viewImage=(ImageView) findViewById(R.id.imgviewHomeprofile);

        Usuario usuario = db.getUser(person.getEmail());

        if(usuario.getFoto() != null) {
            File file = new File(usuario.getFoto());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            viewImage.setImageBitmap(bitmap);
        }

        viewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("PERSON", person);
                startActivity(intent);
                finish();
            }
        });

        //Set spinner choices
        Resources res = getResources();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, res.getStringArray(R.array.event_array));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnEvent.setAdapter(dataAdapter);


        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch(position) {
                    case 0:
                        iv.setImageResource(R.drawable.event_hangout);
                        eventSelected = position;
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.event_concert);
                        eventSelected = position;
                        break;
                    case 2:
                        iv.setImageResource(R.drawable.event_art);
                        eventSelected = 4;
                        break;
                    case 3:
                        iv.setImageResource(R.drawable.event_literature);
                        eventSelected = 2;
                        break;
                    case 4:
                        iv.setImageResource(R.drawable.event_party);
                        eventSelected = position;
                        break;
                }
                // Set event into person
                person.setEventId(eventSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        // Set spinner event
        if (person.getEventId() != 0) {
            if (person.getEventId() == 4) {
                spnEvent.setSelection(2);
            } else if (person.getEventId() == 2) {
                spnEvent.setSelection(3);
            } else {
                spnEvent.setSelection(person.getEventId());
            }
        }


        ///////////////////////////////////////Logica para el menu//////////////////////////////////////////////////////////
        final String[] opciones = getResources().getStringArray(R.array.menu_options);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        final ListView drawer = (ListView) findViewById(R.id.drawer);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tres lineaas de codigo paraa la imgen del menu
        NavList = (ListView) findViewById(R.id.drawer);
        //Declaramos el header el caul sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);
        //Establecemos header
        NavList.addHeaderView(header);

        //obtiene las imagenes desde el string.xml
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        //crea en arraylist de la clae Item_object que tiene imagen y texto
        NavItms = new ArrayList<Item_objct>();
        //Se procede a insertar las imagines y textos
        NavItms.add(new Item_objct(opciones[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new Item_objct(opciones[1], NavIcons.getResourceId(1, -1)));
        NavItms.add(new Item_objct(opciones[2], NavIcons.getResourceId(2, -1)));
        NavItms.add(new Item_objct(opciones[3], NavIcons.getResourceId(5, -1)));
        //seteamos el adaptador y le pasamos los iconos y titulos al adaptador
        NavAdapter = new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                displayView(arg2);
                drawerLayout.closeDrawers();
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.hello_world){
            public void onDrawerClosed(View view) {
                // Drawer cerrado
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // Drawer abierto
                getActionBar().setTitle(getResources().getString(R.string.menu));
                //invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(toggle);

    }

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    public void textName() {
        if (person.getName() != null)
            lblprofilename.setText(person.getName());
    }

    public void onClickMatch(View v) {
        // Create the Intent element
        Intent intent = new Intent(HomeActivity.this, History.class);

        // Set person inside intent
        intent.putExtra("PERSON", person);

        startActivity(intent);

        // Finish activity
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*BluetoothHandler mbh = BluetoothHandler.getInstance(this);
        mbh.redefineActivity(this);
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra("PERSON", person);
        startActivity(intent);*/

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Activamos el toggle con el icono
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**Metodo para abrir el form escogido en el menu**/
    private void displayView(int options){

        switch (options) {
            case 1:
                //aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                //Create the Intent element
                intentProfile.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentProfile);
                this.finish();
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(HomeActivity.this, History.class);
                //Create the Intent element
                intentHistory.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentHistory);
                this.finish();
                break;
            case 3:
                //Actual activity
                break;
            case 4:
                //aqui se abrira la actividad MySettings
                Intent intentMySettings = new Intent(HomeActivity.this, MySettings.class);
                //Create the Intent element
                intentMySettings.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentMySettings);
                this.finish();
                break;
            default:
                break;
        }
    }


}
