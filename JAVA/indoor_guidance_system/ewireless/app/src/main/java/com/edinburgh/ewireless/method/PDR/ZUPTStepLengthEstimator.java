package com.edinburgh.ewireless.method.PDR;

import com.edinburgh.ewireless.Class.PDR.ZUPTStepLengthEstimatorInput;
import com.edinburgh.ewireless.Class.PDR.ZUPTStepLengthEstimatorOutput;

/**
 * Author: yijianzheng
 * Date: 08/04/2023 19:56
 * <p>
 * Notes:
 */
public class ZUPTStepLengthEstimator {

    private static final String TAG = "ZUPT";

    private static final float ALPHA = 0.9f;
    private static final float VELOCITY_THRESHOLD = 35e-2f;
    private static final float VELOCITY_MAX = 1.2e0f;
    private static final float DISPLACE_THRESHOLD = 8e-1f;

    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private float[] velocity = new float[3];
    private float[] displacement = new float[3];

    public float[] getVelocity() {
        return velocity;
    }

    public void setVelocity(float[] velocity) {
        this.velocity = velocity;
    }

    private long lastTimestamp;
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

    private ZUPTStepLengthEstimatorOutput output = new ZUPTStepLengthEstimatorOutput();

    public ZUPTStepLengthEstimatorOutput calculateStepLength(ZUPTStepLengthEstimatorInput input){

        // Calculate time interval
        long timestamp = input.getTimestamp();
        float dt = (timestamp - lastTimestamp) / 1e9f; // Time in seconds
        //float dt = 0.01f;
        lastTimestamp = timestamp;
        if (lastTimestamp != 0 && dt < 1) {
            // Integrate linear acceleration to get velocity
            for (int i = 0; i < 3; i++) {
                velocity[i] += input.getValue()[i] * dt;

                // Apply zero-velocity update (ZUPT)
                if (Math.abs(velocity[i]) < VELOCITY_THRESHOLD) {
                    velocity[i] = 0;
                }
                else if(Math.abs(velocity[i]) > VELOCITY_MAX){
                    velocity[i] = velocity[i]/2;
                }
            }
            //Log.d(TAG, "calculateStepLength: " + "Velocity" + velocity[0] + " " + velocity[1] + " " +velocity[2] + " ");
            // Integrate velocity to get displacement
            for (int i = 0; i < 3; i++) {
                displacement[i] += velocity[i] * dt;
            }

            // Calculate the total horizontal displacement
            float horizontalDisplacement = (float) Math.sqrt(displacement[0] * displacement[0]
                    + displacement[1] * displacement[1]);
            //Log.d(TAG, "calculateStepLength: " + horizontalDisplacement);
            // Check if the horizontal displacement is significant
            if (horizontalDisplacement > DISPLACE_THRESHOLD) {
                stepCount++;
                stepLength = horizontalDisplacement;
                output.setStepLength(stepLength);
                output.setStepCount(stepCount);
                output.setStepDetected(true);
                displacement[0] = 0;
                displacement[1] = 0;
                displacement[2] = 0;
            } else {
                output.setStepDetected(false);
                output.setStepLength(horizontalDisplacement);
            }
        }
        return output;
    }

}
