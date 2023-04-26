package com.edinburgh.ewireless.Class.Trajectory;

import android.hardware.Sensor;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.edinburgh.ewireless.Class.PDR.CurrUserPosition;
import com.edinburgh.ewireless.Class.SensorData.CurrAcc;
import com.edinburgh.ewireless.Class.SensorData.CurrGyro;
import com.edinburgh.ewireless.Class.SensorData.CurrLight;
import com.edinburgh.ewireless.Class.SensorData.CurrMag;
import com.edinburgh.ewireless.Class.SensorData.CurrPressure;
import com.edinburgh.ewireless.Class.SensorData.CurrRotationVector;
import com.edinburgh.ewireless.Class.SensorData.CurrStepCount;
import com.edinburgh.ewireless.Class.SensorData.CurrWifi;
import com.edinburgh.ewireless.Class.SensorData.UserPosition;
import com.edinburgh.ewireless.MotionSensorManager;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.util.Arrays;
import java.util.List;

/**
 A class representing Trajectory object that contains motion sensor data, position data, pressure data, light data, GNSS data, Wi-Fi data, etc.
 It can also generate Protobuf messages from the Trajectory object and serialize them as binary or JSON.
 */
public class Trajectory {
    private TrajectoryOuterClass.Trajectory.Builder trajectoryBuilder;
    private TrajectoryOuterClass.Motion_Sample.Builder imuSample;
    private TrajectoryOuterClass.Pdr_Sample.Builder pdr_data;
    private TrajectoryOuterClass.Position_Sample.Builder position_data;
    private TrajectoryOuterClass.Pressure_Sample.Builder pressure_data;
    private TrajectoryOuterClass.Light_Sample.Builder light_data;
    private TrajectoryOuterClass.GNSS_Sample.Builder gnss_data;
    private TrajectoryOuterClass.WiFi_Sample.Builder wifi_data;
    private TrajectoryOuterClass.Mac_Scan.Builder mac_scans;
    private TrajectoryOuterClass.AP_Data.Builder aps_data;

    private TrajectoryOuterClass.Sensor_Info.Builder accelerometer_info;
    private TrajectoryOuterClass.Sensor_Info.Builder gyroscope_info;
    private TrajectoryOuterClass.Sensor_Info.Builder rotation_vector_info;
    private TrajectoryOuterClass.Sensor_Info.Builder magnetometer_info;
    private TrajectoryOuterClass.Sensor_Info.Builder barometer_info;
    private TrajectoryOuterClass.Sensor_Info.Builder light_sensor_info;

    private MotionSensorManager motionSensorManager;
    private int maxWifiNum = 0;

    /**

     Initializes a new Trajectory object with the given start timestamp and MotionSensorManager.
     @param start_timestamp The start timestamp of the trajectory.
     @param motionSensorManager The MotionSensorManager used to gather sensor data.
     */
    public Trajectory(long start_timestamp, MotionSensorManager motionSensorManager) {
        this.trajectoryBuilder = TrajectoryOuterClass.Trajectory.newBuilder();
        this.trajectoryBuilder.setAndroidVersion("Android SDK 28");
        this.trajectoryBuilder.setStartTimestamp(start_timestamp);
        this.trajectoryBuilder.setDataIdentifier("Temp");
        this.trajectoryBuilder.setCountNumber(0);

        this.imuSample = TrajectoryOuterClass.Motion_Sample.newBuilder();
        this.pdr_data = TrajectoryOuterClass.Pdr_Sample.newBuilder();
        this.position_data = TrajectoryOuterClass.Position_Sample.newBuilder();
        this.pressure_data = TrajectoryOuterClass.Pressure_Sample.newBuilder();
        this.light_data = TrajectoryOuterClass.Light_Sample.newBuilder();
        this.gnss_data = TrajectoryOuterClass.GNSS_Sample.newBuilder();
        this.wifi_data = TrajectoryOuterClass.WiFi_Sample.newBuilder();
        this.mac_scans = TrajectoryOuterClass.Mac_Scan.newBuilder();
        this.aps_data = TrajectoryOuterClass.AP_Data.newBuilder();

        this.accelerometer_info = TrajectoryOuterClass.Sensor_Info.newBuilder();
        this.gyroscope_info = TrajectoryOuterClass.Sensor_Info.newBuilder();
        this.rotation_vector_info = TrajectoryOuterClass.Sensor_Info.newBuilder();
        this.magnetometer_info = TrajectoryOuterClass.Sensor_Info.newBuilder();
        this.barometer_info = TrajectoryOuterClass.Sensor_Info.newBuilder();
        this.light_sensor_info = TrajectoryOuterClass.Sensor_Info.newBuilder();

        this.motionSensorManager = motionSensorManager;
        addSensorInfo(this.accelerometer_info,motionSensorManager.getAccelerometer());
        addSensorInfo(this.gyroscope_info,motionSensorManager.getGyroscope());
        addSensorInfo(this.rotation_vector_info,motionSensorManager.getRotationVector());
        addSensorInfo(this.magnetometer_info,motionSensorManager.getmMagneticField());
        addSensorInfo(this.barometer_info,motionSensorManager.getPressure());
        addSensorInfo(this.light_sensor_info,motionSensorManager.getLight());
    }

