package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pi314.interests.InterestsMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.SQLiteHelper;
import Dialog.InterestInfo;
import GridView.GridCustomAdapter;
import GridView.GridObject;
import misc.BackgroundService;

public class MatchProfileActivity extends Activity {

    Person person;
    Person matchPerson;
    ArrayList<GridObject> gridList;
    ArrayList<GridObject> contactedBy;

    //Elementos del menu
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    private ImageView imageMatch;
    public static HashMap<String, Short> FUERZA_CON = new HashMap<String, Short>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);
        final TextView lblMatchName = (TextView) findViewById(R.id.lblMatchName);
        final TextView lblMatchPercentage = (TextView) findViewById(R.id.lblMatchPercentage);
        final TextView lblSpecialMatch = (TextView) findViewById(R.id.lblSpecialPercentage);
        final Button btnClose = (Button) findViewById(R.id.btnClose);
        imageMatch = (ImageView) findViewById(R.id.imageMatch);
        final Button btnFind = (Button) findViewById(R.id.findMatch);

        try{
            //intentamos quitar el notification (en el caso de que existiese)
            BackgroundService.mNotificationManager.cancel(BackgroundService.mIdNotification);
        }finally {}

        // Get object person from intent extras
        getSetPerson();


        // Create match person from Data Base
        InterestsMethods getMatch = new InterestsMethods();

        // Test getting match from Data Base
        Bundle bundle = this.getIntent().getExtras();
        String idUser = null;
        try {
            idUser = bundle.getString("ID");
        }catch (ClassCastException e){
            Log.e(getClass().getSimpleName(), "ID es numerico no se puede cast a string");
        }
        //yo se que esto es retundante. pero al pareces si yo pongo un putExtra(int) me retorna null
        //si le pido bundle.getString. Solo lo retorna si pido bundle.getInt.
        if(idUser == null){
            int pops = getIntent().getIntExtra("ID",0);
            matchPerson = getMatch.createPerson(MatchProfileActivity.this, pops);
        }else{
            matchPerson = getMatch.createPerson(MatchProfileActivity.this, Integer.parseInt(idUser));
        }

        //ponemos la foto adecuada al image match
        if(matchPerson.getFoto_perfil() != null) {
            File file = new File(matchPerson.getFoto_perfil());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageMatch.setImageBitmap(bitmap);
        }

        // Set match name
        lblMatchName.setText(matchPerson.getName());

        // Locate the gridViewInterests TextView
        final GridView gridViewInterests = (GridView) findViewById(R.id.gridViewMatchInterests);

        if (!fillGridViewInterests().isEmpty()) {
            // Create ArrayAdapterInterests
            GridCustomAdapter adapterInterests = new GridCustomAdapter(this, gridList);

            // Set adapter to gridViewInterests
            gridViewInterests.setAdapter(adapterInterests);
        }

        // Locate the gridViewContactedBy TextView
        GridView gridViewContactedBy = (GridView) findViewById(R.id.gridViewMatchContactedBy);

        if (!fillGridViewContactedBy().isEmpty()) {
            // Create ArrayAdapterContactedBy
            GridCustomAdapter adapterContactedBy = new GridCustomAdapter(this, contactedBy);

            // Set adapter to gridViewContactedBy
            gridViewContactedBy.setAdapter(adapterContactedBy);
        }

        InterestsMethods match = new InterestsMethods();
        int percentage = (int) Math.floor(match.getMatchPercentage(person, matchPerson));
        int specialPercentage = (int) Math.floor(match.specialMatchResult(person.getEventId(), person, matchPerson));

        lblMatchPercentage.setText(getResources().getString(R.string.matchPercentage) + " " + percentage + "%");
        if (specialPercentage != 0) {
            lblSpecialMatch.setVisibility(View.VISIBLE);
            lblSpecialMatch.setText(getResources().getString(R.string.categoryPercentage) + " " + specialPercentage + "%");
        }

        gridViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridObject forDialog = gridList.get(position);
                if (!forDialog.getTitle().isEmpty() && matchPerson.textValue(forDialog.getTitle()) != null)
                    showInterestInfoDialog(getResources().getString(R.string.whatILike) + " " + forDialog.getTitle(), matchPerson.textValue(forDialog.getTitle()), forDialog.getTitle());
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Static list for search stuff in home
                FUERZA_CON.put(matchPerson.getId(), null);

                // Create intent
                Intent intent = new Intent(MatchProfileActivity.this, HomeActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();

                // Slide animation
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                Intent intent = new Intent(MatchProfileActivity.this, History.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();

                // Slide animation
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        ///////////////////////////////////////Logica para el menu//////////////////////////////////////////////////////////
        final String[] opciones = getResources().getStringArray(R.array.menu_options);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        final ListView drawer = (ListView) findViewById(R.id.drawer);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tres lineaas de codigo paraa la imgen del menu
        NavList = (ListView) findViewById(R.id.drawer);
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
        NavItms.add(new Item_objct(opciones[3], NavIcons.getResourceId(5, -1)));
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
                getActionBar().setTitle(getResources().getString(R.string.menu));
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

    public ArrayList<GridObject> fillGridViewInterests() {
        gridList = new ArrayList<GridObject>();
        GridObject object = new GridObject();
        String interestTitle = "";
        String interestGenres = "";
        String [] interestsList = getResources().getStringArray(R.array.identifyInterests);
        InterestsMethods interestsMethods = new InterestsMethods();

        if (matchPerson != null && !matchPerson.getDataBaseInterest().isEmpty()) {
            for (Map.Entry<Integer, List<Integer>> entry : matchPerson.getDataBaseInterest().entrySet()) {
                interestTitle = interestsList[entry.getKey() - 1];
                object.setTitle(interestTitle);
                if (!matchPerson.dataBaseValues(entry.getKey()).isEmpty()) {
                    String value = "";
                    value = interestsMethods.getInterestsStrings(MatchProfileActivity.this, entry.getKey(), entry.getValue());
                    interestGenres = value;
                    object.setGenres(interestGenres);
                }
                gridList.add(object);
                object = new GridObject();
            }
        }

        Collections.sort(gridList, new Comparator<GridObject>() {
            @Override
            public int compare(GridObject lhs, GridObject rhs) {
                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        });

        return gridList;
    }

    public ArrayList<GridObject> fillGridViewContactedBy() {
        contactedBy = new ArrayList<GridObject>();
        String contactedByList = getResources().getString(R.string.lblContactedByList);
        GridObject object = new GridObject();

        if (matchPerson != null && !matchPerson.getGetContactedByList().isEmpty()) {
            for (Map.Entry<String, String> entry : matchPerson.getGetContactedByList().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    contactedByList = "";
                    object = new GridObject();
                    contactedByList = entry.getKey();
                    object.setTitle(contactedByList);
                    contactedByList = entry.getValue();
                    object.setGenres(contactedByList);
                    contactedBy.add(object);
                }
            }
        }

        Collections.sort(contactedBy, new Comparator<GridObject>() {
            @Override
            public int compare(GridObject lhs, GridObject rhs) {
                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        });

        return contactedBy;
    }

    public void showInterestInfoDialog(String tittle, String message, String tittleIcon) {
        // Create an instance of dialogInterestInfo
        InterestInfo dialogInterestInfo = new InterestInfo();
        // Set tittle and description
        dialogInterestInfo.setInfo(tittle, message, tittleIcon);
        // Show SelectInterest instance
        dialogInterestInfo.show(getFragmentManager(), "InterestInfo");
    }

    @Override
    public void onBackPressed() {
        // Create intent to open get contacted by activity
        Intent intent = new Intent(MatchProfileActivity.this, History.class);

        // Set bundle inside intent
        intent.putExtra("PERSON", person);

        // Start change to a new layout
        startActivity(intent);

        // Finish activity
        this.finish();

        // Slide animation
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

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
        return true;
    }

    /**Metodo para abrir el form escogido en el menu**/
    private void displayView(int options){

        switch (options) {
            case 1:
                //aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(MatchProfileActivity.this, ProfileActivity.class);
                //Create the Intent element
                intentProfile.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentProfile);
                this.finish();
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(MatchProfileActivity.this, History.class);
                //Create the Intent element
                intentHistory.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentHistory);
                this.finish();
                break;
            case 3:
                //aqui se abrira la actividad Home
                Intent intentHome = new Intent(MatchProfileActivity.this, HomeActivity.class);
                //Create the Intent element
                intentHome.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentHome);
                this.finish();
                break;
            case 4:
                //aqui se abrira la actividad MySettings
                Intent intentMySettings = new Intent(MatchProfileActivity.this, MySettings.class);
                //Create the Intent element
                intentMySettings.putExtra("PERSON", person);
                //Start the new Activity
                startActivity(intentMySettings);
                this.finish();
                break;
            default:
                break;
        }
    }

}
