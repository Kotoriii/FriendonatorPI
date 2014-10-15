package com.pi314.friendonator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Christian on 10/14/2014.
 */
public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final EditText txtusername = (EditText)findViewById(R.id.txtusername);
        final EditText txtpassword = (EditText)findViewById(R.id.txtpassword);
        final Button btnlogin = (Button)findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtusername.getText().toString().equals("DogeMaster666") && txtpassword.getText().toString().equals("doge")){
                    //Create the Intent element
                    Intent intent = new Intent(LoginActivity.this,
                            HomeActivity.class);
                    //Create the info to pass from one activity to another
                    Bundle b = new Bundle();
                    b.putString("NAME", txtusername.getText().toString());
                    //Add info to the intent
                    intent.putExtras(b);
                    //Start the new Activity
                    startActivity(intent);}
                else{
                    Toast.makeText(btnlogin.getContext(),
                            "Wrong email or password",
                            Toast.LENGTH_SHORT).show();

                }
            }



        });
    }
}
