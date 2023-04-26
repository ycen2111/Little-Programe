package com.edinburgh.ewireless.Class.PDR;

import android.hardware.SensorManager;

public class MadgwickAHRSFilterInput {
    private float[] magnetic = new float[3];
    private float[] acceleration = new float[3];
    private float[] rotation = new float[3];
    private float[] gravity = new float[3];
    private float[] acceleration_uncorrected = new float[3];
    private float[] rotation_uncorrected = new float[3];
    private float[] magnetic_uncorrected = new float[3];
    private double beta = 0.02; // 2 * proportional gain
    private long timestamp = 0;


    public long getTimestamp() {
        return timestamp;
    }

    public long setTimestamp(long timestamp) {
        return this.timestamp = timestamp;
    }

    public float[] getMagnetic() {
        return magnetic;
    }

    public float[] getMagneticUncorrected() {
        return magnetic_uncorrected;
    }

    public float[] getGravity() {
        return gravity;
    }

    public void setGravity(float[] gravity) {
        this.gravity = gravity;
    }

    public void setMagnetic(float[] magnetic) {
        magnetic_uncorrected = magnetic;
        float[] remappedValues = new float[3];
        boolean remapResult = remapVector(magnetic, remappedValues, SensorManager.AXIS_MINUS_Z,
                SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Y);
        if(remapResult) {
            this.magnetic = remappedValues;
        }
    }

    public float[] getAcceleration() {
        return acceleration;
    }

    public float[] getAccelerationUncorrected() {
        return acceleration_uncorrected;
    }

    public void setAcceleration(float[] acceleration) {
        acceleration_uncorrected = acceleration;
        float[] remappedValues = new float[3];
        boolean remapResult = remapVector(acceleration, remappedValues, SensorManager.AXIS_MINUS_Z,
                SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Y);
//        Log.d("MadgwickAHRSFilterInput", "remapResult: "+remapResult);
        if(remapResult) {
            this.acceleration = remappedValues;
        }
    }

    public float[] getGyroscope() {
        return rotation;
    }
    public float[] getGyroscopeUncorrected() {
        return rotation_uncorrected;
    }

    public void setGyroscope(float[] rotation) {
        rotation_uncorrected = rotation;
        float[] remappedValues = new float[3];
        boolean remapResult = remapVector(rotation, remappedValues, SensorManager.AXIS_MINUS_Z,
                SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Y);
//        Log.d("MadgwickAHRSFilterInput", "remapResult: "+remapResult);
        if(remapResult) {
            this.rotation = remappedValues;
        }
    }

    public double getBeta() {return beta;};

    public void setBeta(double beta) {this.beta = beta;};

    private boolean remapVector(float[] inVector, float[] outVector, int X, int Y, int Z) {
        final int VECTOR_SIZE = 3;


        switch (X){
            case SensorManager.AXIS_X:
                outVector[0] = inVector[0];
                break;

            case SensorManager.AXIS_MINUS_X:
                outVector[0] = -inVector[0];
                break;

            case SensorManager.AXIS_Y:
                outVector[0] = inVector[1];
                break;

            case SensorManager.AXIS_MINUS_Y:
                outVector[0] = -inVector[1];
                break;

            case SensorManager.AXIS_Z:
                outVector[0] = inVector[2];
                break;

            case SensorManager.AXIS_MINUS_Z:
                outVector[0] = -inVector[2];
                break;

            /* Return false if an invalid axis parameter is passed */
            default:
                return false;
        }

        switch (Y) {
            case SensorManager.AXIS_X:
                outVector[1] = inVector[0];
                break;

            case SensorManager.AXIS_MINUS_X:
                outVector[1] = -inVector[0];
                break;

            case SensorManager.AXIS_Y:
                outVector[1] = inVector[1];
                break;

            case SensorManager.AXIS_MINUS_Y:
                outVector[1] = -inVector[1];
                break;

            case SensorManager.AXIS_Z:
                outVector[1] = inVector[2];
                break;

            case SensorManager.AXIS_MINUS_Z:
                outVector[1] = -inVector[2];
                break;

            /* Return false if an invalid axis parameter is passed */
            default:
                return false;
        }

        switch (Z) {
            case SensorManager.AXIS_X:
                outVector[2] = inVector[0];
                break;

            case SensorManager.AXIS_MINUS_X:
                outVector[2] = -inVector[0];
                break;

            case SensorManager.AXIS_Y:
                outVector[2] = inVector[1];
                break;

            case SensorManager.AXIS_MINUS_Y:
                outVector[2] = -inVector[1];
                break;

            case SensorManager.AXIS_Z:
                outVector[2] = inVector[2];
                break;

            case SensorManager.AXIS_MINUS_Z:
                outVector[2] = -inVector[2];
                break;

            /* Return false if an invalid axis parameter is passed */
            default:
                return false;
        }

        return true;

    }

}
