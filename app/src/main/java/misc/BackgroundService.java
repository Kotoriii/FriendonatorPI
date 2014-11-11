package misc;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pi314.friendonator.MainActivity;
import com.pi314.friendonator.R;

import Bluetooth.BluetoothHandler;


/**
 * Created by andrea on 01/10/14.
 */
public class BackgroundService extends IntentService {

    private final int mIdNotification = 48454;
    NotificationManager mNotificationManager = null;

    public BackgroundService() {
        super("FriendonatorBackgroundProcess"); //<--- !!!!!!!!!
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // lo que se hace cuando se ejecuta el servicio
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //lo que se ejecuta antes de ejecutar el servicio

        //########################### Momentaneo
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();


        Thread scareTimer = new Thread(){
            public void run(){
                try{
                    Integer p = 9999;
                    Integer o = 0;
                    while (o <p){
                        synchronized (this) {
                            buildNotification(o.toString());
                            o++;
                        }
                        this.sleep(5000);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        scareTimer.start();


        //this.buildNotification();
        //##########################
        return super.onStartCommand(intent, flags, startId);
    }

    public void endService(){
        if(this.mNotificationManager != null) {
            this.mNotificationManager.cancel(mIdNotification);
        }
    }

    public void buildNotification(String mensaje) {

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Friendonator")
                .setContentText(mensaje);
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setOngoing(true);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mIdNotification, mBuilder.build());
    }

    public void ScanEveryX(final int x, final Activity activity){
        final BluetoothHandler bMan = BluetoothHandler.getInstance(activity);
        Thread scanner = new Thread(){
            public void run(){
                try{
                    while (true){
                        synchronized (this) {
                            bMan.StartScan();
                        }
                        this.sleep(x);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        scanner.start();

    }

    public void ScanConstantly(final Activity activity){
        final BluetoothHandler bMan = BluetoothHandler.getInstance(activity);
        Thread scanner = new Thread(){
            public void run(){
                try{
                    while (true){
                        synchronized (this) {
                            if(!bMan.getAdapter().isDiscovering()) {

                                Log.v("BluetoothFR", "^^^^^ start scan Background Service. Conserving list");
                                bMan.getAdapter().startDiscovery();//doesn't clear the devices list
                            }
                        }
                        this.sleep(400);//sleep 400 ms, just to not occupy to much memory
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        scanner.start();

    }
}