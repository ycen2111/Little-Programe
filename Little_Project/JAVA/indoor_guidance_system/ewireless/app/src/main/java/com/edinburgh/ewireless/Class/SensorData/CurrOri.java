package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 11/04/2023 18:14
 * <p>
 * Notes:
 */
public class CurrOri {
    private static float currOriX;
    private static float currOriY;
    private static float currOriZ;
    private static double timeStamp;

    public static float[] getOri(){
        return new float[]{currOriX, currOriY, currOriZ};
    }

    public static float getCurrOriX() {
        return currOriX;
    }

    public static void setCurrOriX(float currOriX) {
        CurrOri.currOriX = currOriX;
    }

    public static float getCurrOriY() {
        return currOriY;
    }

    public static void setCurrOriY(float currOriY) {
        CurrOri.currOriY = currOriY;
    }

    public static float getCurrOriZ() {
        return currOriZ;
    }

    public static void setCurrOriZ(float currOriZ) {
        CurrOri.currOriZ = currOriZ;
    }

    public static double getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(double timeStamp) {
        CurrOri.timeStamp = timeStamp;
    }
}
