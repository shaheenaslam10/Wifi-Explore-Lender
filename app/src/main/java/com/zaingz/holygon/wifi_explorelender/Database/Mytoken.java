package com.zaingz.holygon.wifi_explorelender.Database;

import io.realm.RealmObject;

/**
 * Created by Shani on 4/27/2017.
 */

public class Mytoken extends RealmObject {
    String fToken;

    public Mytoken() {
    }

    public String getfToken() {
        return fToken;
    }

    public void setfToken(String fToken) {
        this.fToken = fToken;
    }
}
