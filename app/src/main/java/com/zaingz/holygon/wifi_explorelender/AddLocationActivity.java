package com.zaingz.holygon.wifi_explorelender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zaingz.holygon.wifi_explorelender.HelperClasses.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;


    Location mLastLocation;
    Marker currLocationMarker;
    LocationRequest mLocationRequest;
    TextView textView;
    FrameLayout frame_list;
    ListView listView;
    String p_address;
    ArrayAdapter<String> adapter;
    LatLng search;
    Double lat_extra, lng_extra;
    ImageView addLocationBack;
    String intnt_name,intnt_ssid,intnt_pass,intnt_security,intnt_speed,intnt_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);


        textView = (EditText) findViewById(R.id.textView);
        frame_list = (FrameLayout) findViewById(R.id.frame_list);
        listView = (ListView) findViewById(R.id.listView);
        addLocationBack = (ImageView) findViewById(R.id.addLocationBack);
        addLocationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLocationActivity.this,AddRouterActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        intnt_name= getIntent().getStringExtra("namego");
        intnt_ssid=getIntent().getStringExtra("ssidgo");
        intnt_pass=getIntent().getStringExtra("passwordgo");
        intnt_security=getIntent().getStringExtra("securitygo");
        intnt_speed=getIntent().getStringExtra("pricego");
        intnt_price=getIntent().getStringExtra("speedgo");
        Log.e("shani","getextra from add Address: "+intnt_name);




        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });





        frame_list.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                p_address = adapter.getItem(position);

                Log.e("shani","p_address on item click listener : "+p_address);
                textView.setText(p_address);
                frame_list.setVisibility(View.INVISIBLE);




                // Toast.makeText(SetDestinationActivity.this, ""+adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GetPlaces task = new GetPlaces();
                String check = textView.getText().toString();
                Log.e("shani","onTextChanged string check  : "+check);

                if (check.trim().isEmpty()) {
                    // do something here

                } else {
                    // now pass the argument in the textview to the task
                    task.execute(check);

                    listView.setVisibility(View.VISIBLE);
                    frame_list.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                Log.d("SHAN","My address="+p_address);
                search = Methods.getLocationFromAddress(getApplicationContext(), p_address);


                Log.e("shani","method search , get from location  : "+search);
                lat_extra = search.latitude;
                lng_extra = search.longitude;

                Log.e("shani","lat from address : "+lat_extra);
                Log.e("shani","lng from address : "+lng_extra);

                Intent intent = new Intent(AddLocationActivity.this,AddRouterActivity.class);
                intent.putExtra("_lat",lat_extra);
                intent.putExtra("_lng",lng_extra);
                intent.putExtra("namegoback",intnt_name);
                intent.putExtra("address",textView.getText().toString());
                intent.putExtra("ssidgoback",intnt_ssid);
                intent.putExtra("passgoback",intnt_pass);
                intent.putExtra("securitygoback",intnt_security);
                intent.putExtra("pricegoback",intnt_price);
                intent.putExtra("speedgoback",intnt_speed);
                Log.e("shani","putextra from add Adress: "+intnt_name);
                finish();
                startActivity(intent);

                return false;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (textView.getText().length()>1){

                    search = Methods.getLocationFromAddress(getApplicationContext(), p_address);
                    Log.e("shani"," new  method search , get from location  : "+search);
                    lat_extar = search.latitude;
                    lng_extar = search.longitude;

                    Log.e("shani","lat from address : "+lat_extar);
                    Log.e("shani","lng from address : "+lng_extar);
                }
                else {
                    Toast.makeText(AddLocationActivity.this, "Please Enter text", Toast.LENGTH_SHORT).show();
                }*/


            }
        });

        /*if (lat_extar.toString().length()<1 && lng_extar.toString().length()<1){

            Toast.makeText(this, "lat lng not get", Toast.LENGTH_SHORT).show();

        }
        else {

            Intent intent = new Intent(AddLocationActivity.this,AddRouterActivity.class);
            intent.putExtra("_lat",lat_extar);
            intent.putExtra("_lng",lng_extar);
            finish();
            startActivity(intent);
        }*/

    }

    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }


    class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        // three dots is java for an array of strings
        protected ArrayList<String> doInBackground(String... args) {
            ArrayList<String> predictionsArr = new ArrayList<String>();

            try {
                URL googlePlaces = new URL(
                        // URLEncoder.encode(url,"UTF-8");
                        "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                                + URLEncoder.encode(args[0].toString(),
                                "UTF-8")
                                + "&types=geocode&language=en&sensor=true&key=AIzaSyCTqiZbPA7LwxnS24Hcxxkn8MT3-MupJqA");

                URLConnection tc = googlePlaces.openConnection();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(tc.getInputStream()));

                String line;
                StringBuffer sb = new StringBuffer();
                // take Google's legible JSON and turn it into one big
                // string.
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                // turn that string into a JSON object
                JSONObject predictions = new JSONObject(sb.toString());
                // now get the JSON array that's inside that object
                JSONArray ja = new JSONArray(predictions
                        .getString("predictions"));

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    // add each entry to our array
                    predictionsArr.add(jo.getString("description"));
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            }
            // return all the predictions based on the typing the in the
            // search
            return predictionsArr;

        }

        // then our post

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // put all the information in the list view to display
            Log.d("YourApp", "onPostExecute : " + result.size());
            // update the adapter
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.addlistitem, R.id.textView8);// show the list view as texts
            // attach the adapter to listView
            listView.setAdapter(adapter);

            for (String string : result) {


                Log.d("SHAN", " in on post" + string);
                adapter.add(string);
                adapter.notifyDataSetChanged();

            }
            // next.setClickable(true);
        }

    }







    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));

        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        mp.icon(icon);
        mp.snippet("Molive");
        Marker marker = mGoogleMap.addMarker(mp);
        //zoom to current position:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (textView.getText().length()>1){
            mLastLocation = location;
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));

             mp = new MarkerOptions();
            mp.position(new LatLng(lat_extra, lng_extra));
            icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
            mp.icon(icon);
            mp.snippet("Location");
            marker = mGoogleMap.addMarker(mp);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                //  mMap.setMyLocationEnabled(true);

                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            buildGoogleApiClient();


            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            //   mMap.setMyLocationEnabled(true);
        }


       /* mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("education",qariInfos.get(0).getQualification());
                intent.putExtra("address",qariInfos.get(0).getAddress());
                intent.putExtra("name",qariInfos.get(0).getName());
                intent.putExtra("time1",qariTimings.get(0).getDay()+" "+qariTimings.get(0).getTime());
                intent.putExtra("time2",qariTimings.get(1).getDay()+" "+qariTimings.get(1).getTime());
                startActivity(intent);

            }
        });*/


        // Defines the contents of the InfoWindow


    }

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }*/


}
