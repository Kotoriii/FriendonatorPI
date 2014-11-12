package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.pi314.interests.InterestsMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Database.SQLiteHelper;


public class InterestsActivity extends Activity implements SelectInterests.NoticeDialogListener {

    EditText txtMusic;
    EditText txtLiterature;
    EditText txtMovies;
    EditText txtArt;
    EditText txtTvShow;
    EditText txtSports;
    EditText txtScience;
    EditText txtLookingFor;
    CheckBox music;
    CheckBox literature;
    CheckBox movies;
    CheckBox art;
    CheckBox tvShow;
    CheckBox sports;
    CheckBox science;
    CheckBox lookingFor;
    ImageButton btnEditMusic;
    ImageButton btnEditLiterature;
    ImageButton btnEditMovies;
    ImageButton btnEditArt;
    ImageButton btnEditTvShow;
    ImageButton btnEditSports;
    ImageButton btnEditLookingFor;
    String newTitle;
    String [] newChoices;
    Person person;
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        final Button btnSaveInterestsChanges = (Button) findViewById(R.id.btnSaveInterestsChanges);
        txtMusic = (EditText) findViewById(R.id.txtMusic);
        txtLiterature = (EditText) findViewById(R.id.txtLiterature);
        txtMovies = (EditText) findViewById(R.id.txtMovies);
        txtArt = (EditText) findViewById(R.id.txtArt);
        txtTvShow = (EditText) findViewById(R.id.txtTvShow);
        txtSports = (EditText) findViewById(R.id.txtSports);
        txtScience = (EditText) findViewById(R.id.txtScience);
        txtLookingFor =(EditText) findViewById(R.id.txtLookingFor);
        music = (CheckBox) findViewById(R.id.checkBoxMusic);
        literature = (CheckBox) findViewById(R.id.checkBoxLiterature);
        movies = (CheckBox) findViewById(R.id.checkBoxMovies);
        art = (CheckBox) findViewById(R.id.checkBoxArt);
        tvShow = (CheckBox) findViewById(R.id.checkBoxTvShow);
        sports = (CheckBox) findViewById(R.id.checkBoxSports);
        science = (CheckBox) findViewById(R.id.checkBoxScience);
        lookingFor = (CheckBox) findViewById(R.id.checkBoxLookingFor);
        btnEditMusic = (ImageButton) findViewById(R.id.btnEditMusic);
        btnEditLiterature = (ImageButton) findViewById(R.id.btnEditLiterature);
        btnEditMovies = (ImageButton) findViewById(R.id.btnEditMovies);
        btnEditArt = (ImageButton) findViewById(R.id.btnEditArt);
        btnEditTvShow = (ImageButton) findViewById(R.id.btnEditTvShows);
        btnEditSports = (ImageButton) findViewById(R.id.btnEditSports);
        btnEditLookingFor = (ImageButton) findViewById(R.id.btnEditLookingFor);

        // Set Data Base
        db = SQLiteHelper.getInstance(getApplicationContext());

        // Get object person from intent extras
        setPerson();

        // Fill checkboxes previously set
        fillCheckBox();

        btnSaveInterestsChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set optional fields into person
                addTextToInterest();

                // Insert interests to user
                InterestsMethods insertInterests = new InterestsMethods();
                insertInterests.insertInterests(InterestsActivity.this, person);

                // Create intent to open interests activity
                Intent intent = new Intent(InterestsActivity.this, ProfileActivity.class);

                // Put bundle inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();

