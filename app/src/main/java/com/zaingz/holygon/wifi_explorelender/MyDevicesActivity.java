package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.RouterGetList;
import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyDevicesActivity extends AppCompatActivity {

    Realm realm;
    String tokenData;
    TextView updata,downdata,ratings,connected,routers,total_money,toolbar_device_count;
    String st_updata,st_downdata,st_ratings,st_connected,st_total_money;
    String month,earning;
    String device_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
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

        updata = (TextView)findViewById(R.id.up_data);
        downdata = (TextView)findViewById(R.id.down_data);
        ratings = (TextView)findViewById(R.id.rating);
        connected = (TextView)findViewById(R.id.connected);
        routers = (TextView)findViewById(R.id.routers);
        total_money = (TextView)findViewById(R.id.total_money);
        toolbar_device_count = (TextView)findViewById(R.id.toolbar_device_count);

        Intent intent = getIntent();
         String intnt = intent.getStringExtra("device_count");

        toolbar_device_count.setText(intnt);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1, 1),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 10),
                new DataPoint(5, 2),
                new DataPoint(6, 2),
                new DataPoint(7, 2),
                new DataPoint(8, 2),
                new DataPoint(9, 2),
                new DataPoint(10, 2),
                new DataPoint(11, 2),
                new DataPoint(12, 2),

        });
        graph.addSeries(series);


        AsynchAllDeviceData asynchAllDeviceData = new AsynchAllDeviceData();
        asynchAllDeviceData.execute();


    }

    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    public class AsynchAllDeviceData extends AsyncTask<String, String, String> {

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
                    .url(URLs.EARNINGS)
                    .addHeader("Authorization", "Token token=" + tokenData)
                    .get()
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {


                    String json_string = response.body().string();
                    Log.e("shani", "Total Earnings get response suuccessfull ====  : " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);

 /*{"earning": {"rating": 3,"download_data": 1081.85,  "upload_data": 138.9,   "connections": 7,   "total_earnings": 375,   "monthly_earning": [
      {"month": "04","earning": 375}*/
                    JSONObject jsonObject = jsonobj.getJSONObject("earning");


                    st_ratings = jsonObject.getString("rating");
                    st_downdata = jsonObject.getString("download_data");
                    st_updata = jsonObject.getString("upload_data");
                    st_connected = jsonObject.getString("connections");
                    st_total_money = jsonObject.getString("total_earnings");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updata.setText(st_updata+" GB");
                            downdata.setText(st_downdata+" GB");
                            ratings.setText(st_ratings+" /5");
                            connected.setText(st_connected);
                            total_money.setText("$"+st_total_money+" USD");

                            realm = Realm.getDefaultInstance();
                          RealmResults<RouterGetList> record = realm.where(RouterGetList.class).findAll();
                            for (int i = 0; i <record.size() ; i++) {
                                 String cnn = record.get(i).getConnections();
                                routers.setText(cnn);

                            }
                        }
                    });



                    JSONArray jsonArray = jsonObject.getJSONArray("monthly_earning");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        month = jsonObject1.getString("month");
                        earning = jsonObject1.getString("earning");

/*
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_name);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_ssid);
                        Log.e("shani", "Lender wifis post response strings ====  : " + jadd_address);*/



                        // store json response in database

                       /* realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
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
                        realm.commitTransaction();


                        //get response for adapter

                        RealmResults<RouterGetList> record = realm.where(RouterGetList.class).findAll();

                        get_name = record.get(i).getName();
                        get_rating = record.get(i).getRating();
                        get_count = record.get(i).getConnections();
                        get_signal = "Good";

                        String rating = get_rating+"/5";
                        Log.i("tag", "making array list foir adapoter" + get_name);*/





                    }

                } else {
                    Log.e("shani", "Total Earnings get response unsuccessful : " + response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "Total Earnings get response failure exception e  : " + e.toString());
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
