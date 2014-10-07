<<<<<<< HEAD
package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnPerfil = (Button)findViewById(R.id.btnPerfil);

        btnPerfil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent element
                Intent intent = new Intent(MainActivity.this,
                        ProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
=======
package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

import Bluetooth.BlueMan;
import Bluetooth.BluetoothHandler;
import Bluetooth.DeviceValidator;
import misc.BackgroundService;


public class MainActivity extends Activity implements Button.OnClickListener{
    BluetoothAdapter mBluetoothAdapter =null;
    BluetoothDevice device;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME = "BluetoothDemo";
    TextView output;
    Button btnServer, btnScan, btnClient, startservice;
    BluetoothHandler bth;
    BlueMan bMan = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        output = (TextView) findViewById(R.id.output);
        output.append("\n");
        btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setOnClickListener(this);
        btnClient = (Button) findViewById(R.id.btnClient);
        btnClient.setOnClickListener(this);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        //////
        startservice = (Button) findViewById(R.id.startservice);
        startservice.setOnClickListener(this);

        bMan = BlueMan.getInstance(this);
        DeviceValidator dv = new DeviceValidator();
        String enc = dv.encrypt("1193434");
        String desenc = dv.decrypt(enc);
        output.setText("Orig: 1193434 \n" +
                "Encripted: " + enc +"\n" +
                "Desencript: " + desenc  );

    }

    @Override
    public void onClick(View v) {
        if ( (Button)v == btnServer) {
          //  bth.startServer();
            ((Button) v).setText("disabe BT");
            bMan.getHandler().stopBlueTooth();
        } else if ( (Button)v == btnClient) {
            if (device != null) {
               // output.append("button client\n");
                //bth.startClient();
            }
            bMan.startScan();

        } else if ( (Button)v == btnScan) {
            output.setText("");
                for (BluetoothDevice b : bMan.getDeviceList()) {
                    output.append(b.getName() + "\n");
                }

            }
        else if ((Button)v == startservice){
            Toast t = Toast.makeText(this, "calling service", Toast.LENGTH_LONG);
            t.show();
            Intent in = new Intent(this, BackgroundService.class);
            startService(in);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
>>>>>>> a32ba83a0c82ac1300fccbc11d552042e6d8b032
