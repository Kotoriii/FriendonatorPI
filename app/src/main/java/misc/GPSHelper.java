package misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.pi314.friendonator.R;

/**
 * Created by andrea on 03/12/14.
 * La clase 'GPSHelper' se encarga automaticamente de inicializar todos las cosas que necesita
 * y de finalizarlas cuando estos den un resultado. El usuario de esta clase nadamas tiene que
 * preocuparse por inicializar y pedir la latitud y longitud. Pero cuidado porque estos metodos
 * son Thread Blocking lo que quiere decir que bloquean la ejecucion del Thread hasta que se
 * completen, asi que lo mas seguro es llamarlos desde un thread para si mismos.
 */
public class GPSHelper {
    private LocationManager mgr = null;
    private Activity mAct = null;
    private String lat = null;
    private String lng = null;
    private boolean gps_location;
    private boolean network_location;
    private static GPSHelper hl = null;

    /**
     * Inicializa el gpshelper y directamente pide la posicion (latitud y longitud)
     *
     * @param act
     */
    private GPSHelper(Activity act) {


        Handler handler = new Handler(Looper.getMainLooper());

        mAct = act;
        mgr = (LocationManager) mAct.getSystemService(mAct.LOCATION_SERVICE);

        //ver si estan disponibles
        this.gps_location = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.network_location = mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start_gps();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    public static GPSHelper getInstance(Activity act){
        if(hl == null){
            hl = new GPSHelper(act);
        }
        return hl;
    }
    /**
     * Empieza el servicio GPS. Lo que quiere decir es q pide la posicion actual de la persona.
     * La posicion se obtiene tanto por GPS como por 'network' en el caso de que este conectado
     * a internet. El metodo que de resultado primero es el que se muestra
     */
    private void start_gps() {

        //por network provider
        mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                3600000, 1000,
                onLocationChange);
        //por GPS
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3600000, 1000,
                onLocationChange);
    }

    /**
     * Metodo estatico que DEBE de ser llamado desde un activity, se fija si el gps se encuentra
     * disponible y sino entonces muestra un alert el cual manda al usuario a settings, donde puede
     * habilitar el location service
     *
     * @param act, este metodo usa metodos propios de la clase activity
     */
    public static void checkIfLocationEnabled(final Activity act) {
        if (!((LocationManager) act.getSystemService(act.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setMessage(R.string.checkIfLocationEnabledAlert)
                    .setCancelable(false)
                    .setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            act.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void stop_gps() {
        mgr.removeUpdates(onLocationChange);
    }

    private LocationListener onLocationChange = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
            stop_gps();
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }
    };

    /**
     * Devuelve la latitud. No es super preciso ya q devuelve la ultima latitud
     * conocida. Pero mejor eso q bloquear el thread esperando a que llegue la latitud
     *
     * @return
     */
    public String getLat() {
        Double dbLat = 0.0;
        if (this.gps_location || this.network_location) {
            dbLat = ((LocationManager) mAct.getSystemService(mAct.LOCATION_SERVICE))
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            if (dbLat == null) {
                dbLat = ((LocationManager) mAct.getSystemService(mAct.LOCATION_SERVICE))
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
            }
        }
        return String.valueOf(dbLat);
    }

    /**
     * Devuelve la longitud. No es super preciso ya q devuelve la ultima longitud
     * conocida. Pero mejor eso q bloquear el thread esperando a que llegue la longintud
     *
     * @return
     */
    public String getLng() {
        Double dbLong = 0.0;
        if (this.gps_location || this.network_location) {
            dbLong = ((LocationManager) mAct.getSystemService(mAct.LOCATION_SERVICE))
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            if (dbLong == null) {
                dbLong = ((LocationManager) mAct.getSystemService(mAct.LOCATION_SERVICE))
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            }
        }
        return String.valueOf(dbLong);
    }
}
