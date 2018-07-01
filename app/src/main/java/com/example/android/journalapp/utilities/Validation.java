package com.example.android.journalapp.utilities;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by JAHSWILL on 4/14/2018.
 */

public class Validation {

    public static boolean validateFields(String name){

        return !TextUtils.isEmpty(name);
    }

    public static boolean validateEmail(String string) {

        return !(TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches());
    }
}
