package Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andrea on 30/09/14.
 */
public class BluetoothHandler {
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice device;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("1aa3e4a8-54c5-11e4-9e35-164230d1df67");
    private static final String NAME = "Friendonator";
    BluetoothDevice remoteDevice;
    private boolean registered = false;
    private DeviceValidator mValidator = null;
    List<BluetoothDevice> lstDisptV = new ArrayList<BluetoothDevice>();
    Activity mAct = null;
    public BluetoothHandler(Activity act) {
        this.mAct=act;
        mValidator = new DeviceValidator();
        if(!this.isBluetoothEnabled()) {
            StartBlueTooth();
        }
    }

    //////////////////
    public BluetoothAdapter getAdapter() {
        return mBluetoothAdapter;
    }

    public List<BluetoothDevice> getListVisibleDevices() {
        return lstDisptV;
    }
///////////////////

    public void startServer() {
        new Thread(new ServerThread()).start();

    }

    public void startClient() {
        if (device != null) {
            new Thread(new ClientThread(device)).start();
        }
    }

    private void registerAdapter() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mAct.registerReceiver(getReceiver(), filter);
        registered = true;
    }

    public void SetUnlimitedVisibility() {
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
    public void setLocalBluetoothName(String name) {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        mBluetoothAdapter.setName(name);
    }

    /**
     * Antes de empezar el scan se eliminan los elementos de la lista de dispositivos validos.
     * Esto para evitar que se muestren dispositivos que ya no se encuentran en la zona ya que
     * el scan solo descubre dispositivos pero no se fija cuando los dispositivos ya no son visibles.
     *
     * Hay que tener en cuenta que la informacion de cada dispositivo (ej: fuerza de se_al, nombre y
     * direccion) son los que se obtienen a la hora de hacer el scan y no son constantemente actualizados.
     */
    public void Scan() {
        if (!registered) {
            registerAdapter();
        }
        getListVisibleDevices().clear();
        getAdapter().startDiscovery();
    }

    /**
     * Devuelve el nombre actual del dispositivo para bluetooth
     */
    public String getLocalBluetoothName() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        String name = mBluetoothAdapter.getName();

        if (name == null) {
            name = "";
        }
        return name;
    }

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

    public void stopBlueTooth(){
       mBluetoothAdapter.disable();
    }

    public BroadcastReceiver getReceiver() {
        return this.mReceiver;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!lstDisptV.contains(device) &&
                        mValidator.isValidDevice(device)) {
                    lstDisptV.add(device);
                }
            }
        }
    };


    public boolean isBluetoothEnabled()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
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
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {

                    //Por el momento este es el objeto que manda, mas adelante va a mandar el
                    //objeto representante al usuario del telefono
                        String potato = "potato";
                        ConnectedToClientThread cntCT = new ConnectedToClientThread(socket, potato);
                        cntCT.start();

                }
            }

        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void close() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
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
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            // temporalmente
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                ConnectedToServerThread coTST = new ConnectedToServerThread(mmSocket);
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void close() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
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
        private Object mObj;
        public ConnectedToClientThread(BluetoothSocket socket, Object obj) {
            mmSocket = socket;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpOut = socket.getOutputStream();
                mObj = obj;
            } catch (IOException e) { }

            mmOutStream = tmpOut;
        }

        public void run() {

                try {
                    // Read from the InputStream
                    ObjectOutputStream oos = new ObjectOutputStream(mmOutStream);
                    oos.writeObject(mObj);
                    this.cancel();

                } catch (Exception e) {

                }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
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
            } catch (IOException e) { }

            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(mmInStream);
                    try {
                        String pop = (String) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Read from the InputStream

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }



}
