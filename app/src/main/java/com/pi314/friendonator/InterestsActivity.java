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
    String newTittle;
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

/*
    public Bundle bundleActivities () {
        Bundle bundle = this.getIntent().getExtras();

        // Checks for empty bundle
        if (bundle != null) {
            if (user == null)
                user = new User(Parcel.obtain());
            bundle.putParcelable("USER", user);
        } else
            bundle = new Bundle();

        // Set bundle with interest list
        //bundle.putStringArrayList("INTERESTS", (ArrayList<String>) listByInterest);

        return bundle;
    }
*/
    public void fillCheckBox() {
        for (String key : person.getInterestList().keySet()) {
            if (key.equals("Music")) {
                CheckBox music = (CheckBox) findViewById(R.id.checkBoxMusic);
                music.setChecked(true);
                TextView lblMusic = (TextView) findViewById(R.id.lblMusic);
                lblMusic.setClickable(true);
                txtMusic.setVisibility(View.VISIBLE);
                txtMusic.setText(person.interestsValue("Music").get(person.interestsValue("Music").size()-1));
            }
            else if (key.equals("Literature")) {
                CheckBox literature = (CheckBox) findViewById(R.id.checkBoxLiterature);
                literature.setChecked(true);
                TextView lblLiterature = (TextView) findViewById(R.id.lblLiterature);
                lblLiterature.setClickable(true);
                txtLiterature.setVisibility(View.VISIBLE);
                txtLiterature.setText(person.interestsValue("Literature").get(person.interestsValue("Literature").size()-1));
            }
            else if (key.equals("Movies")) {
                CheckBox movies = (CheckBox) findViewById(R.id.checkBoxMovies);
                movies.setChecked(true);
                TextView lblMovies = (TextView) findViewById(R.id.lblMovies);
                lblMovies.setClickable(true);
                txtMovies.setVisibility(View.VISIBLE);
                txtMovies.setText(person.interestsValue("Movies").get(person.interestsValue("Movies").size()-1));
            }
            else if (key.equals("Art")) {
                CheckBox art = (CheckBox) findViewById(R.id.checkBoxArt);
                art.setChecked(true);
                TextView lblArt = (TextView) findViewById(R.id.lblArt);
                lblArt.setClickable(true);
                txtArt.setVisibility(View.VISIBLE);
                txtArt.setText(person.interestsValue("Art").get(person.interestsValue("Art").size()-1));
            }
            else if (key.equals("TV Shows")) {
                CheckBox tvShow = (CheckBox) findViewById(R.id.checkBoxTvShow);
                tvShow.setChecked(true);
                TextView lblTvShow = (TextView) findViewById(R.id.lblTvShow);
                lblTvShow.setClickable(true);
                txtTvShow.setVisibility(View.VISIBLE);
                txtTvShow.setText(person.interestsValue("TV Shows").get(person.interestsValue("TV Shows").size()-1));
            }
            else if (key.equals("Sports")) {
                CheckBox sports = (CheckBox) findViewById(R.id.checkBoxSports);
                sports.setChecked(true);
                TextView lblSports = (TextView) findViewById(R.id.lblSports);
                lblSports.setClickable(true);
                txtSports.setVisibility(View.VISIBLE);
                txtSports.setText(person.interestsValue("Sports").get(person.interestsValue("Sports").size()-1));
            }
            else if (key.equals("Science")) {
                CheckBox science = (CheckBox) findViewById(R.id.checkBoxScience);
                science.setChecked(true);
                txtScience.setVisibility(View.VISIBLE);
                txtScience.setText(person.interestsValue("Science").get(0));
            }
            else if (key.equals("Looking For")) {
                CheckBox lookingFor = (CheckBox) findViewById(R.id.checkBoxLookingFor);
                lookingFor.setChecked(true);
                TextView lblLookingFor = (TextView) findViewById(R.id.lblLookingFor);
                lblLookingFor.setClickable(true);
                txtLookingFor.setVisibility(View.VISIBLE);
                txtLookingFor.setText(person.interestsValue("Looking For").get(person.interestsValue("Looking For").size()-1));
            }
        }
    }

    public void onCheckBoxInterestClicked(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        EditText txtMusic = (EditText) findViewById(R.id.txtMusic);
        EditText txtLiterature = (EditText) findViewById(R.id.txtLiterature);
        EditText txtMovies = (EditText) findViewById(R.id.txtMovies);
        EditText txtArt = (EditText) findViewById(R.id.txtArt);
        EditText txtTvShow = (EditText) findViewById(R.id.txtTvShow);
        EditText txtSports = (EditText) findViewById(R.id.txtSports);
        EditText txtScience = (EditText) findViewById(R.id.txtScience);
        EditText txtLookingFor =(EditText) findViewById(R.id.txtLookingFor);
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
                    newTittle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTittle, newChoices);
                    txtMusic.setVisibility(View.VISIBLE);
                } else {
                    txtMusic.setVisibility(View.GONE);
                    lblMusic.setClickable(false);
                    person.getInterestList().remove("Music");
                }
                break;
            case R.id.checkBoxLiterature:
                if (checked) {
                    lblLiterature.setClickable(true);
                    newTittle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTittle, newChoices);
                    txtLiterature.setVisibility(View.VISIBLE);
                } else {
                    txtLiterature.setVisibility(View.GONE);
                    lblLiterature.setClickable(false);
                    person.getInterestList().remove("Literature");
                }
                break;
            case R.id.checkBoxMovies:
                if (checked) {
                    lblMovies.setClickable(true);
                    newTittle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTittle, newChoices);
                    txtMovies.setVisibility(View.VISIBLE);
                } else {
                    txtMovies.setVisibility(View.GONE);
                    lblMovies.setClickable(false);
                    person.getInterestList().remove("Movies");
                }
                break;
            case R.id.checkBoxArt:
                if (checked) {
                    lblArt.setClickable(true);
                    newTittle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTittle, newChoices);
                    txtArt.setVisibility(View.VISIBLE);
                } else {
                    txtArt.setVisibility(View.GONE);
                    lblArt.setClickable(false);
                    person.getInterestList().remove("Art");
                }
                break;
            case R.id.checkBoxTvShow:
                if (checked) {
                    lblTvShow.setClickable(true);
                    newTittle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTittle, newChoices);
                    txtTvShow.setVisibility(View.VISIBLE);
                } else {
                    txtTvShow.setVisibility(View.GONE);
                    lblTvShow.setClickable(false);
                    person.getInterestList().remove("TV Shows");
                }
                break;
            case R.id.checkBoxSports:
                if (checked) {
                    lblSports.setClickable(true);
                    newTittle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTittle, newChoices);
                    txtSports.setVisibility(View.VISIBLE);
                } else {
                    txtSports.setVisibility(View.GONE);
                    lblSports.setClickable(false);
                    person.getInterestList().remove("Sports");
                }
                break;
            case R.id.checkBoxScience:
                if (checked) {
                    txtScience.setVisibility(View.VISIBLE);
                    person.fillInterestList("Science", new ArrayList<String>());
                    //listByInterest.add(txtScience.getText().toString());
                } else {
                    txtScience.setVisibility(View.GONE);
                    person.getInterestList().remove("Science");
                    //listByInterest.remove(listByInterest.indexOf(txtScience.getText().toString()));
                }
                break;
            case R.id.checkBoxLookingFor:
                if (checked) {
                    lblLookingFor.setClickable(true);

                    newTittle = (String) getResources().getText(R.string.selectInterestLookingFor);
                    newChoices = getResources().getStringArray(R.array.lookingFor);
                    showNoticeDialog(newTittle, newChoices);
                    txtLookingFor.setVisibility(View.VISIBLE);
                } else {
                    txtLookingFor.setVisibility(View.GONE);
                    lblLookingFor.setClickable(false);
                    person.getInterestList().remove("Looking For");
                }
                break;
        }
    }

    public void onLabelInterestClicked(View v) {
        Boolean clicked = v.isClickable();
        CheckBox music = (CheckBox) findViewById(R.id.checkBoxMusic);
        CheckBox literature = (CheckBox) findViewById(R.id.checkBoxLiterature);
        CheckBox movies = (CheckBox) findViewById(R.id.checkBoxMovies);
        CheckBox art = (CheckBox) findViewById(R.id.checkBoxArt);
        CheckBox tvShow = (CheckBox) findViewById(R.id.checkBoxTvShow);
        CheckBox sports = (CheckBox) findViewById(R.id.checkBoxSports);
        CheckBox lookingFor = (CheckBox) findViewById(R.id.checkBoxLookingFor);

        switch (v.getId()){
            case R.id.lblMusic:
                if (clicked && music.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestMusic);
                    newChoices = getResources().getStringArray(R.array.music);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblLiterature:
                if (clicked && literature.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestLiterature);
                    newChoices = getResources().getStringArray(R.array.literature);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblMovies:
                if (clicked && movies.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestMovies);
                    newChoices = getResources().getStringArray(R.array.movies);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblArt:
                if (clicked && art.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestArt);
                    newChoices = getResources().getStringArray(R.array.art);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblTvShow:
                if (clicked && tvShow.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestTVShows);
                    newChoices = getResources().getStringArray(R.array.tvShows);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblSports:
                if (clicked && sports.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestSports);
                    newChoices = getResources().getStringArray(R.array.sports);
                    showNoticeDialog(newTittle, newChoices);
                }
                break;
            case R.id.lblLookingFor:
                if (clicked && lookingFor.isChecked()) {
                    newTittle = (String) getResources().getText(R.string.selectInterestLookingFor);
                    newChoices = getResources().getStringArray(R.array.lookingFor);
                    showNoticeDialog(newTittle, newChoices);
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
        CheckBox music = (CheckBox) findViewById(R.id.checkBoxMusic);
        CheckBox literature = (CheckBox) findViewById(R.id.checkBoxLiterature);
        CheckBox movies = (CheckBox) findViewById(R.id.checkBoxMovies);
        CheckBox art = (CheckBox) findViewById(R.id.checkBoxArt);
        CheckBox tvShow = (CheckBox) findViewById(R.id.checkBoxTvShow);
        CheckBox sports = (CheckBox) findViewById(R.id.checkBoxSports);
        CheckBox science = (CheckBox) findViewById(R.id.checkBoxScience);
        CheckBox lookingFor = (CheckBox) findViewById(R.id.checkBoxLookingFor);

        if (!sMusic.isEmpty() && music.isChecked())
            person.interestsValue("Music").add(sMusic);
        if (!sLiterature.isEmpty() && literature.isChecked())
            person.interestsValue("Literature").add(sLiterature);
        if (!sMovies.isEmpty() && movies.isChecked())
            person.interestsValue("Movies").add(sMovies);
        if (!sArt.isEmpty() && art.isChecked())
            person.interestsValue("Art").add(sArt);
        if (!sTvShow.isEmpty() && tvShow.isChecked())
            person.interestsValue("TV Shows").add(sTvShow);
        if (!sSports.isEmpty() && sports.isChecked())
            person.interestsValue("Sports").add(sSports);
        if (!sScience.isEmpty() && science.isChecked())
            person.interestsValue("Science").add(sScience);
        if (!sLookingFor.isEmpty() && lookingFor.isChecked())
            person.interestsValue("Looking For").add(sLookingFor);
    }

    public void showNoticeDialog(String tittle, String[] choices) {
        // Create an instance of the SelectInterests
        SelectInterests dialogInterests = new SelectInterests();
        // Set tittle and choices
        dialogInterests.setOptions(tittle, choices, person);
        // Show SelectInterest instance
        dialogInterests.show(getFragmentManager(), "SelectInterests");
    }

    @Override
    public void onDialogPositiveClick(List<String> listBySelectedInterest, String tittle) {
        /*
        if (listByInterest == null && listBySelectedInterest != null) {
            listByInterest = new ArrayList<String>();
            for (String s : listBySelectedInterest){
                listByInterest.add(s);
            }
        } else if (listByInterest != null && listBySelectedInterest != null) {
            for (String s : listBySelectedInterest){
                listByInterest.add(s);
            }
        }
        */

        if (person != null) {
            String interestTittle;
            List<String> listByInterest;
            if (listBySelectedInterest != null) {
                listByInterest = new ArrayList<String>();
                for (String interest : listBySelectedInterest) {
                    listByInterest.add(interest);
                }
                interestTittle = tittle;
                person.fillInterestList(interestTittle, listByInterest);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(List<String> listBySelectedInterest) {

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
        this.finish();
        //Exit with slide animation
        this.overridePendingTransition (R.anim.left_to_right, R.anim.right_to_left);
    }

}
