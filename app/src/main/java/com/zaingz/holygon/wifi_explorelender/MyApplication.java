package com.zaingz.holygon.wifi_explorelender;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Shani on 3/20/2017.
 */

public class MyApplication extends Application {
    private  static MyApplication instance;

    public static MyApplication get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("com.zaingz.holygon.wifi_explorelender")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
