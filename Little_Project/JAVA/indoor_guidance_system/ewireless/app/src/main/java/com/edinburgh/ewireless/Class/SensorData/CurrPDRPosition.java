package com.edinburgh.ewireless.Class.SensorData;

public class CurrPDRPosition {
    private static float X;
    private static float Y;
    private static float Z;

    private static long timeStamp;

    public static float[] getCurrDevicePDR(){
        return new float[]{X, Y};
    }

    public static void setCurrDevicePDR(float X, float Y, float Z){
        CurrPDRPosition.X = X;
        CurrPDRPosition.Y = Y;
        CurrPDRPosition.Z = Z;
    }

    public static float getX(){
        return X;
    }
    public static float getY(){
        return Y;
    }
    public static float getZ(){
        return Z;
    }
}
