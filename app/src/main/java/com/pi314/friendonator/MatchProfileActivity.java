package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import GridView.GridCustomAdapter;
import GridView.GridObject;

public class MatchProfileActivity extends Activity {

    Person person;
    Person matchPerson;
    int eventSelected;
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

        Bundle bundle = this.getIntent().getExtras();
        eventSelected = bundle.getInt("EVENT");

        // Fill match interests
        matchPersonInterests();

        // Fill get contacted by
        matchGetContactedBy();

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
        int specialPercentage = (int) Math.floor(match.specialMatchResult(eventSelected, person, matchPerson));

        lblMatchPercentage.setText("Match: " + percentage + " %");
        lblSpecialMatch.setText("Category match: " + specialPercentage + " %");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent
                Intent intent = new Intent(MatchProfileActivity.this, HomeActivity.class);

                // Set person inside intent
                intent.putExtra("PERSON", person);

                // Start change to a new layout
                startActivity(intent);

                // Finish activity
                finish();
            }
        });
    }

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    // For testing matching method
    public void matchPersonInterests() {
        matchPerson = new Person();

        HashMap<Integer, List<Integer>> interests = new HashMap<Integer, List<Integer>>();
        List<Integer> genres = new ArrayList<Integer>();
        genres.add(0);
        genres.add(1);
        genres.add(2);
        genres.add(3);

        List<Integer> genres2 = new ArrayList<Integer>();
        genres2.add(40);
        genres2.add(43);
        genres2.add(44);
        genres2.add(47);

        List<Integer> genres3 = new ArrayList<Integer>();
        genres3.add(18);
        genres3.add(22);

        interests.put(1,genres);
        interests.put(3, genres3);
        interests.put(6, genres2);

        matchPerson.setDataBaseInterest(interests);
        matchPerson.setName("Female Doge");
    }

    public void matchGetContactedBy() {
        HashMap<String, String> contactedBy = new HashMap<String, String>();
        contactedBy.put("Phone", "8649-5984");
        contactedBy.put("Facebook", "www.facebook.com/female.doge");
        contactedBy.put("Twitter", "@SuchClass");

        matchPerson.setGetContactedByList(contactedBy);
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
}
