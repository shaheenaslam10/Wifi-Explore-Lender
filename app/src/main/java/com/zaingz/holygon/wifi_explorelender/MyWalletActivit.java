package com.zaingz.holygon.wifi_explorelender;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Adapters.MonthListAdapter;
import com.zaingz.holygon.wifi_explorelender.Database.WalletDataBase;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyWalletActivit extends AppCompatActivity implements MyInterface, MyWalletActivity {


    RecyclerView recyclerView;
    MonthListAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> routerDatas = new ArrayList<>();
    TextView bank_info, withdraw, bank_send, curr_balance, income, outcome, circleMonth;
    Realm realm;
    String tokenData;
    EditText bank_name, bank_currency, bank_country, bank_rout, bank_accnumber, amount;
    String st_bank_name, st_bank_currency, st_bank_country, st_bank_rout, st_bank_accnumber, st_amount, balance;
    CustomDialogClass cdd;
    CustomDialogWithdraw cdw;
    int state = 0, check = 0;
    String month, cr_blnc;
    String temEEarn, temDEarn;
    ProgressDialog dialog;
    private CircleProgressView mCircleProgressView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        dialog = ProgressDialog.show(MyWalletActivit.this, "", "Please wait...", true);


        curr_balance = (TextView) findViewById(R.id.currentBlnce);
        withdraw = (TextView) findViewById(R.id.withdraw_payment);
        bank_info = (TextView) findViewById(R.id.bank_info);


        income = (TextView) findViewById(R.id.income);
        outcome = (TextView) findViewById(R.id.outcome);
        circleMonth = (TextView) findViewById(R.id.circleMonth);
        bank_info = (TextView) findViewById(R.id.bank_info);
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
        bank_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                state = 1;
                cdd = new CustomDialogClass(MyWalletActivit.this);
                Window window = cdd.getWindow();
                window.setGravity(Gravity.CENTER);
                cdd.show();


                bank_name = (EditText) cdd.findViewById(R.id.tv_bank_name);
                bank_country = (EditText) cdd.findViewById(R.id.tv_bank_country);
                bank_currency = (EditText) cdd.findViewById(R.id.tv_bank_currency);
                bank_rout = (EditText) cdd.findViewById(R.id.tv_bank_routnumber);
                bank_accnumber = (EditText) cdd.findViewById(R.id.tv_bank_accnumber);
                bank_send = (TextView) cdd.findViewById(R.id.tv_back_ok);


            }
        });
        state = 0;

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                state = 2;
                cdw = new CustomDialogWithdraw(MyWalletActivit.this);
                Window window = cdw.getWindow();
                window.setGravity(Gravity.CENTER);
                cdw.show();


                amount = (EditText) cdw.findViewById(R.id.tv_withdraw_amount);

            }
        });


        state = 0;


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mCircleProgressView3 = (CircleProgressView) findViewById(R.id.circle_progress_view3);
        mCircleProgressView3.setTextEnabled(false);
        mCircleProgressView3.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView3.setStartAngle(45);
        mCircleProgressView3.setProgressWithAnimation(0, 2000);


        routerDatas.add("January");
        routerDatas.add("February");
        routerDatas.add("March");
        routerDatas.add("April");
        routerDatas.add("May");
        routerDatas.add("June");
        routerDatas.add("July");
        routerDatas.add("August");
        routerDatas.add("September");
        routerDatas.add("October");
        routerDatas.add("November");
        routerDatas.add("December");

        recyclerAdapter = new MonthListAdapter(routerDatas, getApplicationContext(), this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);


        AsynchbankInfo asynchbankInfo = new AsynchbankInfo();
        asynchbankInfo.execute();
        dialog.show();

    }

    @Override
    public void sendToAdapter(String month) {
        Log.e("shani", "sendtomonth  called : ");
        mCircleProgressView3.setProgressWithAnimation(0, 2000);
        this.month = month;
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        realm = Realm.getDefaultInstance();
        RealmResults<WalletDataBase> record = realm.where(WalletDataBase.class).findAll();
        for (int j = 0; j < months.length; j++) {
            if (month.equals(months[j])) {
                circleMonth.setText(months[j]);
                int flag = 0;
                check = j + 1;
                for (int i = 0; i < record.size(); i++) {

                    try {
                        if ((record.get(i).getEmonth()).equals("0" + check)) {

                            String mnth = record.get(i).getEmonth();
                            temEEarn = record.get(i).getEearning();
                            temDEarn = record.get(i).getDearning();
                            income.setText("$ " + temEEarn + " USD");
                            outcome.setText("$ " + temDEarn + " USD");
                            Log.e("shani", " record Month earning  : " + mnth);
                            Log.e("shani", " record Month earning  : " + record.get(i).getEearning());
                            Log.e("shani", "record Month Withdraw : " + record.get(i).getDearning());
                            flag = 1;
                            i = record.size();

                            float e = Float.parseFloat(temEEarn);
                            float d = Float.parseFloat(temDEarn);
                            float ed = e + d;
                            float progress = (e / ed) * 100;
                            mCircleProgressView3.setProgressWithAnimation(progress, 2000);
                        }
                    } catch (NullPointerException e) {
                        Log.e("shani", "null exception   if((record.get(i).getEmonth()).equals( \"0\"+check))...." + e.toString());
                    }
                }
                if (flag == 0) {
                    income.setText("$ 0.0 USD");
                    outcome.setText("$ 0.0 USD");
                }
            }

        }


    }

    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }


    public class AsynchbankInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            realm = Realm.getDefaultInstance();
            WifiLenderData wifiLenderData = realm.where(WifiLenderData.class).findFirst();
            tokenData = wifiLenderData.getToken();
            realm.close();

            //for increase time to get response from server
            OkHttpClient client;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
           /* builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);*/
            client = builder.build();

            /// bank info start
            if (state == 1) {

                RequestBody requestBody = new FormBody.Builder()
                        .add("lender[currency]", st_bank_currency)
                        .add("lender[country]", st_bank_country)
                        .add("lender[name]", st_bank_name)
                        .add("lender[routing_number]", st_bank_rout)
                        .add("lender[account_number]", st_bank_accnumber)
                        .build();

                Request request = new Request.Builder()
                        .url(URLs.SET_BANK_INFO)
                        .addHeader("Authorization", "Token token=" + tokenData)
                        .post(requestBody)
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {


                        String json_string = response.body().string();
                        Log.e("shani", "Bank post response suuccessfull ====  : " + json_string);

                    } else {
                        Log.e("shani", "Bank post response unsuccessful : " + response.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("shani", "Bank post response failure exception e  : " + e.toString());
                }
            }
            /// bank info end


            ///// withdraw money start
            if (state == 2) {

                RequestBody requestBody = new FormBody.Builder()
                        .add("lender[amount]", st_amount)
                        .build();

                Request request = new Request.Builder()
                        .url(URLs.WITHDRAW)
                        .addHeader("Authorization", "Token token=" + tokenData)
                        .post(requestBody)
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {


                        String json_string = response.body().string();
                        Log.e("shani", "Withdraw post response suuccessfull ====  : " + json_string);

                    } else {
                        Log.e("shani", "Withdraw post response unsuccessful : " + response.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("shani", "Withdraw post response failure exception e  : " + e.toString());
                }
            }
            ///// withdraw money end


            Request request = new Request.Builder()
                    .url(URLs.WALLET)
                    .addHeader("Authorization", "Token token=" + tokenData)
                    .get()
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {


                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    WalletDataBase walletDataBase = realm.createObject(WalletDataBase.class);

                    // {"wallet":{"earning":[],"withdraw":[],"balance":0}}

                    String json_string = response.body().string();
                    Log.e("shani", "Wallet get response suuccessfull ====  : " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);
                    JSONObject wallet = jsonobj.getJSONObject("wallet");

                    balance = wallet.getString("balance");

                    if (!balance.isEmpty()) {
                        walletDataBase.setBalance(balance);
                    } else {
                        walletDataBase.setBalance("$ 0.0 USD");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String temblnc = balance;
                            curr_balance.setText("$ " + temblnc + " USD");
                            dialog.dismiss();
                        }
                    });


                    JSONArray earning = wallet.getJSONArray("earning");

                    for (int i = 0; i < earning.length(); i++) {

                        JSONObject earningObj = earning.getJSONObject(i);
                        final String emonth = earningObj.getString("month");
                        final String eearning = earningObj.getString("earning");

                        Log.e("shani", "record Month earning  jsonparsing : " + emonth);
                        Log.e("shani", "record Month earning jsonparsing : " + eearning);
                        if (!emonth.isEmpty() && !eearning.isEmpty()) {
                            walletDataBase.setEmonth(emonth);

                            walletDataBase.setEearning(eearning);

                            if (emonth.equals("01")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        income.setText("$ " + eearning + " USD");
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        income.setText("$ 0.0 USD");
                                    }
                                });
                            }
                            Log.e("shani", "record Month earning  jsonparsing : " + emonth);
                            Log.e("shani", "record Month earning jsonparsing : " + eearning);
                        }

                    }
                    JSONArray withdraw = wallet.getJSONArray("withdraw");
                    for (int i = 0; i < withdraw.length(); i++) {

                        JSONObject withdrawObj = withdraw.getJSONObject(i);

                        String dmonth = withdrawObj.getString("month");
                        final String dearning = withdrawObj.getString("earning");
                        if (!dearning.isEmpty()) {
                            walletDataBase.setDmonth(dmonth);
                            walletDataBase.setDearning(dearning);
                            if (dmonth.equals("01")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        outcome.setText("$ " + dearning + " USD");
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        outcome.setText("$ 0.0 USD");
                                    }
                                });
                            }
                        }
                        Log.e("shani", "record Month withdraw  jsonparsing : " + dmonth);
                        Log.e("shani", "record Month Withdraw jsonparsing : " + dearning);

                    }
                    realm.commitTransaction();
                    realm.close();


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


    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.bank_info);
            yes = (TextView) findViewById(R.id.tv_back_ok);
            yes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_back_ok:

                    st_bank_name = bank_name.getText().toString();
                    st_bank_currency = bank_currency.getText().toString();
                    st_bank_country = bank_country.getText().toString();
                    st_bank_rout = bank_rout.getText().toString();
                    st_bank_accnumber = bank_accnumber.getText().toString();

                    if (st_bank_name.length() < 1) {

                        Toast.makeText(getApplicationContext(), "Please Enter Bank Name", Toast.LENGTH_SHORT).show();
                    } else if (st_bank_currency.length() < 1) {

                        Toast.makeText(getApplicationContext(), "Please specify Currency", Toast.LENGTH_SHORT).show();
                    } else if (st_bank_country.length() < 1) {

                        Toast.makeText(getApplicationContext(), "Please specify Country Name", Toast.LENGTH_SHORT).show();
                    } else if (st_bank_rout.length() < 1) {

                        Toast.makeText(getApplicationContext(), "Please Enter Rout", Toast.LENGTH_SHORT).show();
                    } else if (st_bank_accnumber.length() < 1) {

                        Toast.makeText(getApplicationContext(), "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                    } else {

                        AsynchbankInfo asynchbankInfo = new AsynchbankInfo();
                        asynchbankInfo.execute();
                        Toast.makeText(getApplicationContext(), "Successfuly Submitted", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }

                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public class CustomDialogWithdraw extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes;

        public CustomDialogWithdraw(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.withdrawpayment);
            yes = (TextView) findViewById(R.id.tv_withdraw_submit);
            yes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_withdraw_submit:


                    realm = Realm.getDefaultInstance();
                    RealmResults<WalletDataBase> record = realm.where(WalletDataBase.class).findAll();
                    for (int i = 0; i < record.size(); i++) {
                        cr_blnc = record.get(i).getBalance();
                    }

                    st_amount = amount.getText().toString();

                    double amnt = Double.parseDouble(st_amount);
                    double crblnc = Double.parseDouble(cr_blnc);


                    if (amnt < 10) {

                        Toast.makeText(getApplicationContext(), "Minimum Amount Should $10", Toast.LENGTH_SHORT).show();
                    } else if (amnt >= crblnc) {

                        Toast.makeText(getApplicationContext(), "You Have Only $" + cr_blnc + " USD", Toast.LENGTH_SHORT).show();
                    } else {

                        AsynchbankInfo asynchbankInfo = new AsynchbankInfo();
                        asynchbankInfo.execute();
                        Toast.makeText(getApplicationContext(), "Successfuly Submitted", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                    realm.close();


                    break;
                default:
                    break;
            }

        }
    }


}

