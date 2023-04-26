package com.edinburgh.ewireless.Class.PDR;

public class MadgwickAHRSFilterOutput {
    private float[] orientation = new float[3];
    private long timestamp;

    public float[] getOrientation() {
        return orientation;
    }

    public void setOrientation(float[] orientation) {
        this.orientation = orientation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
