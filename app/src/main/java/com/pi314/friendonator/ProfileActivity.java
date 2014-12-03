package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ImageButton;
import android.widget.Toast;

import com.pi314.interests.InterestsMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
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
    ImageView viewImage;
    SQLiteHelper db;
    Boolean gotPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = SQLiteHelper.getInstance(getApplicationContext());
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

        viewImage=(ImageView) findViewById(R.id.btnProfileImage);

        Usuario usuario = db.getUser(person.getEmail());

        if(usuario.getFoto() != null) {
            File file = new File(usuario.getFoto());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            viewImage.setImageBitmap(bitmap);
            viewImage.setBackgroundColor(0x0000FF00);
            gotPhoto = true;
        } else
            gotPhoto = false;

            viewImage.setOnClickListener(new View.OnClickListener() {
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
                // Save/update name into Data Base and go to Home Activity
                if (!gotPhoto) {
                    customToast(getResources().getString(R.string.setPicture));
                } else if (person.getDataBaseInterest().isEmpty()) {
                    customToast(getResources().getString(R.string.setInterests));
                } else if (person.getGetContactedByList().isEmpty()) {
                    customToast(getResources().getString(R.string.setContactedBy));
                } else if (validateSaveProfile()) {
                    // Set name to person object
                    setName();

                    // Create intent
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);

                    // Set person inside intent
                    intent.putExtra("PERSON", person);

                    // Start change to a new layout
                    startActivity(intent);

                    // Finish activity
                    finish();
                } else
                    customToast(getResources().getString(R.string.profileNameRequired));
                    //Toast.makeText(getApplication(), R.string.profileNameRequired, Toast.LENGTH_SHORT).show();
            }
        });

        gridViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridObject forDialog = gridList.get(position);
                if (!forDialog.getTitle().isEmpty() && person.textValue(forDialog.getTitle()) != null)
                    showInterestInfoDialog(getResources().getString(R.string.whatILike) + " " + forDialog.getTitle(), person.textValue(forDialog.getTitle()), forDialog.getTitle());
            }
        });}




    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
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
        String [] interestsList = getResources().getStringArray(R.array.identifyInterests);
        InterestsMethods interestsMethods = new InterestsMethods();

        if (person != null && !person.getDataBaseInterest().isEmpty()) {
            for (Map.Entry<Integer, List<Integer>> entry : person.getDataBaseInterest().entrySet()) {
                interestTitle = interestsList[entry.getKey() - 1];
                object.setTitle(interestTitle);
                if (!person.dataBaseValues(entry.getKey()).isEmpty()) {
                    String value = "";
                    value = interestsMethods.getInterestsStrings(ProfileActivity.this, entry.getKey(), entry.getValue());
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

    public boolean validateSaveProfile() {
        boolean good = false;
        String profileName = txtProfileName.getText().toString();

        if (!profileName.isEmpty()) {
            Usuario userUpdate = new Usuario();
            userUpdate.setId(person.getId());
            userUpdate.setNombre(profileName);
            db.updateUserProfileName(userUpdate);
            good = true;
        }

        return good;
    }

    public void showInterestInfoDialog(String tittle, String message, String tittleIcon) {
        // Create an instance of dialogInterestInfo
        InterestInfo dialogInterestInfo = new InterestInfo();
        // Set tittle and description
        dialogInterestInfo.setInfo(tittle, message, tittleIcon);
        // Show SelectInterest instance
        dialogInterestInfo.show(getFragmentManager(), "InterestInfo");
    }

    public void customToast(String message) {
        // Toast for validates messages when save button is clicked
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // Set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 270);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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

        final CharSequence[] options = { getResources().getString(R.string.txttakephoto), getResources().getString(R.string.txtaddfromgallery),getResources().getString(R.string.txtphotocancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(getResources().getString(R.string.txtaddphoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.txttakephoto))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals( getResources().getString(R.string.txtaddfromgallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals(getResources().getString(R.string.txtphotocancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        db = SQLiteHelper.getInstance(getApplicationContext());
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

                    int targetW = viewImage.getWidth();
                    int targetH = viewImage.getHeight();
                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);



                   /* bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false);*/
                    viewImage.setImageBitmap(bitmap);

                    Usuario usuario = db.getUser(person.getEmail());


                    if(usuario.getFoto() == null) {
                        usuario.setId(person.getId());
                        usuario.setFoto(f.getAbsolutePath());
                        db.updateUsuario(usuario);
                    }
                    else{
                        usuario.setId(person.getId());
                        usuario.setFoto(f.getAbsolutePath());
                        db.updateUsuario(usuario);
                    }


/*
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
                    }*/
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


                Usuario usuario = db.getUser(person.getEmail());

                if(usuario.getFoto() == null) {
                    usuario.setFoto(picturePath);
                    db.updateUsuario(usuario);
                }
                else{
                    usuario.setFoto(picturePath);
                    db.updateUsuario(usuario);
                }

                gotPhoto = true;
                viewImage.setBackgroundColor(0x0000FF00);

                //Bitmap resizedBitmap = Bitmap.createScaledBitmap(thumbnail, 600, 600, false);

                Log.w("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(bitmap);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
