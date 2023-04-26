package com.edinburgh.ewireless.Class.SensorData;

public class CurrRotationVector {
    private static float currRotationVecX;
    private static float currRotationVecY;
    private static float currRotationVecZ;
    private static float currRotationVecW;

    private static long timeStamp;

    public static float[] getCurrRotationVec(){
        return new float[]{currRotationVecX, currRotationVecY, currRotationVecZ, currRotationVecW};
    }

    public static float getCurrRotationVecX() {
        return currRotationVecX;
    }

    public static void setCurrRotationVecX(float currRotationVecX) {
        CurrRotationVector.currRotationVecX = currRotationVecX;
    }

    public static float getCurrRotationVecY() {
        return currRotationVecY;
    }

    public static void setCurrRotationVecY(float currRotationVecY) {
        CurrRotationVector.currRotationVecY = currRotationVecY;
    }

    public static float getCurrRotationVecZ() {
        return currRotationVecZ;
    }

    public static void setCurrRotationVecZ(float currRotationVecZ) {
        CurrRotationVector.currRotationVecZ = currRotationVecZ;
    }

    public static float getCurrRotationVecW() {
        return currRotationVecW;
    }

    public static void setCurrRotationVecW(float currRotationVecW) {
        CurrRotationVector.currRotationVecW = currRotationVecW;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrRotationVector.timeStamp = timeStamp;
    }
}
