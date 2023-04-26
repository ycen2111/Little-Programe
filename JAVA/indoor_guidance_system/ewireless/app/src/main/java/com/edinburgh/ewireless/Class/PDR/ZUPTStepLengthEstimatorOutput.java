package com.edinburgh.ewireless.Class.PDR;

/**
 * Author: yijianzheng
 * Date: 08/04/2023 19:58
 * <p>
 * Notes:
 */
public class ZUPTStepLengthEstimatorOutput {
    private int stepCount;
    private float stepLength;

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public float getStepLength() {
        return stepLength;
    }

    public void setStepLength(float stepLength) {
        this.stepLength = stepLength;
    }

    public boolean isStepDetected() {
        return isStepDetected;
    }

    public void setStepDetected(boolean stepDetected) {
        isStepDetected = stepDetected;
    }

    private boolean isStepDetected;


}
