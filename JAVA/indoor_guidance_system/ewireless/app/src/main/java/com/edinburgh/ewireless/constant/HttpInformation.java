package com.edinburgh.ewireless.constant;

/**
 * Author: yijianzheng
 * Date: 23/03/2023 16:59
 * <p>
 * Notes:
 *
 */
public class HttpInformation {
    private final String BASE_LOCATION = "https://openpositioning.org";
    private final String API_KEY = "?key=ewireless";
    private final String token = "kIb44OjVC5vT54RcC9XJ0Q";

    //https://openpositioning.org/api/live/users/
    public final String CREATE_USER = BASE_LOCATION + "/api/live/users/" + API_KEY;
    public final String READ_USER_TRAJECTORIES = BASE_LOCATION +"/api/live/users/trajectories/" + token + API_KEY;
    public final String DOWNLOAD_TRAJECTORIES = BASE_LOCATION + "/api/live/trajectory/download/"+ token + "/"  + API_KEY;
    public final String CREATE_UPLOAD_FILE = BASE_LOCATION + "/api/live/trajectory/upload/" + token + "/" + API_KEY;
    public final String UPLOAD_SCV_FILE = "http://149.102.149.208:5000/upload_csv?uuid=a8762478-af2b-45b6-a97a-29782d5ddd7c";
    public final String DOWNLOAD_ZIP_FILE = "http://149.102.149.208:5000/download_trajectory?uuid=a8762478-af2b-45b6-a97a-29782d5ddd7c";

    public final String TRAJECTORY = "http://149.102.149.208:5000";
}
