package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pi314.interests.InterestsMethods;

import Database.SQLiteHelper;
import Database.Usuario;


public class GetContactedByActivity extends Activity {

    EditText txtPhone;
    EditText txtGoogle;
    EditText txtFacebook;
    EditText txtTwitter;
    CheckBox checkBoxCellPhone;
    CheckBox checkBoxGoogle;
    CheckBox checkBoxFacebook;
    CheckBox checkBoxTwitter;
    String toggleOnOff;
    Person person;
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacted_by);
        final Button btnSaveContactMeChanges = (Button) findViewById(R.id.btnSaveContactMeChanges);

        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtGoogle = (EditText) findViewById(R.id.txtGoogle);
        txtFacebook = (EditText) findViewById(R.id.txtFacebook);
        txtTwitter = (EditText) findViewById(R.id.txtTwitter);
        checkBoxCellPhone = (CheckBox) findViewById(R.id.checkBoxCellPhone);
        checkBoxGoogle = (CheckBox) findViewById(R.id.checkBoxGoogle);
        checkBoxFacebook = (CheckBox) findViewById(R.id.checkBoxFacebook);
        checkBoxTwitter = (CheckBox) findViewById(R.id.checkBoxTwitter);

        // Set Data Base
        db = SQLiteHelper.getInstance(getApplicationContext());

        // Get object person from intent extras
        setPersonCont();

        // Fill checkboxes previously set
        fillCheckBoxContactedBy();

        btnSaveContactMeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = txtPhone.getText().toString();
                String google = txtGoogle.getText().toString();
                String facebook = txtFacebook.getText().toString();
                String twitter = txtTwitter.getText().toString();

                // Verify is every text from selections are filled
                if (validateSave()) {

                    // Set contacted by list into person
                    createContactedByList(phone, google, facebook, twitter);

                    // Update user contact fields in data base
                    /*Usuario updateUser = new Usuario();
                    updateUser.setId(person.getId());
                    updateUser.setNum(txtPhone.getText().toString());
                    updateUser.setGplus(txtGoogle.getText().toString());
                    updateUser.setFb(txtFacebook.getText().toString());
                    updateUser.setTwitter(txtTwitter.getText().toString());
                    db.updateUsuario(updateUser);*/

                    // Save get contacted by into Data Base


                    // Create intent to open interests activity
                    Intent intent = new Intent(GetContactedByActivity.this, ProfileActivity.class);

                    // Put bundle inside intent
                    intent.putExtra("PERSON", person);

                    // Start change to a new layout
                    startActivity(intent);

                    // Finish activity
                    finish();

                    // Slide animation
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else
                    customToast(getResources().getString(R.string.textRequired));
                    //Toast.makeText(getApplicationContext(), R.string.textRequired, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setPersonCont() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");
    }

    public void fillCheckBoxContactedBy() {
        for (String key : person.getGetContactedByList().keySet()) {
            if (key.equals(getResources().getString(R.string.lblCellphone))) {
                txtPhone.setText(person.contactedByValue(key));
                txtPhone.setVisibility(View.VISIBLE);
                checkBoxCellPhone.setChecked(true);
            }
            else if (key.equals(getResources().getString(R.string.lblGoogle))) {
                txtGoogle.setText(person.contactedByValue(key));
                txtGoogle.setVisibility(View.VISIBLE);
                checkBoxGoogle.setChecked(true);
            }
            else if (key.equals(getResources().getString(R.string.lblFacebook))) {
                txtFacebook.setText(person.contactedByValue(key));
                txtFacebook.setVisibility(View.VISIBLE);
                checkBoxFacebook.setChecked(true);
            }
            else if (key.equals(getResources().getString(R.string.lblTwitter))) {
                txtTwitter.setText(person.contactedByValue(key));
                txtTwitter.setVisibility(View.VISIBLE);
                checkBoxTwitter.setChecked(true);
            }
            else if (key.equals(getResources().getString(R.string.lblFindMeTool))) {
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
                    person.getGetContactedByList().remove(getResources().getString(R.string.lblCellphone));
                }
                break;
            case R.id.checkBoxGoogle:
                if (checked)
                    txtGoogle.setVisibility(View.VISIBLE);
                else {
                    txtGoogle.setVisibility(View.GONE);
                    person.getGetContactedByList().remove(getResources().getString(R.string.lblGoogle));
                }
                break;
            case R.id.checkBoxFacebook:
                if (checked)
                    txtFacebook.setVisibility(View.VISIBLE);
                else {
                    txtFacebook.setVisibility(View.GONE);
                    person.getGetContactedByList().remove(getResources().getString(R.string.lblFacebook));
                }
                break;
            case R.id.checkBoxTwitter:
                if (checked)
                    txtTwitter.setVisibility(View.VISIBLE);
                else {
                    txtTwitter.setVisibility(View.GONE);
                    person.getGetContactedByList().remove(getResources().getString(R.string.lblTwitter));
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
        if (!phone.isEmpty() && checkBoxCellPhone.isChecked())
            person.fillContactedList(getResources().getString(R.string.lblCellphone), phone);
        if (!google.isEmpty() && checkBoxGoogle.isChecked())
            person.fillContactedList(getResources().getString(R.string.lblGoogle), google);
        if (!facebook.isEmpty() && checkBoxFacebook.isChecked())
            person.fillContactedList(getResources().getString(R.string.lblFacebook), facebook);
        if (!twitter.isEmpty() && checkBoxTwitter.isChecked())
            person.fillContactedList(getResources().getString(R.string.lblTwitter), twitter);
        if (onSwitchToggle())
            person.fillContactedList(getResources().getString(R.string.lblFindMeTool), toggleOnOff);
    }

    public boolean validateSave() {
        boolean good = true;

        if (txtPhone.getText().toString().isEmpty() && checkBoxCellPhone.isChecked() ||
                txtGoogle.getText().toString().isEmpty() && checkBoxGoogle.isChecked() ||
                txtFacebook.getText().toString().isEmpty() && checkBoxFacebook.isChecked() ||
                txtTwitter.getText().toString().isEmpty() && checkBoxTwitter.isChecked()) {
            good = false;
        }
        return good;
    }

    public void customToast(String message) {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // Set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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
        /*
        this.finish();
        //Exit with slide animation
        this.overridePendingTransition (R.anim.left_to_right, R.anim.right_to_left);
        */
    }
}
