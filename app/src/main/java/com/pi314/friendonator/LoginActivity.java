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

        db = SQLiteHelper.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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
                    Toast.makeText(btnlogin.getContext(),
                            "Wrong email or password",
                            Toast.LENGTH_SHORT).show();

                }
            }


        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.activity_sign_up);
                dialog.setTitle("Register");

                final EditText editTextUserName, editTextPassword, editTextConfirmPassword;
                final Button btnCreateAccount;

                editTextUserName = (EditText) findViewById(R.id.editTextUserName);
                editTextPassword = (EditText) findViewById(R.id.editTextPassword);
                editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

                btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
                btnCreateAccount.setOnClickListener(new View.OnClickListener() {

                    public boolean isEmail(CharSequence email) {
                        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
                    }

                    public List getUserEmail() {

                        List<String> UserArray = new ArrayList<String>();
                        final SQLiteHelper baseDataUsers =
                                new SQLiteHelper(getApplicationContext(), "BaseFriendonator", null, 1);

                        for (Usuario u : baseDataUsers.getAllUsuarios()) {
                            UserArray.add(u.getCorreo());
                        }

                        return UserArray;
                    }

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        String userName = editTextUserName.getText().toString();
                        String password = editTextPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();


                        if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                            Toast.makeText(getApplicationContext(), "Empty field", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // check if both password matches
                        if (!password.equals(confirmPassword)) {
                            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (getUserEmail().contains(userName)) {
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!isEmail(userName)) {
                            Toast.makeText(getApplicationContext(), "Please provide a valid email address", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Usuario usuario = new Usuario();
                            usuario.setCorreo(userName);
                            usuario.setPassword(password);

                            db.insertUsuario(usuario);
                            editTextUserName.setText("");
                            editTextPassword.setText("");
                            editTextConfirmPassword.setText("");
                            // Save the Data in Database
                            Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }
    }