    /**
     * Creates a new Trajectory object with the given start timestamp.
     *
     * @param start_timestamp The start timestamp of the trajectory.
     */
    public Trajectory(long start_timestamp){
        this.trajectoryBuilder = TrajectoryOuterClass.Trajectory.newBuilder();
        this.pdr_data = TrajectoryOuterClass.Pdr_Sample.newBuilder();
        this.trajectoryBuilder.setStartTimestamp(start_timestamp);
    }

    /**
     Adds sensor information to the trajectory builder based on the given sensor.
     @param sensorBuilder The Sensor_Info builder to add the information to.
     @param sensor The sensor to extract information from.
     */
    private void addSensorInfo(TrajectoryOuterClass.Sensor_Info.Builder sensorBuilder,Sensor sensor){
        sensorBuilder.setName(sensor.getName())
                .setVendor(sensor.getVendor())
                .setResolution(sensor.getResolution())
                .setPower(sensor.getPower())
                .setVersion(sensor.getVersion())
                .setType(sensor.getType())
                .build();
        switch (sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                this.trajectoryBuilder.setAccelerometerInfo(sensorBuilder);
                break;
            case Sensor.TYPE_GYROSCOPE:
                this.trajectoryBuilder.setGyroscopeInfo(sensorBuilder);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                this.trajectoryBuilder.setRotationVectorInfo(sensorBuilder);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                this.trajectoryBuilder.setMagnetometerInfo(sensorBuilder);
                break;
            case Sensor.TYPE_PRESSURE:
                this.trajectoryBuilder.setBarometerInfo(sensorBuilder);
                break;
            case Sensor.TYPE_LIGHT:
                this.trajectoryBuilder.setLightSensorInfo(sensorBuilder);
                break;
        }
    }

    private boolean isSaved = false;
    /**
     Records sensor data and adds it to the trajectory.
     @param x float representing the x-axis value of the sensor data.
     @param y float representing the y-axis value of the sensor data.
     @param z float representing the z-axis value of the sensor data.
     */
    public void recordValues(float x, float y, float z){
        addCountNumber();
        addMotionSample(CurrAcc.getTimeStamp(), CurrAcc.getCurrDeviceAcc(), CurrGyro.getCurrGyro(), CurrRotationVector.getCurrRotationVec(), CurrStepCount.getStepCount());
        addPdrSample(CurrUserPosition.getTimeStamp(), x, y, z, CurrStepCount.getStepCount());
        addPositionSample(CurrMag.getTimeStamp(), CurrMag.getMag());
        addPressureSample(CurrPressure.getTimeStamp(), z);
        addLightSample(CurrLight.getTimeStamp(), CurrLight.getCurrLight());
        addGNSSSample(UserPosition.getTimeStamp(), (float) UserPosition.getLongitude(), (float) UserPosition.getLatitude(), UserPosition.getAccuracy(), UserPosition.getSpeed(), UserPosition.getProvider());
        addWifiData(CurrWifi.getTimeStamp(), CurrWifi.getWifiScanList());
        //addAPData(1, "SSID", 3);

        isSaved = true;
    }

    /**
     Increases the count number by 1 and sets it in the trajectory builder.
     */
    private void addCountNumber(){
        this.trajectoryBuilder.setCountNumber(this.trajectoryBuilder.getCountNumber()+1);
    }

