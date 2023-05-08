package com.edinburgh.ewireless.method.PDR;

/**
 * Author: yijianzheng
 * Date: 10/04/2023 11:14
 * <p>
 * Notes:
 */
public class KalmanFilter1D {
    private double[] state;      // [position, velocity]
    private double[][] pMatrix;  // State covariance matrix
    private double[][] qMatrix;  // Process noise covariance matrix
    private double r;            // Measurement noise covariance

    public KalmanFilter1D(double[] initialState, double[][] initialP, double[][] q, double r) {
        this.state = initialState;
        this.pMatrix = initialP;
        this.qMatrix = q;
        this.r = r;
    }

    public double[] getState() {
        return state;
    }

    public void setState(double[] state) {
        this.state = state;
    }

    public double[][] getpMatrix() {
        return pMatrix;
    }

    public void setpMatrix(double[][] pMatrix) {
        this.pMatrix = pMatrix;
    }

    public double[][] getqMatrix() {
        return qMatrix;
    }

    public void setqMatrix(double[][] qMatrix) {
        this.qMatrix = qMatrix;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void predict(double dt) {
        // State transition matrix
        double[][] fMatrix = {
                {1, dt},
                {0,  1}
        };

        // Predict the next state
        state = matrixVectorMultiply(fMatrix, state);

        // Predict the next state covariance
        pMatrix = matrixAddition(matrixMultiply(fMatrix, pMatrix, fMatrix.length, fMatrix[0].length, pMatrix[0].length),
                qMatrix);
    }

    public void update(double measurement) {
        // Compute the Kalman gain
        double k = pMatrix[0][0] / (pMatrix[0][0] + r);

        // Update the state
        state[0] += k * (measurement - state[0]);

        // Update the state covariance
        double[][] iMinusKH = {
                {1 - k, 0},
                {  0, 1}
        };
        pMatrix = matrixMultiply(iMinusKH, pMatrix, iMinusKH.length, iMinusKH[0].length, pMatrix[0].length );
        //pMatrix = matrixMultiply(iMinusKH, pMatrix, iMinusKH.length, iMinusKH[0].length, pMatrix[0].length);
    }

    private static double[] matrixVectorMultiply(double[][] m, double[] v) {
        int mRows = m.length;
        int mCols = m[0].length;
        double[] result = new double[mRows];

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mCols; j++) {
                result[i] += m[i][j] * v[j];
            }
        }
        return result;
    }

    private static double[][] matrixMultiply(double[][] a, double[][] b, int aRows, int aCols, int cCols) {
        double[][] result = new double[aRows][cCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < cCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
    private static double[][] matrixAddition(double[][] a, double[][] b) {
        int rows = a.length;
        int cols = a[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }
}
