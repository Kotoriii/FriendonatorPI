package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class GetContactedByActivity extends Activity {

    EditText txtPhone;
    EditText txtGoogle;
    EditText txtFacebook;
    EditText txtTwitter;
    //List<String> contactedSelections;
    String toggleOnOff;
    User user;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacted_by);
        final Button btnSaveContactMeChanges = (Button) findViewById(R.id.btnSaveContactMeChanges);

        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtGoogle = (EditText) findViewById(R.id.txtGoogle);
        txtFacebook = (EditText) findViewById(R.id.txtFacebook);
        txtTwitter = (EditText) findViewById(R.id.txtTwitter);

        //setUserCont();
        setPersonCont();

        fillCheckBoxContactedBy();

        btnSaveContactMeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Testing with an array list
                //contactedSelections = new ArrayList<String>();
                String phone = txtPhone.getText().toString();
                String google = txtGoogle.getText().toString();
                String facebook = txtFacebook.getText().toString();
                String twitter = txtTwitter.getText().toString();
                /*if (!phone.isEmpty())
                    contactedSelections.add(phone);
                if (!google.isEmpty())
                    contactedSelections.add(google);
                if (!facebook.isEmpty())
                    contactedSelections.add(facebook);
                if (!twitter.isEmpty())
                    contactedSelections.add(twitter);*/

                if (validateSave()) {
                    createContactedByList(phone, google, facebook, twitter);
                    validateSave();

                    // Create intent to open interests activity
                    Intent intent = new Intent(GetContactedByActivity.this, ProfileActivity.class);

                    // Put bundle inside intent
                    //intent.putExtras(bundle());
                    //intent.putExtra("USER", user);
                    intent.putExtra("PERSON", person);

                    // Start change to a new layout
                    startActivity(intent);

                    // Finish activity
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else
                    Toast.makeText(getApplicationContext(), R.string.textRequired, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setUserCont() {
        user = this.getIntent().getParcelableExtra("USER");
    }

    public void setPersonCont() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");
    }
/*
    public Bundle bundle() {
        Bundle bundle = this.getIntent().getExtras();

        // Checks for empty Bundle
        if (bundle == null)
            bundle = new Bundle();

        // Set bundle with contacted by list
        bundle.putStringArrayList("CONTACTEDBY", (ArrayList<String>) contactedSelections);

        // Pass toggle On/Off to profile
        bundle.putInt("TOGGLETOOL", toggleOnOff);

        return bundle;
    }
*/
    public void fillCheckBoxContactedBy() {
        for (String key : person.getGetContactedByList().keySet()) {
            if (key.equals("Phone")) {
                txtPhone.setText(person.contactedByValue(key));
                txtPhone.setVisibility(View.VISIBLE);
                CheckBox checkBoxCellPhone = (CheckBox) findViewById(R.id.checkBoxCellPhone);
                checkBoxCellPhone.setChecked(true);
            }
            else if (key.equals("Google")) {
                txtGoogle.setText(person.contactedByValue(key));
                txtGoogle.setVisibility(View.VISIBLE);
                CheckBox checkBoxGoogle = (CheckBox) findViewById(R.id.checkBoxGoogle);
                checkBoxGoogle.setChecked(true);
            }
            else if (key.equals("Facebook")) {
                txtFacebook.setText(person.contactedByValue(key));
                txtFacebook.setVisibility(View.VISIBLE);
                CheckBox checkBoxFacebook = (CheckBox) findViewById(R.id.checkBoxFacebook);
                checkBoxFacebook.setChecked(true);
            }
            else if (key.equals("Twitter")) {
                txtTwitter.setText(person.contactedByValue(key));
                txtTwitter.setVisibility(View.VISIBLE);
                CheckBox checkBoxTwitter = (CheckBox) findViewById(R.id.checkBoxTwitter);
                checkBoxTwitter.setChecked(true);
            }
            else if (key.equals("Tool")) {
                Switch tool = (Switch) findViewById(R.id.switchActivateTool);
                tool.setChecked(true);
            }
        }
    }

    public void onCheckBoxClicked(View v) {
        boolean checked = ((CheckBox) v).isChecked();

        // Check which checkbox was clicked and enable or disable the corresponding textField
        switch (v.getId()) {
            case R.id.checkBoxCellPhone:
                if (checked)
                    txtPhone.setVisibility(View.VISIBLE);
                else {
                    txtPhone.setVisibility(View.GONE);
                    person.getGetContactedByList().remove("Phone");
                }
                break;
            case R.id.checkBoxGoogle:
                if (checked)
                    txtGoogle.setVisibility(View.VISIBLE);
                else {
                    txtGoogle.setVisibility(View.GONE);
                    person.getGetContactedByList().remove("Google");
                }
                break;
            case R.id.checkBoxFacebook:
                if (checked)
                    txtFacebook.setVisibility(View.VISIBLE);
                else {
                    txtFacebook.setVisibility(View.GONE);
                    person.getGetContactedByList().remove("Facebook");
                }
                break;
            case R.id.checkBoxTwitter:
                if (checked)
                    txtTwitter.setVisibility(View.VISIBLE);
                else {
                    txtTwitter.setVisibility(View.GONE);
                    person.getGetContactedByList().remove("Twitter");
                }
                break;
        }
    }

    public boolean onSwitchToggle() {
        boolean on = ((Switch) findViewById(R.id.switchActivateTool)).isChecked();
        if (on)
            toggleOnOff = "Look for me tool is ON";
        else {
            toggleOnOff = "Look for me tool is OFF";
            person.getGetContactedByList().remove("Tool");
        }
        return on;
    }

    public void createContactedByList(String phone, String google, String facebook, String twitter) {
        CheckBox checkBoxCellPhone = (CheckBox) findViewById(R.id.checkBoxCellPhone);
        CheckBox checkBoxGoogle = (CheckBox) findViewById(R.id.checkBoxGoogle);
        CheckBox checkBoxFacebook = (CheckBox) findViewById(R.id.checkBoxFacebook);
        CheckBox checkBoxTwitter = (CheckBox) findViewById(R.id.checkBoxTwitter);

        if (!phone.isEmpty() && checkBoxCellPhone.isChecked())
            person.fillContactedList("Phone", phone);
        if (!google.isEmpty() && checkBoxGoogle.isChecked())
            person.fillContactedList("Google", google);
        if (!facebook.isEmpty() && checkBoxFacebook.isChecked())
            person.fillContactedList("Facebook", facebook);
        if (!twitter.isEmpty() && checkBoxTwitter.isChecked())
            person.fillContactedList("Twitter", twitter);
        if (onSwitchToggle())
            person.fillContactedList("Tool", toggleOnOff);
    }

    public boolean validateSave() {
        CheckBox checkBoxCellPhone = (CheckBox) findViewById(R.id.checkBoxCellPhone);
        CheckBox checkBoxGoogle = (CheckBox) findViewById(R.id.checkBoxGoogle);
        CheckBox checkBoxFacebook = (CheckBox) findViewById(R.id.checkBoxFacebook);
        CheckBox checkBoxTwitter = (CheckBox) findViewById(R.id.checkBoxTwitter);
        boolean good = true;

        if (txtPhone.getText().toString().isEmpty() && checkBoxCellPhone.isChecked() ||
                txtGoogle.getText().toString().isEmpty() && checkBoxGoogle.isChecked() ||
                txtFacebook.getText().toString().isEmpty() && checkBoxFacebook.isChecked() ||
                txtTwitter.getText().toString().isEmpty() && checkBoxTwitter.isChecked()) {
            good = false;
        }
        return good;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.get_contacted_by, menu);
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