    /**
     Adds a motion sample to the trajectory builder.
     @param timeStamp The timestamp of the motion sample.
     @param acc An array of size 3 containing the accelerometer readings in x, y, z axis respectively.
     @param gyro An array of size 3 containing the gyroscope readings in x, y, z axis respectively.
     @param rotation An array of size 4 containing the quaternion representation of the rotation vector.
     @param stepCount The number of steps taken during this motion sample.
     */
    public void addMotionSample(long timeStamp, float[] acc, float[] gyro, float[] rotation, int stepCount){
        TrajectoryOuterClass.Motion_Sample motion_sample = TrajectoryOuterClass.Motion_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setAccX(acc[0])
                .setAccY(acc[1])
                .setAccZ(acc[2])
                .setGyrX(gyro[0])
                .setGyrY(gyro[1])
                .setGyrZ(gyro[2])
                .setRotationVectorX(rotation[0])
                .setRotationVectorY(rotation[1])
                .setRotationVectorZ(rotation[2])
                .setRotationVectorW(rotation[3])
                .setStepCount(stepCount)
                .build();
        this.trajectoryBuilder.addImuData(motion_sample);
    }

    /**
     Adds a PDR sample to the trajectory.
     @param timeStamp The relative timestamp of the PDR sample.
     @param x The x-coordinate of the PDR sample.
     @param y The y-coordinate of the PDR sample.
     @param z The z-coordinate of the PDR sample.
     @param step The current step count.
     */
    public void addPdrSample(long timeStamp, float x, float y, float z, int step){
        TrajectoryOuterClass.Pdr_Sample pdr_sample = TrajectoryOuterClass.Pdr_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setX(x)
                .setY(y)
                .setZ(z)
                .setStep(step)
                .build();
        this.trajectoryBuilder.addPdrData(pdr_sample);
    }

    /**
     Adds a new PDR sample to the trajectory.
     @param x The x-coordinate of the PDR sample.
     @param y The y-coordinate of the PDR sample.
     @param z The z-coordinate of the PDR sample.
     @param step The step count at the time of the PDR sample.
     */
    public void addPdrSample(float x, float y, float z, int step){
        TrajectoryOuterClass.Pdr_Sample pdr_sample = TrajectoryOuterClass.Pdr_Sample.newBuilder()
                .setX(x)
                .setY(y)
                .setZ(z)
                .setStep(step)
                .build();
        this.trajectoryBuilder.addPdrData(pdr_sample);
    }

    /**
     Adds a position sample to the trajectory builder.
     @param timeStamp The relative timestamp of the sample.
     @param mag The magnetic field values in x, y, z directions.
     */
    public void addPositionSample(long timeStamp, float[] mag){
        TrajectoryOuterClass.Position_Sample position_sample = TrajectoryOuterClass.Position_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setMagX(mag[0])
                .setMagY(mag[1])
                .setMagZ(mag[2])
                .build();
        this.trajectoryBuilder.addPositionData(position_sample);
    }

    /**
     Adds a sample of pressure data to the trajectory.
     @param timeStamp The timestamp of the sample.
     @param pressure The value of the pressure sample.
     */
    public void addPressureSample(long timeStamp, float pressure){
        TrajectoryOuterClass.Pressure_Sample pressure_sample = TrajectoryOuterClass.Pressure_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setPressure(pressure)
                .build();
        this.trajectoryBuilder.addPressureData(pressure_sample);
    }

    /**

     Add a new light sample to the trajectory.
     @param timeStamp The timestamp of the light sample in milliseconds.
     @param light The value of the light sample.
     */
    public void addLightSample(long timeStamp, float light){
        TrajectoryOuterClass.Light_Sample light_sample = TrajectoryOuterClass.Light_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setLight(light)
                .build();
        this.trajectoryBuilder.addLightData(light_sample);
    }

    /**

     Adds a GNSS sample to the trajectory data.
     @param timeStamp the relative timestamp of the sample
     @param longitude the longitude of the location
     @param altitude the altitude of the location
     @param accuracy the accuracy of the location
     @param speed the speed at which the device is moving
     @param provider the name of the GNSS provider
     */
    public void addGNSSSample(long timeStamp, float longitude, float altitude, float accuracy, float speed, String provider){
        if (provider == null){
            provider = "null";
        }
        TrajectoryOuterClass.GNSS_Sample gnss_sample = TrajectoryOuterClass.GNSS_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setLongitude(longitude)
                .setAltitude(altitude)
                .setAccuracy(accuracy)
                .setSpeed(speed)
                .setProvider(provider)
                .build();
        this.trajectoryBuilder.addGnssData(gnss_sample);
    }

