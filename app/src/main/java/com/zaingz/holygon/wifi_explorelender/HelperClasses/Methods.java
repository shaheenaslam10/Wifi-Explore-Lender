package com.zaingz.holygon.wifi_explorelender.HelperClasses;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Muhammad Shan on 21/02/2017.
 */

public class Methods {

    static double  twoDigits;


    public Methods() {
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        double lat_extar,lng_extar;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address != null || !address.equals("")) {

                Address location = address.get(0);
                lat_extar =  location.getLatitude();
                lng_extar =  location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            }
            else {
                return null;
            }



            Log.e("shani","method lat from address : "+lat_extar);
            Log.e("shani","method lng from address : "+lng_extar);



        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public  static double roundTwoDecimal(double value ){
        DecimalFormat df = new DecimalFormat("#.##");
        twoDigits = Double.valueOf(df.format(value));
        return  twoDigits;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
