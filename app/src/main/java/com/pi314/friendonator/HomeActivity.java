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

    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Profile", "History", "Home", "MainActivity", "Match", "My settings"};
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        iv = (ImageView) findViewById(R.id.imgviewEventpic);
        final String txtSpinner = spnEvent.getSelectedItem().toString();


        //addItemsOnEventSpinner();
        lblprofilename = (TextView)findViewById(R.id.lblProfileName);
        getSetPerson();
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
                        iv.setImageResource(R.drawable.snoopdoge);
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.dogeart);
                    break;
                    case 2:
                        iv.setImageResource(R.drawable.dogepotter);
                    break;
                    case 3:
                        iv.setImageResource(R.drawable.dogeparty);
                    break;
                    case 4:
                        iv.setImageResource(R.drawable.dogehangout);
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

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        final ListView drawer = (ListView) findViewById(R.id.drawerHa);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tres lineaas de codigo paraa la imgen del menu
        NavList = (ListView) findViewById(R.id.drawerHa);
        //Declaramos el header el caul sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);
        //Establecemos header
        NavList.addHeaderView(header);

        //obtiene las imagenes desde el string.xml
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        //crea en arraylist de la clae Item_object que tiene imagen y texto
        NavItms = new ArrayList<Item_objct>();
        //Se procede a insertar las imagines y textos
        NavItms.add(new Item_objct(opciones[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new Item_objct(opciones[1], NavIcons.getResourceId(1, -1)));
        NavItms.add(new Item_objct(opciones[2], NavIcons.getResourceId(2, -1)));
        NavItms.add(new Item_objct(opciones[3], NavIcons.getResourceId(3, -1)));
        NavItms.add(new Item_objct(opciones[4], NavIcons.getResourceId(4, -1)));
        NavItms.add(new Item_objct(opciones[5], NavIcons.getResourceId(5, -1)));
        //seteamos el adaptador y le pasamos los iconos y titulos al adaptador
        NavAdapter = new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                displayView(arg2);
                drawerLayout.closeDrawers();

            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.hello_world){
            public void onDrawerClosed(View view) {
                // Drawer cerrado
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // Drawer abierto
                getActionBar().setTitle("Menu");
                //invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(toggle);
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
        Bundle bundle = this.getIntent().getExtras();
        if (person.getName() != null)
            lblprofilename.setText(person.getName());
        else if (bundle != null){
            String name = bundle.getString("NAME");
            if (name != null){
                lblprofilename.setText(name);
            }
        }
    }

   /* public void onItemSelected(){




        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (txtSpinner.equals("Concert")) {
                    iv.setImageResource(R.drawable.snoopdoge);
                }
                if (txtSpinner.equals("Art")) {
                    iv.setImageResource(R.drawable.dogeart);
                }
                if (txtSpinner.equals("Literature")) {
                    iv.setImageResource(R.drawable.dogepotter);
                }
                if (txtSpinner.equals("Party")) {
                    iv.setImageResource(R.drawable.dogeparty);
                }
                if (txtSpinner.equals("Hang Out")) {
                    iv.setImageResource(R.drawable.dogehangout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        }*/


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
   /* public void addItemsOnEventSpinner() {

        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }*/




   /* class TheTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                Spinner spnEvent = (Spinner)findViewById(R.id.spnEvent);
                String txtSpinner = spnEvent.getSelectedItem().toString();
                if (txtSpinner.equals("Concert")){
                image = downloadBitmap("http://i.imgur.com/IdVlsFw.jpg");}
                if (txtSpinner.equals("Art")){
                    image = downloadBitmap("http://th01.deviantart.net/fs71/PRE/i/2013/362/f/f/___doge_with_a_pearl_earring____by_skitzo_picklez-d6zu3i8.png");}
                if (txtSpinner.equals("Literature")){
                    image = downloadBitmap("http://img.sparknotes.com/content/sparklife/sparktalk/dogedoes3_Slide.jpg");}
                if (txtSpinner.equals("Party")){
                    image = downloadBitmap("http://www.lily.fi/sites/lily/files/user/45/2013/12/hjoqkmy.png");}
                if (txtSpinner.equals("Hang Out")){
                    image = downloadBitmap("https://lh6.ggpht.com/Gg2BA4RXi96iE6Zi_hJdloQAZxO6lC6Drpdr7ouKAdCbEcE_Px-1o4r8bg8ku_xzyF4y=h310");}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (image != null) {
                iv.setImageBitmap(image);
            }

        }
    }

    private Bitmap downloadBitmap(String url) {
        // initialize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttpGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }*/
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       if (toggle.onOptionsItemSelected(item)) {
           return true;
       }
       return super.onOptionsItemSelected(item);
   }

    // Activamos el toggle con el icono
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

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
