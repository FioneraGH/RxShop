package com.centling.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumCheck {
    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
        if (str.length() == 11) {
            try {
                Pattern p = Pattern.compile("^(13|14|15|17|18)[0-9]{9}$");
                Matcher m = p.matcher(str);
                isMobileNo = m.matches();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isMobileNo;
    }

    public static Boolean isPassword(String str) {
        Boolean isPassword = false;

        try {
            Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
            Matcher m = p.matcher(str);
            isPassword = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPassword;
    }
}
