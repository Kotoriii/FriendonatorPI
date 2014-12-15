package Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.pi314.friendonator.LoginActivity;
import com.pi314.friendonator.Person;
import com.pi314.interests.InterestsMethods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import Database.SQLiteHelper;
import Database.Usuario;
import misc.BackgroundService;

/**
 * Created by andrea on 30/09/14.
 */
public class BluetoothHandler {
    BluetoothAdapter mBluetoothAdapter = null;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("f409e1f6-5665-11e4-9e35-164230d1df67");
    private static final String NAME = "Friendonator";
    private boolean registered = false;
    private DeviceValidator mValidator = null;
    List<BluetoothDevice> lstDisptV = new ArrayList<BluetoothDevice>();
    Activity mAct = null;
    private static BluetoothHandler mbth;
    private boolean user_in_intent = false;

    public static BluetoothHandler getInstance(Activity act) {
        if (mbth == null) {
            mbth = new BluetoothHandler(act);
        }
        return mbth;
    }

    public void redefineActivity(Activity act) {
        //si no esta entonces va a dar nullpointerexception
        try {
            Person p = (Person)act.getIntent().getSerializableExtra("PERSON");
            if(p != null) {
                user_in_intent = true;
            }
        } catch (NullPointerException e) {
            user_in_intent = false;
        }
        this.mAct = act;
    }

    private BluetoothHandler(Activity act) {
        this.redefineActivity(act);

        mValidator = new DeviceValidator();

        //primero se registra el listener y luego se prende el bluetooth
        if (!registered) {
            registerAdapter();
        }
        if (!this.isBluetoothEnabled()) {
            StartBlueTooth();
        }

        if(this.isBluetoothEnabled()){
            salvarNombre(mAct);
            setNuevoNombre();
            setUnlimitedVisibility();
            if (user_in_intent) {
                startBluetoothServer();
            }
        }
    }


    public BluetoothAdapter getAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    public List<BluetoothDevice> getDevicesList() {
        return lstDisptV;
    }


