package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaingz.holygon.wifi_explorelender.API.URLs;
import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;
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

    TextView txt_signup;
    ImageView btn_login;
    Intent mainIntent;
    String jname,jemail,jmobile_number,jemail_verified,jmobile_number_verified,jblocked,jtoken;
    Realm realm;

    String st_email, st_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.editText_emailsigniin);
        password = (EditText) findViewById(R.id.editText_passwordsigniin);



        txt_signup= (TextView) findViewById(R.id.txt_signup);
        btn_login= (ImageView) findViewById(R.id.btn_login);




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if (email.getText().toString().length() < 1) {
                        Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    } else if (!(Validations.isEmailValid(email.getText().toString()))) {
                        Toast.makeText(LoginActivity.this, "Email is not Valid", Toast.LENGTH_SHORT).show();
                    } else if (password.getText().toString().length() < 1) {
                        Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    } else {

                        st_email = email.getText().toString();
                        st_password = password.getText().toString();

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


    public class AsynchTaskDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            Log.e("shani", "In do in background " );

            OkHttpClient client = new OkHttpClient();

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

                    Log.e("shani", "post response: " + response.body().toString());

                    String json_string = response.body().toString();

                    Log.e("shani", "json string: " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);
                    JSONObject jsonobjChild = jsonobj.getJSONObject("lender");

                    jname = jsonobjChild.getString("name");
                    jemail = jsonobjChild.getString("email");
                    jmobile_number = jsonobjChild.getString("mobile_number");
                    jemail_verified = jsonobjChild.getString("email_verified");
                    jmobile_number_verified = jsonobjChild.getString("mobile_number_verified");
                    jblocked = jsonobjChild.getString("blocked");
                    jtoken = jsonobjChild.getString("token");


                    realm = Realm.getDefaultInstance();

                    if (realm.where(SignUpDatabase.class).count() > 0) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                    }



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


                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    finish();
                    startActivity(intent);




                }
                else{
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
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
