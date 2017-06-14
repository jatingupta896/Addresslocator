package com.example.jatin.AddressLocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {


    private EditText room;
    private EditText locality;
    private EditText zipCode;
    private EditText city;
    private EditText state;
    private EditText country;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private Button submitButton;

    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Address");
        submitButton = (Button) findViewById(R.id.submit);
        room = (EditText) findViewById(R.id.room_no);
        locality = (EditText) findViewById(R.id.locality);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        zipCode = (EditText) findViewById(R.id.pincode);

        SharedPreferences sharedPreferences=getSharedPreferences("DemoFile",0);
        String sr=sharedPreferences.getString("STATUS1",null);
        room.setText(sr);
        String sl=sharedPreferences.getString("STATUS2",null);
        locality.setText(sl);
        String sc=sharedPreferences.getString("STATUS3",null);
        city.setText(sc);
        String ss=sharedPreferences.getString("STATUS4",null);
        state.setText(ss);
        String sco=sharedPreferences.getString("STATUS5",null);
        country.setText(sco);
        String sz=sharedPreferences.getString("STATUS6",null);
        zipCode.setText(sz);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                if(room.length()>6 || locality.length()>20 || zipCode.length()!=6 )
                {
                    Toast.makeText(MainActivity.this,"Room No field is having more than 6 character or Locality field is having more than 20 character or Zipcode is not correct ", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String Room = room.getText().toString().trim();
                    String Locality=locality.getText().toString().trim();
                    String City = city.getText().toString().trim();
                    String State = state.getText().toString().trim();
                    String Country = country.getText().toString().trim();
                    String Zipcode = zipCode.getText().toString().trim();
                    if(!TextUtils.isEmpty(Room)&&!TextUtils.isEmpty(Locality)&&!TextUtils.isEmpty(City)&&!TextUtils.isEmpty(State)&&!TextUtils.isEmpty(Country)&&!TextUtils.isEmpty(Zipcode))
                    {
                        progressDialog.show();
                        DatabaseReference post=databaseReference.push();
                        post.child("RoomNo").setValue(Room);
                        post.child("Locality").setValue(Locality);
                        post.child("City").setValue(City);
                        post.child("State").setValue(State);
                        post.child("Country").setValue(Country);
                        post.child("Zipcode").setValue(Zipcode);
                        progressDialog.dismiss();
//                        room.setText(null);
//                        locality.setText(null);
//                        country.setText(null);
//                        city.setText(null);
//                        state.setText(null);
//                        zipCode.setText(null);
                        location= Room+", "+Locality+", "+City+", "+State+", "+Country+", "+ Zipcode;
                        Intent intent= new Intent(MainActivity.this,MapsActivity.class);
                        intent.putExtra("Location", location);
                        startActivityForResult(intent,7777);




                    }
                }
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("DemoFile",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("STATUS1",room.getText().toString());
        editor.putString("STATUS2",locality.getText().toString());
        editor.putString("STATUS3",city.getText().toString());
        editor.putString("STATUS4",state.getText().toString());
        editor.putString("STATUS5",country.getText().toString());
        editor.putString("STATUS6",zipCode.getText().toString());
        editor.commit();
    }
}



