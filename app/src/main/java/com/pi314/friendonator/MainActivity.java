package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import Bluetooth.BluetoothHandler;
import Database.SQLiteHelper;
import misc.BackgroundService;
import misc.GPSHelper;


public class MainActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This will close the app from Home
        if(getIntent().getBooleanExtra("Exit me", false)){
            BluetoothHandler.restaurarNombre(this);//restaura el nombre del telefono a su original
            BackgroundService.stopBackgroundService(); //para todos los threads del BKS
            BackgroundService.endService();
            finish();
        } else {
            /** Aqui solamente se inicializan cosas y se utiliza el OnDestroy de esta Clase */
            //metodo estatico.. muestra un alert si location no esta disponible en los settings.
            GPSHelper.checkIfLocationEnabled(this);

            SQLiteHelper.getInstance(getApplicationContext());

            GPSHelper.home = this;

            // solamente para que se pueda apreciar mejor el splashscreen
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            this.wait(3000);
                            Intent login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(login);

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Activamos el toggle con el icono
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Metodo que usa GPSHelper para obtener la posicion.
     * @return
     */
    public Location getLocation(){
        LocationManager man = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location loc = man.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(loc == null)
            loc = man.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(loc == null) {
            loc = new Location(LocationManager.GPS_PROVIDER);
            loc.setLongitude(0.0);
            loc.setLatitude(0.0);
        }
        return loc;
    }
}
