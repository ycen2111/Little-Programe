package com.edinburgh.ewireless.Class.PDR;

/**
 * Author: yijianzheng
 * Date: 10/04/2023 13:23
 * <p>
 * Notes:
 */
public class PDRDataOut {
    private float[] data = new float[3];
    private long timestamp;

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