    /**
     Adds WiFi scan data to the trajectory.
     @param timeStamp The relative timestamp of the WiFi scan.
     @param wifiScanList The list of WiFi scan results.
     */
    public void addWifiData(long timeStamp, List<ScanResult> wifiScanList){
        TrajectoryOuterClass.WiFi_Sample.Builder wifi_dataBuilder = TrajectoryOuterClass.WiFi_Sample.newBuilder()
                .setRelativeTimestamp(timeStamp);

        int temp = 0;
        if (wifiScanList != null) {
            for (ScanResult scanResult : wifiScanList) {
                String[] macParts = scanResult.BSSID.split(":");
                long macLong = 0;
                for (int i = 0; i < macParts.length; i++) {
                    macLong = (macLong << 8) | Long.parseLong(macParts[i], 16);
                }
                //addMacScans(wifi_data, scanResult.timestamp, macLong, scanResult.level, scanResult.SSID, scanResult.frequency);
                TrajectoryOuterClass.Mac_Scan.Builder mac_scans = TrajectoryOuterClass.Mac_Scan.newBuilder()
                        .setRelativeTimestamp(timeStamp)
                        .setMac(macLong)
                        .setRssi(scanResult.level)
                        .setSsid(scanResult.SSID)
                        .setFrequency(scanResult.frequency);
                wifi_dataBuilder.addMacScans(mac_scans);
            }
        }

        TrajectoryOuterClass.WiFi_Sample wifiSample = wifi_dataBuilder.build();
        this.trajectoryBuilder.addWifiData(wifiSample);
    }

    /**
     * Adds a new Mac scan to the given WiFi sample.
     *
     * @param wifi_data The WiFi sample to add the Mac scan to.
     * @param timeStamp The relative timestamp of the Mac scan.
     * @param mac The MAC address of the Wi-Fi access point.
     * @param rssi The signal strength in dBm.
     * @param Ssid The SSID of the Wi-Fi access point.
     * @param freq The frequency in MHz of the Wi-Fi access point.
     */
    private void addMacScans(TrajectoryOuterClass.WiFi_Sample wifi_data, long timeStamp, long mac, int rssi, String Ssid, long freq){
        TrajectoryOuterClass.Mac_Scan mac_scans = TrajectoryOuterClass.Mac_Scan.newBuilder()
                .setRelativeTimestamp(timeStamp)
                .setMac(mac)
                .setRssi(rssi)
                .setSsid(Ssid)
                .setFrequency(freq)
                .build();
        wifi_data.toBuilder().addMacScans(mac_scans);
    }

    /**
     * Adds AP data to the trajectory.
     *
     * @param mac The MAC address of the AP.
     * @param ssid The SSID of the AP.
     * @param frequency The frequency of the AP.
     */
    private void addAPData(long mac, String ssid, long frequency){
        this.aps_data.setMac(mac)
                .setSsid(ssid)
                .setFrequency(frequency)
                .build();
        this.trajectoryBuilder.addApsData(aps_data);
    }

    /**
     * Returns the Trajectory.Builder object that was created when this TrajectoryGenerator object was instantiated.
     *
     * @return the Trajectory.Builder object
     */
    public TrajectoryOuterClass.Trajectory.Builder getTrajectoryBuilder(){
        return this.trajectoryBuilder;
    }

    /**

     Performs a self-test on a given Trajectory builder object, by serializing it to a byte array,
     printing the byte array to logcat, then deserializing it back to a JSON-formatted string and
     printing it to logcat as well.
     @param builder The Trajectory builder object to test.
     @throws InvalidProtocolBufferException If an error occurs during protocol buffer parsing.
     */
    public void selfTest(TrajectoryOuterClass.Trajectory.Builder builder) throws InvalidProtocolBufferException {
        byte[] bytes;
        String JsonForm;

        bytes = builder.build().toByteArray();
        Log.d("Trajectory byte array test", Arrays.toString(bytes));
        try {
            JsonForm = JsonFormat.printer().print(TrajectoryOuterClass.Trajectory.parseFrom(bytes));
            //JsonForm = JsonFormat.printer().print(builder);
            Log.d("Trajectory value test", JsonForm);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the maximum number of Wi-Fi samples that can be stored in the Trajectory object.
     * @param num The maximum number of Wi-Fi samples.
     */
    public void setMaxWifiNum(int num){
        if(num > this.maxWifiNum)
            this.maxWifiNum = num;
    }

    /**
     * Retrieves the maximum number of Wi-Fi samples that can be stored in the Trajectory object.
     * @return The maximum number of Wi-Fi samples.
     */
    public int getMaxWifiNum(){
        return this.maxWifiNum;
    }
}