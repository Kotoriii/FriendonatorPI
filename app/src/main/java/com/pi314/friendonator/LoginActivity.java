package com.pi314.friendonator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pi314.interests.InterestsMethods;

import Database.SQLiteHelper;
import Database.Usuario;
import misc.ApiWrapper;

/**
 * Created by Christian on 10/14/2014.
 */
public class LoginActivity extends Activity {

    SQLiteHelper db;
    Person person;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        db = SQLiteHelper.getInstance(getApplicationContext());
        final EditText txtusername = (EditText) findViewById(R.id.txtusername);
        final EditText txtpassword = (EditText) findViewById(R.id.txtpassword);
        final Button btnlogin = (Button) findViewById(R.id.btnlogin);
        final Button btnregister = (Button) findViewById(R.id.btnRegister);

        // Create object person
        getSetPerson();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String correo = txtusername.getText().toString();
                final String password = txtpassword.getText().toString();

                //implementacion de api para login
                final ApiWrapper api = new ApiWrapper();

                // Start progress dialog while app waits for log in to check info
                progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //la clase de api tiene un metodod especial para ver si se encuentra actualmente
                        //conectado a internet y otro para pedir la conexion
                        try {
                            if (!api.isConnected(LoginActivity.this)) {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        enableWIFI();
                                    }
                                });
                            }
                            //esto se encarga de sacar y armar automaticamente la persona. Si hay algun error
                            //por ejemplo, no existe la persona en el servidor entonces va a mandar null
                            person = api.loginConServidor(correo, password, LoginActivity.this);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Usuario userLogin = null;

                        //si el login no fuera existoso entonces devolveria null
                        if (person != null && person.getId() != null) {

                            //deberia de existir, ya que existe un usuario..
                            //al hacer login de un nuevo usuario el sistema se encarga de guardar
                            //en su respectivo lugar
                            userLogin = db.getUser(person.getEmail());

                            // Create intent to open interests activity
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);


                            if (person.getName() == null) {
                                // Create intent to open interests activity
                                intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            } else {
                                InterestsMethods fillPerson = new InterestsMethods();
                                person.setDataBaseInterest(fillPerson.getInterestFromDataBase(LoginActivity.this, Integer.parseInt(person.getId())));
                                person.setGetTextFieldInfo(fillPerson.getTextsFromDataBase(LoginActivity.this, Integer.parseInt(person.getId())));
                                person.setGetContactedByList(fillPerson.getContactedByFromDataBase(LoginActivity.this, userLogin));
                            }

                            // Set person inside intent
                            intent.putExtra("PERSON", person);

                            // Start change to a new layout
                            startActivity(intent);

                            // Finish activity
                            finish();
                        } else {
                            // This makes possible to use toast inside a threat
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customToast(getResources().getString(R.string.toastWrongLogIn));
                                }
                            });
                        }
                        // Close progress dialog
                        progressDialog.dismiss();
                    }
                }).start();
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

    public void getSetPerson() {
        person = (Person) this.getIntent().getSerializableExtra("PERSON");

        if (person == null)
            person = new Person();
    }

    public void customToast(String message) {
        // Toast for validate messages when login is wrong
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // Set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.check_si_existe(); // se fija si ya hay algo en limbo.. si hay lo manda al activity correspondiente
    }

    private void check_si_existe(){
        SQLiteHelper pop = SQLiteHelper.getInstance(this);
        if(pop.hay_algo_en_limbo()){
            InterestsMethods mthl = new InterestsMethods();
            Person pers = mthl.createPerson(this, Integer.valueOf(pop.getLimbo1().getId()));

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            if (pers.getName() == null) {
                // Create intent to open interests activity
                intent = new Intent(LoginActivity.this, ProfileActivity.class);
            } else {
                InterestsMethods fillPerson = new InterestsMethods();
                pers.setDataBaseInterest(fillPerson.getInterestFromDataBase(LoginActivity.this, Integer.parseInt(pers.getId())));
                pers.setGetTextFieldInfo(fillPerson.getTextsFromDataBase(LoginActivity.this, Integer.parseInt(pers.getId())));
                pers.setGetContactedByList(fillPerson.getContactedByFromDataBase(LoginActivity.this, pop.getUserByID(Integer.parseInt(pers.getId()))));
            }

            intent.putExtra("PERSON", pers);

            startActivity(intent);
            this.finish();
        }
    }
    public void enableWIFI() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.wifiNotConnectedLogin)
                    .setCancelable(false)
                    .setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

    }

}



