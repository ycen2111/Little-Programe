/***************************************************************************************
*    Title: MadgwickAHRSFilter.java
*    Author: Sebastian Madgwick
*    Edited by: Lang Huo
*    Date: 13/04/2023
*    Availability: https://x-io.co.uk/open-source-imu-and-ahrs-algorithms/
*    
*
***************************************************************************************/

package com.edinburgh.ewireless.method.PDR;

import android.hardware.SensorManager;
import android.renderscript.Matrix3f;

import com.edinburgh.ewireless.Class.PDR.MadgwickAHRSFilterInput;
import com.edinburgh.ewireless.Class.PDR.MadgwickAHRSFilterOutput;
import com.edinburgh.ewireless.Class.PDR.Quaternion;

public class MadgwickAHRSFilter {
    MadgwickAHRSFilterInput madgwickAHRSFilterInput;
    MadgwickAHRSFilterOutput madgwickAHRSFilterOutput;
    private Quaternion quaternion = new Quaternion(new double[]{1,0,0,0});
    boolean isBaseOrientationSet = false;
    private final double NS2S = 1.0f / 1000000000.0f; // Nano-seconds to seconds
    private long timestamp = 0;
    private double dT = 0.0f;
    private boolean enable = false;

    public MadgwickAHRSFilter(MadgwickAHRSFilterInput madgwickAHRSFilterInput, MadgwickAHRSFilterOutput madgwickAHRSFilterOutput){
        this.madgwickAHRSFilterInput = madgwickAHRSFilterInput;
        this.madgwickAHRSFilterOutput = madgwickAHRSFilterOutput;
    }

    private double[] transQuaternion(float[] rotationMatrix){
        Matrix3f m1 = new Matrix3f(rotationMatrix);
        double w = Math.sqrt(1.0 + m1.get(0,0) + m1.get(1,1) + m1.get(2,2)) / 2.0;
        double w4 = (4.0 * w);
        double x = (m1.get(2,1) - m1.get(1,2)) / w4 ;
        double y = (m1.get(0,2) - m1.get(2,0)) / w4 ;
        double z = (m1.get(1,0) - m1.get(0,1)) / w4 ;

        return new double[]{w,x,y,z};
    }

    private float[] transQuaterniontoAngles(Quaternion quaternion){
        double[] q = quaternion.getQuternion();
        float q0 = (float)q[0];
        float q1 = (float)q[1];
        float q2 = (float)q[2];
        float q3 = (float)q[3];

        double roll = Math.toDegrees((Math.atan2(2f * (q0 * q3 + q1 * q2), 1f - 2f * (q1 * q1 + q3 * q3))));
        double pitch = Math.toDegrees(Math.asin(2f * (q0*q2 - q3*q1)));
        double yaw = Math.toDegrees(Math.atan2(2f * (q0*q1 + q2*q3), 1f - 2f * (q1*q1 + q2*q2)));

        return new float[]{(float)yaw, (float)pitch, (float)roll};
    }

