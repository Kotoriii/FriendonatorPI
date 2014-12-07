package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.pi314.interests.InterestsMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Dialog.InterestInfo;
import GridView.GridCustomAdapter;
import GridView.GridObject;

public class MatchProfileActivity extends Activity {

    Person person;
    Person matchPerson;
    ArrayList<GridObject> gridList;
    ArrayList<GridObject> contactedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);
        final TextView lblMatchName = (TextView) findViewById(R.id.lblMatchName);
        final TextView lblMatchPercentage = (TextView) findViewById(R.id.lblMatchPercentage);
        final TextView lblSpecialMatch = (TextView) findViewById(R.id.lblSpecialPercentage);
        final Button btnClose = (Button) findViewById(R.id.btnClose);

        // Get object person from intent extras
        getSetPerson();

        // Test getting match from Data Base
        Bundle bundle = this.getIntent().getExtras();
        int idUser = bundle.getInt("ID");

        // Create match person from Data Base
        InterestsMethods getMatch = new InterestsMethods();
        matchPerson = getMatch.createPerson(MatchProfileActivity.this, idUser);

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

        lblMatchPercentage.setText(getResources().getString(R.string.matchPercentage) + percentage + " %");
        if (specialPercentage != 0) {
            lblSpecialMatch.setVisibility(View.VISIBLE);
            lblSpecialMatch.setText(getResources().getString(R.string.categoryPercentage) + specialPercentage + " %");
        }

        gridViewInterests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridObject forDialog = gridList.get(position);
                if (!forDialog.getTitle().isEmpty() && matchPerson.textValue(forDialog.getTitle()) != null)
                    showInterestInfoDialog(getResources().getString(R.string.whatILike) + " " + forDialog.getTitle(), matchPerson.textValue(forDialog.getTitle()), forDialog.getTitle());
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_profile, menu);
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

}
