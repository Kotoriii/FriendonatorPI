package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MySettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        final Button btnPrivacy = (Button)findViewById(R.id.btnPrivacy);
        final Button btnAdvanced = (Button)findViewById(R.id.btnAdvanced);
        final Button btnLanguage = (Button)findViewById(R.id.btnLanguage);
        final Button btnIntervaleScan = (Button)findViewById(R.id.btnIntervale);
        final SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar);

        final TextView textPercentage = (TextView) findViewById(R.id.txtPercentage);

        final CharSequence[] privacyArr = {getResources().getString(R.string.showinterests), getResources().getString(R.string.showcontactme)};
        final AlertDialog.Builder alt_bldPr = new AlertDialog.Builder(this);
        alt_bldPr.setIcon(R.drawable.ic_settings_alert );
        alt_bldPr.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldPr.setNegativeButton(getResources().getString(R.string.cancel), null);
        alt_bldPr.setTitle(getResources().getString(R.string.privacyoptions));
        alt_bldPr.setSingleChoiceItems(privacyArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
               String mensajePr = getResources().getString(R.string.privacyoptiontoast) + privacyArr[item];
                ToastCostumizado(mensajePr);
            }
        });

        final CharSequence[] AdvancedArr = {getResources().getString(R.string.wifi),getResources().getString(R.string.bluetooth),getResources().getString(R.string.dataconnection), getResources().getString(R.string.application)};
        final AlertDialog.Builder alt_bldAd = new AlertDialog.Builder(this);
        alt_bldAd.setIcon(R.drawable.ic_settings_alert);
        alt_bldAd.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldAd.setNegativeButton(getResources().getString(R.string.cancel), null);
        alt_bldAd.setTitle(getResources().getString(R.string.advancedoptions));
        alt_bldAd.setSingleChoiceItems(AdvancedArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String mensajeAd = getResources().getString(R.string.advancedoptiontoast) +AdvancedArr[item];
                ToastCostumizado(mensajeAd);
            }
        });


        final CharSequence[] LanguageArr = {getResources().getString(R.string.english), getResources().getString(R.string.espanol)};
        final AlertDialog.Builder alt_bldLa = new AlertDialog.Builder(this);
        alt_bldLa.setIcon(R.drawable.ic_settings_alert);
        alt_bldLa.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldLa.setNegativeButton(getResources().getString(R.string.cancel), null);
        alt_bldLa.setTitle(getResources().getString(R.string.languageoptions));
        alt_bldLa.setSingleChoiceItems(LanguageArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String mensajeLa = getResources().getString(R.string.languageoptiontoast)+LanguageArr[item];
                ToastCostumizado(mensajeLa);
            }
        });

        final CharSequence[] Intervale = {getResources().getString(R.string.always),getResources().getString(R.string.minutes5), getResources().getString(R.string.minutes10), getResources().getString(R.string.minutes15)};
        final AlertDialog.Builder alt_bldInt = new AlertDialog.Builder(this);
        alt_bldInt.setIcon(R.drawable.ic_settings_alert);
        alt_bldInt.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldInt.setNegativeButton(getResources().getString(R.string.cancel), null);
        alt_bldInt.setTitle(getResources().getString(R.string.intervalscan));
        alt_bldInt.setSingleChoiceItems(Intervale, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String mensajePr = getResources().getString(R.string.intervalscantoast) + Intervale[item];
                ToastCostumizado(mensajePr);
            }
        });

        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fd =  "boton Privacy presionado";
                //Toast.makeText(getApplicationContext(), fd,
                //        Toast.LENGTH_SHORT).show();
                AlertDialog alertPr = alt_bldPr.create();
                alertPr.show();
            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //String fd =  "boton Advanced presionado";
                //Toast.makeText(getApplicationContext(), fd,
                //Toast.LENGTH_SHORT).show();
                AlertDialog alertAd = alt_bldAd.create();
                alertAd.show();
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fd =  "boton Language presionado";
                //Toast.makeText(getApplicationContext(), fd,
                //Toast.LENGTH_SHORT).show();
                AlertDialog alertLa = alt_bldLa.create();
                alertLa.show();
            }
        });

        btnIntervaleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fd =  "boton Privacy presionado";
                //Toast.makeText(getApplicationContext(), fd,
                //        Toast.LENGTH_SHORT).show();
                AlertDialog alertInt = alt_bldInt.create();
                alertInt.show();
            }
        });



        textPercentage.setText(seekBar1.getProgress() + "/" + seekBar1.getMax());

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textPercentage.setText(seekBar1.getProgress() + "/" + seekBar1.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void ToastCostumizado (String mensaje){
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(mensaje);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
