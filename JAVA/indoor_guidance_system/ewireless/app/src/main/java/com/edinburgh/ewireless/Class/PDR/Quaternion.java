package com.edinburgh.ewireless.Class.PDR;

/**
 * Author: yijianzheng
 * Date: 30/03/2023 12:20
 * <p>
 * Notes:
 */
public class Quaternion {
    private double[] quaternion = new double[4];

    public Quaternion(double[] rotation){
        if (rotation.length == 4){
            this.quaternion = rotation;
        }
    }

    //TODO

    public double[] getQuternion() {
        return quaternion;
    }

    public void setQuternion(double[] quternion) {
        this.quaternion = quternion;
    }
}
