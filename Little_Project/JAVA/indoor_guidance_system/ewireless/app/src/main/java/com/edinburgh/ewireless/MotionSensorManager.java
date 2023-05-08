package com.edinburgh.ewireless;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.edinburgh.ewireless.Class.SensorData.CurrAcc;
import com.edinburgh.ewireless.Class.SensorData.CurrGyro;
import com.edinburgh.ewireless.Class.SensorData.CurrLight;
import com.edinburgh.ewireless.Class.SensorData.CurrMag;
import com.edinburgh.ewireless.Class.SensorData.CurrOri;
import com.edinburgh.ewireless.Class.PDR.PDRDataOut;
import com.edinburgh.ewireless.Class.SensorData.CurrPressure;
import com.edinburgh.ewireless.Class.SensorData.CurrRotationVector;
import com.edinburgh.ewireless.Class.SensorData.CurrWifi;
import com.edinburgh.ewireless.Class.SensorData.UserPosition;

import java.util.List;


public class MotionSensorManager implements SensorEventListener {

    private static final String TAG = "MotionSensorManager";

    final float alpha = (float) 0.8;
    private final float[] gravity = new float[3];
    private final float[] gravityEarth = new float[3];

    private float[] gravityValues = new float[3];
    private float[] magnitudeValues = new float[3];
    private float[] gyroValues = new float[3];
    private float[] deviceAccValue = new float[4];
    private float[] earthAccValue = new float[3];
    private float[] oriValue = new float[3];

    private float[] linear_acceleration = new float[4];
    private float[] linearAccelerationEarth = new float[3];
    private float[] rotationMatrix = new float[9];