    private void setBaseOrientation(){
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null, madgwickAHRSFilterInput.getGravity(), madgwickAHRSFilterInput.getMagneticUncorrected());
        double[] baseQuaternion = transQuaternion(rotationMatrix);
        this.quaternion.setQuternion(new double[]{1,0,0,0});
        this.isBaseOrientationSet = true;
    }

    public void update() {
        // Get updated sensor data
        float gx = madgwickAHRSFilterInput.getGyroscope()[0];
        float gy = madgwickAHRSFilterInput.getGyroscope()[1];
        float gz = madgwickAHRSFilterInput.getGyroscope()[2];
        float ax = madgwickAHRSFilterInput.getAcceleration()[0];
        float ay = madgwickAHRSFilterInput.getAcceleration()[1];
        float az = madgwickAHRSFilterInput.getAcceleration()[2];
        float mx = madgwickAHRSFilterInput.getMagnetic()[0];
        float my = madgwickAHRSFilterInput.getMagnetic()[1];
        float mz = madgwickAHRSFilterInput.getMagnetic()[2];

        // Initial Quaternion
        double[] quaternion = this.quaternion.getQuternion();
        double q0 = quaternion[0];
        double q1 = quaternion[1];
        double q2 = quaternion[2];
        double q3 = quaternion[3];

        double beta = madgwickAHRSFilterInput.getBeta();

        double norm;
        double s1, s2, s3, s0;
        double qDot1, qDot2, qDot3, qDot4;
        double hx, hy;
        double _2q0mx, _2q0my, _2q0mz, _2q1mx, _2bx, _2bz, _4bx, _4bz, _8bx, _8bz,  _2q0, _2q1, _2q2,
                _2q3, q0q0, q0q1, q0q2, q0q3, q1q1, q1q2, q1q3, q2q2, q2q3, q3q3;

        // Rate of change of quaternion from gyroscope
        qDot1 = 0.5f * (-q1 * gx - q2 * gy - q3 * gz);
        qDot2 = 0.5f * (q0 * gx + q2 * gz - q3 * gy);
        qDot3 = 0.5f * (q0 * gy - q1 * gz + q3 * gx);
        qDot4 = 0.5f * (q0 * gz + q1 * gy - q2 * gx);

        if(!((ax == 0.0f) && (ay == 0.0f) && (az == 0.0f))) {
            // Normalise accelerometer measurement
            norm = Math.sqrt(ax * ax + ay * ay + az * az);
            norm = 1/norm;
            ax *= norm;
            ay *= norm;
            az *= norm;

            // Normalise magnetometer measurement
            norm = 1f / Math.sqrt(mx * mx + my * my + mz * mz);
            mx *= norm;
            my *= norm;
            mz *= norm;

            // Auxiliary variables to avoid repeated arithmetic
            _2q0mx = 2.0f * q0 * mx;
            _2q0my = 2.0f * q0 * my;
            _2q0mz = 2.0f * q0 * mz;
            _2q1mx = 2.0f * q1 * mx;
            _2q0 = 2.0f * q0;
            _2q1 = 2.0f * q1;
            _2q2 = 2.0f * q2;
            _2q3 = 2.0f * q3;
            q0q0 = q0 * q0;
            q0q1 = q0 * q1;
            q0q2 = q0 * q2;
            q0q3 = q0 * q3;
            q1q1 = q1 * q1;
            q1q2 = q1 * q2;
            q1q3 = q1 * q3;
            q2q2 = q2 * q2;
            q2q3 = q2 * q3;
            q3q3 = q3 * q3;

            // Reference direction of Earth's magnetic field
            hx = mx * q0q0 - _2q0my * q3 + _2q0mz * q2 + mx * q1q1 + _2q1 * my * q2 + _2q1 * mz * q3 - mx * q2q2 - mx * q3q3;
            hy = _2q0mx * q3 + my * q0q0 - _2q0mz * q1 + _2q1mx * q2 - my * q1q1 + my * q2q2 + _2q2 * mz * q3 - my * q3q3;
            _2bx = Math.sqrt(hx * hx + hy * hy);
            _2bz = -_2q0mx * q2 + _2q0my * q1 + mz * q0q0 + _2q1mx * q3 - mz * q1q1 + _2q2 * my * q3 - mz * q2q2 + mz * q3q3;
            _4bx = 2.0f * _2bx;
            _4bz = 2.0f * _2bz;
            _8bz = 2.0f * _4bz;
            _8bx = 2.0f * _2bx;

            // Gradient decent algorithm corrective step
            s0 = -_2q2 * (2 * (q1q3 - q0q2) - ax) + _2q1 * (2 * (q0q1 + q2q3) - ay) -_4bz * q2 * (_4bx * (0.5f - q2q2 - q3q3) + _4bz * (q1q3 - q0q2) - mx) + (-_4bx * q3 + _4bz * q1) * (_4bx * (q1q2 - q0q3) + _4bz * (q0q1 + q2q3) - my) + _4bx * q2 * (_4bx * (q0q2 + q1q3) + _4bz * (0.5f - q1q1 - q2q2) - mz);
            s1 = _2q3 * (2 * (q1q3 - q0q2) - ax) + _2q0 * (2 * (q0q1 + q2q3) - ay) -4 * q1 * (2 * (0.5f - q1q1 - q2q2) - az) + _4bz * q3 * (_4bx * (0.5f - q2q2 - q3q3) + _4bz * (q1q3 - q0q2) - mx) + (_4bx * q2 + _4bz * q0) * (_4bx * (q1q2 - q0q3) + _4bz * (q0q1 + q2q3) - my) + (_4bx * q3 - _8bz * q1) * (_4bx * (q0q2 + q1q3) + _4bz * (0.5f - q1q1 - q2q2) - mz);
            s2 = -_2q0*(2*(q1q3 - q0q2) - ax)    +     _2q3*(2*(q0q1 + q2q3) - ay)   +   (-4*q2)*(2*(0.5f - q1q1 - q2q2) - az) +   (-_8bx*q2-_4bz*q0)*(_4bx*(0.5f - q2q2 - q3q3) + _4bz*(q1q3 - q0q2) - mx)+(_4bx*q1+_4bz*q3)*(_4bx*(q1q2 - q0q3) + _4bz*(q0q1 + q2q3) - my)+(_4bx*q0-_8bz*q2)*(_4bx*(q0q2 + q1q3) + _4bz*(0.5f - q1q1 - q2q2) - mz);
            s3 = _2q1*(2*(q1q3 - q0q2) - ax) +   _2q2*(2*(q0q1 + q2q3) - ay)+(-_8bx*q3+_4bz*q1)*(_4bx*(0.5f - q2q2 - q3q3) + _4bz*(q1q3 - q0q2) - mx)+(-_4bx*q0+_4bz*q2)*(_4bx*(q1q2 - q0q3) + _4bz*(q0q1 + q2q3) - my)+(_4bx*q1)*(_4bx*(q0q2 + q1q3) + _4bz*(0.5f - q1q1 - q2q2) - mz);
            norm = 1f /  Math.sqrt(s0 * s0 + s1 * s1 + s2 * s2 + s3 * s3); // normalise step magnitude
            s0 *= norm;
            s1 *= norm;
            s2 *= norm;
            s3 *= norm;

            // Apply feedback step
            qDot1 -= beta * s0;
            qDot2 -= beta * s1;
            qDot3 -= beta * s2;
            qDot4 -= beta * s3;
        }

        // Integrate rate of change of quaternion to yield quaternion
        q0 += qDot1 * dT;
        q1 += qDot2 * dT;
        q2 += qDot3 * dT;
        q3 += qDot4 * dT;

        // Normalise quaternion
        norm = 1f / Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
        q0 *= norm;
        q1 *= norm;
        q2 *= norm;
        q3 *= norm;

        this.quaternion.setQuternion(new double[]{q0,q1,q2,q3});
    }

    public void MadgwickAHRSupdateIMU() {
        double gx = madgwickAHRSFilterInput.getGyroscope()[0];
        double gy = madgwickAHRSFilterInput.getGyroscope()[1];
        double gz = madgwickAHRSFilterInput.getGyroscope()[2];
        double ax = madgwickAHRSFilterInput.getAcceleration()[0];
        double ay = madgwickAHRSFilterInput.getAcceleration()[1];
        double az = madgwickAHRSFilterInput.getAcceleration()[2];

        // Initial Quaternion
        double[] quaternion = this.quaternion.getQuternion();
        double q0 = quaternion[0];
        double q1 = quaternion[1];
        double q2 = quaternion[2];
        double q3 = quaternion[3];

        double beta = madgwickAHRSFilterInput.getBeta();
        double recipNorm;
        double s0, s1, s2, s3;
        double qDot1, qDot2, qDot3, qDot4;
        double _2q0, _2q1, _2q2, _2q3, _4q0, _4q1, _4q2 ,_8q1, _8q2, q0q0, q1q1, q2q2, q3q3;

        // Rate of change of quaternion from gyroscope
        qDot1 = 0.5f * (-q1 * gx - q2 * gy - q3 * gz);
        qDot2 = 0.5f * (q0 * gx + q2 * gz - q3 * gy);
        qDot3 = 0.5f * (q0 * gy - q1 * gz + q3 * gx);
        qDot4 = 0.5f * (q0 * gz + q1 * gy - q2 * gx);

        // Compute feedback only if accelerometer measurement valid (avoids NaN in accelerometer normalisation)
        if(!((ax == 0.0f) && (ay == 0.0f) && (az == 0.0f))) {

            // Normalise accelerometer measurement
            recipNorm = 1f / (float) Math.sqrt(ax * ax + ay * ay + az * az);
            ax *= recipNorm;
            ay *= recipNorm;
            az *= recipNorm;

            // Auxiliary variables to avoid repeated arithmetic
            _2q0 = 2.0f * q0;
            _2q1 = 2.0f * q1;
            _2q2 = 2.0f * q2;
            _2q3 = 2.0f * q3;
            _4q0 = 4.0f * q0;
            _4q1 = 4.0f * q1;
            _4q2 = 4.0f * q2;
            _8q1 = 8.0f * q1;
            _8q2 = 8.0f * q2;
            q0q0 = q0 * q0;
            q1q1 = q1 * q1;
            q2q2 = q2 * q2;
            q3q3 = q3 * q3;

            // Gradient decent algorithm corrective step
            s0 = _4q0 * q2q2 + _2q2 * ax + _4q0 * q1q1 - _2q1 * ay;
            s1 = _4q1 * q3q3 - _2q3 * ax + 4.0f * q0q0 * q1 - _2q0 * ay - _4q1 + _8q1 * q1q1 + _8q1 * q2q2 + _4q1 * az;
            s2 = 4.0f * q0q0 * q2 + _2q0 * ax + _4q2 * q3q3 - _2q3 * ay - _4q2 + _8q2 * q1q1 + _8q2 * q2q2 + _4q2 * az;
            s3 = 4.0f * q1q1 * q3 - _2q1 * ax + 4.0f * q2q2 * q3 - _2q2 * ay;
            recipNorm = 1f / (float) Math.sqrt(s0 * s0 + s1 * s1 + s2 * s2 + s3 * s3); // normalise step magnitude
            s0 *= recipNorm;
            s1 *= recipNorm;
            s2 *= recipNorm;
            s3 *= recipNorm;

            // Apply feedback step
            qDot1 -= beta * s0;
            qDot2 -= beta * s1;
            qDot3 -= beta * s2;
            qDot4 -= beta * s3;
        }

        // Integrate rate of change of quaternion to yield quaternion
        q0 += qDot1 * dT;
        q1 += qDot2 * dT;
        q2 += qDot3 * dT;
        q3 += qDot4 * dT;

        // Normalise quaternion
        recipNorm = 1f / (float) Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
        q0 *= recipNorm;
        q1 *= recipNorm;
        q2 *= recipNorm;
        q3 *= recipNorm;
        this.quaternion.setQuternion(new double[]{q0,q1,q2,q3});
    }

    // Output change of angle in degrees (yaw, pitch and roll)

    public float[] getOrientation(){
        if (enable) {
            if (!isBaseOrientationSet) {
                setBaseOrientation();
            } else {
                long currentTime = madgwickAHRSFilterInput.getTimestamp();
                if (timestamp != 0) {
                    dT = (currentTime - timestamp) * NS2S;
                }
                timestamp = currentTime;
                MadgwickAHRSupdateIMU();
            }
            float [] orientation = transQuaterniontoAngles(this.quaternion);
            madgwickAHRSFilterOutput.setOrientation(orientation);
            madgwickAHRSFilterOutput.setTimestamp(timestamp);
            return orientation;
        } else {
            madgwickAHRSFilterOutput.setOrientation(new float[]{0,0,0});
            madgwickAHRSFilterOutput.setTimestamp(timestamp);
            this.quaternion.setQuternion(new double[]{1,0,0,0});
            return new float[]{0,0,0};
        }
    }

    public boolean setEnable(boolean enable){
        this.enable = enable;
        return this.enable;
    }

}
