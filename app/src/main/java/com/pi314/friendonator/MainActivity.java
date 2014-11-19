package com.pi314.friendonator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import Bluetooth.BluetoothHandler;
import Bluetooth.DeviceValidator;
import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
import misc.BackgroundService;


public class MainActivity extends Activity implements Button.OnClickListener{
    BluetoothAdapter mBluetoothAdapter =null;
    BluetoothDevice device;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME = "BluetoothDemo";
    TextView output;
    Button btnServer, btnScan, btnClient, startservice, startServer, startClientTest;
    BluetoothHandler mBHand;
    /** Called when the activity is first created. */

    //Elementos del menu
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Profile", "History", "Home", "MainActivity", "Match", "My settings"};
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    SQLiteHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnPerfil = (Button)findViewById(R.id.btnPerfil);
        final Button btnHome = (Button)findViewById(R.id.btnHome);

        getApplicationContext().deleteDatabase("BaseFriendonator");

        db = SQLiteHelper.getInstance(getApplicationContext());

        // Insert of all interests for the first time
        initialDataBaseInserts();

        btnPerfil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent element
                Intent intent = new Intent(MainActivity.this,
                        ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent element
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        output = (TextView) findViewById(R.id.output);
        output.append("\n");
        btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setOnClickListener(this);
        btnClient = (Button) findViewById(R.id.btnClient);
        btnClient.setOnClickListener(this);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        startServer = (Button) findViewById(R.id.StrtServer);
        startServer.setOnClickListener(this);
        startClientTest = (Button) findViewById(R.id.clntTest);
        startClientTest.setOnClickListener(this);
        //////
        startservice = (Button) findViewById(R.id.startservice);
        startservice.setOnClickListener(this);

        mBHand = BluetoothHandler.getInstance(this);
        DeviceValidator dv = new DeviceValidator();
        String enc = dv.encrypt("1193434");
        String desenc = dv.decrypt(enc);
        output.setText("Orig: 1193434 \n" +
                "Encripted: " + enc + "\n" +
                "Desencript: " + desenc);
        Log.d("TAG", "Message");


        //Logica para el menu
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
        NavItms.add(new Item_objct(opciones[3], NavIcons.getResourceId(3, -1)));
        NavItms.add(new Item_objct(opciones[4], NavIcons.getResourceId(4, -1)));
        NavItms.add(new Item_objct(opciones[5], NavIcons.getResourceId(5, -1)));
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
                getActionBar().setTitle("Menu");
                //invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(toggle);

    }

    public void initialDataBaseInserts() {
        if (db.checkDataBase()) {
            // Insert Superintereses
            db.insertSuperinter(new Superinteres("Music")); //1
            db.insertSuperinter(new Superinteres("Literature")); //2
            db.insertSuperinter(new Superinteres("Movies")); //3
            db.insertSuperinter(new Superinteres("Art")); //4
            db.insertSuperinter(new Superinteres("TV Shows")); //5
            db.insertSuperinter(new Superinteres("Sports")); //6
            db.insertSuperinter(new Superinteres("Science")); //7
            db.insertSuperinter(new Superinteres("Looking For")); //8

            // Insert intereses
            db.insertInteres(new Intereses("1", "Rock")); //1
            db.insertInteres(new Intereses("1", "Jazz")); //2
            db.insertInteres(new Intereses("1", "Blues")); //3
            db.insertInteres(new Intereses("1", "Classical")); //4
            db.insertInteres(new Intereses("1", "Hip Hop")); //5
            db.insertInteres(new Intereses("1", "Electronic")); //6
            db.insertInteres(new Intereses("1", "Pop")); //7
            db.insertInteres(new Intereses("1", "Romantic")); //8

            db.insertInteres(new Intereses("2", "Novel")); //9
            db.insertInteres(new Intereses("2", "Drama")); //10
            db.insertInteres(new Intereses("2", "Poetry")); //11
            db.insertInteres(new Intereses("2", "Romance")); //12
            db.insertInteres(new Intereses("2", "Comedy")); //13
            db.insertInteres(new Intereses("2", "Fiction")); //14
            db.insertInteres(new Intereses("2", "Fantasy")); //15
            db.insertInteres(new Intereses("2", "Mythology")); //16

            db.insertInteres(new Intereses("3", "Action")); //17
            db.insertInteres(new Intereses("3", "Thriller")); //18
            db.insertInteres(new Intereses("3", "Romantic")); //19
            db.insertInteres(new Intereses("3", "Comedy")); //20
            db.insertInteres(new Intereses("3", "Fantasy")); //21
            db.insertInteres(new Intereses("3", "Historical")); //22
            db.insertInteres(new Intereses("3", "Horror")); //23
            db.insertInteres(new Intereses("3", "Scy Fy")); //24

            db.insertInteres(new Intereses("4", "Portrait")); //25
            db.insertInteres(new Intereses("4", "Landscape")); //26
            db.insertInteres(new Intereses("4", "Conceptual")); //27
            db.insertInteres(new Intereses("4", "Modernism")); //28
            db.insertInteres(new Intereses("4", "Criticism")); //29
            db.insertInteres(new Intereses("4", "Neoclassic")); //30
            db.insertInteres(new Intereses("4", "Classic")); //31
            db.insertInteres(new Intereses("4", "Expressionism")); //32

            db.insertInteres(new Intereses("5", "Soap Opera")); //33
            db.insertInteres(new Intereses("5", "Sitcom")); //34
            db.insertInteres(new Intereses("5", "Sports")); //35
            db.insertInteres(new Intereses("5", "Documentary")); //36
            db.insertInteres(new Intereses("5", "News")); //37
            db.insertInteres(new Intereses("5", "Reality")); //38
            db.insertInteres(new Intereses("5", "Cookery")); //39
            db.insertInteres(new Intereses("5", "Drama")); //40

            db.insertInteres(new Intereses("6", "Mix Martial Arts")); //41
            db.insertInteres(new Intereses("6", "Climbing")); //42
            db.insertInteres(new Intereses("6", "Gymnastic")); //43
            db.insertInteres(new Intereses("6", "Cycling")); //44
            db.insertInteres(new Intereses("6", "Jogging")); //45
            db.insertInteres(new Intereses("6", "Tennis")); //46
            db.insertInteres(new Intereses("6", "Basketball")); //47
            db.insertInteres(new Intereses("6", "Soccer")); //48

            db.insertInteres(new Intereses("7", "Science"));  //49

            db.insertInteres(new Intereses("8", "Man"));  //50
            db.insertInteres(new Intereses("8", "Woman"));  //51
        }
    }

