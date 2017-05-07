package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.zaingz.holygon.wifi_explorelender.API.URLs;
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

public class SignupActivity extends AppCompatActivity {

    EditText edit_name,edit_email, edit_mobile, edit_password;
    String st_edit_name, st_edit_email, st_edit_mobile, st_edit_password;
    ImageView btn_login;
    String jname,jemail,jmobile_number,jemail_verified,jmobile_number_verified,jblocked,jtoken;
    Realm realm;
    CircularProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edit_name = (EditText) findViewById(R.id.edt_name);
        edit_email = (EditText) findViewById(R.id.editText_emailsignup);
        edit_mobile = (EditText) findViewById(R.id.editText_mobilesignup);
        edit_password = (EditText) findViewById(R.id.editText_passwordsignup);
        btn_login = (ImageView) findViewById(R.id.btn_loginsignup);

        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setVisibility(View.INVISIBLE);



        edit_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (edit_password.getRight() - edit_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edit_password.getRight() - edit_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        return true;
                    }
                }
                return false;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validations.checkConnection(getApplicationContext())) {
                    if (edit_name.getText().toString().length() < 1) {

                        Toast.makeText(SignupActivity.this, "Name can't be Empty", Toast.LENGTH_SHORT).show();
                    } else if (edit_email.getText().toString().length() < 1) {
                        Toast.makeText(SignupActivity.this, "Email can't be Empty", Toast.LENGTH_SHORT).show();
                    } else if (!(Validations.isEmailValid(edit_email.getText().toString()))) {
                        edit_email.setText(null);
                        Toast.makeText(SignupActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                    } /*else if (pass.getText().toString().length() < 1) {
                    Processing.myToast(getApplicationContext(), " Password can't be empty");
                } */ else if (edit_mobile.getText().toString().length() < 1) {
                        Toast.makeText(SignupActivity.this, "Mobile Number can't be Empty", Toast.LENGTH_SHORT).show();
                    } /*else if (!(phoneNumber.matches("[+][0-9]+"))) {
                    Processing.myToast(getApplicationContext(), "Number format not corrected");
                } */ else if (edit_password.getText().toString().length() < 1) {
                        Toast.makeText(SignupActivity.this, "Please enter password...", Toast.LENGTH_SHORT).show();
                    }
                    else if(edit_password.getText().toString().length() < 6 ) {
                        Toast.makeText(SignupActivity.this, "Password is short, minimum 6 characters", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.e("shani", "In else part: ");

                        btn_login.setVisibility(View.INVISIBLE);
                        progressView.setVisibility(View.VISIBLE);

                        st_edit_name = edit_name.getText().toString();
                        st_edit_email = edit_email.getText().toString();
                        st_edit_mobile = edit_mobile.getText().toString();
                        st_edit_password = edit_password.getText().toString();

                        AsynchTaskDownload asynchTaskDownload = new AsynchTaskDownload();
                        asynchTaskDownload.execute();

                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
    @Override
    protected void onPause() {

        overridePendingTransition(R.animator.pull_in_right, R.animator.puch_out_left);
        super.onPause();
    }

    public class AsynchTaskDownload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            Log.e("shani", "In do in background " );

            OkHttpClient client;

            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            client = builder.build();

            RequestBody requestBody = new FormBody.Builder()
                    .add("lender[name]", st_edit_name)
                    .add("lender[email]", st_edit_email)
                    .add("lender[mobile_number]", st_edit_mobile)
                    .add("lender[password]", st_edit_password)
                    .build();

            Request request = new Request.Builder()
                    .url(URLs.LENDER_SIGNUP)
                    .post(requestBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {

                    String json_string = response.body().string();

                    Log.e("shani", "post response signup  : " + json_string);
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

                    if (realm.where(WifiLenderData.class).count() > 0) {
                        realm.beginTransaction();
                        realm.clear(WifiLenderData.class);
                        realm.commitTransaction();
                        Log.e("shani", "All data deleted : ");

                    }


                    realm.beginTransaction();
                    WifiLenderData signupData = realm.createObject(WifiLenderData.class);
                    signupData.setName(jname);
                    signupData.setEmail(jemail);
                    signupData.setMobile_number(jmobile_number);
                    signupData.setEmail_verified(jemail_verified);
                    signupData.setMobile_number_verified(jmobile_number_verified);
                    signupData.setBlocked(jblocked);
                    signupData.setToken(jtoken);
                    realm.commitTransaction();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           // Toast.makeText(SignupActivity.this, "Kindly Sign In", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                           finish();
                           startActivity(intent);
                       }
                   });


                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_login.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Response Unsuccessful...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("shani", "post response unsuccessful : " + response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("shani", "post response failure: " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_login.setVisibility(View.VISIBLE);
                        progressView.setVisibility(View.INVISIBLE);
                    }
                });
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

