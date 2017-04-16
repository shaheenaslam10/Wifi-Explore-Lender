package com.zaingz.holygon.wifi_explorelender.Database;

import io.realm.RealmObject;

/**
 * Created by Shani on 4/14/2017.
 */

public class SignUpDatabase extends RealmObject {
    String name;
    String email;
    String mobile_number;
    String email_verified;
    String mobile_number_verified;
    String blocked;
    String token;

    public SignUpDatabase(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getMobile_number_verified() {
        return mobile_number_verified;
    }

    public void setMobile_number_verified(String mobile_number_verified) {
        this.mobile_number_verified = mobile_number_verified;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
