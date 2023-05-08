package com.edinburgh.ewireless.Class.PDR;

/**
 * Author: yijianzheng
 * Date: 08/04/2023 19:58
 * <p>
 * Notes:
 */
public class ZUPTStepLengthEstimatorInput {
    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    float[] value = new float[3];

    long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
