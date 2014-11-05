package com.pi314.friendonator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Database.SQLiteHelper;
import Database.Usuario;


public class SignUp extends Activity {

    TextView editTextUserName;
    TextView editTextPassword;
    TextView editTextConfirmPassword;
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        final Button btnCancelSignUp = (Button) findViewById(R.id.btnCancelSignUp);
        editTextUserName = (TextView) findViewById(R.id.editTextUserName);
        editTextPassword = (TextView) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (TextView) findViewById(R.id.editTextConfirmPassword);
        db = SQLiteHelper.getInstance(getApplicationContext());

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.toastEmptyField, Toast.LENGTH_LONG).show();
                } else if (db.getUser(userName).getCorreo() != null) {
                    Toast.makeText(getApplicationContext(), R.string.toastEmail, Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    // check if both password matches
                    Toast.makeText(getApplicationContext(), R.string.toastPassword, Toast.LENGTH_LONG).show();
                } else if (!isEmail(userName)) {
                    Toast.makeText(getApplicationContext(), R.string.toastCheckEmail, Toast.LENGTH_LONG).show();
                } else {
                    Usuario usuario = new Usuario();
                    usuario.setCorreo(userName);
                    usuario.setPassword(password);

                    // Save the Data in Database
                    db.insertUsuario(usuario);

                    Toast.makeText(getApplicationContext(), R.string.toastAccountSuccess, Toast.LENGTH_LONG).show();

                    // Close SignUp
                    finish();
                }
            }
        });

        btnCancelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close SignUp
                finish();
            }
        });
    }

    public boolean isEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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