                // Slide animation
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    public void setPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");
    }

    public void fillCheckBox() {
        for (int key : person.getDataBaseInterest().keySet()) {
            if (key == 1) {
                music.setChecked(true);
                btnEditMusic.setVisibility(View.VISIBLE);
                txtMusic.setVisibility(View.VISIBLE);
                //txtMusic.setText(person.textValue(key));
            }
            else if (key == 2) {
                literature.setChecked(true);
                btnEditLiterature.setVisibility(View.VISIBLE);
                txtLiterature.setVisibility(View.VISIBLE);
                //txtLiterature.setText(person.textValue(key));
            }
            else if (key == 3) {
                movies.setChecked(true);
                btnEditMovies.setVisibility(View.VISIBLE);
                txtMovies.setVisibility(View.VISIBLE);
                //txtMovies.setText(person.textValue(key));
            }
            else if (key == 4) {
                art.setChecked(true);
                btnEditArt.setVisibility(View.VISIBLE);
                txtArt.setVisibility(View.VISIBLE);
                //txtArt.setText(person.textValue(key));
            }
            else if (key == 5) {
                tvShow.setChecked(true);
                btnEditTvShow.setVisibility(View.VISIBLE);
                txtTvShow.setVisibility(View.VISIBLE);
                //txtTvShow.setText(person.textValue(key));
            }
            else if (key == 6) {
                sports.setChecked(true);
                btnEditSports.setVisibility(View.VISIBLE);
                txtSports.setVisibility(View.VISIBLE);
                //txtSports.setText(person.textValue(key));
            }
            else if (key == 7) {
                science.setChecked(true);
                txtScience.setVisibility(View.VISIBLE);
                //txtScience.setText(person.textValue(key));
            }
            else if (key == 8) {
                lookingFor.setChecked(true);
                btnEditLookingFor.setVisibility(View.VISIBLE);
                txtLookingFor.setVisibility(View.VISIBLE);
                //txtLookingFor.setText(person.textValue(key));
            }
        }
    }

    public void onCheckBoxInterestClicked(View v) {
        boolean checked = ((CheckBox) v).isChecked();

        // Check which checkbox was clicked and enable or disable the corresponding textField,
        // enable or disable edit button, and call the interests selection dialog
        switch (v.getId()) {
            case R.id.checkBoxMusic:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTitle, newChoices);
                    txtMusic.setVisibility(View.VISIBLE);
                    btnEditMusic.setVisibility(View.VISIBLE);
                } else {
                    txtMusic.setVisibility(View.GONE);
                    btnEditMusic.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(1);
                }
                break;
            case R.id.checkBoxLiterature:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTitle, newChoices);
                    txtLiterature.setVisibility(View.VISIBLE);
                    btnEditLiterature.setVisibility(View.VISIBLE);
                } else {
                    txtLiterature.setVisibility(View.GONE);
                    btnEditLiterature.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(2);
                }
                break;
            case R.id.checkBoxMovies:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTitle, newChoices);
                    txtMovies.setVisibility(View.VISIBLE);
                    btnEditMovies.setVisibility(View.VISIBLE);
                } else {
                    txtMovies.setVisibility(View.GONE);
                    btnEditMovies.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(3);
                }
                break;
            case R.id.checkBoxArt:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTitle, newChoices);
                    txtArt.setVisibility(View.VISIBLE);
                    btnEditArt.setVisibility(View.VISIBLE);
                } else {
                    txtArt.setVisibility(View.GONE);
                    btnEditArt.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(4);
                }
                break;
            case R.id.checkBoxTvShow:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTitle, newChoices);
                    txtTvShow.setVisibility(View.VISIBLE);
                    btnEditTvShow.setVisibility(View.VISIBLE);
                } else {
                    txtTvShow.setVisibility(View.GONE);
                    btnEditTvShow.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(5);
                }
                break;
            case R.id.checkBoxSports:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTitle, newChoices);
                    txtSports.setVisibility(View.VISIBLE);
                    btnEditSports.setVisibility(View.VISIBLE);
                } else {
                    txtSports.setVisibility(View.GONE);
                    btnEditSports.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(6);
                }
                break;
            case R.id.checkBoxScience:
                if (checked) {
                    txtScience.setVisibility(View.VISIBLE);
                    person.fillInterestList(getResources().getString(R.string.selectInterestScience), new ArrayList<String>());
                } else {
                    txtScience.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(7);
                }
                break;
            case R.id.checkBoxLookingFor:
                if (checked) {
                    newTitle = (String) getResources().getText(R.string.selectInterestLookingFor);
                    newChoices = getResources().getStringArray(R.array.lookingFor);
                    showNoticeDialog(newTitle, newChoices);
                    txtLookingFor.setVisibility(View.VISIBLE);
                    btnEditLookingFor.setVisibility(View.VISIBLE);
                } else {
                    txtLookingFor.setVisibility(View.GONE);
                    btnEditLookingFor.setVisibility(View.GONE);
                    person.getDataBaseInterest().remove(8);
                }
                break;
        }
    }

    public void onPencilClicked(View v) {
        Boolean clicked = v.isClickable();

        switch (v.getId()){
            case R.id.btnEditMusic:
                if (clicked && music.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditLiterature:
                if (clicked && literature.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditMovies:
                if (clicked && movies.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditArt:
                if (clicked && art.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditTvShows:
                if (clicked && tvShow.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditSports:
                if (clicked && sports.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.btnEditLookingFor:
                if (clicked && lookingFor.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestLookingFor);
                    newChoices = getResources().getStringArray(R.array.lookingFor);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
        }
    }

    public void addTextToInterest() {
        String sMusic = txtMusic.getText().toString();
        String sLiterature = txtLiterature.getText().toString();
        String sMovies = txtMovies.getText().toString();
        String sArt = txtArt.getText().toString();
        String sTvShow = txtTvShow.getText().toString();
        String sSports = txtSports.getText().toString();
        String sScience = txtScience.getText().toString();
        String sLookingFor = txtLookingFor.getText().toString();

        if (!sMusic.isEmpty() && music.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestMusic), sMusic);
        if (!sLiterature.isEmpty() && literature.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestLiterature), sLiterature);
        if (!sMovies.isEmpty() && movies.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestMovies), sMovies);
        if (!sArt.isEmpty() && art.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestArt), sArt);
        if (!sTvShow.isEmpty() && tvShow.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestTVShows), sTvShow);
        if (!sSports.isEmpty() && sports.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestSports), sSports);
        if (!sScience.isEmpty() && science.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestScience), sScience);
        if (!sLookingFor.isEmpty() && lookingFor.isChecked())
            person.fillTextFieldInfo(getResources().getString(R.string.selectInterestLookingFor), sLookingFor);
    }

    public void showNoticeDialog(String title, String[] choices) {
        // Create an instance of SelectInterests dialog
        SelectInterests dialogInterests = new SelectInterests();
        // Set tittle and choices
        dialogInterests.setOptions(title, choices, person);
        // Cancelable false, user can only close the dialog by Accept or Cancel buttons
        dialogInterests.setCancelable(false);
        // Show SelectInterest instance
        dialogInterests.show(getFragmentManager(), "SelectInterests");
    }

    @Override
    public void onDialogPositiveClick(String title, List<Integer> interestList) {
        if (title.equals(getResources().getString(R.string.selectInterestLookingFor)) && interestList.isEmpty()) {
        }
        else {
            int index = Arrays.asList(getResources().getStringArray(R.array.identifyInterests)).indexOf(title);
            person.fillDataBaseInterests(index + 1, interestList);
            Log.i("===> ", "CHECKING ALL HASHMAPS");
            for (Map.Entry<Integer, List<Integer>> entry : person.getDataBaseInterest().entrySet()) {
                Log.i("===> ", "***********************");
                Log.i("===> ", "Title " + entry.getKey());
                Log.i("===> ", "Values " + entry.getValue());
            }
        }
    }

    @Override
    public void onDialogNegativeClick(List<Integer> mirrorList,  String title) {
        if (title.equals(getResources().getString(R.string.selectInterestLookingFor)) && mirrorList.isEmpty()) {
        }
        else {
            int index = Arrays.asList(getResources().getStringArray(R.array.identifyInterests)).indexOf(title);
            person.fillDataBaseInterests(index, mirrorList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interests, menu);
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
        /*
        this.finish();
        //Exit with slide animation
        this.overridePendingTransition (R.anim.left_to_right, R.anim.right_to_left);
        */
    }

}
