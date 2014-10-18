package com.pi314.friendonator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Christian on 10/12/2014.
 */
public class HomeActivity extends Activity {

    private Spinner spnEvent;
    ImageView iv;
    Bitmap image;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        iv = (ImageView) findViewById(R.id.imgviewEventpic);
        final String txtSpinner = spnEvent.getSelectedItem().toString();

        Bundle bundle = this.getIntent().getExtras();
        //addItemsOnEventSpinner();
        TextView lblprofilename = (TextView)findViewById(R.id.lblProfileName);
        lblprofilename.setText(""+ bundle.getString("NAME"));
        //onItemSelected();

        final TextView btnprofile = (TextView)findViewById(R.id.lblProfileName);

        btnprofile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent element
                Intent intent = new Intent(HomeActivity.this,
                        ProfileActivity.class);
                startActivity(intent);
            }
        });

        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                switch(position) {
                    case 0:
                        iv.setImageResource(R.drawable.snoopdoge);
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.dogeart);
                    break;
                    case 2:
                        iv.setImageResource(R.drawable.dogepotter);
                    break;
                    case 3:
                        iv.setImageResource(R.drawable.dogeparty);
                    break;
                    case 4:
                        iv.setImageResource(R.drawable.dogehangout);
                    break;


                }
                Toast.makeText(spnEvent.getContext(),
                        "Selected Event: " + spnEvent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

   /* public void onItemSelected(){




        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (txtSpinner.equals("Concert")) {
                    iv.setImageResource(R.drawable.snoopdoge);
                }
                if (txtSpinner.equals("Art")) {
                    iv.setImageResource(R.drawable.dogeart);
                }
                if (txtSpinner.equals("Literature")) {
                    iv.setImageResource(R.drawable.dogepotter);
                }
                if (txtSpinner.equals("Party")) {
                    iv.setImageResource(R.drawable.dogeparty);
                }
                if (txtSpinner.equals("Hang Out")) {
                    iv.setImageResource(R.drawable.dogehangout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        }*/


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
   /* public void addItemsOnEventSpinner() {

        spnEvent = (Spinner) findViewById(R.id.spnEvent);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }*/




   /* class TheTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                Spinner spnEvent = (Spinner)findViewById(R.id.spnEvent);
                String txtSpinner = spnEvent.getSelectedItem().toString();
                if (txtSpinner.equals("Concert")){
                image = downloadBitmap("http://i.imgur.com/IdVlsFw.jpg");}
                if (txtSpinner.equals("Art")){
                    image = downloadBitmap("http://th01.deviantart.net/fs71/PRE/i/2013/362/f/f/___doge_with_a_pearl_earring____by_skitzo_picklez-d6zu3i8.png");}
                if (txtSpinner.equals("Literature")){
                    image = downloadBitmap("http://img.sparknotes.com/content/sparklife/sparktalk/dogedoes3_Slide.jpg");}
                if (txtSpinner.equals("Party")){
                    image = downloadBitmap("http://www.lily.fi/sites/lily/files/user/45/2013/12/hjoqkmy.png");}
                if (txtSpinner.equals("Hang Out")){
                    image = downloadBitmap("https://lh6.ggpht.com/Gg2BA4RXi96iE6Zi_hJdloQAZxO6lC6Drpdr7ouKAdCbEcE_Px-1o4r8bg8ku_xzyF4y=h310");}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (image != null) {
                iv.setImageBitmap(image);
            }

        }
    }

    private Bitmap downloadBitmap(String url) {
        // initialize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttpGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }*/

}
