package com.example.constant;

public class Constant {
    public static final Long ID = 1l;
    public static final String FIRST_NAME = "TestFirstName";
    public static final String LAST_NAME = "TestLastName";
    public static final String EMAIL = "test@test.test";
    public static final String PHONE_NUMBER = "+70000000000";

    private Constant() {
        throw new RuntimeException("A constant class cannot have instances");
    }
}
