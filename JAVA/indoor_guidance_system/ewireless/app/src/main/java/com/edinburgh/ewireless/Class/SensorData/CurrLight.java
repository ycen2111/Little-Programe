package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 13/04/2023 20:06
 * <p>
 * Notes:
 */
public class CurrLight {

    private static float currLight;

    private static long timeStamp;

    private static float lightPower;

    public static float getCurrLight() {
        return currLight;
    }

    public static void setCurrLight(float currLight) {
        CurrLight.currLight = currLight;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrLight.timeStamp = timeStamp;
    }

    public static float getLightPower() {
        return lightPower;
    }

    public static void setLightPower(float lightPower) {
        CurrLight.lightPower = lightPower;
    }
}
