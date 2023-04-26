package com.edinburgh.ewireless.Class.PDR;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yijianzheng
 * Date: 15/04/2023 13:12
 * <p>
 * Notes:
 */
public class TrajectoryOut {

    private List<float[]> coordinates = new ArrayList<>();

    public List<float[]> getCoordinates() {
        return coordinates;
    }

    public void reset(){
        coordinates.clear();
    }

    public void setCoordinates(List<float[]> coordinates) {
        this.coordinates = coordinates;
    }

    public void appendCoordinates(float[] coordinate) {
        this.coordinates.add(coordinate);
    }
}
