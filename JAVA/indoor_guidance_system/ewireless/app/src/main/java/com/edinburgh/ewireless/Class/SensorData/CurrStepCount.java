package com.edinburgh.ewireless.Class.SensorData;

public class CurrStepCount {
    private static long timeStamp;
    private static int stepCount;

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrStepCount.timeStamp = timeStamp;
    }

    public static int getStepCount() {
        return stepCount;
    }

    public static void setStepCount(int stepCount) {
        CurrStepCount.stepCount = stepCount;
    }
}
