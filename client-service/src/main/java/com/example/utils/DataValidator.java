package com.example.utils;

import java.util.regex.Pattern;

public class DataValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]{1,20}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+\\d{11}");

    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public static boolean validateName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }
    public static boolean validatePhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
