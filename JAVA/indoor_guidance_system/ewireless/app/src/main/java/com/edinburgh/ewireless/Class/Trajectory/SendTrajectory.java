package com.edinburgh.ewireless.Class.Trajectory;

import java.io.File;

/**
 * A class used to send trajectory data.
 */
public class SendTrajectory {
    String file;

    /**
     * Constructs a new SendTrajectory object.
     *
     * @param owner_id the owner ID for the trajectory data
     * @param id the ID for the trajectory data
     * @param file the file containing the trajectory data to be sent
     * @param content the content of the trajectory data
     */
    public SendTrajectory(int owner_id, int id, File file, String content){
        this.file = content;
    }
}
