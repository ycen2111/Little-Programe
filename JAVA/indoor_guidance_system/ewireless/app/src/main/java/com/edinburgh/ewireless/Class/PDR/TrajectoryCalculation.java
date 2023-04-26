package com.edinburgh.ewireless.Class.PDR;

import android.util.Log;

/**
 * Author: yijianzheng
 * Date: 15/04/2023 12:35
 * <p>
 * Notes:
 */
public class TrajectoryCalculation {

    private static final String TAG = "TrajectoryCalculation";

    private float[] trajectory = new float[3];
    private boolean enable = false;
    private boolean isStep = false;

    public void reset(){
        trajectory[0] = 0;
        trajectory[1] = 0;
        trajectory[2] = 0;
        stepCount = 0;
        isStep = false;
    }

    private int stepCount = 0;
    public float[] calculate(float orientation, StepDetectOut stepDetectOut, float pressure){
        if(enable){
            double radians = Math.toRadians(orientation);
            //Log.d(TAG, "calculate01: " + Math.cos(radians)+ " " + stepDetectOut.getStep() + " " + Math.sin(radians)* stepDetectOut.getStep());
            if(stepCount != stepDetectOut.getStepCount()){
                trajectory[0] += Math.sin(radians)* stepDetectOut.getStep();
                trajectory[1] += Math.cos(radians)* stepDetectOut.getStep();
                trajectory[2] = pressure;
                stepCount = stepDetectOut.getStepCount();
                Log.d(TAG, "Coordination upgraded: " + "X = " + trajectory[0]+ " Y = " + trajectory[1] + " Z =  " + trajectory[2]);
                isStep = true;
            }
            else {
                isStep =false;
            }
        }
        else {
            isStep = false;
        }
        return trajectory;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isStep() {
        return isStep;
    }

    public void setStep(boolean step) {
        isStep = step;
    }
}
