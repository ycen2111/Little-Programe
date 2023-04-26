package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 13/04/2023 20:06
 * <p>
 * Notes:
 */
public class CurrPressure {

    private static float currMillibarsOfPressure;

    private static long timeStamp;

    private static float pressurePower;

    public static float getCurrMillibarsOfPressure() {
        return currMillibarsOfPressure;
    }

    public static void setCurrMillibarsOfPressure(float currMillibarsOfPressure) {
        CurrPressure.currMillibarsOfPressure = currMillibarsOfPressure;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrPressure.timeStamp = timeStamp;
    }

    public static float getPressurePower() {
        return pressurePower;
    }

    public static void setPressurePower(float pressurePower) {
        CurrPressure.pressurePower = pressurePower;
    }
}