    private void registerAdapter() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        try {
            mAct.unregisterReceiver(getReceiver());
            Log.d("BluetoothFR", "Reciever registered and unregistering");
        } catch (Exception e) {
            Log.d("BluetoothFR", "Reciever wasn't registered");
        }
        try {
            mAct.registerReceiver(getReceiver(), filter);
            registered = true;
        } catch (Exception e) {
            Log.e("BluetoothFR", "Register is trying to register on top of another");
        }
    }

    public void setUnlimitedVisibility() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        mAct.startActivity(discoverableIntent);
    }

    /**
     * Cambia el nombre bluetooth del dispositivo
     * PEDIR PERMISO ANTES!
     *
     * @param name
     */
    private void setLocalBluetoothName(String name) {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        mBluetoothAdapter.setName(name);
    }

    /**
     * Antes de empezar el scan se eliminan los elementos de la lista de dispositivos validos.
     * Esto para evitar que se muestren dispositivos que ya no se encuentran en la zona ya que
     * el scan solo descubre dispositivos pero no se fija cuando los dispositivos ya no son visibles.
     * <p/>
     * Hay que tener en cuenta que la informacion de cada dispositivo (ej: fuerza de se_al, nombre y
     * direccion) son los que se obtienen a la hora de hacer el scan y no son constantemente actualizados.
     */
    public void StartScan() {
        getDevicesList().clear();
        getAdapter().startDiscovery();
    }

    /**
     * Devuelve el nombre actual del dispositivo para bluetooth
     */
    private String getLocalBluetoothName() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        String name = mBluetoothAdapter.getName();

        if (name == null) {
            name = "";
        }
        return name;
    }

    /**
     * Starts the actual bluetooth.
     *
     * @return
     */
    public boolean StartBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            return false;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mAct.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        return true;
    }

    public void startBluetoothServer() {
        ServerThread SV = new ServerThread();
        SV.start();
    }

    public void stopBlueTooth() {
        mBluetoothAdapter.disable();
    }


    public BroadcastReceiver getReceiver() {
        return this.mReceiver;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private boolean finding = true;
        private BluetoothDevice findingDevice;

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!lstDisptV.contains(device) &&
                        mValidator.isValidDevice(device)) {
                    lstDisptV.add(device);

                    //Ejecuta el client thread con el dispositivo recien encontrado
                    new ClientThread(device).start();
                }

                try {
                    BluetoothDevice dev = getAdapter().getRemoteDevice(device.getAddress());
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    Log.d("BluetoothFR", device.getName() + " RSSI: " + rssi + " describe_contents: " + device.describeContents());

                    mAct.getIntent().putExtra("name", device.getName());
                    mAct.getIntent().putExtra("strg", rssi);

                } catch (Exception e) {
                }


                if (finding && mValidator.isValidDevice(device)) {// && device.getAddress().equals(findingDevice.getAddress())){

                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    Log.v("BluetoothFR", "Device RSSI: " + rssi + "\n Name: " + device.getName());

                }

            }

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        //
                        salvarNombre(mAct);
                        setNuevoNombre();
                        setUnlimitedVisibility();
                        if (user_in_intent) {
                            startBluetoothServer();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }


        }

        public void fingdDeviceMR(BluetoothDevice bd) {
            this.findingDevice = bd;
            this.finding = true;
        }

        public void stopFingdDeviceMR() {
            this.findingDevice = null;
            this.finding = false;
        }
    };


    /**
     * Calcula un nombre numerico Random, lo encripta y se lo asigna como
     * nuevo nombre Bluetooth al telefono
     */
    public void setNuevoNombre() {
        String nuevoNombre = "";
        int cont = 0;
        while (cont <= this.getRandomLenght()) {
            cont++;
            nuevoNombre += this.getRandomNumber();
        }
        nuevoNombre = mValidator.encrypt(nuevoNombre);
        setLocalBluetoothName(nuevoNombre);
    }

    private int getRandomLenght() {
        Random ran = new Random();
        int[] opciones = {
                3, 4, 5
        };
        return opciones[ran.nextInt(3)];
    }

    /**
     * Retorna un numero random del 1 al 9
     *
     * @return
     */
    private int getRandomNumber() {
        Random ran = new Random();
        int[] opciones = {
                1, 2, 3, 4, 5, 6, 7, 8, 9
        };
        return opciones[ran.nextInt(8)];
    }


    public boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
    }

    public static void salvarNombre(Activity act){
        SQLiteHelper.getInstance(act).salvarNombreBluetooth(
                BluetoothAdapter.getDefaultAdapter().getName()
        );
    }

    public static void restaurarNombre(Activity act){
        BluetoothAdapter.getDefaultAdapter().setName(
                SQLiteHelper.getInstance(act).getDatabaseName()
        );
    }

    /**
     * Corre indefinidamente, acepta conexiones inseguras de dispositivos con el mismo UUID y
     * le manda un objeto serializado el cual corresponderia a una representacion del usuario
     */
    private class ServerThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public ServerThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = getAdapter().listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    if(isBluetoothEnabled()) {
                        socket = mmServerSocket.accept();
                    }
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {

                    //Por el momento este es el objeto que manda, mas adelante va a mandar el
                    //objeto representante al usuario del telefono
                    Log.v("BluetoothFR", "got connection from client, starting send");
                    ConnectedToClientThread cntCT = new ConnectedToClientThread(socket);
                    cntCT.start();

                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void close() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }

    }


    /**
     * Busca servidores, cuando lo encuentra se conecta y pide el objeto usuario, despues lo guarda
     * en la base de datos y se cierra. El que se encarga de empezar este thread es el
     * bluetooth adapter cuando encuentra un nuevo dispositivo!
     */
    private class ClientThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ClientThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            // temporalmente
            getAdapter().cancelDiscovery();

            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            //mmSocket.connect();
            Log.v("BluetoothFR", "Connected to Server, starting data reciever'");
            ConnectedToServerThread coTST = new ConnectedToServerThread(mmSocket);
            coTST.start();

        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void close() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Serializa y manda el objeto que se le manda al constructor por parametro
     * El objeto debe ser serializable!
     * Despues de ejecutarse el tread cierra el socket y se termina a si mismo
     */
    private class ConnectedToClientThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;

        public ConnectedToClientThread(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmOutStream = tmpOut;
        }

        public void run() {

            try {
                Log.v("BluetoothFR", "Sending data via ObjectOutputStream");

                //solo escribe el usuario si user_in_intent es true
                //eso quiere decir que el usuario esta disponible como extra
                //en el intent de mActivirty
                if (user_in_intent) {
                    Person person = (Person) mAct.getIntent().getSerializableExtra("PERSON");
                    File foto_p_file = new File(person.getFoto_perfil());
                    Bitmap foto_p_bitmp = BitmapFactory.decodeFile(foto_p_file.getAbsolutePath());
                    person.setEncodedImage(this.BitMapToString(foto_p_bitmp));
                    ObjectOutputStream oos = new ObjectOutputStream(mmOutStream);
                    oos.flush();

                    oos.writeObject(person);
                }

            } catch (Exception e) {

            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }

        public String BitMapToString(Bitmap bitmap){
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String temp= Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }
    }

    private class ConnectedToServerThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        public ConnectedToServerThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
        }

        public void run() {
            Person matchPerson = null;

            Log.v("BluetoothFR", "Connected to server- Starting receiving loop ");

            try {
                if (!mmSocket.isConnected()) {
                    mmSocket.connect();
                    Log.v("BluetoothFR", "Connected to server- ****** Conected *****");
                }

                if (user_in_intent) {


                    ObjectInputStream bjr = new ObjectInputStream(mmInStream);
                    //otenemos a la persona
                    matchPerson = (Person) bjr.readObject();
                    //nos tenemos q fijar si el usuario existe o no.. en el caso de q exista
                    //entonces hay que borrar su foto vieja y su viejo registro en la BD (para ahorrar recursos)
                    boolean usuario_existe = false;
                    SQLiteHelper hlp = SQLiteHelper.getInstance(mAct);
                    Usuario us =hlp.getUserByID(Integer.parseInt(matchPerson.getId()));
                    if(us.getNombre() != null){
                        usuario_existe = true;
                    }
                    if(usuario_existe){
                        SQLiteDatabase dtb = hlp.getWritableDatabase();
                        File f = new File(us.getFoto());
                        f.delete(); // <-- borramos la foto vieja
                        hlp.deleteUserTextData(us.getId());
                        hlp.deleteUserInterestData(us.getId());
                        dtb.execSQL("delete from usuario where idUsuario=" + us.getId());
                        dtb.execSQL("delete from historial where idMatch="+us.getId());
                    }

                    //decodificamos la foto
                    Bitmap foto_p = this.StringToBitMap(matchPerson.getEncodedImage());
                    //la salvamos y le asignamos el path a la persona
                    matchPerson.setFoto_perfil(this.saveBitmap(mAct, foto_p));


                    final InterestsMethods mtf = new InterestsMethods();
                    Person person = (Person) mAct.getIntent().getSerializableExtra("PERSON");
                    final int percentage = (int) Math.floor(mtf.getMatchPercentage(person, matchPerson));

                    final Person finalMatchPerson = matchPerson;
                    mtf.insertReceivedPerson(mAct, finalMatchPerson, finalMatchPerson.getId(), percentage);
                    hlp.updateSync(hlp.HISTORIAL, 1);


                    //obtiene el min match para poder saber si mostrar una notificacion o no.
                    int min_match = Integer.parseInt(hlp.getConfig(person.getId()).getMinmatch());
                    if(percentage >= min_match)
                        //bum
                        BackgroundService.alert_new_match(matchPerson.getId(),person,percentage);

                    bjr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            Log.v("BluetoothFR", "Datos recieved!");

        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
        public Bitmap StringToBitMap(String encodedString){
            try{
                byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
                Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            }catch(Exception e){
                e.getMessage();
                return null;
            }
        }

        /**
         * Salva el bitmap path default. Devuelve el path
         * a la imagen.
         *
         * @param act
         * @param img_serv
         * @return El string donde se guardo.
         */
        public String saveBitmap(Activity act, Bitmap img_serv) {
            Bitmap btmp = img_serv;


            Random rndm = new Random();
            boolean salvada = false;
            while (!salvada) {
                try {
                    OutputStream fOut = null;
                    // save image
                    String name = String.valueOf(rndm.nextLong());
                    ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("Pictures", Context.MODE_PRIVATE);
                    File file = new File(directory, name + ".jpg"); // the File to save to
                    fOut = new FileOutputStream(file);
                    btmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    salvada = true;
                    fOut.flush();
                    fOut.close(); // do not forget to close the stream
                    Log.v(getClass().getSimpleName(), "image directory path " + file.getAbsolutePath());
                    return file.getAbsolutePath();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;

        }

    }


    public void ClientTest() {
        BluetoothDevice btd = lstDisptV.get(0);
        ClientThread CT = new ClientThread(btd);
        CT.start();
    }

}
