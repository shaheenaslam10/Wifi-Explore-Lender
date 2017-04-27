package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Adapters.GridAdapter;
import com.zaingz.holygon.wifi_explorelender.Adapters.RoutersListAdapter;
import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;
import com.zaingz.holygon.wifi_explorelender.Database.RouterGetList;
import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    DrawerLayout mDrawerLayout;
    ImageView img_list;

    RecyclerView recyclerView;
    RoutersListAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RoutersModel> routersModels = new ArrayList<>();
    Realm realm;
    String jadd_name, jadd_ssid, jadd_address, jadd_security, jadd_price, jadd_avspeed, jadd_lat, jadd_lng, jadd_connection, jadd_rating;

    String get_name,get_rating,get_signal,get_count;

    GridView grid;
    boolean list = true;
    FrameLayout fl_grid;
    String tokenData;
    int device_cnt;
    String st_device_cnt;

    LinearLayout ll_wallet, ll_history, ll_add, ll_devices, ll_logout, ll_profile;
    TextView txt_earnings,toolbar_device_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_devices = (LinearLayout) findViewById(R.id.ll_devices);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_profile = (LinearLayout) findViewById(R.id.ll_profile);

        txt_earnings = (TextView) findViewById(R.id.txt_earnings);
        toolbar_device_count = (TextView)findViewById(R.id.toolbar_device_count);


        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyWalletActivit.class);
                startActivity(intent);
            }
        });
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouterActivity.class);
                startActivity(intent);
            }
        });
        ll_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDevicesActivity.class);
                intent.putExtra("device_count",st_device_cnt);
                startActivity(intent);
            }
        });
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        txt_earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDevicesActivity.class);
                startActivity(intent);
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if (realm.where(SignUpDatabase.class).count() > 0) {
                    realm.deleteAll();
                }
                realm.commitTransaction();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        img_list = (ImageView) findViewById(R.id.img_list);
        fl_grid = (FrameLayout) findViewById(R.id.fl_grid);
        fl_grid.setVisibility(View.INVISIBLE);


        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list) {

                    img_list.setImageDrawable(getResources().getDrawable(R.drawable.list));
                    fl_grid.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    list = false;

                } else {
                    img_list.setImageDrawable(getResources().getDrawable(R.drawable.grid));
                    fl_grid.setVisibility(View.INVISIBLE);
                    list = true;
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });


        AsynchRouterList asynchRouterList = new AsynchRouterList();
        asynchRouterList.execute();





    }
    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }


    public class AsynchRouterList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {






            realm = Realm.getDefaultInstance();

            if (realm.where(SignUpDatabase.class).count()>0){

                RealmResults<SignUpDatabase> record = realm.where(SignUpDatabase.class).findAll();
                for (int i = 0; i < record.size(); i++) {
                    tokenData = record.get(i).getToken();
                }
            }




            OkHttpClient client;

            //for increase time to get response from server

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);

            client = builder.build();

            Request request = new Request.Builder()
                    .url(URLs.LENDER_WIFIS)
                    .addHeader("Authorization", "Token token=" + tokenData)
                    .build();


            try {
                Response response = client.newCall(request).execute();

                realm.beginTransaction();

                if (response.isSuccessful()) {


                    String json_string = response.body().string();
                    Log.e("shani", "Lender wifis post response suuccessfull ====  : " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);
                    JSONArray jsonArray = jsonobj.getJSONArray("wifis");

                    device_cnt = jsonArray.length();
                    st_device_cnt = String.valueOf(device_cnt);

                    Log.e("shani", "device count  : " + st_device_cnt);
                    for (int i = 0; i < jsonArray.length(); i++) {



                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        jadd_name = jsonObject.getString("name");
                        jadd_ssid = jsonObject.getString("ssid");

                        jadd_address = jsonObject.getString("address");
                        jadd_lat = jsonObject.getString("latitude");
                        jadd_lng = jsonObject.getString("longitude");
                        jadd_security = jsonObject.getString("security_type");
                        jadd_price = jsonObject.getString("price");
                        jadd_avspeed = jsonObject.getString("avg_speed");
                        jadd_connection = jsonObject.getString("connections");
                        jadd_rating = jsonObject.getString("rating");

                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_name);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_ssid);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_address);



                        // store json response in database


                        RouterGetList routerGetList = realm.createObject(RouterGetList.class);


                        routerGetList.setName(jadd_name);
                        routerGetList.setSsid(jadd_ssid);
                        routerGetList.setAddress(jadd_address);
                        routerGetList.setLatitude(jadd_lat);
                        routerGetList.setLongitude(jadd_lng);
                        routerGetList.setSecurity_type(jadd_security);
                        routerGetList.setPrice(jadd_price);
                        routerGetList.setAvg_speed(jadd_avspeed);
                        routerGetList.setConnections(jadd_connection);
                        routerGetList.setRating(jadd_rating);



                        //get response for adapter

                        RealmResults<RouterGetList> record = realm.where(RouterGetList.class).findAll();

                            get_name = record.get(i).getName();
                            get_rating = record.get(i).getRating();
                            get_count = record.get(i).getConnections();
                            get_signal = "Good";

                        String rating = get_rating+"/5";
                            Log.i("tag", "making array list foir adapoter" + get_name);


                            RoutersModel routerData = new RoutersModel(get_name, get_signal, rating, get_count);
                            routersModels.add(routerData);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                toolbar_device_count.setText(st_device_cnt);
                                recyclerAdapter = new RoutersListAdapter(routersModels, getApplicationContext());
                                recyclerView.setHasFixedSize(true);
                                layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(recyclerAdapter);

                                //grid view

                                GridAdapter adapter = new GridAdapter(MainActivity.this, routersModels);
                                grid = (GridView) findViewById(R.id.grid_view);
                                grid.setAdapter(adapter);
                                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        Toast.makeText(MainActivity.this, "You Clicked at " + routersModels.get(position).getName(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });

                    }



                    realm.commitTransaction();
                } else {
                    Log.e("shani", "Lender wifis get response unsuccessful : " + response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "Lender wifis get response failure exception e  : " + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                   /* Toast.makeText(getApplicationContext(), "this is my HomeT",
                            Toast.LENGTH_LONG).show();*/
                mDrawerLayout.openDrawer(GravityCompat.START);


                return true;

            /*    case R.id.action_settings:
                    return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
