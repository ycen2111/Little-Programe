package com.edinburgh.ewireless.Class.Trajectory;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.edinburgh.ewireless.Class.FileStorage.CreateFile;
import com.edinburgh.ewireless.MotionSensorManager;
import com.edinburgh.ewireless.event.ButtonEvent;
import com.edinburgh.ewireless.event.UserEnterEvent;
import com.edinburgh.ewireless.method.System.ToastShow;

import java.io.File;
import java.util.ArrayList;

/**

 The TrajectoryManager class manages the recording and saving of trajectory data.
 It provides methods to start and stop recording, check WiFi state, and save the recording to a file.
 It also keeps track of any un-uploaded files.
 The class utilizes a RecordTimer to keep track of the recording time and a TrajectoryThread to handle the recording itself.
 @author Edinburgh EWireless
 */
public class TrajectoryManager {
    private RecordTimer recordTimer = new RecordTimer();
    private Trajectory trajectory;
    private  ArrayList<String> unUploadedFiles = new ArrayList<>();
    private ButtonEvent buttonEvent;
    private ToastShow toastShow;
    private boolean isRecording =false;
    private TrajectoryThread trajectoryThread;

    /**

     Constructor for TrajectoryManager class.
     @param buttonEvent instance of ButtonEvent class used to check wifi connection and send trajectory files.
     @param toastShow instance of ToastShow class used to display toast messages.
     */
    public TrajectoryManager(ButtonEvent buttonEvent, ToastShow toastShow){
        this.buttonEvent = buttonEvent;
        this.toastShow = toastShow;
    }

    /**

     Starts recording the trajectory by creating a new instance of the Trajectory class with the current time and the given motion sensor manager.
     Clears the trajectory builder if there is an existing trajectory.
     Starts a new TrajectoryThread instance and starts the timer.
     @param motionSensorManager The motion sensor manager used to record the trajectory.
     @param recordTimer_tv The text view to show the recording time.
     */
    public void startRecording(MotionSensorManager motionSensorManager, TextView recordTimer_tv){
        if(trajectory != null)
            trajectory.getTrajectoryBuilder().clear();
        trajectory = new Trajectory(System.currentTimeMillis(), motionSensorManager);
        trajectoryThread = new TrajectoryThread(trajectory);
        trajectoryThread.start();
        recordTimer.startTimer(recordTimer_tv);
        this.isRecording = true;
    }

    /**

     Stops the trajectory recording and stops the timer. Calls a UserEnterEvent to prompt the user to enter a filename for the saved trajectory.
     @param context the context of the calling activity
     @param userEnterEvent an instance of the UserEnterEvent class for prompting the user to enter a filename
     */
    public void stopRecording(Context context,UserEnterEvent userEnterEvent){
        trajectoryThread.stopThread();
        recordTimer.stopTimer();
        this.isRecording = false;
        userEnterEvent.enterFileNameEvent(context,this, trajectory, System.currentTimeMillis());
    }

    /**

     Checks the Wi-Fi connection and saves the recording if it is connected to Wi-Fi,
     otherwise displays a warning message to the user.
     @param context The context of the calling activity.
     @param name The name of the file to be saved.
     @param userEnterEvent An instance of UserEnterEvent to handle user input events.
     */
    public void checkWifiState(Context context, String name, UserEnterEvent userEnterEvent){
        if (buttonEvent.isConnectedToWifi())
            saveRecording(context, name, true);
        else
            userEnterEvent.noWifiWarningCheckMessage(context, name, this);
    }

    /**

     Saves the recorded trajectory into a CSV file and sends it to the server if the device is connected to a Wi-Fi network.
     @param context the context of the application
     @param name the name of the file to be created
     @param hasWifi a boolean value indicating whether the device is connected to a Wi-Fi network
     */
    public void saveRecording(Context context, String name, boolean hasWifi) {
        CreateFile createFile = new CreateFile();

        if(name != null) {
            if (createFile.createNewFile(name, trajectory, context, true)) {
                Log.d("stopRecord", "Totally count points: " + String.valueOf(trajectory.getTrajectoryBuilder().getCountNumber()));

                if(hasWifi) {
                    if (unUploadedFiles.size() != 0){
                        for (String add : unUploadedFiles) {
                            File file = new File(add);
                            if(file.exists()) {
                                buttonEvent.sendTrajectory(add, toastShow);
                            }
                        }
                        unUploadedFiles.clear();
                    }

                    buttonEvent.sendTrajectory(createFile.getSCVFileAddress(), toastShow);
                }
                else
                    unUploadedFiles.add(createFile.getSCVFileAddress());
            }
        }
        else
            Log.d("stopRecord", "Save file cancelled");
    }

    /**

     Saves one step of the recording by recording the values of the x, y, and z axes of the motion sensor.
     If the recording is currently in progress, the values are added to the trajectory. Otherwise, the values are not recorded.
     @param x the value of the x axis of the motion sensor
     @param y the value of the y axis of the motion sensor
     @param z the value of the z axis of the motion sensor
     */
    public void saveOneStep(float x, float y, float z){
        if (this.isRecording)
            trajectory.recordValues(x,y,z);
    }
}
