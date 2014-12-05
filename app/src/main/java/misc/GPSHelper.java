package misc;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by andrea on 03/12/14.
 * La clase 'GPSHelper' se encarga automaticamente de inicializar todos las cosas que necesita
 * y de finalizarlas cuando estos den un resultado. El usuario de esta clase nadamas tiene que
 * preocuparse por inicializar y pedir la latitud y longitud. Pero cuidado porque estos metodos
 * son Thread Blocking lo que quiere decir que bloquean la ejecucion del Thread hasta que se
 * completen, asi que lo mas seguro es llamarlos desde un thread para si mismos.
 */
public class GPSHelper {
    private LocationManager mgr=null;
    private Activity mAct = null;
    private String lat = null;
    private String lng = null;

    /**
     * Inicializa el gpshelper y directamente pide la posicion (latitud y longitud)
     * @param act
     */
    public GPSHelper(Activity act) {
        mAct = act;
        mgr=(LocationManager)mAct.getSystemService(mAct.LOCATION_SERVICE);
        this.start_gps();
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


    private void stop_gps() {
        mgr.removeUpdates(onLocationChange);
    }

    private LocationListener onLocationChange=new LocationListener() {
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
     * Devuelve la latitud. Bloquea el Thread hasta que se finalice su ejecucion.
     * @return
     */
    public String getLat() {
        while(this.lat == null){

        }
        return lat;
    }

    /**
     * Devuelve la longitud. Bloquea el Thread hasta que se finalice su ejecucion.
     * @return
     */
    public String getLng() {
        while(this.lng == null){

        }
        return lng;
    }
}
