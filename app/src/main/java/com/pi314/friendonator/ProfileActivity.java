package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Dialog.InterestInfo;
import GridView.GridObject;
import GridView.GridCustomAdapter;


public class ProfileActivity extends Activity {
    User user;
    Person person;
    EditText txtProfileName;
    List<String> gridInterests;
    ArrayList<GridObject> gridList;
    TextView lblInterestsList;
    TextView getContactedBy;

    ImageButton viewImage;
    ImageButton b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Button btnChooseInterest = (Button) findViewById(R.id.btnChooseInterest);
        final Button btnChooseContact = (Button) findViewById(R.id.btnChooseContact);
        final Button btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        txtProfileName = (EditText) findViewById(R.id.txtProfileName);

        //getSetUser();
        getSetPerson();
        textName();

        // Locate the gridViewInterests TextView
        final GridView gridViewInterests = (GridView) findViewById(R.id.gridViewInterests);

        // Create ArrayAdapterInterests
        GridCustomAdapter adapterInterests = new GridCustomAdapter(this, fillGridViewInterests());

        // Set adapter to gridViewInterests
        gridViewInterests.setAdapter(adapterInterests);

        // Locate the gridViewInterests TextView
        GridView gridViewContactedBy = (GridView) findViewById(R.id.gridViewContactedBy);

        // Create ArrayAdapterInterests
        GridCustomAdapter adapterContactedBy = new GridCustomAdapter(this, fillGridViewContactedBy());

        // Set adapter to gridViewInterests
        gridViewContactedBy.setAdapter(adapterContactedBy);


        b =(ImageButton) findViewById(R.id.btnProfileImage);
        viewImage=(ImageButton) findViewById(R.id.btnProfileImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnChooseInterest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setName();

                // Create intent to open interests activity
                Intent intent = new Intent(ProfileActivity.this, InterestsActivity.class);

                // Set person inside intent
                //intent.putExtra("USER", user);
                intent.putExtra("PERSON", person);

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
                setName();

                // Create intent to open get contacted by activity
                Intent intent = new Intent(ProfileActivity.this, GetContactedByActivity.class);

                // Set bundle inside intent
                //intent.putExtras(bundleProfile());
                intent.putExtra("PERSON", person);

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
                setName();

                // Create intent
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

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
                    showInterestInfoDialog(textFavoriteType(forDialog.getTitle()), person.textValue(forDialog.getTitle()));
            }
        });

    }

    public void getSetUser() {
        user = this.getIntent().getParcelableExtra("USER");

        if (user == null)
            user = new User(Parcel.obtain());
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

    public String getInterestsList() {
        String interestList = "Set your Interests preferences...";

        if (person != null && !person.getInterestList().isEmpty()) {
            interestList = "";
            for (Map.Entry<String, List<String>> entry : person.getInterestList().entrySet()) {
                interestList += entry.getKey() + "\n";
                if (entry.getKey().equals("Science"))
                    interestList += "\n";
                else
                    interestList += entry.getValue() + "\n";
                if (!person.textValue(entry.getKey()).isEmpty())
                    interestList += textFavoriteType(entry.getKey()) + "\n" + person.textValue(entry.getKey()) + "\n\n";
                else
                    interestList += "\n";
            }
        }

        return interestList;
    }

    public String textFavoriteType(String title) {
        String favorite = "";
        if (title.equals(getResources().getString(R.string.selectInterestMusic)))
            favorite = getResources().getString(R.string.txtMusic);
        if (title.equals(getResources().getString(R.string.selectInterestLiterature)))
            favorite = getResources().getString(R.string.txtLiterature);
        if (title.equals(getResources().getString(R.string.selectInterestMovies)))
            favorite = getResources().getString(R.string.txtMovies);
        if (title.equals(getResources().getString(R.string.selectInterestArt)))
            favorite = getResources().getString(R.string.txtArt);
        if (title.equals(getResources().getString(R.string.selectInterestTVShows)))
            favorite = getResources().getString(R.string.txtTvShow);
        if (title.equals(getResources().getString(R.string.selectInterestSports)))
            favorite = getResources().getString(R.string.txtSports);
        if (title.equals(getResources().getString(R.string.selectInterestScience)))
            favorite = getResources().getString(R.string.txtScience);
        if (title.equals(getResources().getString(R.string.selectInterestLookingFor)))
            favorite = getResources().getString(R.string.txtLookingFor);
        return favorite;
    }

    public ArrayList<GridObject> fillGridViewInterests() {
        gridList = new ArrayList<GridObject>();
        GridObject object = new GridObject();
        String interestTitle = getResources().getString(R.string.lblInterestsList);
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
        } else {
            object.setTitle(interestTitle);
            object.setGenres(interestGenres);
            gridList.add(object);
        }

        Collections.sort(gridList, new Comparator<GridObject>() {
            @Override
            public int compare(GridObject lhs, GridObject rhs) {
                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        });

        return gridList;
    }

    public String getContactedByList() {
        String contactedByList = "Set your Get Contacted By preferences...";

        if (person != null && !person.getGetContactedByList().isEmpty()) {
            contactedByList = "";
            for (Map.Entry<String, String> entry : person.getGetContactedByList().entrySet()) {
                contactedByList += entry.getKey() + "\n";
                contactedByList += entry.getValue() + "\n\n";
            }
        }

        return contactedByList;
    }

    public ArrayList<GridObject> fillGridViewContactedBy() {
        ArrayList<GridObject> contactedBy = new ArrayList<GridObject>();
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
        } else {
            object.setTitle(contactedByList);
            object.setGenres("");
            contactedBy.add(object);
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
        InterestInfo dialogInterestInfo = new InterestInfo();
        dialogInterestInfo.setInfo(tittle, message);
        dialogInterestInfo.show(getFragmentManager(), "InterestInfo");
    }

    public String getTitle(int position) {
        String check = gridInterests.get(position);
        String title = "";
        char letter = ' ';
        for (int c = 0; c < check.length(); c++) {
            letter = check.charAt(c);
            if (title.equals(getResources().getString(R.string.selectInterestMusic))) {
                title = getResources().getString(R.string.selectInterestMusic);
            } else if (title.equals(getResources().getString(R.string.selectInterestLiterature))) {
                title = getResources().getString(R.string.selectInterestLiterature);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestMovies))) {
                title = getResources().getString(R.string.selectInterestMovies);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestArt))) {
                title = getResources().getString(R.string.selectInterestArt);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestTVShows).substring(0,1))) {
                title = getResources().getString(R.string.selectInterestTVShows);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestSports))) {
                title = getResources().getString(R.string.selectInterestSports);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestScience))) {
                title = getResources().getString(R.string.selectInterestScience);
                break;
            } else if (title.equals(getResources().getString(R.string.selectInterestLookingFor))) {
                title = getResources().getString(R.string.selectInterestLookingFor);
                break;
            } else {
                title += letter;
            }
        }
        return title;
    }

    @Override
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

    @Override
    public void onBackPressed()
    {
        //this.finish();
    }

    //cosas de imagen


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
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

                //Bitmap resizedBitmap = Bitmap.createScaledBitmap(thumbnail, 600, 600, false);

                Log.w("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(bitmap);
            }
        }
    }
}
