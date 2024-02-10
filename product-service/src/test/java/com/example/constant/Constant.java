package com.example.constant;

public class Constant {

    public static final Long ID = 1l;
    public static final String NAME = "Test name";
    public static final String WRONG_NAME = "Wrong_Name";
    public static final Double PRICE = 1234.4321;
    public static final String MANUFACTURER = "Test Manufacturer Name";

    private Constant() {
        throw new RuntimeException("A constant class cannot have instances");
    }
}
