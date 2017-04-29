package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zaingz.holygon.wifi_explorelender.Database.WifiLenderData;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    Intent mainIntent=null;
    private Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                realm = Realm.getDefaultInstance();
                if (realm.where(WifiLenderData.class).count() > 0) {

                    mainIntent = new Intent(getApplicationContext(), MainActivity.class);

                }
                else {
                    mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                }

                realm.close();
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
