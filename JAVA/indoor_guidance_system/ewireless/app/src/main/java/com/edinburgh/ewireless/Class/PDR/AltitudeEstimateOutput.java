package com.edinburgh.ewireless.Class.PDR;

public class AltitudeEstimateOutput {
    private float altitude = 0;
    private float relativeAltitude = 0;
    private long timestamp;

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getRelativeAltitude() {
        return relativeAltitude;
    }

    public void setRelativeAltitude(float relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
