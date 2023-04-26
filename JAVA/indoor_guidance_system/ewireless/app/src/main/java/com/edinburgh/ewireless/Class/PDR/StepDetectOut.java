package com.edinburgh.ewireless.Class.PDR;

import com.edinburgh.ewireless.Class.SensorData.CurrStepCount;

/**
 * Author: yijianzheng
 * Date: 11/04/2023 18:34
 * It records the step detect weibrug algrithm output
 * Notes:
 */
public class StepDetectOut {

    private int count;

    private int stepCount;

    private double step;

    private boolean isStep;

    private long timeStamp;

    public void reset(){
        count = 0;
        stepCount = 0;
        step = 0;
        isStep = false;
        timeStamp = 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
        CurrStepCount.setStepCount(this.stepCount);
    }

    public boolean isStep() {
        return isStep;
    }

    public void setStep(boolean step) {
        isStep = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        CurrStepCount.setTimeStamp(this.timeStamp);
    }
}
