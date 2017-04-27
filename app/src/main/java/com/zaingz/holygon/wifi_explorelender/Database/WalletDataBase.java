package com.zaingz.holygon.wifi_explorelender.Database;

import io.realm.RealmObject;

/**
 * Created by Shani on 4/21/2017.
 */

public class WalletDataBase extends RealmObject{
    String balance;
    String emonth;
    String eearning;
    String dmonth;
    String dearning;

    public WalletDataBase() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmonth() {
        return emonth;
    }

    public void setEmonth(String emonth) {
        this.emonth = emonth;
    }

    public String getEearning() {
        return eearning;
    }

    public void setEearning(String eearning) {
        this.eearning = eearning;
    }

    public String getDmonth() {
        return dmonth;
    }

    public void setDmonth(String dmonth) {
        this.dmonth = dmonth;
    }

    public String getDearning() {
        return dearning;
    }

    public void setDearning(String dearning) {
        this.dearning = dearning;
    }
}