    public void findmeT(View v){
        Intent inte = new Intent(this, FindMeTool.class);
        startActivity(inte);
    }
    @Override
    public void onClick(View v) {
        if ( (Button)v == btnServer) {
          //
        } else if ( (Button)v == btnClient) {
            Log.v("BluetoothFR", "******** start new scan *******" );
            //mBHand.StartScan();
            //ahora background service se encarga de empezar el scan cada ves que
            //este termine
            BackgroundService bks = new BackgroundService();
            bks.ScanConstantly(this);
        } else if ( (Button)v == btnScan) {
            output.setText("");
                for (BluetoothDevice b : mBHand.getDevicesList()) {
                    output.append(b.getName() + "\n");
                }

            }
        else if ((Button)v == startservice){
            Toast t = Toast.makeText(this, "calling service", Toast.LENGTH_LONG);
            t.show();
            Intent in = new Intent(this, BackgroundService.class);
            startService(in);
        }
        else if ((Button)v == startServer){
            mBHand.startBluetoothServer();
        }
        else if ((Button)v == startClientTest){
            mBHand.ClientTest();
        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**Metodo para abrir el form escogido en el menu**/
    private void displayView(int options){

        switch (options) {
            case 1:
                //aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                //Create the Intent element
                Bundle bProfile = new Bundle();
                intentProfile.putExtras(bProfile);
                //Start the new Activity
                startActivity(intentProfile);
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(MainActivity.this, History.class);
                //Create the Intent element
                Bundle bHistory = new Bundle();
                intentHistory.putExtras(bHistory);
                //Start the new Activity
                startActivity(intentHistory);
                break;
            case 3:
                //aqui se abrira la actividad Home
                Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                //Create the Intent element
                Bundle bHome = new Bundle();
                intentHome.putExtras(bHome);
                //Start the new Activity
                startActivity(intentHome);
                break;
            case 4:
               /* //aqui se abrira la actividad MainActivity
                Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                //Create the Intent element
                Bundle bMain = new Bundle();
                intentMain.putExtras(bMain);
                //Start the new Activity
                startActivity(intentMain);
                break;*/
                break;
            case 5:
                Intent intentMatch = new Intent(MainActivity.this, MenuActivity.class);
                //Create the Intent element
                Bundle bMatch = new Bundle();
                intentMatch.putExtras(bMatch);
                //Start the new Activity
                startActivity(intentMatch);
                //Toast.makeText(MainActivity.this,
                //       "Maintenance",
                //        Toast.LENGTH_SHORT).show();
                break;
            case 6:
                //aqui se abrira la actividad MySettings
                Intent intentMySettings = new Intent(MainActivity.this, MySettings.class);
                //Create the Intent element
                Bundle bMySettings = new Bundle();
                intentMySettings.putExtras(bMySettings);
                //Start the new Activity
                startActivity(intentMySettings);
                break;
            default:
                break;
        }
    }
}
