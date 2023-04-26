package com.edinburgh.ewireless.Class.SensorData;

/**
 * Author: yijianzheng
 * Date: 11/04/2023 18:08
 * It records all acc values and it's static for all method to use.
 * Notes:
 */
public class CurrAcc {
    private static float currDeviceAccX;
    private static float currDeviceAccY;
    private static float currDeviceAccZ;
    private static float currEarthAccX;
    private static float currEarthAccY;
    private static float currEarthAccZ;
    private static float currNoGDeviceAccX;
    private static float currNoGDeviceAccY;
    private static float currNoGDeviceAccZ;
    private static float currNoGEarthAccX;
    private static float currNoGEarthAccY;
    private static float currNoGEarthAccZ;

    private static float accPower;

    private static long timeStamp;

    public static float[] getCurrDeviceAcc(){
        return new float[]{currDeviceAccX, currDeviceAccY, currDeviceAccZ};
    }

    public static float[] getCurrEarthAcc(){
        return new float[]{currEarthAccX, currEarthAccY, currEarthAccZ};
    }

    public static float getCurrDeviceAccX() {
        return currDeviceAccX;
    }

    public static void setCurrDeviceAccX(float currDeviceAccX) {
        CurrAcc.currDeviceAccX = currDeviceAccX;
    }

    public static float getCurrDeviceAccY() {
        return currDeviceAccY;
    }

    public static void setCurrDeviceAccY(float currDeviceAccY) {
        CurrAcc.currDeviceAccY = currDeviceAccY;
    }

    public static float getCurrDeviceAccZ() {
        return currDeviceAccZ;
    }

    public static void setCurrDeviceAccZ(float currDeviceAccZ) {
        CurrAcc.currDeviceAccZ = currDeviceAccZ;
    }

    public static float getCurrEarthAccX() {
        return currEarthAccX;
    }

    public static void setCurrEarthAccX(float currEarthAccX) {
        CurrAcc.currEarthAccX = currEarthAccX;
    }

    public static float getCurrEarthAccY() {
        return currEarthAccY;
    }

    public static void setCurrEarthAccY(float currEarthAccY) {
        CurrAcc.currEarthAccY = currEarthAccY;
    }

    public static float getCurrEarthAccZ() {
        return currEarthAccZ;
    }

    public static void setCurrEarthAccZ(float currEarthAccZ) {
        CurrAcc.currEarthAccZ = currEarthAccZ;
    }

    public static float getCurrNoGDeviceAccX() {
        return currNoGDeviceAccX;
    }

    public static void setCurrNoGDeviceAccX(float currNoGDeviceAccX) {
        CurrAcc.currNoGDeviceAccX = currNoGDeviceAccX;
    }

    public static float getCurrNoGDeviceAccY() {
        return currNoGDeviceAccY;
    }

    public static void setCurrNoGDeviceAccY(float currNoGDeviceAccY) {
        CurrAcc.currNoGDeviceAccY = currNoGDeviceAccY;
    }

    public static float getCurrNoGDeviceAccZ() {
        return currNoGDeviceAccZ;
    }

    public static void setCurrNoGDeviceAccZ(float currNoGDeviceAccZ) {
        CurrAcc.currNoGDeviceAccZ = currNoGDeviceAccZ;
    }

    public static float getCurrNoGEarthAccX() {
        return currNoGEarthAccX;
    }

    public static void setCurrNoGEarthAccX(float currNoGEarthAccX) {
        CurrAcc.currNoGEarthAccX = currNoGEarthAccX;
    }

    public static float getCurrNoGEarthAccY() {
        return currNoGEarthAccY;
    }

    public static void setCurrNoGEarthAccY(float currNoGEarthAccY) {
        CurrAcc.currNoGEarthAccY = currNoGEarthAccY;
    }

    public static float getCurrNoGEarthAccZ() {
        return currNoGEarthAccZ;
    }

    public static void setCurrNoGEarthAccZ(float currNoGEarthAccZ) {
        CurrAcc.currNoGEarthAccZ = currNoGEarthAccZ;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrAcc.timeStamp = timeStamp;
    }

    public static float getAccPower() {
        return accPower;
    }

    public static void setAccPower(float accPower) {
        CurrAcc.accPower = accPower;
    }
}
