package com.zaingz.holygon.wifi_explorelender;

        import android.app.Application;

        import io.realm.Realm;
        import io.realm.RealmConfiguration;

/**
 * Created by Shani on 3/20/2017.
 */

public class MyApplication extends Application {
    private  static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("com.zaingz.holygon.wifi_explorelender")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
    public static MyApplication get() {
        return instance;
    }
}
