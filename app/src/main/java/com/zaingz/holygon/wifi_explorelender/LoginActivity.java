package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;
import com.zaingz.holygon.wifi_explorelender.Valoidators.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;

    TextView txt_signup;
    ImageView btn_login;
    Intent mainIntent;
    String jname,jemail,jmobile_number,jemail_verified,jmobile_number_verified,jblocked,jtoken;
    Realm realm;
    String tokenData;
    CircularProgressView progressView;
    String st_email, st_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.editText_emailsigniin);
        password = (EditText) findViewById(R.id.editText_passwordsigniin);

        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.INVISIBLE);



        txt_signup= (TextView) findViewById(R.id.txt_signup);
        btn_login= (ImageView) findViewById(R.id.btn_login);


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



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               /* Intent intent = new Intent(LoginActivity.this,AddRouterActivity.class);
                startActivity(intent);*/

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


                        AsynchTaskDownload asynchData = new AsynchTaskDownload();
                        asynchData.execute();

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

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    public class AsynchTaskDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            realm = Realm.getDefaultInstance();

            if (realm.where(SignUpDatabase.class).count() > 0) {
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
            }


            Log.e("shani", "In do in background " );

            OkHttpClient client;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);

            client = builder.build();

            RequestBody requestBody = new FormBody.Builder()

                    .add("lender[email]",st_email)
                    .add("lender[password]",st_password)
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





                    realm.beginTransaction();
                    SignUpDatabase signupData = realm.createObject(SignUpDatabase.class);
                    signupData.setName(jname);
                    signupData.setEmail(jemail);
                    signupData.setMobile_number(jmobile_number);
                    signupData.setEmail_verified(jemail_verified);
                    signupData.setMobile_number_verified(jmobile_number_verified);
                    signupData.setBlocked(jblocked);
                    signupData.setToken(jtoken);
                    realm.commitTransaction();



                    realm = Realm.getDefaultInstance();

                    if (realm.where(SignUpDatabase.class).count()>0){

                        RealmResults<SignUpDatabase> record = realm.where(SignUpDatabase.class).findAll();
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
                    }


                }
                else{
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
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("shani", "Json Error  : " + e.toString());
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
