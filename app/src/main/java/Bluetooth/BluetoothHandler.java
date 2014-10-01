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
    private static final String NAME = "BluetoothDemo";
    BluetoothDevice remoteDevice;
    private boolean registered = false;
    List<String> lstDisptV = new ArrayList<String>();

    public BluetoothHandler() {
    }

//////////////////
    public BluetoothAdapter getAdapter() {
        return mBluetoothAdapter;
    }

    public List<String> getList() {
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

    private void registerAdapter(Activity activity) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(getmReceiver(), filter);
        registered = true;
    }

    public void SetUnlimitedVisibility(Activity activity){
        Intent discoverableIntent = new
        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        activity.startActivity(discoverableIntent);
    }

    public void Scan(Activity activity) {
        if (!registered) {
            registerAdapter(activity);
        }
        getAdapter().startDiscovery();
    }

    public boolean startbt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return false;
        }
        //make sure bluetooth is enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Activity main = new Activity();
            main.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Register the BroadcastReceiver

        return true;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }

    };

    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }


    public BroadcastReceiver getmReceiver() {
        return this.mReceiver;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!lstDisptV.contains(device.getName())) {
                    lstDisptV.add(device.getName());
                }
            }
        }
    };


    public String querypaired(Context context) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        String pop = "";
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            final BluetoothDevice blueDev[] = new BluetoothDevice[pairedDevices.size()];
            String[] items = new String[blueDev.length];
            int i = 0;

            for (BluetoothDevice devicel : pairedDevices) {
                blueDev[i] = devicel;
                items[i] = blueDev[i].getName() + ": " + blueDev[i].getAddress();
                pop += (items[i] + "\n");
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                i++;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose Bluetooth:");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    dialog.dismiss();
                    if (item >= 0 && item < blueDev.length) {
                        device = blueDev[item];
                    }

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return pop;
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                mkmsg("Failed to start server\n");
            }
            mmServerSocket = tmp;
        }



        public void run() {
            mkmsg("waiting on accept");
            BluetoothSocket socket = null;
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                mkmsg("Failed to accept\n");
            }

            // If a connection was accepted
            if (socket != null) {
                mkmsg("Connection made\n");
                mkmsg("Remote device address: " + socket.getRemoteDevice().getAddress().toString() + "\n");
                //Note this is copied from the TCPdemo code.
                try {
                    mkmsg("Attempting to receive a message ...\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str + "\n");

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mkmsg("Attempting to send message ...\n");
                    out.println("Reponse from Bluetooth Demo Server");
                    out.flush();
                    mkmsg("Message sent...\n");

                    mkmsg("We are done, closing connection\n");
                } catch (Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        mkmsg("Unable to close socket" + e.getMessage() + "\n");
                    }
                }
            } else {
                mkmsg("Made connection, but socket is null\n");
            }
            mkmsg("Server ending \n");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {

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
                mkmsg("Client connection failed: " + e.getMessage().toString() + "\n");
            }
            socket = tmp;

        }
    }
}
