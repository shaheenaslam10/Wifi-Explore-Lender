package com.zaingz.holygon.wifi_explorelender.Database;

        import io.realm.RealmObject;

/**
 * Created by Shani on 4/14/2017.
 */

public class RouterGetList extends RealmObject {
    String id;
    String name;
    String ssid;
    String address;
    String security_type;
    String price;
    String avg_speed;
    String latitude;
    String longitude;
    String connections;
    String rating;

    public RouterGetList(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSecurity_type() {
        return security_type;
    }

    public void setSecurity_type(String security_type) {
        this.security_type = security_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvg_speed() {
        return avg_speed;
    }

    public void setAvg_speed(String avg_speed) {
        this.avg_speed = avg_speed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
