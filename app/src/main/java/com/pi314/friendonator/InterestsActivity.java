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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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
    String newTitle;
    String [] newChoices;
    User user;
    Person person;

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

        //setUser();
        setPerson();

        fillCheckBox();

        btnSaveInterestsChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextToInterest();

                // Create intent to open interests activity
                Intent intent = new Intent(InterestsActivity.this, ProfileActivity.class);

                // Put bundle inside intent
                //intent.putExtra("USER", user);
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    public void setUser() {
        user = this.getIntent().getParcelableExtra("USER");
    }

    public void setPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");
    }

    public void fillCheckBox() {
        for (String key : person.getInterestList().keySet()) {
            if (key.equals(getResources().getString(R.string.selectInterestMusic))) {
                music.setChecked(true);
                TextView lblMusic = (TextView) findViewById(R.id.lblMusic);
                lblMusic.setClickable(true);
                txtMusic.setVisibility(View.VISIBLE);
                txtMusic.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestLiterature))) {
                literature.setChecked(true);
                TextView lblLiterature = (TextView) findViewById(R.id.lblLiterature);
                lblLiterature.setClickable(true);
                txtLiterature.setVisibility(View.VISIBLE);
                txtLiterature.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestMovies))) {
                movies.setChecked(true);
                TextView lblMovies = (TextView) findViewById(R.id.lblMovies);
                lblMovies.setClickable(true);
                txtMovies.setVisibility(View.VISIBLE);
                txtMovies.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestArt))) {
                art.setChecked(true);
                TextView lblArt = (TextView) findViewById(R.id.lblArt);
                lblArt.setClickable(true);
                txtArt.setVisibility(View.VISIBLE);
                txtArt.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestTVShows))) {
                tvShow.setChecked(true);
                TextView lblTvShow = (TextView) findViewById(R.id.lblTvShow);
                lblTvShow.setClickable(true);
                txtTvShow.setVisibility(View.VISIBLE);
                txtTvShow.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestSports))) {
                sports.setChecked(true);
                TextView lblSports = (TextView) findViewById(R.id.lblSports);
                lblSports.setClickable(true);
                txtSports.setVisibility(View.VISIBLE);
                txtSports.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestScience))) {
                science.setChecked(true);
                txtScience.setVisibility(View.VISIBLE);
                txtScience.setText(person.textValue(key));
            }
            else if (key.equals(getResources().getString(R.string.selectInterestLookingFor))) {
                lookingFor.setChecked(true);
                TextView lblLookingFor = (TextView) findViewById(R.id.lblLookingFor);
                lblLookingFor.setClickable(true);
                txtLookingFor.setVisibility(View.VISIBLE);
                txtLookingFor.setText(person.textValue(key));
            }
        }
    }

    public void onCheckBoxInterestClicked(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        TextView lblMusic = (TextView) findViewById(R.id.lblMusic);
        TextView lblLiterature = (TextView) findViewById(R.id.lblLiterature);
        TextView lblMovies = (TextView) findViewById(R.id.lblMovies);
        TextView lblArt = (TextView) findViewById(R.id.lblArt);
        TextView lblTvShow = (TextView) findViewById(R.id.lblTvShow);
        TextView lblSports = (TextView) findViewById(R.id.lblSports);
        TextView lblLookingFor = (TextView) findViewById(R.id.lblLookingFor);

        // Check which checkbox was clicked and enable or disable the corresponding textField
        switch (v.getId()) {
            case R.id.checkBoxMusic:
                if (checked) {
                    lblMusic.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTitle, newChoices);
                    txtMusic.setVisibility(View.VISIBLE);
                } else {
                    txtMusic.setVisibility(View.GONE);
                    lblMusic.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestMusic));
                }
                break;
            case R.id.checkBoxLiterature:
                if (checked) {
                    lblLiterature.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTitle, newChoices);
                    txtLiterature.setVisibility(View.VISIBLE);
                } else {
                    txtLiterature.setVisibility(View.GONE);
                    lblLiterature.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestLiterature));
                }
                break;
            case R.id.checkBoxMovies:
                if (checked) {
                    lblMovies.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTitle, newChoices);
                    txtMovies.setVisibility(View.VISIBLE);
                } else {
                    txtMovies.setVisibility(View.GONE);
                    lblMovies.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestMovies));
                }
                break;
            case R.id.checkBoxArt:
                if (checked) {
                    lblArt.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTitle, newChoices);
                    txtArt.setVisibility(View.VISIBLE);
                } else {
                    txtArt.setVisibility(View.GONE);
                    lblArt.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestArt));
                }
                break;
            case R.id.checkBoxTvShow:
                if (checked) {
                    lblTvShow.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTitle, newChoices);
                    txtTvShow.setVisibility(View.VISIBLE);
                } else {
                    txtTvShow.setVisibility(View.GONE);
                    lblTvShow.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestTVShows));
                }
                break;
            case R.id.checkBoxSports:
                if (checked) {
                    lblSports.setClickable(true);
                    newTitle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTitle, newChoices);
                    txtSports.setVisibility(View.VISIBLE);
                } else {
                    txtSports.setVisibility(View.GONE);
                    lblSports.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestSports));
                }
                break;
            case R.id.checkBoxScience:
                if (checked) {
                    txtScience.setVisibility(View.VISIBLE);
                    person.fillInterestList(getResources().getString(R.string.selectInterestScience), new ArrayList<String>());
                    //listByInterest.add(txtScience.getText().toString());
                } else {
                    txtScience.setVisibility(View.GONE);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestScience));
                    //listByInterest.remove(listByInterest.indexOf(txtScience.getText().toString()));
                }
                break;
            case R.id.checkBoxLookingFor:
                if (checked) {
                    lblLookingFor.setClickable(true);

                    newTitle = (String) getResources().getText(R.string.selectInterestLookingFor);
                    newChoices = getResources().getStringArray(R.array.lookingFor);
                    showNoticeDialog(newTitle, newChoices);
                    txtLookingFor.setVisibility(View.VISIBLE);
                } else {
                    txtLookingFor.setVisibility(View.GONE);
                    lblLookingFor.setClickable(false);
                    person.getInterestList().remove(getResources().getString(R.string.selectInterestLookingFor));
                }
                break;
        }
    }

    public void onLabelInterestClicked(View v) {
        Boolean clicked = v.isClickable();

        switch (v.getId()){
            case R.id.lblMusic:
                if (clicked && music.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblLiterature:
                if (clicked && literature.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblMovies:
                if (clicked && movies.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblArt:
                if (clicked && art.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblTvShow:
                if (clicked && tvShow.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblSports:
                if (clicked && sports.isChecked()) {
                    newTitle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTitle, newChoices);
                }
                break;
            case R.id.lblLookingFor:
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
        // Create an instance of the SelectInterests
        SelectInterests dialogInterests = new SelectInterests();
        // Set tittle and choices
        dialogInterests.setOptions(title, choices, person);
        // Cancelable false, user can only close the dialog by Accept or Cancel buttons
        dialogInterests.setCancelable(false);
        // Show SelectInterest instance
        dialogInterests.show(getFragmentManager(), "SelectInterests");
    }

    @Override
    public void onDialogPositiveClick(List<String> listBySelectedInterest, String title) {
        if (title.equals(getResources().getString(R.string.selectInterestLookingFor)) && listBySelectedInterest.isEmpty()) {
        }
        else
            person.fillInterestList(title, listBySelectedInterest);
    }

    @Override
    public void onDialogNegativeClick(List<String> mirrorList,  String title) {
        if (title.equals(getResources().getString(R.string.selectInterestLookingFor)) && mirrorList.isEmpty()) {
        }
        else
            person.fillInterestList(title, mirrorList);
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
