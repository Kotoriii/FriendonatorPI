package misc;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
 *
 * como usar:
 *      Intent in = new Intent(this, BackgroundService.class);
 *      startService(in);
 */
public class BackgroundService extends IntentService {

    private final int mIdNotification = 48454;
    NotificationManager mNotificationManager = null;
    private boolean running = true;


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
        this.running = true;

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        //todo descomentar
        //this.ScanEveryX(getApplicationContext(), (Person)intent.getSerializableExtra("PERSON"));
        this.ScanForSync(getBaseContext());

        return super.onStartCommand(intent, flags, startId);
    }

    public void endService() {
        if (this.mNotificationManager != null) {
            this.mNotificationManager.cancel(mIdNotification);
        }
    }

    public void alert_new_match(String id_match, Person person, int match_perc) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Friendonator");
        mBuilder.setContentText(getResources().getText(R.string.found_a_match) + String.valueOf(match_perc));

        Intent resultIntent = new Intent(this, MatchProfileActivity.class);
        resultIntent.putExtra("PERSON", person);
        resultIntent.putExtra("ID", id_match);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MatchProfileActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        this.mIdNotification,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setOngoing(true);
        mNotificationManager.notify(mIdNotification, mBuilder.build());
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
                        this.mIdNotification,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setOngoing(true);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mIdNotification, mBuilder.build());
    }

    public void ScanEveryX(final Context activity, final Person person) {
        final BluetoothHandler bMan = BluetoothHandler.getInstance((Activity) activity);
        final SQLiteHelper db = SQLiteHelper.getInstance(getApplicationContext());
        Thread scanner = new Thread() {
            public void run() {
                try {
                    while (running) {
                        synchronized (this) {
                            bMan.StartScan();
                        }
                        this.sleep(Integer.parseInt(db.getConfig(person.getEmail()).getInterval()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scanner.start();

    }

    public void ScanForSync(final Context act) {
        final SQLiteHelper help = SQLiteHelper.getInstance(act);
        final SyncWithServer sync = new SyncWithServer((Activity) act);

        Thread syncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean algo_cambio = false;
                while (running) {
                    if (sync.isConnected(act)) {
                        if (help.textosCambiaron()) {
                            if(sync.sync_textos_upstream())
                                help.updateSync(help.TEXTOS, 0);
                            algo_cambio = true;
                        }
                        if (help.imgPerfCambiaron()) {
                            if(sync.sync_image_upstream())
                                help.updateSync(help.IMAGEN_PERFIL, 0);
                            algo_cambio = true;
                        }
                        if (help.interesesCambiaron()) {
                            if(sync.sync_interests_upstream())
                                help.updateSync(help.INTERESES, 0);
                            algo_cambio = true;
                        }
                        if (help.datosPCambiaron()) {
                            if(sync.sync_user_upstream())
                                help.updateSync(help.DATOS_PERSONALES, 0);
                            algo_cambio = true;
                        }
                        if (!algo_cambio) {
                            sync.sync_user_downstream();
                        } else {
                            algo_cambio = false;
                        }
                    }
                    try {
                        Thread.sleep(120000); //2.2 min

                        //todo descomentar
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


    public void AlertMatch(Person person, Activity act, int percentagePerson, int percentageMain) {
        // todo sacar minimo pocentage de la base de datos y assignar a percentageMain
        if (percentagePerson <= percentageMain) {
        }
           /*
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.Matchfound))
                .setMessage(getResources().getString(R.string.seePerson))
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // code
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // code
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
                */
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//Agregando el icono, texto y momento para lanzar la notificación
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "Notification Bar";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        Context context = getApplicationContext();
        CharSequence contentTitle = getResources().getString(R.string.Matchfound);
        CharSequence contentText = getResources().getString(R.string.seePerson);
        //Agregando sonido
        notification.defaults |= Notification.DEFAULT_SOUND;
        //Agregando vibración
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        Intent notificationIntent = new Intent(this, History.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);


        mNotificationManager.notify(1, notification);

    }
}