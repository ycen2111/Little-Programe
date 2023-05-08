package com.edinburgh.ewireless.method.PDR;

import com.edinburgh.ewireless.Class.PDR.PDRDataOut;

/**
 * Author: yijianzheng
 * Date: 10/04/2023 11:21
 * <p>
 * Notes:
 */
public class PDRData {

    private float[][] output = new float[3][2];

    private long lastTimestamp;

    public float[][] filter(PDRDataOut dataOut) {
        // Initial state [position, velocity]
        double[] initialState = {0, 0};

        // Initial state covariance
        double[][] initialP = {
                {1000, 0},
                {0, 1000}
        };

        // Process noise covariance
        double[][] q = {
                {0.001, 0},
                {0, 0.001}
        };

        // Measurement noise covariance
        double r = 10;

        // Create a Kalman Filter instance
        KalmanFilter1D[] kf = new KalmanFilter1D[3];
        kf[0] = new KalmanFilter1D(initialState, initialP, q, r);
        kf[1] = new KalmanFilter1D(initialState, initialP, q, r);
        kf[2] = new KalmanFilter1D(initialState, initialP, q, r);
        long timestamp = dataOut.getTimestamp();
        float dt = (timestamp - lastTimestamp) / 1e9f; // Time in seconds
        lastTimestamp = timestamp;
        // Example data (replace with real accelerometer and gyroscope measurements)
        if (dt < 1){
            kf[0].predict(dt);
            kf[0].update(dataOut.getData()[0]);
            output[0][0] = (float) kf[0].getState()[0];
            output[0][1] = (float) kf[0].getState()[1];
            kf[1].predict(dt);
            kf[1].update(dataOut.getData()[1]);
            output[1][0] = (float) kf[1].getState()[0];
            output[1][1] = (float) kf[1].getState()[1];
            kf[2].predict(dt);
            kf[2].update(dataOut.getData()[2]);
            output[2][0] = (float) kf[2].getState()[0];
            output[2][1] = (float) kf[2].getState()[1];
        }
        return output;


    }
}
