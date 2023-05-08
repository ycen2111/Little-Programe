package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 12/04/2023 11:07
 * <p>
 * Notes:
 */
public class CurrMag {

    private static float currMagX;
    private static float currMagY;
    private static float currMagZ;
    private static float currMagH;
    private static long timeStamp;
    private static float magPower;

    public static float[] getMag(){
        return new float[]{currMagX, currMagY, currMagZ};
    }

    public static float getCurrMagX() {
        return currMagX;
    }

    public static void setCurrMagX(float currMagX) {
        CurrMag.currMagX = currMagX;
    }

    public static float getCurrMagY() {
        return currMagY;
    }

    public static void setCurrMagY(float currMagY) {
        CurrMag.currMagY = currMagY;
    }

    public static float getCurrMagZ() {
        return currMagZ;
    }

    public static void setCurrMagZ(float currMagZ) {
        CurrMag.currMagZ = currMagZ;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrMag.timeStamp = timeStamp;
    }

    public static float getCurrMagH() {
        return currMagH;
    }

    public static void setCurrMagH(float currMagH) {
        CurrMag.currMagH = currMagH;
    }

    public static float getMagPower() {
        return magPower;
    }

    public static void setMagPower(float magPower) {
        CurrMag.magPower = magPower;
    }
}
