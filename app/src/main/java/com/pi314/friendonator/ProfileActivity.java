package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class ProfileActivity extends Activity {
    User user;
    Person person;
    EditText txtProfileName;

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

        // Locate the lblInterestsList TextView
        TextView lblInterestsList = (TextView) findViewById(R.id.lblInterestsList);

        // Build the message to be displayed on lblInterestsList
        lblInterestsList.setText(getInterestsList());

        // Locate the lblContactedByList TextView
        TextView getContactedBy = (TextView) findViewById(R.id.lblContactedByList);

        // Build the message to be displayed on lblContactedByList
        getContactedBy.setText(getContactedByList());

        btnChooseInterest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setName();

                // Create intent to open interests activity
                Intent intent = new Intent(ProfileActivity.this, InterestsActivity.class);

                // Set bundle inside intent
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
                // Create intent
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);

                // Finish activity
                finish();
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
        if (person.getName() == null)
            person.setName(name);
    }

    public void textName() {
        if (person.getName() != null)
            txtProfileName.setText(person.getName());
    }
/*
    public Bundle bundleProfile() {
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null){
            User user = bundle.getParcelable("USER");
            if (user == null)
                user = new User(Parcel.obtain());
            bundle.putParcelable("USER", user);
        } else
            bundle = new Bundle();

        return bundle;
    }
*/
    public String getInterestsList() {
        // Retrieve the info passed through the intent
        //Bundle bundle = this.getIntent().getExtras();
        String interestList = "Set your Interests preferences...";
        /*
        if (bundle != null) {
            List<String> interests = bundle.getStringArrayList("INTERESTS");
            if (interests != null) {
                interestList = "";
                for (String s : interests) {
                    interestList += s + "\n";
                }
            }
        }
        */
        if (person != null && !person.getInterestList().isEmpty()) {
            interestList = "";
            int count;

            for (Map.Entry<String, List<String>> entry : person.getInterestList().entrySet()) {
                interestList += entry.getKey() + "\n";
                interestList += entry.getValue() + "\n\n";
                /*count = 0;
                for (String value : entry.getValue()) {
                    if (count <= entry.getValue().size()-1) {
                        interestList += value + ", ";
                        count += 1;
                    } else
                        interestList += value + "\n\n";
                }*/
            }
        }

        return interestList;
    }

    public String getContactedByList() {
        // Retrieve the info passed through the intent
        //Bundle bundle = this.getIntent().getExtras();
        String contactedByList = "Set your Get Contacted By preferences...";
        /*
        if (bundle != null) {
            List<String> contact = bundle.getStringArrayList("CONTACTEDBY");
            if (contact != null) {
                contactedByList = "";
                for (String s : contact) {
                    contactedByList += s + "\n";
                }
            }
        }
        */
        if (person != null && !person.getGetContactedByList().isEmpty()) {
            contactedByList = "";
            for (Map.Entry<String, String> entry : person.getGetContactedByList().entrySet()) {
                contactedByList += entry.getKey() + "\n";
                contactedByList += entry.getValue() + "\n\n";
            }
        }

        return contactedByList;
    }
/*
    public String toggleOnOff() {
        // Retrieve the info passed through the intent
        Bundle bundle = this.getIntent().getExtras();
        String toggle = "Look for me tool is off";
        if(bundle != null){
            int onOff = bundle.getInt("TOGGLETOOL");
            if (onOff == 1)
                toggle = "Look for me tool is on";
            else
                toggle = "Look for me tool is off";
        }
        return toggle;
    }
*/
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
}
