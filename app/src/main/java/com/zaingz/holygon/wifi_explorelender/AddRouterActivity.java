package com.zaingz.holygon.wifi_explorelender;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRouterActivity extends AppCompatActivity {

    EditText ed_name, ed_ssid, ed_security, ed_pass, ed_avspeed, ed_price;
    Button add;
    Geocoder coder;
    List<Address> address;
    double lat, lng;
    String st_ed_name, st_ed_ssid, st_ed_security, st_ed_pass, st_ed_avspeed, st_ed_price;
    String st_lat, st_lng;
    Context context;
    ImageView btn_addRouter;
    Realm realm;
    String tokenData;
    String jaddname, jadd_ssid, jadd_pass, jadd_address, jadd_security, jadd_price, jadd_avspeed, jadd_lat, jadd_lng;
    String intnt_name,intnt_ssid,intnt_pass,intnt_security,intnt_price,intnt_speed;
    CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_router);


        ed_name = (EditText) findViewById(R.id.editTextadd_name);
        ed_ssid = (EditText) findViewById(R.id.editTextadd_ssid);
        ed_security = (EditText) findViewById(R.id.editTextadd_securityType);
        ed_avspeed = (EditText) findViewById(R.id.editTextadd_av_speed);
        ed_price = (EditText) findViewById(R.id.editTextadd_price);
        ed_pass = (EditText) findViewById(R.id.editTextadd_password);
        btn_addRouter = (ImageView) findViewById(R.id.btn_addrouter);
        add = (Button) findViewById(R.id.editTextadd_address);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        progressView.setVisibility(View.INVISIBLE);


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

        if (getIntent().getStringExtra("namegoback")!=null){

            intnt_name= getIntent().getStringExtra("namegoback");
            intnt_ssid=getIntent().getStringExtra("ssidgoback");
            intnt_pass=getIntent().getStringExtra("passgoback");
            intnt_security=getIntent().getStringExtra("securitygoback");
            intnt_price=getIntent().getStringExtra("pricegoback");
            intnt_speed=getIntent().getStringExtra("speedgoback");

            add.setText(getIntent().getStringExtra("address"));

            ed_name.setText(intnt_name);
            ed_ssid.setText(intnt_ssid);
            ed_pass.setText(intnt_pass);
            ed_security.setText(intnt_security);
            ed_price.setText(intnt_price);
            ed_avspeed.setText(intnt_speed);
        }








        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddRouterActivity.this, AddLocationActivity.class);
                intent.putExtra("namego",ed_name.getText().toString());
                intent.putExtra("ssidgo",ed_ssid.getText().toString());
                intent.putExtra("passwordgo",ed_security.getText().toString());
                intent.putExtra("securitygo",ed_name.getText().toString());
                intent.putExtra("pricego",ed_price.getText().toString());
                intent.putExtra("speedgo",ed_avspeed.getText().toString());
                Log.e("shani","putextra from addrouter: "+ed_name);
                startActivity(intent);
            }
        });






        ed_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (ed_pass.getRight() - ed_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ed_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ed_pass.getRight() - ed_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ed_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        return true;
                    }
                }
                return false;
            }
        });

        btn_addRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ed_name.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Name can't be Empty", Toast.LENGTH_SHORT).show();

                } else if (ed_ssid.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "SSID can't be Empty", Toast.LENGTH_SHORT).show();

                } else if (ed_pass.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Please enter password...", Toast.LENGTH_SHORT).show();

                } else if (ed_security.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Please Mention Security Type", Toast.LENGTH_SHORT).show();

                } else if (ed_price.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Please mention Price", Toast.LENGTH_SHORT).show();

                } else if (ed_avspeed.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Enter Average Speed", Toast.LENGTH_SHORT).show();

                } else if (add.getText().toString().length() < 1) {

                    Toast.makeText(AddRouterActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();

                } else {

                    Log.e("shani", "In else part: ");


                    btn_addRouter.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.VISIBLE);

                    st_ed_name = ed_name.getText().toString();
                    st_ed_ssid = ed_ssid.getText().toString();
                    st_ed_pass = ed_pass.getText().toString();
                    st_ed_security = ed_security.getText().toString();
                    st_ed_price = ed_price.getText().toString();
                    st_ed_avspeed = ed_avspeed.getText().toString();

                    Intent intent = getIntent();
                    lat = intent.getExtras().getDouble("_lat");
                    lng = intent.getExtras().getDouble("_lng");
                    st_lat = lat + "";
                    st_lng = lng + "";
                    Log.e("shani", "create wifi longi  : " + lng);
                    Log.e("shani", "create wifi lati   : " + lat);

                    AsynchAddRouter asynchAddRouter = new AsynchAddRouter();
                    asynchAddRouter.execute();

                }

            }
        });
    }

    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }


    public class AsynchAddRouter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            realm = Realm.getDefaultInstance();

            if (realm.where(WifiLenderData.class).count() > 0) {

                RealmResults<WifiLenderData> record = realm.where(WifiLenderData.class).findAll();
                for (int i = 0; i < record.size(); i++) {
                    tokenData = record.get(i).getToken();
                }
            }
            else {
                Toast.makeText(context, "Token is not available....", Toast.LENGTH_SHORT).show();
            }


            Log.e("shani", "token  ====  : " + tokenData);

            OkHttpClient client;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);

            client = builder.build();


            RequestBody requestBody = new FormBody.Builder()
                    .add("wifi[name]", st_ed_name)
                    .add("wifi[latitude]", st_lat)
                    .add("wifi[longitude]", st_lng)
                    .add("wifi[password]", st_ed_pass)
                    .add("wifi[ssid]", st_ed_ssid)
                    .add("wifi[security_type]", st_ed_security)
                    .add("wifi[price]", st_ed_price)
                    .add("wifi[avg_speed]", st_ed_avspeed)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Authorization", "Token token=" + tokenData)
                    .url(URLs.CREATE_WIFI)
                    .post(requestBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {


                    String json_string = response.body().string();
                    Log.e("shani", "create wifi post response suuccessfull ====  : " + json_string);

                    Intent intent = new Intent(AddRouterActivity.this,MainActivity.class);
                    startActivity(intent);

                   /* JSONObject jsonobj = new JSONObject(json_string);
                    JSONObject jsonobjChild = jsonobj.getJSONObject("wifi");

                    jaddname = jsonobjChild.getString("name");
                    jadd_ssid = jsonobjChild.getString("ssid");
                    jadd_pass = jsonobjChild.getString("password");
                    jadd_address = jsonobjChild.getString("address");
                    jadd_lat = jsonobjChild.getString("latitude");
                    jadd_lng = jsonobjChild.getString("longitude");
                    jadd_security = jsonobjChild.getString("security_type");
                    jadd_price = jsonobjChild.getString("price");
                    jadd_avspeed = jsonobjChild.getString("avg_speed");


                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    AddRouterDatabase addRouterDatabase = realm.createObject(AddRouterDatabase.class);
                    addRouterDatabase.setName(jaddname);
                    addRouterDatabase.setSsid(jadd_ssid);
                    addRouterDatabase.setPassword(jadd_pass);
                    addRouterDatabase.setAddress(jadd_address);
                    addRouterDatabase.setLatitude(jadd_lat);
                    addRouterDatabase.setLongitude(jadd_lng);
                    addRouterDatabase.setSecurity_type(jadd_security);
                    addRouterDatabase.setPrice(jadd_price);
                    addRouterDatabase.setAvg_speed(jadd_avspeed);
                    realm.commitTransaction();*/


                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_addRouter.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "Response Unsuccessful..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "create wifi post response failure exception e  : " + e.toString());
            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
