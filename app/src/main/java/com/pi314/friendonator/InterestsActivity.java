package com.pi314.friendonator;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InterestsActivity extends Activity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    List<String> listOfInterests= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        expandableListView = (ExpandableListView) findViewById(R.id.explstInterests);
        expandableListDetail = ExpandableListDataInterests.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListInterestsAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        //final EditText txtName = (EditText) findViewById(R.id.txtName);
        //final Button btnSaveChangesButton = (Button) findViewById(R.id.btnSaveChangesButton);

        expandableListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String interestSelected= expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition);
                Toast.makeText(
                        getApplicationContext(),
                        "Selected: " + interestSelected, Toast.LENGTH_SHORT
                ).show();
                /*CheckedTextView check = (CheckedTextView) v;
                check.setChecked(!check.isChecked());*/
                // Check if the list already contains the interest selected
                if (!listOfInterests.contains(interestSelected))
                    listOfInterests.add(interestSelected);
                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String save = expandableListTitle.get(groupPosition);
                if (save.equals("Save Changes")) {
                    FragmentManager fragmentManager = getFragmentManager();
                    InterestConfirmation dialogue = new InterestConfirmation();
                    dialogue.Message(messageAlert());
                    dialogue.show(fragmentManager, "tagConfirmation");
                }
                return false;
            }
        });

    }

    public String messageAlert () {
        String messageAlert = "";
        for (String s : listOfInterests){
            messageAlert += s.toString() + "\n";
        }
        return messageAlert;
    }

    public HashMap<String, List<String>> interests() {
        HashMap<String, List<String>> interest = ExpandableListDataInterests.getData();

        return null;
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
        this.overridePendingTransition  (R.anim.left_to_right, R.anim.right_to_left);
    }
}
