package com.zaingz.holygon.wifi_explorelender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker currLocationMarker;
    LocationRequest mLocationRequest;
    TextView textView;
    FrameLayout frame_list;
    ListView listView;
    String p_address;
    List<Address> addresses;
    ArrayAdapter<String> adapter;
    LatLng search;
    Double lat_extra, lng_extra;
    ImageView addLocationBack;
    GoogleMap mMap;
    TextView locationMarkertext, tv_set_location;
    double x, y, currentLat, currentLong;

    String city, address, intnt_name, intnt_ssid, intnt_pass, intnt_security, intnt_speed, intnt_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);


        textView = (EditText) findViewById(R.id.textView);
        locationMarkertext = (TextView) findViewById(R.id.locationMarkertext);
        tv_set_location = (TextView) findViewById(R.id.tv_set_location);
        frame_list = (FrameLayout) findViewById(R.id.frame_list);
        listView = (ListView) findViewById(R.id.listView);


        Intent intent = getIntent();


        tv_set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPakistan();
            }
        });


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }

                }

        );


        intnt_name = getIntent().getStringExtra("namego");
        intnt_ssid = getIntent().getStringExtra("ssidgo");
        intnt_pass = getIntent().getStringExtra("passwordgo");
        intnt_security = getIntent().getStringExtra("securitygo");
        intnt_speed = getIntent().getStringExtra("pricego");
        intnt_price = getIntent().getStringExtra("speedgo");
        Log.e("shani", "getextra from add Address: " + intnt_name);


        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        frame_list.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                p_address = adapter.getItem(position);

                Log.e("shani", "p_address on item click listener : " + p_address);
                textView.setText(p_address);
                frame_list.setVisibility(View.INVISIBLE);


                goPakistan();

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
                Log.e("shani", "onTextChanged string check  : " + check);

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

                goPakistan();

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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
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
    public void onLocationChanged(Location location) {

        currentLat = location.getLatitude();
        currentLong = location.getLongitude();
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(currentLat, currentLong, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append("\n");
                sb.append(address.getLocality()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //Place current location marker
        final LatLng latLng = new LatLng(currentLat, currentLong);
        //31.610899053960765  74.34358019381762


        //  if (c > 6) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

       /* if(count>9){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }*/
        //}
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.getUiSettings().setScrollGesturesEnabled(false);
      /*  mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                mMap.clear();
                markerLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onMarkerDrag(Marker marker) {
            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerLayout.setVisibility(View.INVISIBLE);
            }
        });
*/
  /*      mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    Log.d("SHAN", "The user gestured on the map.");
                } else if (reason == GoogleMap.OnCameraMoveStartedListener
                        .REASON_API_ANIMATION) {
                    Log.d("SHAN", "The user tapped something on the map");
                } else if (reason == GoogleMap.OnCameraMoveStartedListener
                        .REASON_DEVELOPER_ANIMATION) {
                    Log.d("SHAN", "TThe app moved the camera");
                }
            }
        });*/

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                //  mMap.setMyLocationEnabled(true);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //   mMap.getUiSettings().setZoomControlsEnabled(false);
            //   mMap.setMyLocationEnabled(true);
        }
        mMap.setIndoorEnabled(true);
        // googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub

                Log.d("SHAN", "3 is not =3 ");
                LatLng center = mMap.getCameraPosition().target;
                CameraPosition cameraPosition = mMap.getCameraPosition();
                Log.d("SHAN", " zoom position " + cameraPosition.zoom);
                if (cameraPosition.zoom < 16.0) {
                }

                locationMarkertext.setText("SETUP PICKUP LOCATION");
                // markerLayout.setVisibility(View.VISIBLE);
                try {
                    GetLocationAsync task = new GetLocationAsync(center.latitude, center.longitude);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        task.execute();
                } catch (Exception e) {
                }

                Log.d("SHAN", "3 is  =3 ");


            }
        });
    }

    public void goPakistan() {

        Log.d("SHAN", "My address=" + p_address);
        search = Methods.getLocationFromAddress(getApplicationContext(), p_address);


        Log.e("shani", "method search , get from location  : " + search);
        lat_extra = search.latitude;
        lng_extra = search.longitude;

        Log.e("shani", "lat from address : " + lat_extra);
        Log.e("shani", "lng from address : " + lng_extra);

        Intent intent = new Intent(AddLocationActivity.this, AddRouterActivity.class);
        intent.putExtra("_lat", lat_extra);
        intent.putExtra("_lng", lng_extra);
        intent.putExtra("namegoback", intnt_name);
        intent.putExtra("address", p_address);
        intent.putExtra("ssidgoback", intnt_ssid);
        intent.putExtra("passgoback", intnt_pass);
        intent.putExtra("securitygoback", intnt_security);
        intent.putExtra("pricegoback", intnt_price);
        intent.putExtra("speedgoback", intnt_speed);
        Log.e("shani", "putextra from add Adress: " + intnt_name);
        finish();
        startActivity(intent);

    }


    // Fetches data from url passed

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }*/

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        //  mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
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

    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        // boolean duplicateResponse;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub
            x = latitude;
            y = longitude;
            //   Log.i("SHAN", " in GetLocation  " + x + "  " + y);
        }

        @Override
        protected void onPreExecute() {
            //Address
            Log.d("SHAN", " x y =" + x + "    " + y);
            locationMarkertext.setText(" Getting location ");
            //    Log.d("SHAN", " my Address" + address);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Geocoder geocoder = new Geocoder(AddLocationActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
                    String localityString = returnAddress.getLocality();
                    city = returnAddress.getCountryName();
                    //   Log.d("LAHORE", "CITY    " + city);
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();
                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");
                    String naam = addresses.get(0).getAddressLine(0)
                            + addresses.get(0).getAddressLine(1) + " ";
                    address = addresses.get(0).getAddressLine(0)
                            + addresses.get(0).getAddressLine(1);
                    // Log.d("SHAN", naam + address);
                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                Log.e("tag", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                address = addresses.get(0).getAddressLine(0) + " "
                        + addresses.get(0).getAddressLine(1) + " ";
                locationMarkertext.setText(address);
                locationMarkertext.setText(address);

                p_address = address;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
/*mMap.clear();
                        mp = new MarkerOptions();
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                        mp.position(new LatLng(x, y));
                        mp.title("my position");
                        mp.icon(icon);
                        mMap.addMarker(mp);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(x,y), 16));*/
                        //  markerLayout.setVisibility(View.INVISIBLE);
                    }
                });
               /* if(c==3){
                    markerText.setText(get);
                }*/
         /*       if(c==2){
                textView2.setText(address);}*/
                //   textView2.setText(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }



}
