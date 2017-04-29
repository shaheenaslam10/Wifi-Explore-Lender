package com.zaingz.holygon.wifi_explorelender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.zaingz.holygon.wifi_explorelender.Database.RouterGetList;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyDevicesActivity extends AppCompatActivity {

    Realm realm;
    String tokenDataf;
    TextView updata, downdata, ratings, connected, routers, total_money, toolbar_device_count, toolTitle;
    String st_updata, st_downdata, st_ratings, st_connected, st_total_money;
    String month, earning;
    String device_count;
    ProgressDialog dialog;
    String idd;
    GraphView graph;
    String[] months;
    String[] earnings;
    LineGraphSeries<DataPoint> series;
    DataPoint v;
    int count = 12;
    DataPoint[] values;
    StaticLabelsFormatter staticLabelsFormatter;
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
        dialog = ProgressDialog.show(MyDevicesActivity.this, "", "Please wait...", true);

        toolTitle = (TextView) findViewById(R.id.toll);
        updata = (TextView) findViewById(R.id.up_data);
        downdata = (TextView) findViewById(R.id.down_data);
        ratings = (TextView) findViewById(R.id.rating);
        connected = (TextView) findViewById(R.id.connected);
        routers = (TextView) findViewById(R.id.routers);
        total_money = (TextView) findViewById(R.id.total_money);
        toolbar_device_count = (TextView) findViewById(R.id.toolbar_device_count);


        Intent intent = getIntent();
        String intntrouterName = intent.getStringExtra("namrouter");
        String intRouterIdList = intent.getStringExtra("idrouterlist");
        String intRouterIdgrid = intent.getStringExtra("idroutergrid");
        toolTitle.setText(intntrouterName);

        if (intRouterIdList != null) {
            idd = intRouterIdList;
        } else {
            idd = intRouterIdgrid;
        }


        graph = (GraphView) findViewById(R.id.graph);


        staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"$0", "$10", "$20", "$30", "$40", "$50", "$60", "$70", "$70", "$80", "$90", "$100"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        values = new DataPoint[12];


       /* graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);*/


        // custom label formatter to show currency "EUR"
       /* graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueY) {
                if (isValueY) {
                    // show normal x values
                    return super.formatLabel(value, isValueY);
                } else {
                    // show currency for y values
                    return  "$ "+super.formatLabel(value, isValueY);
                }
            }
        });
*/


        AsynchAllDeviceData asynchAllDeviceData = new AsynchAllDeviceData();
        asynchAllDeviceData.execute();
        dialog.show();


        // use static labels for horizontal and vertical labels


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
            WifiLenderData record = realm.where(WifiLenderData.class).findFirst();
            tokenDataf = record.getToken();
            realm.close();

            Log.e("shani", "in mY device activity toekn" + tokenDataf);


            OkHttpClient client;
            //for increase time to get response from server
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            client = builder.build();


            Log.e("shani", "id >........" + idd);
            Request request = new Request.Builder()
                    .url("https://wifiexplore.herokuapp.com/api/lender/wifis/" + idd + ".json")
                    .addHeader("Authorization", "Token token=" + tokenDataf)
                    //   .addHeader("Authorization", "Token token=" + tokenDataf)
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


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                        }
                    });




                    st_ratings = jsonObject.getString("rating");
                    st_downdata = jsonObject.getString("download_data");
                    st_updata = jsonObject.getString("upload_data");
                    st_connected = jsonObject.getString("connections");
                    st_total_money = jsonObject.getString("total_earnings");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updata.setText(st_updata + " GB");
                            downdata.setText(st_downdata + " GB");
                            ratings.setText(st_ratings + " /5");
                            connected.setText(st_connected);
                            total_money.setText("$" + st_total_money + " USD");
                            toolbar_device_count.setText(st_connected);

                            realm = Realm.getDefaultInstance();
                            RealmResults<RouterGetList> record = realm.where(RouterGetList.class).findAll();
                            for (int i = 0; i < record.size(); i++) {
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

                        Log.e("shani", "after response  month values :..." + month);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                String[] monthssss = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
                                for (int j = 0; j < monthssss.length; j++) {
                                    Log.e("shani", "for loop j count  ..." + j);
                                    Log.e("shani", "for loop monthssss[j]  ..." + monthssss[j] + "=====" + month);
                                    if (monthssss[j].equals(month)) {
                                        //  Float d = Float.parseFloat(month);
                                        Log.e("shani", "month  natched...");
                                        Float m = Float.parseFloat(earning);
                                        v = new DataPoint(j + 1, m);
                                        Log.e("shani", "data point v ..." + v);

                                        values[j] = v;

                                        Log.e("shani", "data point vlaues ..." + values[j]);
                                        Log.e("shani", "series........" + series);
                                        Log.e("shani", "j........" + j);
                                    }

                                }

                            }
                        });


                        Log.e("shani", "month of response.." + month);

                          /*  months[i]=month;
                            earnings[i]=earning;
*/

                       /* final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                staticLabelsFormatter = new StaticLabelsFormatter(graph);
                                //staticLabelsFormatter.setHorizontalLabels(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
                                staticLabelsFormatter.setHorizontalLabels(months);
                                staticLabelsFormatter.setVerticalLabels(earnings);
                                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


                                double d = (double) finalI;
                                Double m = Double.parseDouble(month);
                                series = new LineGraphSeries<>(new DataPoint[] {

                                        new DataPoint(d, m),


                                });

                            }
                        });*/

                    }
                    for (int k = 0; k < 12; k++) {
                        if (values[k] == null) {
                            v = new DataPoint(k + 1, 0);
                            values[k] = v;
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            series = new LineGraphSeries<DataPoint>(values);
                            Log.e("shani", "seriesin UI thread series = new LineGraphSeries<>(values);........" + series.toString());
                            graph.addSeries(series);
                            series.setColor(getResources().getColor(R.color.green_light));

                            series.setDataPointsRadius(10);

                        }
                    });

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
