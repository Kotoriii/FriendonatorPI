package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Database.SQLiteHelper;
import Database.Usuario;
import Dialog.InterestInfo;
import GridView.GridObject;
import GridView.GridCustomAdapter;


public class ProfileActivity extends Activity {
    Person person;
    EditText txtProfileName;
    ArrayList<GridObject> gridList;
    ArrayList<GridObject> contactedBy;
    TextView lblInterests;
    TextView lblGetContactedBy;

    ImageButton viewImage;
    ImageButton b;
    SQLiteHelper db;

    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Profile", "History", "Home", "MainActivity", "Match", "My settings"};
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Button btnChooseInterest = (Button) findViewById(R.id.btnChooseInterest);
        final Button btnChooseContact = (Button) findViewById(R.id.btnChooseContact);
        final Button btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        txtProfileName = (EditText) findViewById(R.id.txtProfileName);
        lblInterests = (TextView) findViewById(R.id.lblInterests);
        lblGetContactedBy = (TextView) findViewById(R.id.lblGetContactedBy);

        // Get object person from intent extras
        getSetPerson();

        // Set user name
        textName();

        // Locate the gridViewInterests TextView
        final GridView gridViewInterests = (GridView) findViewById(R.id.gridViewInterests);

        if (!fillGridViewInterests().isEmpty()) {
            // Create ArrayAdapterInterests
            GridCustomAdapter adapterInterests = new GridCustomAdapter(this, gridList);

            // Set adapter to gridViewInterests
            gridViewInterests.setAdapter(adapterInterests);

            lblInterests.setText(getResources().getString(R.string.lblInterests));
        }

        // Locate the gridViewContactedBy TextView
        GridView gridViewContactedBy = (GridView) findViewById(R.id.gridViewContactedBy);

        if (!fillGridViewContactedBy().isEmpty()) {
            // Create ArrayAdapterContactedBy
            GridCustomAdapter adapterContactedBy = new GridCustomAdapter(this, contactedBy);

            // Set adapter to gridViewContactedBy
            gridViewContactedBy.setAdapter(adapterContactedBy);

            lblGetContactedBy.setText(getResources().getString(R.string.lblGetContactedBy));
        }

        b =(ImageButton) findViewById(R.id.btnProfileImage);
        viewImage=(ImageButton) findViewById(R.id.btnProfileImage);

        Usuario usuario = new Usuario();
        usuario.setId(person.getId());
        if(!usuario.getFoto().equals("")) {


            Bitmap bitmap = BitmapFactory.decodeFile(usuario.getFoto());
            viewImage.setImageBitmap(bitmap);

        }
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    selectImage();
                }
            });

        btnChooseInterest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set name to person object
                setName();

                // Create intent to open interests activity
                Intent intent = new Intent(ProfileActivity.this, InterestsActivity.class);

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

        btnChooseContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set name to person object
                setName();

                // Create intent to open get contacted by activity
                Intent intent = new Intent(ProfileActivity.this, GetContactedByActivity.class);

                // Set bundle inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();

                // Slide animation
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        btnSaveChanges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set name to person object
                setName();

                // Save picture and name into data base
                // method here

                // Create intent
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();
            }
        });

        gridViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String title = getTitle(position);
                if (!title.isEmpty() && person.textValue(title) != null)
                    showInterestInfoDialog(textFavoriteType(title), person.textValue(title));*/
                GridObject forDialog = gridList.get(position);
                if (!forDialog.getTitle().isEmpty() && person.textValue(forDialog.getTitle()) != null)
                    showInterestInfoDialog(getResources().getString(R.string.whatILike) + " " + forDialog.getTitle(), person.textValue(forDialog.getTitle()));
            }
        });

