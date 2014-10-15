package com.pi314.friendonator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import Bluetooth.BlueMan;
import Bluetooth.BluetoothHandler;
import Bluetooth.DeviceValidator;
import misc.BackgroundService;


public class MainActivity extends Activity implements Button.OnClickListener{
    BluetoothAdapter mBluetoothAdapter =null;
    BluetoothDevice device;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME = "BluetoothDemo";
    TextView output;
    Button btnServer, btnScan, btnClient, startservice;
    BluetoothHandler bth;
    BlueMan bMan = null;
    /** Called when the activity is first created. */

    //Elementos del menu
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Profile", "History", "Match", "My settings"};
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //necesitamos que el primer activity sea el Login, solo si la persona no ha hecho login
        //por el momento siempre va a ser login, hasta que tengamos base de datos :D
        Intent logingAct = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logingAct);

        setContentView(R.layout.activity_main);
        final Button btnPerfil = (Button)findViewById(R.id.btnPerfil);
        final Button btnHome = (Button)findViewById(R.id.btnHome);

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

        //////
        startservice = (Button) findViewById(R.id.startservice);
        startservice.setOnClickListener(this);

        bMan = BlueMan.getInstance(this);
        DeviceValidator dv = new DeviceValidator();
        String enc = dv.encrypt("1193434");
        String desenc = dv.decrypt(enc);
        output.setText("Orig: 1193434 \n" +
                "Encripted: " + enc +"\n" +
                "Desencript: " + desenc  );


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

    @Override
    public void onClick(View v) {
        if ( (Button)v == btnServer) {
          //  bth.startServer();
            ((Button) v).setText("disabe BT");
            bMan.getHandler().stopBlueTooth();
        } else if ( (Button)v == btnClient) {
            if (device != null) {
               // output.append("button client\n");
                //bth.startClient();
            }
            bMan.startScan();

        } else if ( (Button)v == btnScan) {
            output.setText("");
                for (BluetoothDevice b : bMan.getDeviceList()) {
                    output.append(b.getName() + "\n");
                }

            }
        else if ((Button)v == startservice){
            Toast t = Toast.makeText(this, "calling service", Toast.LENGTH_LONG);
            t.show();
            Intent in = new Intent(this, BackgroundService.class);
            startService(in);
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
               /* //aqui se abrira la actividad Match
                Intent intentMatch = new Intent(MainActivity.this, Match.class);
                //Create the Intent element
                Bundle bMatch = new Bundle();
                intentMatch.putExtras(bMatch);
                //Start the new Activity
                startActivity(intentMatch);*/

                break;
            case 4:
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
