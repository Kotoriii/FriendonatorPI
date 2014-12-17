package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Database.Configuracion;
import Database.SQLiteHelper;
import Database.Usuario;


public class MySettings extends Activity {

    //Elementos del menu
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;
    Person person;

    String interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);


        final Button btnIntervaleScan = (Button)findViewById(R.id.btnIntervale);
        final SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar);
        final Button btnSave = (Button)findViewById(R.id.btnSave);
        final Button btnAboutUs = (Button)findViewById(R.id.btnAboutUs);

        final TextView textPercentage = (TextView) findViewById(R.id.txtPercentage);
        final SQLiteHelper db = SQLiteHelper.getInstance(getApplicationContext());

        getSetPerson();

        final CharSequence[] Intervale = {getResources().getString(R.string.minutes2),getResources().getString(R.string.minutes5), getResources().getString(R.string.minutes10), getResources().getString(R.string.minutes15), getResources().getString(R.string.always)};
        final AlertDialog.Builder alt_bldInt = new AlertDialog.Builder(this);
        alt_bldInt.setIcon(R.drawable.ic_settings_alert);
        alt_bldInt.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });




        alt_bldInt.setNegativeButton(getResources().getString(R.string.cancel), null);
        alt_bldInt.setTitle(getResources().getString(R.string.intervalscan));
        alt_bldInt.setSingleChoiceItems(Intervale, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String mensajePr = getResources().getString(R.string.intervalscantoast) + Intervale[item];
                ToastCostumizado(mensajePr);
                if (Intervale[item]==getResources().getString(R.string.minutes2)){
                    interval = "7200";
                }
                else if (Intervale[item]==getResources().getString(R.string.minutes5)){
                    interval = "18000";
                }
                else if (Intervale[item]==getResources().getString(R.string.minutes10)){
                    interval = "36000";
                }
                else if (Intervale[item]==getResources().getString(R.string.minutes15)){
                    interval = "54000";
                }

            }
        });

        btnIntervaleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertInt = alt_bldInt.create();
                alertInt.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuracion config = db.getConfig(person.getEmail());
                config.setInterval(interval);
                db.updateConfig(config);

                // Create intent
                Intent intent = new Intent(MySettings.this, HomeActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                Intent intent = new Intent(MySettings.this, AboutUs.class);

                // Start change to a new layout
                startActivity(intent);
            }
        });

        textPercentage.setText(seekBar1.getProgress() + "/" + seekBar1.getMax());

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textPercentage.setText(seekBar1.getProgress() + "/" + seekBar1.getMax());

            }

        });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return true;
    }

    /**Metodo para abrir el form escogido en el menu**/
    private void displayView(int options){

        switch (options) {
            case 1:
                //aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(MySettings.this, ProfileActivity.class);
                //Create the Intent element
                intentProfile.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentProfile);
                this.finish();
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(MySettings.this, History.class);
                //Create the Intent element
                intentHistory.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentHistory);
                this.finish();
                break;
            case 3:
                //aqui se abrira la actividad Home
                Intent intentHome = new Intent(MySettings.this, HomeActivity.class);
                //Create the Intent element
                intentHome.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentHome);
                this.finish();
                break;
            case 4:
                //Actual activity
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        /*
        this.finish();
        */

    }


}
