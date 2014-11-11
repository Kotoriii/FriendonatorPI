package com.pi314.friendonator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Christian on 10/12/2014.
 */
public class HomeActivity extends Activity {

    private Spinner spnEvent;
    ImageView iv;
    Bitmap image;
    ProgressDialog pd;
    Person person;
    TextView lblprofilename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        iv = (ImageView) findViewById(R.id.imgviewEventpic);
        final String txtSpinner = spnEvent.getSelectedItem().toString();


        //addItemsOnEventSpinner();
        lblprofilename = (TextView)findViewById(R.id.lblProfileName);

        // Get object person from intent extras
        getSetPerson();

        // Set user name
        textName();

        //onItemSelected();

        final TextView btnprofile = (TextView)findViewById(R.id.lblProfileName);

        btnprofile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setName();

                // Create the Intent element
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                startActivity(intent);

                // Finish activity
                finish();
            }
        });

        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch(position) {
                    case 0:
                        iv.setImageResource(R.drawable.dogehangout);
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.snoopdoge);
                        break;
                    case 2:
                        iv.setImageResource(R.drawable.dogeart);
                        break;
                    case 3:
                        iv.setImageResource(R.drawable.dogepotter);
                        break;
                    case 4:
                        iv.setImageResource(R.drawable.dogeparty);
                        break;
                }
                Toast.makeText(spnEvent.getContext(),
                        "Selected Event: " + spnEvent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    public void setName() {
        String name = lblprofilename.getText().toString();
        if (!lblprofilename.getText().toString().isEmpty())
            person.setName(name);
    }

    public void textName() {
        if (person.getName() != null)
            lblprofilename.setText(person.getName());
    }




    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }






/*
    // Activamos el toggle con el icono
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */
    /**Metodo para abrir el form escogido en el menu**/
    private void displayView(int options){

        switch (options) {
            case 1:
                //aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                //Create the Intent element
                Bundle bProfile = new Bundle();
                intentProfile.putExtras(bProfile);
                //Start the new Activity
                startActivity(intentProfile);
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(HomeActivity.this, History.class);
                //Create the Intent element
                Bundle bHistory = new Bundle();
                intentHistory.putExtras(bHistory);
                //Start the new Activity
                startActivity(intentHistory);
                break;
            case 3:
                /*//aqui se abrira la actividad Home
                Intent intentHome = new Intent(HomeActivity.this, HomeActivity.class);
                //Create the Intent element
                Bundle bHome = new Bundle();
                intentHome.putExtras(bHome);
                //Start the new Activity
                startActivity(intentHome);
                break;*/
                break;
            case 4:
                //aqui se abrira la actividad MainActivity
                Intent intentMain = new Intent(HomeActivity.this, MainActivity.class);
                //Create the Intent element
                Bundle bMain = new Bundle();
                intentMain.putExtras(bMain);
                //Start the new Activity
                startActivity(intentMain);
                break;
            case 5:
                /* //aqui se abrira la actividad Match
                Intent intentMatch = new Intent(ProfileActivity.this, Match.class);
                //Create the Intent element
                Bundle bMatch = new Bundle();
                intentMatch.putExtras(bMatch);
                //Start the new Activity
                startActivity(intentMatch);
                break;*/
                Toast.makeText(HomeActivity.this,
                        "Maintenance",
                        Toast.LENGTH_SHORT).show();
                break;
            case 6:
                //aqui se abrira la actividad MySettings
                Intent intentMySettings = new Intent(HomeActivity.this, MySettings.class);
                //Create the Intent element
                Bundle bMySettings = new Bundle();
                intentMySettings.putExtras(bMySettings);
                //Start the new Activity
                startActivity(intentMySettings);
                break;
            default:
                break;
        }
    }


}
