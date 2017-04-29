package com.zaingz.holygon.wifi_explorelender;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ProfileActivity extends AppCompatActivity {

    EditText edit_pass,first_pass,second_pass;
    String st_first_pass,st_second_pass;
    CustomDialogChangePass cdcp;
    Realm realm;
    String tokenData;
    TextView name,email,mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        name = (TextView)findViewById(R.id.txt_profile_name);
        email = (TextView)findViewById(R.id.txt_profile_mail);
        mobile = (TextView)findViewById(R.id.txt_profile_mobile);

        realm = Realm.getDefaultInstance();
        RealmResults<WifiLenderData> record = realm.where(WifiLenderData.class).findAll();
        for (int i = 0; i < record.size(); i++) {
            name.setText(record.get(i).getName());
            email.setText(record.get(i).getEmail());
            mobile.setText(record.get(i).getMobile_number());
        }





        edit_pass = (EditText)findViewById(R.id.profile_edit);

        edit_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edit_pass.getRight() - edit_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here



                        cdcp = new CustomDialogChangePass(ProfileActivity.this);
                        Window window = cdcp.getWindow();
                        window.setGravity(Gravity.CENTER);
                        cdcp.show();

                        first_pass = (EditText)cdcp.findViewById(R.id.ed_first_pass);
                        second_pass = (EditText)cdcp.findViewById(R.id.ed_second_pass);




                    }
                }
                return false;
            }
        });



    }
    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }


    public class AsynchTaskEditPass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            realm = Realm.getDefaultInstance();

            if (realm.where(WifiLenderData.class).count() > 0) {

                RealmResults<WifiLenderData> record = realm.where(WifiLenderData.class).findAll();
                for (int i = 0; i < record.size(); i++) {
                    tokenData = record.get(i).getToken();
                }
            }

            Log.e("shani", "In do in background " );

            OkHttpClient client;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);

            client = builder.build();

            RequestBody requestBody = new FormBody.Builder()

                    .add("lender[old_password]",st_first_pass)
                    .add("lender[password]",st_second_pass)
                    .build();

            Request request = new Request.Builder()
                    .url(URLs.PASSWORD)
                    .put(requestBody)
                    .addHeader("Authorization", "Token token=" + tokenData)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {

                    String obj = response.body().string();
                    Log.d("shani", "Response successfulll )))))" + obj);

                }
                else{
                    Log.e("shani", "change pass put response unsuccessful : "+response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "change pass put response failure: " + e.toString());
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
    public class CustomDialogChangePass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes;

        public CustomDialogChangePass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.changepass);
            yes = (TextView) findViewById(R.id.tv_change_pass);
            yes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_change_pass:



                    st_first_pass = first_pass.getText().toString();
                    st_second_pass = second_pass.getText().toString();

                    if (first_pass.getText().toString().length() < 1) {
                        Toast.makeText(ProfileActivity.this, "Please Enter First Password", Toast.LENGTH_SHORT).show();
                    }else if (second_pass.getText().toString().length() < 1) {
                        Toast.makeText(ProfileActivity.this, "Please Enter Second Password", Toast.LENGTH_SHORT).show();
                    } else {


                        AsynchTaskEditPass asynchTaskEditPass = new AsynchTaskEditPass();
                        asynchTaskEditPass.execute();
                        Toast.makeText(getApplicationContext(), "Your Are Logged out", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }



                    break;
                default:
                    break;
            }

        }
    }
}
