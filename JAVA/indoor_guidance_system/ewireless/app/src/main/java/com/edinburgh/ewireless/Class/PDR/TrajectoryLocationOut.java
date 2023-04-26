package com.edinburgh.ewireless.Class.PDR;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yijianzheng
 * Date: 19/04/2023 16:45
 * <p>
 * Notes:
 */
public class TrajectoryLocationOut {

    private List<double[]> locations = new ArrayList<>();

    public void reset(){
        locations.clear();
    }

    public List<double[]> getLocations() {
        return locations;
    }

    public void setLocations(List<double[]> locations) {
        this.locations = locations;
    }

    public void appendLocation(double[] location) {
        this.locations.add(location);
    }
}
