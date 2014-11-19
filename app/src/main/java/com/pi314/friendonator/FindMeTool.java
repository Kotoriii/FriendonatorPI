package com.pi314.friendonator;

import android.app.Activity;
import android.os.Bundle;
//import android.renderscript.ProgramStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import Bluetooth.BluetoothHandler;
import Database.Superinteres;
import misc.ApiWrapper;


public class FindMeTool extends Activity {

    TextView txtRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me_tool);

        txtRS = (TextView) findViewById(R.id.txtPOP);
        BluetoothHandler bth = BluetoothHandler.getInstance(this);
        bth.redefineActivity(this);

        ((Button) findViewById(R.id.btnReload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        //TODO remove!!
        final TextView txtService = (TextView) findViewById(R.id.txtServiceR);
        final Button btnRequest = (Button) findViewById(R.id.btnRequesS);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiWrapper api = new ApiWrapper();
                String oo = "";
                for(Superinteres su : api.getSuperIntereses()){
                    oo += su.getId() + "\n";
                }
                    txtService.setText(oo);
            }
        });

    }

    public void reload() {

        Thread reloadT = new Thread() {
            public void run() {
                try {
                    while (true) {
                        synchronized (this) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    txtRS.setText("name: " + getIntent().getStringExtra("name") + "\n RSSI: " + getIntent().getIntExtra("strg", 0));
                                }
                            });
                        }
                        this.sleep(300);
                    }
                } catch (Exception e) {
                    txtRS.setText(e.getMessage());
                }
            }
        };
        reloadT.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_me_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
