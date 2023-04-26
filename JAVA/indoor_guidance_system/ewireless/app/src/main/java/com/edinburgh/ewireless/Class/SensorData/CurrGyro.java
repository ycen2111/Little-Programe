package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 12/04/2023 11:00
 * <p>
 * Notes:
 */
public class CurrGyro {
    private static float currGyroX;
    private static float currGyroY;
    private static float currGyroZ;
    private static long timeStamp;
    private static float gyroPower;

    public static float[] getCurrGyro(){
        return new float[]{currGyroX, currGyroY, currGyroZ};
    }

    public static float getCurrGyroX() {
        return currGyroX;
    }

    public static void setCurrGyroX(float currGyroX) {
        CurrGyro.currGyroX = currGyroX;
    }

    public static float getCurrGyroY() {
        return currGyroY;
    }

    public static void setCurrGyroY(float currGyroY) {
        CurrGyro.currGyroY = currGyroY;
    }

    public static float getCurrGyroZ() {
        return currGyroZ;
    }

    public static void setCurrGyroZ(float currGyroZ) {
        CurrGyro.currGyroZ = currGyroZ;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrGyro.timeStamp = timeStamp;
    }

    public static float getGyroPower() {
        return gyroPower;
    }

    public static void setGyroPower(float gyroPower) {
        CurrGyro.gyroPower = gyroPower;
    }
}