    /**
     Called when the Google Map is ready to be used
     @param sensorEvent the GoogleMap object that is ready to be used
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //////////////////////////////
                //
                // Device acc
                //
                //////////////////////////////
                deviceAccValue[0] = sensorEvent.values[0];
                deviceAccValue[1] = sensorEvent.values[1];
                deviceAccValue[2] = sensorEvent.values[2];
                deviceAccValue[3] = 0;
                CurrAcc.setCurrDeviceAccX(sensorEvent.values[0]);
                CurrAcc.setCurrDeviceAccY(sensorEvent.values[1]);
                CurrAcc.setCurrDeviceAccZ(sensorEvent.values[2]);
                CurrAcc.setTimeStamp(sensorEvent.timestamp);
                gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];


                linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
                linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
                linear_acceleration[2] = sensorEvent.values[2] - gravity[2];
//                worldLinearAcceleration = matrixMultiplication(rotationMatrix, linear_acceleration);

                //SensorManager.getRotationMatrix(R, I, g)
                CurrAcc.setCurrNoGDeviceAccX(linear_acceleration[0]);
                CurrAcc.setCurrNoGDeviceAccY(linear_acceleration[1]);
                CurrAcc.setCurrNoGDeviceAccZ(linear_acceleration[2]);
                motionSensorManagerListener.onAccValueUpdated(new float[]{linear_acceleration[0],
                        linear_acceleration[1], linear_acceleration[2],
                        deviceAccValue[0], deviceAccValue[1], deviceAccValue[2]});
                motionSensorManagerListener.onAccValueUpdated(new float[]{linear_acceleration[0],
                        linear_acceleration[1], linear_acceleration[2]}, sensorEvent.timestamp);

                //////////////////////////////
                //
                // earth coordinate acc
                //
                //////////////////////////////
                earthAccValue = matrixMultiplication(deviceAccValue, gravityValues, magnitudeValues);
                CurrAcc.setCurrEarthAccX(earthAccValue[0]);
                CurrAcc.setCurrEarthAccY(earthAccValue[1]);
                CurrAcc.setCurrEarthAccZ(earthAccValue[2]);
                gravityEarth[0] = alpha * gravityEarth[0] + (1 - alpha) * earthAccValue[0];
                gravityEarth[1] = alpha * gravityEarth[1] + (1 - alpha) * earthAccValue[1];
                gravityEarth[2] = alpha * gravityEarth[2] + (1 - alpha) * earthAccValue[2];

                linearAccelerationEarth[0] = earthAccValue[0] - gravityEarth[0];
                linearAccelerationEarth[1] = earthAccValue[1] - gravityEarth[1];
                linearAccelerationEarth[2] = earthAccValue[2] - gravityEarth[2];

                CurrAcc.setCurrNoGEarthAccX(linearAccelerationEarth[0]);
                CurrAcc.setCurrNoGEarthAccY(linearAccelerationEarth[1]);
                CurrAcc.setCurrNoGEarthAccZ(linearAccelerationEarth[2]);
                PDRDataOut pdrDataOut = new PDRDataOut();
                pdrDataOut.setData(new float[]{linearAccelerationEarth[0], linearAccelerationEarth[1], linearAccelerationEarth[2]});
                pdrDataOut.setTimestamp(sensorEvent.timestamp);
//                motionSensorManagerListener.onEarthAccValueUpdated(new float[]{
//                        linearAccelerationEarth[0], linearAccelerationEarth[1], linearAccelerationEarth[2], sensorEvent.timestamp});
                motionSensorManagerListener.onEarthAccValueUpdated(pdrDataOut);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroValues = sensorEvent.values.clone();
                CurrGyro.setCurrGyroX(sensorEvent.values[0]);
                CurrGyro.setCurrGyroY(sensorEvent.values[1]);
                CurrGyro.setCurrGyroZ(sensorEvent.values[2]);
                CurrGyro.setTimeStamp(sensorEvent.timestamp);
                motionSensorManagerListener.onGyoValueUpdated(new float[]{sensorEvent.values[0],
                        sensorEvent.values[1], sensorEvent.values[2]});

                motionSensorManagerListener.onGyoValueUpdated(new float[]{sensorEvent.values[0],
                        sensorEvent.values[1], sensorEvent.values[2]}, sensorEvent.timestamp);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                magnitudeValues = sensorEvent.values.clone();
                CurrMag.setCurrMagX(sensorEvent.values[0]);
                CurrMag.setCurrMagY(sensorEvent.values[1]);
                CurrMag.setCurrMagZ(sensorEvent.values[2]);
                CurrMag.setTimeStamp(sensorEvent.timestamp);
                double h = Math.sqrt(sensorEvent.values[0] * sensorEvent.values[0] + sensorEvent.values[1]
                        * sensorEvent.values[1] + sensorEvent.values[2] * sensorEvent.values[2]);
                CurrMag.setCurrMagH((float) h);
                motionSensorManagerListener.onMagValueUpdated(new float[]{sensorEvent.values[0],
                        sensorEvent.values[1], sensorEvent.values[2], (float) h});
                motionSensorManagerListener.onMagValueUpdated(new float[]{sensorEvent.values[0],
                        sensorEvent.values[1], sensorEvent.values[2], (float) h, sensorEvent.timestamp}, sensorEvent.timestamp);
                break;

            case Sensor.TYPE_GRAVITY:
                gravityValues = sensorEvent.values.clone();
                motionSensorManagerListener.onGravityUpdated(new float[]{
                        sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]});

                motionSensorManagerListener.onGravityUpdated(new float[]{
                        sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]}, sensorEvent.timestamp);

                break;

            case Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT:
                oriValue = sensorEvent.values.clone();
                CurrOri.setCurrOriX(sensorEvent.values[0]);
                CurrOri.setCurrOriY(sensorEvent.values[1]);
                CurrOri.setCurrOriZ(sensorEvent.values[2]);
                CurrOri.setTimeStamp(sensorEvent.timestamp);
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                CurrRotationVector.setCurrRotationVecX(sensorEvent.values[0]);
                CurrRotationVector.setCurrRotationVecY(sensorEvent.values[1]);
                CurrRotationVector.setCurrRotationVecZ(sensorEvent.values[2]);
                CurrRotationVector.setCurrRotationVecW(sensorEvent.values[3]);
                CurrRotationVector.setTimeStamp(sensorEvent.timestamp);
                break;

            case Sensor.TYPE_LIGHT:
                CurrLight.setCurrLight(sensorEvent.values[0]);
                CurrLight.setTimeStamp(sensorEvent.timestamp);
                break;

            case Sensor.TYPE_PRESSURE:
                CurrPressure.setCurrMillibarsOfPressure(sensorEvent.values[0]);
                CurrPressure.setTimeStamp(sensorEvent.timestamp);
                motionSensorManagerListener.onPressureUpdated(sensorEvent.values[0], sensorEvent.timestamp);
                break;

        }
    }

    /**
     * Multiplies the device acceleration values by the rotation matrix to get earth coordinates.
     *
     * @param deviceAccValue float[] containing the x, y, and z device acceleration values
     * @param gravityValues float[] containing the x, y, and z gravity values
     * @param magnitudeValues float[] containing the x, y, and z magnetic field strength values
     * @return float[] containing the x, y, and z earth acceleration values
     */
    private float[] matrixMultiplication(float[] deviceAccValue, float[] gravityValues, float[] magnitudeValues) {
        float[] R = new float[16], I = new float[16], earthAcc = new float[16];
        SensorManager.getRotationMatrix(R, I, gravityValues, magnitudeValues);
        float[] inv = new float[16];

        android.opengl.Matrix.invertM(inv, 0, R, 0);
        android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceAccValue, 0);
        //Log.d(TAG, "matrixMultiplication: " + earthAcc[0] + " " + earthAcc[1] + " " +earthAcc[2]);
        return earthAcc;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private OnMotionSensorManagerListener motionSensorManagerListener;


