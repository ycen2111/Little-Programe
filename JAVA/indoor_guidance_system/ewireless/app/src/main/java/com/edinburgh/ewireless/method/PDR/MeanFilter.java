/***************************************************************************************
*    Title: MeanFilter.java
*    Author: Lang Huo
*    Date: 13/04/2023
***************************************************************************************/

package com.edinburgh.ewireless.method.PDR;

public class MeanFilter {
    // private instance variables
    private int size;     // size of the sliding window
    private float[] values;   // array to store the values in the sliding window
    private int index;    // index of the oldest value in the sliding window
    private int count;    // number of values currently in the sliding window

    // constructor
    public MeanFilter(int size) {
        this.size = size;
        values = new float[size];
        index = 0;
        count = 0;
    }

    // add a new value to the sliding window and return the current mean
    public float addValue(float value) {
        values[index] = value;
        index = (index + 1) % size;   // wrap around to the beginning of the array if we reach the end
        count = Math.min(count + 1, size);   // increment the count, but don't go over the size limit
        return getMean();   // return the current mean
    }

    // calculate and return the current mean of the values in the sliding window
    public float getMean() {
        float sum = 0;
        for (int i = 0; i < count; i++) {
            sum += values[i];
        }
        return sum / count;
    }
}
