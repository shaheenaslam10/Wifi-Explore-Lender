package com.zaingz.holygon.wifi_explorelender.Valoidators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shani on 4/14/2017.
 */

public class Validations {

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