    private SensorManager sensorManager;
    private Sensor Accelerometer;
    private Sensor Gyroscope;
    private Sensor mMagneticField;
    private Sensor Gravity;
    private Sensor Ori;
    private Sensor rotationVector;
    private Sensor pressure;
    private Sensor light;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private WifiManager wifiManager;
    private Context context;

    /**
     Initializes the MotionSensorManager by accessing required sensors and services, and setting initial power values for each sensor.
     Also starts the wifi scan runnable using the handler.
     */
    public MotionSensorManager(Context context) {
        //Get instance of the sensor manager and then access all of the required sensors
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //3 sensors are being accessed as follows:
        mMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Ori = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //LocationManager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new myLocationListener();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "Open GPS!", Toast.LENGTH_LONG).show();
        }
        if (wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO:
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        context.registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        CurrAcc.setAccPower(Accelerometer.getPower());
        CurrGyro.setGyroPower(Gyroscope.getPower());
        CurrLight.setLightPower(light.getPower());
        CurrMag.setMagPower(mMagneticField.getPower());
        CurrPressure.setPressurePower(pressure.getPower());

        handler.post(wifiScanRunnable);
    }

    /**
     Callback function for when the accelerometer sensor values are updated
     */
    public interface OnMotionSensorManagerListener {
        void onAccValueUpdated(float[] acceleration);

        void onAccValueUpdated(float[] acceleration, long timestamp);

        void onGyoValueUpdated(float[] gyroscope);

        void onGyoValueUpdated(float[] gyroscope, long timestamp);

        void onMagValueUpdated(float[] magneticField);

        void onMagValueUpdated(float[] magneticField, long timestamp);

        void onGravityUpdated(float[] gravity);

        void onGravityUpdated(float[] gravity, long timestamp);

        void onEarthAccValueUpdated(PDRDataOut dataOut);

        void onLocationUpdated(float[] locations);

        void onOriValueUpdated(float[] ori);

        void onPressureUpdated(float pressure, long timestamp);
    }

    /**
     * Sets the listener for motion sensor events.
     *
     * @param motionSensorManagerListener the listener to set
     */
    public void setOnMotionSensorManagerListener(OnMotionSensorManagerListener motionSensorManagerListener) {
        this.motionSensorManagerListener = motionSensorManagerListener;
    }

    /**
     * Unregisters all motion sensors.
     */
    public void unregisterMotionSensors() {
        sensorManager.unregisterListener(this);
    }

    /**
     Registers all motion sensors with the sensor manager and sets their sampling delay.
     The sensor listener is set to this instance of MotionSensorManager.
     The sampling delay varies based on the specific sensor being registered.
     Sensor sampling rate can have a significant impact on battery life.
     */
    public void registerMotionSensors() {
        sensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, Gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, Gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, Ori, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }




