package com.edinburgh.ewireless.Class.PDR;

import com.edinburgh.ewireless.Class.SensorData.UserPosition;

/**
 * Author: yijianzheng
 * Date: 11/04/2023 12:21
 *
 * Notes: CurrUserPosition is a public class
 *  It records the latitude and longitude for each class
 */
public class CurrUserPosition {
    private static long timeStamp;
    private static double currLongitude;
    private static double currLatitude;
    private static int currFloor;

    public static void init(){
        currLatitude = UserPosition.getLatitude();
        currLongitude = UserPosition.getLongitude();
        timeStamp = System.currentTimeMillis();
        currFloor = 0;
    }

    public static double getCurrLongitude() {
        return currLongitude;
    }

    public static void setCurrLongitude(double currLongitude) {
        CurrUserPosition.currLongitude = currLongitude;
    }

    public static double getCurrLatitude() {
        return currLatitude;
    }

    public static void setCurrLatitude(double currLatitude) {
        CurrUserPosition.currLatitude = currLatitude;
    }

    public static int getCurrFloor() {
        return currFloor;
    }

    public static void setCurrFloor(int currFloor) {
        CurrUserPosition.currFloor = currFloor;
    }

    public static void setPosPara(UserPosition userPosition) {
        CurrUserPosition.currLongitude = userPosition.getLongitude();
        CurrUserPosition.currLatitude = userPosition.getLatitude();
        CurrUserPosition.currFloor = userPosition.getFloor();
    }

    public static void setTimeStamp(long timeStamp){
        CurrUserPosition.timeStamp = timeStamp;
    }

    public static long getTimeStamp(){
        return CurrUserPosition.timeStamp;
    }
}

