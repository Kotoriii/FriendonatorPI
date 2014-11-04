package com.pi314.friendonator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Database.SQLiteHelper;
import Database.Usuario;

/**
 * Created by Christian on 10/14/2014.
 */
public class LoginActivity extends Activity {

    SQLiteHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        db = SQLiteHelper.getInstance(getApplicationContext());
        final EditText txtusername = (EditText) findViewById(R.id.txtusername);
        final EditText txtpassword = (EditText) findViewById(R.id.txtpassword);
        final Button btnlogin = (Button) findViewById(R.id.btnlogin);
        final Button btnregister = (Button) findViewById(R.id.btnRegister);


        // por el momento
        final List<String> usuarios = new ArrayList<String>();
        usuarios.add("DogeMaster666");
        usuarios.add("1");
        final List<String> contras = new ArrayList<String>();
        contras.add("doge");
        contras.add("1");

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((usuarios.contains(txtusername.getText().toString()) && contras.contains(txtpassword.getText().toString()))
                        || (!txtusername.getText().toString().isEmpty() && !txtpassword.getText().toString().isEmpty())) {
                    //Create the Intent element
                    Intent intent = new Intent(LoginActivity.this,
                            HomeActivity.class);
                    //Create the info to pass from one activity to another
                    Bundle b = new Bundle();
                    b.putString("NAME", txtusername.getText().toString());
                    //Add info to the intent
                    intent.putExtras(b);
                    //Start the new Activity
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(btnlogin.getContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();

                }
            }


        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create sign up intent
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }

}



