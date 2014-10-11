package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MySettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        final Button btnPrivacy = (Button)findViewById(R.id.btnPrivacy);
        final Button btnAdvanced = (Button)findViewById(R.id.btnAdvanced);
        final Button btnLanguage = (Button)findViewById(R.id.btnLanguage);


        final CharSequence[] privacyArr = {"Show interests", "Show contact me"};
        final AlertDialog.Builder alt_bldPr = new AlertDialog.Builder(this);
        // alt_bld.setIcon(R.drawable.icon);
        alt_bldPr.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldPr.setNegativeButton("No", null);
        alt_bldPr.setTitle("Privacy Options");
        alt_bldPr.setSingleChoiceItems(privacyArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "Privacy option = " + privacyArr[item], Toast.LENGTH_SHORT).show();
            }
        });

        final CharSequence[] AdvancedArr = {"Wi-Fi", "Bluetooth", "Data Connection", "Application"};
        final AlertDialog.Builder alt_bldAd = new AlertDialog.Builder(this);
        // alt_bld.setIcon(R.drawable.icon);
        alt_bldAd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldAd.setNegativeButton("No", null);
        alt_bldAd.setTitle("Advanced Options");
        alt_bldAd.setSingleChoiceItems(AdvancedArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "Advanced option = "+AdvancedArr[item], Toast.LENGTH_SHORT).show();
            }
        });


        final CharSequence[] LanguageArr = {"Italian", "Deutsch", "English", "Español", "中国"};
        final AlertDialog.Builder alt_bldLa = new AlertDialog.Builder(this);
        // alt_bld.setIcon(R.drawable.icon);
        alt_bldLa.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt_bldLa.setNegativeButton("No", null);
        alt_bldLa.setTitle("Language Options");
        alt_bldLa.setSingleChoiceItems(LanguageArr, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),
                        "Language option = "+LanguageArr[item], Toast.LENGTH_SHORT).show();
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_settings, menu);
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
