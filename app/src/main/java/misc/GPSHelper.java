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

import com.pi314.friendonator.MainActivity;
import com.pi314.friendonator.R;

/**
 * Created by andrea on 03/12/14.
 * La clase 'GPSHelper' es un conjunto de metodos estaticos para comunicar con el api de GPS.
 * No devuelve una direccion exacta, sino mas bien una direccion aproximada.
 */
public class GPSHelper {
    public static MainActivity home = null;



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

    /**
     * Es un wrapper para el MainActivity.getLocation
     * @return
     */
    public static Location getLocation() {
        return home.getLocation();
    }

}
