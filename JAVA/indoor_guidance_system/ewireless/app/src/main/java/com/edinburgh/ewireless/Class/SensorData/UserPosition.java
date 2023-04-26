package com.edinburgh.ewireless.Class.SensorData;

import android.util.Log;

import java.io.Serializable;

/**
 * Author: yijianzheng
 * Date: 11/04/2023 12:27
 * <p>
 * Notes:
 */
public class UserPosition implements Serializable {
    private static double longitude;    // Longitude
    private static double latitude;    // latitude
    private static int floor;          // Current floor
    private static float accuracy;     // How does the position accuracy
    private static String provider;    // How to determine the position?
    private static float speed;
    private static long timeStamp;

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        UserPosition.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        UserPosition.latitude = latitude;
    }

    public static int getFloor() {
        return floor;
    }

    public static void setFloor(int floor) {
        UserPosition.floor = floor;
    }

    public static float getAccuracy() {
        return accuracy;
    }

    public static void setAccuracy(float accuracy) {
        UserPosition.accuracy = accuracy;
    }

    public static String getProvider() {
        return provider;
    }

    public static void setProvider(String provider) {
        UserPosition.provider = provider;
    }

    public static float getSpeed() {
        return speed;
    }

    public static void setSpeed(float speed) {
        UserPosition.speed = speed;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        UserPosition.timeStamp = timeStamp;
    }
}
