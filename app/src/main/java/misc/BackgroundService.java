package misc;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pi314.friendonator.History;
import com.pi314.friendonator.MainActivity;
import com.pi314.friendonator.MatchProfileActivity;
import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;

import Bluetooth.BluetoothHandler;
import Database.SQLiteHelper;


/**
 * Created by andrea on 01/10/14.
 * <p/>
 * como usar:
 * Intent in = new Intent(this, BackgroundService.class);
 * startService(in);
 */
public class BackgroundService extends IntentService {

    public static final int mIdNotification = 48454;
    public static NotificationManager mNotificationManager = null;
    private boolean running = true;
    private static Activity mAct;


    public BackgroundService() {
        super("FriendonatorBackgroundProcess"); //<--- !!!!!!!!!
    }

    public static void setmAct(Activity mAct) {
        BackgroundService.mAct = mAct;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // lo que se hace cuando se ejecuta el servicio
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //lo que se ejecuta antes de ejecutar el servicio
        this.running = true;
        this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //todo quitar toasts
        if (this.mAct != null) {
            Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
            SyncWithServer sync = new SyncWithServer(mAct);
            this.ScanEveryX(mAct, sync.obtain_user());
            this.ScanForSync(mAct);
        } else {
            Toast.makeText(this, "BackgroundService. Variable estatica mAct es null. no se puede inicializar", Toast.LENGTH_SHORT).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void endService() {
        if (this.mNotificationManager != null) {
            this.mNotificationManager.cancel(mIdNotification);
        }
    }

    public static void alert_new_match(String id_match, Person person, int match_perc) {
        if(mAct!= null && mNotificationManager != null) {
            NotificationCompat.Builder mBuilder;
            mBuilder = new NotificationCompat.Builder(mAct)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Clover")
                    .setDefaults(Notification.DEFAULT_SOUND);

            mBuilder.setContentText(mAct.getResources().getText(R.string.found_a_match) + " " + String.valueOf(match_perc));

            Intent resultIntent = new Intent(mAct, MatchProfileActivity.class);
            resultIntent.putExtra("PERSON", person);
            resultIntent.putExtra("ID", id_match);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mAct);
            stackBuilder.addParentStack(MatchProfileActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            mIdNotification,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mNotificationManager.notify(mIdNotification, mBuilder.build());
        }
    }

    public void ScanEveryX(final Activity activity, final Person person) {
        final BluetoothHandler bMan = BluetoothHandler.getInstance(activity);
        final SQLiteHelper db = SQLiteHelper.getInstance(getApplicationContext());
        Thread scanner = new Thread() {
            public void run() {
                try {
                    while (running) {
                        synchronized (this) {
                            if(bMan.isBluetoothEnabled()) {
                                bMan.StartScan();
                            }else{
                                bMan.getDevicesList().clear();
                                bMan.StartBlueTooth();
                            }
                        }
                        this.sleep(Integer.parseInt(db.getConfig(person.getId()).getInterval()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scanner.start();

    }

    public void ScanForSync(final Activity act) {
        final SQLiteHelper help = SQLiteHelper.getInstance(act);
        final SyncWithServer sync = new SyncWithServer(act);

        Thread syncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean algo_cambio = false;
                while (running) {
                    if (sync.isConnected(act)) {
                        if (help.textosCambiaron()) {
                            if (sync.sync_textos_upstream())
                                help.updateSync(help.TEXTOS, 0);
                            algo_cambio = true;
                        }
                        if (help.imgPerfCambiaron()) {
                            if (sync.sync_image_upstream())
                                help.updateSync(help.IMAGEN_PERFIL, 0);
                            algo_cambio = true;
                        }
                        if (help.interesesCambiaron()) {
                            if (sync.sync_interests_upstream())
                                help.updateSync(help.INTERESES, 0);
                            algo_cambio = true;
                        }
                        if (help.datosPCambiaron()) {
                            if (sync.sync_user_upstream())
                                help.updateSync(help.DATOS_PERSONALES, 0);
                            algo_cambio = true;
                        }
                        if (help.historialCambiaron()) {
                            if (sync.sync_history_upstream())
                                help.updateSync(help.HISTORIAL, 0);
                                //todo descomentar para que el historial se borre despues de que se
                                //actualize con el servidor. Supongo que para la presentacion lo vamos
                                //a dejar sin q se borre para q el historial se muestre
                                //help.getWritableDatabase().execSQL("dete from historial");
                            algo_cambio = true;
                        }
                        if (!algo_cambio) {
                            sync.sync_user_downstream();
                        } else {
                            algo_cambio = false;
                        }
                    }
                    try {
                        Thread.sleep(300000); //2.2 min

                        //todo se deberia de fijar cada 2 min o cada 30?
                        //Thread.sleep(1800000); // 30 min
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        syncThread.start();
    }

    public void stopBackgroundService() {
        this.running = false;
    }

    public void ScanConstantly(final Activity activity) {
        final BluetoothHandler bMan = BluetoothHandler.getInstance(activity);
        //sets this.is_scanning to true
        Thread scanner = new Thread() {
            public void run() {
                try {
                    while (running) {
                        synchronized (this) {
                            if (!bMan.getAdapter().isDiscovering()) {
                                Log.v("BackgroundService", "^^^^^ start scan Background Service. Conserving list");
                                bMan.getAdapter().startDiscovery();//doesn't clear the devices list
                            }
                        }
                        this.sleep(400);//sleep 400 ms, just to not occupy to much memory
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scanner.start();

    }

}