    //    private Thread inertialThread;
//    private WeinbergStepLengthEstimate weinbergStepLengthEstimate;
//    @Override
//    public int onStartCommand(final Intent intent, int flags, int startId) {
//        inertialThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                weinbergStepLengthEstimate = new WeinbergStepLengthEstimate();
//            }
//        });
//        inertialThread.setPriority(10);
//        inertialThread.start();
//        return super.onStartCommand(intent, flags, startId);
//    }


    List<ScanResult> wifiScanList;
    String[] wifis;
    /**
     * BroadcastReceiver that listens for the results of a WiFi scan. When the results are received,
     * it stores them in a list and sets the time stamp. Then it unregisters the receiver.
     */
    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wifiScanList = wifiManager.getScanResults();
            CurrWifi.setWifiScanList(wifiScanList);
            CurrWifi.setTimeStamp(System.currentTimeMillis());
            context.unregisterReceiver(this);

//            wifis = new String[wifiScanList.size()];
//            //Log.v(TAG,String.valueOf(wifiScanList.size()));
//            for (int i = 0; i<wifiScanList.size();i++){
//                wifis[i] = wifiScanList.get(i).SSID + "," + wifiScanList.get(i).BSSID + "," + String.valueOf(wifiScanList.get(i).level);
//                //Log.v(TAG, String.valueOf(wifis[i]));
//            }

        }

    };

    /**
     * Starts a wifi scan using the WifiManager.
     */
    private void startWifiScan(){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan();
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    /**
     * The Runnable object that periodically starts a WiFi scan and updates the current WiFi scan list.
     */
    private final Runnable wifiScanRunnable = new Runnable() {
        @Override
        public void run() {
            startWifiScan();
            wifiScanList = wifiManager.getScanResults();
            CurrWifi.setWifiScanList(wifiScanList);
            CurrWifi.setTimeStamp(System.currentTimeMillis());
//            Log.d(TAG, "run: " + System.currentTimeMillis());
//            wifis = new String[wifiScanList.size()];
//            //Log.v(TAG,String.valueOf(wifiScanList.size()));
//            for (int i = 0; i<CurrWifi.getWifiScanList().size();i++){
//                wifis[i] = wifiScanList.get(i).SSID + "," + wifiScanList.get(i).BSSID + "," + String.valueOf(wifiScanList.get(i).level);
//                Log.v(TAG, String.valueOf(wifis[i]));
//            }
            handler.postDelayed(this, 2000); // Scan every 2 seconds
        }
    };

    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                double tlat = location.getLatitude();
                double tlong = location.getLongitude();
                UserPosition.setLatitude(tlat);
                UserPosition.setLongitude(tlong);
                UserPosition.setAccuracy(location.getAccuracy());
                UserPosition.setProvider(location.getProvider());
                UserPosition.setSpeed(location.getSpeed());
                UserPosition.setTimeStamp(location.getTime());
                //Log.d("MotionSensorManager", "Location updated");
            }
        }
    }


    public Sensor getAccelerometer() {
        return Accelerometer;
    }

    public void setAccelerometer(Sensor accelerometer) {
        Accelerometer = accelerometer;
    }

    public Sensor getGyroscope() {
        return Gyroscope;
    }

    public void setGyroscope(Sensor gyroscope) {
        Gyroscope = gyroscope;
    }

    public Sensor getmMagneticField() {
        return mMagneticField;
    }

    public void setmMagneticField(Sensor mMagneticField) {
        this.mMagneticField = mMagneticField;
    }

    public Sensor getRotationVector() {
        return rotationVector;
    }

    public void setRotationVector(Sensor rotationVector) {
        this.rotationVector = rotationVector;
    }

    public Sensor getPressure() {
        return pressure;
    }

    public void setPressure(Sensor pressure) {
        this.pressure = pressure;
    }

    public Sensor getLight() {
        return light;
    }

    public void setLight(Sensor light) {
        this.light = light;
    }
}
