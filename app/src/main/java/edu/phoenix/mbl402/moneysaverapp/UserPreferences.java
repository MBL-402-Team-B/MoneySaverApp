package edu.phoenix.mbl402.moneysaverapp;

public class UserPreferences {

    // Static class data
    private static int backgroundColor;
    private static boolean saveBattery;


    public static int getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(int backgroundColor) {
        UserPreferences.backgroundColor = backgroundColor;
    }

    public static boolean isSaveBattery() {
        return saveBattery;
    }

    public static void setSaveBattery(boolean saveBattery) {
        UserPreferences.saveBattery = saveBattery;
    }





}