/*
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        final ListView drawer = (ListView) findViewById(R.id.drawer2);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tres lineaas de codigo paraa la imgen del menu
        NavList = (ListView) findViewById(R.id.drawer2);
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
*/

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    public void setName() {
        String name = txtProfileName.getText().toString();
        if (!txtProfileName.getText().toString().isEmpty())
            person.setName(name);
    }

    public void textName() {
        if (person.getName() != null)
            txtProfileName.setText(person.getName());
    }

    public ArrayList<GridObject> fillGridViewInterests() {
        gridList = new ArrayList<GridObject>();
        GridObject object = new GridObject();
        String interestTitle = "";
        String interestGenres = "";

        if (person != null && !person.getInterestList().isEmpty()) {
            for (Map.Entry<String, List<String>> entry : person.getInterestList().entrySet()) {
                interestTitle = entry.getKey();
                object.setTitle(interestTitle);
                if (!person.interestsValue(entry.getKey()).isEmpty()) {
                    String value = "";
                    int count = 0;
                    for (String v : entry.getValue()) {
                        if(count < entry.getValue().size()-1) {
                            value += v + ", ";
                            count += 1;
                        } else
                            value += v + ".";
                    }
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

        if (person != null && !person.getGetContactedByList().isEmpty()) {
            for (Map.Entry<String, String> entry : person.getGetContactedByList().entrySet()) {
                contactedByList = "";
                object = new GridObject();
                contactedByList = entry.getKey();
                object.setTitle(contactedByList);
                contactedByList = entry.getValue();
                object.setGenres(contactedByList);
                contactedBy.add(object);
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

    public void showInterestInfoDialog(String tittle, String message) {
        // Create an instance of dialogInterestInfo
        InterestInfo dialogInterestInfo = new InterestInfo();
        // Set tittle and description
        dialogInterestInfo.setInfo(tittle, message);
        // Show SelectInterest instance
        dialogInterestInfo.show(getFragmentManager(), "InterestInfo");
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
*/
    @Override
    public void onBackPressed()
    {
        //this.finish();
    }

    //cosas de imagen


    private void selectImage() {

        //recordar cambiar strings xml con esto

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;

                   /* BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inDensity = 300;
                    opt.inTargetDensity = 300;*/



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false);
                    viewImage.setImageBitmap(resizedBitmap);

                    Usuario usuario = new Usuario();

                    if(usuario.getFoto().equals("")) {
                        usuario.setId(person.getId());
                        usuario.setFoto(BitMapToString(resizedBitmap));
                        db.insertUsuario(usuario);
                    }
                    else{
                        usuario.setId(person.getId());
                        usuario.setFoto(BitMapToString(resizedBitmap));
                        db.updateUsuario(usuario);
                    }



                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                /*BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inDensity = 300;
                opt.inTargetDensity = 300;*/
              //  Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                int targetW = viewImage.getWidth();
                int targetH = viewImage.getHeight();
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);


                Usuario usuario = new Usuario();

                if(usuario.getFoto().equals("")) {
                    usuario.setId(person.getId());
                    usuario.setFoto(BitMapToString(bitmap));
                    db.insertUsuario(usuario);
                }
                else{
                    usuario.setId(person.getId());
                    usuario.setFoto(BitMapToString(bitmap));
                    db.updateUsuario(usuario);
                }


                //Bitmap resizedBitmap = Bitmap.createScaledBitmap(thumbnail, 600, 600, false);

                Log.w("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                /*//aqui se abrira la actividad de Perfil
                Intent intentProfile = new Intent(ProfileActivity.this, ProfileActivity.class);
                //Create the Intent element
                Bundle bProfile = new Bundle();
                intentProfile.putExtras(bProfile);
                //Start the new Activity
                startActivity(intentProfile);
                break;*/
                break;
            case 2:
                //aqui se abrira la actividad Historial
                Intent intentHistory = new Intent(ProfileActivity.this, History.class);
                //Create the Intent element
                Bundle bHistory = new Bundle();
                intentHistory.putExtras(bHistory);
                //Start the new Activity
                startActivity(intentHistory);
                break;
            case 3:
                //aqui se abrira la actividad Home
                Intent intentHome = new Intent(ProfileActivity.this, HomeActivity.class);
                //Create the Intent element
                Bundle bHome = new Bundle();
                intentHome.putExtras(bHome);
                //Start the new Activity
                startActivity(intentHome);
                break;
            case 4:
                //aqui se abrira la actividad MainActivity
                Intent intentMain = new Intent(ProfileActivity.this, MainActivity.class);
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
                Toast.makeText(ProfileActivity.this,
                        "Maintenance",
                        Toast.LENGTH_SHORT).show();
                break;
            case 6:
                //aqui se abrira la actividad MySettings
                Intent intentMySettings = new Intent(ProfileActivity.this, MySettings.class);
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
