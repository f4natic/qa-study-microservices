package com.example.utils;

import java.util.regex.Pattern;

public class DataValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
