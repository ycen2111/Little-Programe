package com.edinburgh.ewireless.Class.Trajectory;

import android.util.Log;

import com.edinburgh.ewireless.Class.PDR.CurrUserPosition;
import com.edinburgh.ewireless.Class.SensorData.CurrAcc;
import com.edinburgh.ewireless.Class.SensorData.CurrGyro;
import com.edinburgh.ewireless.Class.SensorData.CurrLight;
import com.edinburgh.ewireless.Class.SensorData.CurrMag;
import com.edinburgh.ewireless.Class.SensorData.CurrPDRPosition;
import com.edinburgh.ewireless.Class.SensorData.CurrPressure;
import com.edinburgh.ewireless.Class.SensorData.CurrRotationVector;
import com.edinburgh.ewireless.Class.SensorData.CurrStepCount;
import com.edinburgh.ewireless.Class.SensorData.CurrWifi;
import com.edinburgh.ewireless.Class.SensorData.UserPosition;

/**

 The TrajectoryThread class is responsible for continuously adding motion, position, PDR, pressure, light,
 GPS and Wi-Fi samples to the Trajectory object passed in the constructor at a certain frequency.
 The thread stops when the stopThread() method is called.
 @author [Author Name]
 @version [Version Number]
 */
public class TrajectoryThread extends Thread {
    private Trajectory trajectory;
    private TrajectoryOuterClass.Trajectory.Builder trajectoryBuilder;
    private int preStepCount = -1;
    private long preGPSTimeStamp = 0;
    private boolean stop = false;

    /**
     Constructs a new TrajectoryThread object with the given Trajectory object.
     @param trajectory the Trajectory object to use
     */
    public TrajectoryThread(Trajectory trajectory){
        this.trajectory = trajectory;
        this.trajectoryBuilder = trajectory.getTrajectoryBuilder();
    }

    /**

     The main execution function of the TrajectoryThread.
     The thread runs indefinitely until the stopThread() method is called.
     In each iteration, it runs three different methods at different frequencies:
     run100Hz() is executed every 10ms.
     run1Hz() is executed every 1s (100 iterations of the main loop).
     run2Hz() is executed every 2s (200 iterations of the main loop).
     */
    @Override
    public void run() {
        int i=0;
        while (!this.stop) {
            i++;

            run100Hz();

            if(i % 100 == 0)
                run1Hz();

            if(i % 200 == 0)
                run2Hz();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the TrajectoryThread.
     */
    public void stopThread(){
        this.stop = true;
        Log.d("Trajectory thread", "stopped");
    }

    /**
     * Records motion and position samples at 100 Hz.
     */
    private void run100Hz(){
        trajectory.addMotionSample(CurrAcc.getTimeStamp(), CurrAcc.getCurrDeviceAcc(), CurrGyro.getCurrGyro(), CurrRotationVector.getCurrRotationVec(), CurrStepCount.getStepCount());
        trajectory.addPositionSample(CurrMag.getTimeStamp(), CurrMag.getMag());

        if (CurrStepCount.getStepCount() != preStepCount){
            trajectory.addPdrSample(CurrUserPosition.getTimeStamp(), CurrPDRPosition.getX(), CurrPDRPosition.getY(), CurrPDRPosition.getZ(), CurrStepCount.getStepCount());
            this.preStepCount = CurrStepCount.getStepCount();
        }
    }

    /**
     * Records pressure, light, GNSS and wifi data samples at 1 Hz.
     */
    private void run1Hz(){
        trajectory.addPressureSample(CurrPressure.getTimeStamp(), CurrPressure.getCurrMillibarsOfPressure());
        trajectory.addLightSample(CurrLight.getTimeStamp(), CurrLight.getCurrLight());
        trajectory.setMaxWifiNum(CurrWifi.getWifiScanList().size());

        if(UserPosition.getProvider() != null && UserPosition.getTimeStamp() != preGPSTimeStamp)
            trajectory.addGNSSSample(UserPosition.getTimeStamp(), (float) UserPosition.getLongitude(), (float) UserPosition.getLatitude(), UserPosition.getAccuracy(), UserPosition.getSpeed(), UserPosition.getProvider());

    }

    /**
     * Records wifi data samples at 2 Hz.
     */
    private  void run2Hz(){
        trajectory.addWifiData(CurrWifi.getTimeStamp(), CurrWifi.getWifiScanList());
    }
}
