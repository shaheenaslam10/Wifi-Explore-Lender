package com.zaingz.holygon.wifi_explorelender;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.Mytoken;
import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;
import com.zaingz.holygon.wifi_explorelender.Valoidators.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    CustomDialogForgot cdw;

    TextView txt_signup, tv_forgot_submit;
    TextView forgot_pass;
    EditText tv_forgot_email;
    Dialog dialog;
    ImageView btn_login;
    Intent mainIntent;
    ProgressDialog progressDialog;
    String jname, jemail, jmobile_number, jemail_verified, jmobile_number_verified, jblocked, jtoken, st_tv_forgot_email;
    Realm realm;

    CircularProgressView progressView;
    String st_email, st_password;
    int turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.editText_emailsigniin);
        password = (EditText) findViewById(R.id.editText_passwordsigniin);
        forgot_pass = (TextView) findViewById(R.id.textView_forgotPass);


        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.INVISIBLE);


        txt_signup = (TextView) findViewById(R.id.txt_signup);
        btn_login = (ImageView) findViewById(R.id.btn_login);


        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        return true;
                    }
                }
                return false;
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cdw = new CustomDialogForgot(LoginActivity.this);
                Window window = cdw.getWindow();
                window.setGravity(Gravity.CENTER);
                cdw.show();


            }
        });
        turn = 0;


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validations.checkConnection(getApplicationContext())) {
                    if (email.getText().toString().length() < 1) {
                        Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    } else if (!(Validations.isEmailValid(email.getText().toString()))) {
                        Toast.makeText(LoginActivity.this, "Email is not Valid", Toast.LENGTH_SHORT).show();
                    } else if (password.getText().toString().length() < 1) {
                        Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    } else {

                        st_email = email.getText().toString();
                        st_password = password.getText().toString();
                        btn_login.setVisibility(View.INVISIBLE);
                        progressView.setVisibility(View.VISIBLE);
                        turn = 0;


                        AsynchTaskDownload asynchData = new AsynchTaskDownload();
                        asynchData.execute();

                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(mainIntent);
            }
        });
    }


    @Override
    protected void onPause() {

        overridePendingTransition(R.animator.pull_in_left, R.animator.puch_out_right);
        super.onPause();
    }

    public class AsynchTaskDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.clear(WifiLenderData.class);
            realm.commitTransaction();
            realm.close();


            Log.e("shani", "In do in background ");

            OkHttpClient client;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
       /*     builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);*/

            client = builder.build();

            if (turn == 0) {

                RequestBody requestBody = new FormBody.Builder()

                        .add("lender[email]", st_email)
                        .add("lender[password]", st_password)
                        .build();

                Request request = new Request.Builder()
                        .url(URLs.LENDER_SIGNIN)
                        .post(requestBody)
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {

                        String obj = response.body().string();
                        Log.d("shani", "Response successfulll )))))" + obj);

                        JSONObject jsonObject = new JSONObject(obj);
                        JSONObject jsonobjChild = jsonObject.getJSONObject("lender");

                        jname = jsonobjChild.getString("name");
                        jemail = jsonobjChild.getString("email");
                        jmobile_number = jsonobjChild.getString("mobile_number");
                        jemail_verified = jsonobjChild.getString("email_verified");
                        jmobile_number_verified = jsonobjChild.getString("mobile_number_verified");
                        jblocked = jsonobjChild.getString("blocked");
                        jtoken = jsonobjChild.getString("token");


                        Log.e("shani", "Token*&*****************" + jtoken);


                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();

                        WifiLenderData signupData = realm.createObject(WifiLenderData.class);
                        Mytoken mytoken = realm.createObject(Mytoken.class);
                        mytoken.setfToken(jtoken);
                        signupData.setName(jname);
                        signupData.setEmail(jemail);
                        signupData.setMobile_number(jmobile_number);
                        signupData.setEmail_verified(jemail_verified);
                        signupData.setMobile_number_verified(jmobile_number_verified);
                        signupData.setBlocked(jblocked);
                        signupData.setToken(jsonobjChild.getString("token").toString());
                        realm.commitTransaction();
                        realm.close();


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);

             /*       if (realm.where(WifiLenderData.class).count()>0){

                        RealmResults<WifiLenderData> record = realm.where(WifiLenderData.class).findAll();
                        for (int i = 0; i < record.size(); i++) {
                            if(record.get(i).getToken()!= null){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "token not found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }*/


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_login.setVisibility(View.VISIBLE);
                                progressView.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                                email.setText(null);
                                password.setText(null);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("shani", "post response failure: " + e.toString());
                    Toast.makeText(LoginActivity.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_login.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.INVISIBLE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("shani", "Json Error  : " + e.toString());
                }
            } else if (turn == 1) {

                RequestBody requestBody = new FormBody.Builder()

                        .add("email", st_tv_forgot_email)
                        .build();

                Request request = new Request.Builder()
                        .url(URLs.FORGOT_PASSWORD)
                        .post(requestBody)
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {

                        String obj = response.body().string();
                        Log.d("shani", "Forgot Pass  put Response successfulll )))))" + obj);
                        progressDialog.dismiss();
                        dialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(LoginActivity.this, "Successfully Submit", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Log.d("shani", "Forgot Pass  put Response unsuccessfulll )))))" + response.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(LoginActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                                tv_forgot_email.setText(null);
                                progressDialog.dismiss();

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("shani", "Forgot Pass  put response failure: " + e.toString());
                }
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


    public class CustomDialogForgot extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes;

        public CustomDialogForgot(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.forgot_pass_dialog);
            yes = (TextView) findViewById(R.id.tv_forgot_submit);
            // yes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_forgot_submit:


                    tv_forgot_email = (EditText) dialog.findViewById(R.id.tv_forgot_pass);
                    tv_forgot_submit = (TextView) dialog.findViewById(R.id.tv_forgot_submit);
                    tv_forgot_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tv_forgot_email.getText().toString().length() < 1) {
                                Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                            } else if (!(Validations.isEmailValid(tv_forgot_email.getText().toString()))) {
                                Toast.makeText(LoginActivity.this, "Email is not Valid", Toast.LENGTH_SHORT).show();
                            } else {

                                st_tv_forgot_email = tv_forgot_email.getText().toString();
                                progressDialog = ProgressDialog.show(LoginActivity.this, "", "Please wait...", true);
                                AsynchTaskDownload asynchData = new AsynchTaskDownload();
                                asynchData.execute();

                                turn = 1;

                            }
                        }
                    });

                    break;
                default:
                    break;
            }

        }
    }


}
