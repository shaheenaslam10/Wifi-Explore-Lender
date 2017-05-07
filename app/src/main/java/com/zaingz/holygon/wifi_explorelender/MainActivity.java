package com.zaingz.holygon.wifi_explorelender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.zaingz.holygon.wifi_explorelender.Database.Mytoken;
import com.zaingz.holygon.wifi_explorelender.Database.RouterGetList;
import com.zaingz.holygon.wifi_explorelender.Database.WalletDataBase;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;
import com.zaingz.holygon.wifi_explorelender.Valoidators.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
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
    String jadd_id, jadd_name, jadd_ssid, jadd_address, jadd_security, jadd_price, jadd_avspeed, jadd_lat, jadd_lng, jadd_connection, jadd_rating;

    GridView grid;
    boolean list = true;
    FrameLayout fl_grid;
    String tokenData;
    int device_cnt;
    String st_device_cnt;
    LinearLayout ll_wallet, ll_add, ll_devices, ll_logout, ll_profile;
    TextView txt_earnings, toolbar_device_count;
    String myToekn;
    ProgressDialog dialog;
    Intent intent;
    int connt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = ProgressDialog.show(MainActivity.this, "", "Please wait...", true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        // ll_devices = (LinearLayout) findViewById(R.id.ll_devices);

        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_profile = (LinearLayout) findViewById(R.id.ll_profile);

        txt_earnings = (TextView) findViewById(R.id.txt_earnings);
        toolbar_device_count = (TextView) findViewById(R.id.toolbar_device_count);


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
        /*ll_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDevicesActivity.class);
                intent.putExtra("device_count", st_device_cnt);
                startActivity(intent);
            }
        });*/
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnt = String.valueOf(connt);
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("device_count_profile", st_device_cnt);
                intent.putExtra("users_count_profile", cnt);
                startActivity(intent);
            }
        });
        txt_earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getApplicationContext(), MyDevicesActivity.class);
                startActivity(intent);*/
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                realm.clear(WifiLenderData.class);
                realm.clear(Mytoken.class);
                realm.clear(WalletDataBase.class);
                realm.clear(RouterGetList.class);
                realm.commitTransaction();
                realm.close();


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


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
                            Animation fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);
                            fl_grid.startAnimation(fadeInAnimation);
                            img_list.startAnimation(fadeInAnimation);

                            img_list.setImageDrawable(getResources().getDrawable(R.drawable.list));
                            fl_grid.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);

                        }
                    }, 500);

                    list = false;

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
                            Animation fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);
                            recyclerView.startAnimation(fadeInAnimation);
                            img_list.startAnimation(fadeInAnimation);

                            img_list.setImageDrawable(getResources().getDrawable(R.drawable.grid));
                            fl_grid.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }, 500);



                    list = true;
                }

            }
        });

        if (Validations.checkConnection(getApplicationContext())) {
            AsynchRouterList asynchRouterList = new AsynchRouterList();
            asynchRouterList.execute();
            dialog.show();
        } else {
            Toast.makeText(this, "Check Your Intenet Connection..", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onPause() {

        overridePendingTransition(R.animator.pull_in_right, android.R.anim.fade_out);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

    public class AsynchRouterList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            realm = Realm.getDefaultInstance();
            WifiLenderData wifiLenderData = realm.where(WifiLenderData.class).findFirst();
            myToekn = wifiLenderData.getToken();
            realm.close();

            Log.e("shani", "before call my token=" + myToekn);


            OkHttpClient client;

            //for increase time to get response from server

            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            client = builder.build();

            Request request = new Request.Builder()
                    .url(URLs.LENDER_WIFIS)
                    .addHeader("Authorization", "Token token=" + myToekn)
                    .build();


            try {
                Response response = client.newCall(request).execute();


                if (response.isSuccessful()) {


                    final String json_string = response.body().string();
                    Log.e("shani", "Lender wifis post response suuccessfull ====  : " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);
                    final JSONArray jsonArray = jsonobj.getJSONArray("wifis");

                    device_cnt = jsonArray.length();
                    st_device_cnt = String.valueOf(device_cnt);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            if (device_cnt == 0) {
                                Toast.makeText(MainActivity.this, "No device added yet !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Log.e("shani", "device count  : " + st_device_cnt);
                    for (int i = 0; i < jsonArray.length(); i++) {



                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        jadd_id = jsonObject.getString("id");
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


                        int con = Integer.parseInt(jadd_connection);
                        connt = connt + con;


                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_name);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_ssid);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_address);


                        Log.e("shani", "id in main ......" + jadd_id);


                        //get response for adapter

                        String rating = jadd_rating + "/5";
                        Log.i("tag", "making array list foir adapoter" + jadd_name);


                        RoutersModel routerData = new RoutersModel(jadd_name, "Good", rating, jadd_id, jadd_connection);
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

                                        intent = new Intent(getApplicationContext(), MyDevicesActivity.class);
                                        intent.putExtra("namrouter", routersModels.get(position).getName());
                                        intent.putExtra("idroutergrid", routersModels.get(position).getId());
                                        startActivity(intent);

                                    }
                                });
                            }
                        });
                    }
                } else {
                    Log.e("shani", "Lender wifis get response unsuccessful : " + response.toString());
                    dialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "Lender wifis get response failure exception e  : " + e.toString());
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                // Toast.makeText(MainActivity.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();

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
}
