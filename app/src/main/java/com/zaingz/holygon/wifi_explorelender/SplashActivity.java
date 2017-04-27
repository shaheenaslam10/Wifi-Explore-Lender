package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    Intent mainIntent=null;
    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        realm = Realm.getDefaultInstance();


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (realm.where(SignUpDatabase.class).count() > 0) {

                    mainIntent = new Intent(getApplicationContext(), MainActivity.class);

                }
                else {
                    mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                }

                startActivity(mainIntent);

            }
        },2000);

    }
    @Override
    protected void onPause() {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }
}
