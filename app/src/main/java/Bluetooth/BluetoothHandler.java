package Bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by andrea on 30/09/14.
 */
public class BluetoothHandler {
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice device;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
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
        new Thread(new AcceptThread()).start();

    }

    public void startClient() {
        if (device != null) {
            new Thread(new ConnectThread(device)).start();
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


    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(this.getName(), MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }


        public void run() {
            BluetoothSocket socket = null;
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
            }

            // If a connection was accepted
            if (socket != null) {
                //Note this is copied from the TCPdemo code.
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    out.println("Reponse from Bluetooth Demo Server");
                    out.flush();

                } catch (Exception e) {

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            } else {
            }
        }

    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            socket = tmp;

        }
    }


}
