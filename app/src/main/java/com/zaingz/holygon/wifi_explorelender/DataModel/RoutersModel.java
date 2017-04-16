package com.zaingz.holygon.wifi_explorelender.DataModel;

/**
 * Created by Muhammad Shan on 11/04/2017.
 */

public class RoutersModel {


    String name;
    String signal_strength;
    String rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(String signal_strength) {
        this.signal_strength = signal_strength;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public RoutersModel(String name, String signal_strength, String rating, String devices) {
        this.name = name;
        this.signal_strength = signal_strength;
        this.rating = rating;
        this.devices = devices;
    }

    public RoutersModel() {

    }

    String devices;
}
