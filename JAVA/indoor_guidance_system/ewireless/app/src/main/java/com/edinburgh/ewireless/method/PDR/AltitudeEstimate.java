/***************************************************************************************
*    Title: AltitudeEstimate.java
*    Author: Lang Huo
*    Date: 10/04/2023
***************************************************************************************/

package com.edinburgh.ewireless.method.PDR;

import android.hardware.SensorManager;

import com.edinburgh.ewireless.Class.PDR.AltitudeEstimateInput;
import com.edinburgh.ewireless.Class.PDR.AltitudeEstimateOutput;

public class AltitudeEstimate {

    // instance variables
    AltitudeEstimateInput altitudeEstimateInput;   // input object containing the current pressure and timestamp
    AltitudeEstimateOutput altitudeEstimateOutput;   // output object containing the estimated altitude and relative altitude
    MeanFilter meanFilter = new MeanFilter(15);   // mean filter object to smooth the pressure readings

    private float currentAltitude;   // current altitude estimate
    private float currentPressure;   // current barometric pressure reading
    private float initialPressure = 0;   // initial barometric pressure reading
    private float initialAltitude;   // initial altitude estimate
    private float relativeAltitude;   // relative altitude (difference between current altitude and initial altitude)

    private static final float ALPHA = 0.5f; // Smoothing constant

    private boolean enable = false;   // flag indicating whether altitude estimation is enabled or not

    // constructor
    public AltitudeEstimate(AltitudeEstimateInput altitudeEstimateInput, AltitudeEstimateOutput altitudeEstimateOutput) {
        this.altitudeEstimateInput = altitudeEstimateInput;
        this.altitudeEstimateOutput =  altitudeEstimateOutput;
    }

    // method to estimate the altitude based on the current pressure reading
    public void estimateAltitude() {
        if (enable) {   // if altitude estimation is enabled
            if (initialPressure == 0) {   // if we don't have an initial pressure reading yet
                initialPressure = altitudeEstimateInput.getPressure();   // set the initial pressure reading
                initialAltitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, initialPressure);   // calculate the initial altitude estimate
            } else {   // if we have an initial pressure reading
                currentPressure = currentPressure + ALPHA * (altitudeEstimateInput.getPressure() - currentPressure);   // smooth the current pressure reading using a low-pass filter
                meanFilter.addValue(currentPressure);   // add the smoothed pressure reading to the mean filter
                currentAltitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, meanFilter.getMean());   // calculate the current altitude estimate using the mean pressure reading
                relativeAltitude = currentAltitude - initialAltitude;   // calculate the relative altitude

                // set the altitude estimate output object with the current altitude, relative altitude, and timestamp
                altitudeEstimateOutput.setAltitude(currentAltitude);
                altitudeEstimateOutput.setRelativeAltitude(relativeAltitude);
                altitudeEstimateOutput.setTimestamp(altitudeEstimateInput.getTimeStamp());
            }
        } else {   // if altitude estimation is disabled
            // set the altitude estimate output object with zeros and the current timestamp, and reset the initial pressure and initial altitude
            altitudeEstimateOutput.setAltitude(0);
            altitudeEstimateOutput.setRelativeAltitude(0);
            this.initialPressure = 0;
            altitudeEstimateOutput.setTimestamp(altitudeEstimateInput.getTimeStamp());
            this.initialAltitude = 0;
        }
    }

    // method to enable/disable altitude estimation and return the new enable state
    public boolean setEnable(boolean enable){
        this.enable = enable;
        return this.enable;
    }
}

