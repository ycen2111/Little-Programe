package com.edinburgh.ewireless.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.edinburgh.ewireless.Class.Charts.Chart;
import com.edinburgh.ewireless.Class.PDR.PDRDataOut;
import com.edinburgh.ewireless.Class.SensorData.CurrAcc;
import com.edinburgh.ewireless.Class.SensorData.CurrGyro;
import com.edinburgh.ewireless.Class.SensorData.CurrLight;
import com.edinburgh.ewireless.Class.SensorData.CurrMag;
import com.edinburgh.ewireless.Class.SensorData.CurrPressure;
import com.edinburgh.ewireless.Class.SensorData.UserPosition;
import com.edinburgh.ewireless.MotionSensorManager;
import com.edinburgh.ewireless.R;
import com.edinburgh.ewireless.message.MessageSensor;

public class ProgrammerActivity extends AppCompatActivity implements MotionSensorManager.OnMotionSensorManagerListener {

    private static final String TAG = "Info_page";
    private final MessageSensor messageSensor = new MessageSensor();
    private MotionSensorManager mMotionSensorManager;

    private TextView mag_x;
    private TextView mag_y;
    private TextView mag_z;
    private TextView mag_h;
    private TextView mag_power;

    private TextView acc_x;
    private TextView acc_y;
    private TextView acc_z;
    private TextView acc_power;

    private TextView gyr_x;
    private TextView gyr_y;
    private TextView gyr_z;
    private TextView gry_power;

    private TextView latitude;
    private TextView longitude;

    private TextView light;
    private TextView light_power;
    private TextView pressure;
    private TextView pressure_power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmer_page);

        mag_x = findViewById(R.id.magX);
        mag_y = findViewById(R.id.magY);
        mag_z = findViewById(R.id.magZ);
        mag_h = findViewById(R.id.magField);
        mag_power = findViewById(R.id.magFieldPower);
        acc_x = findViewById(R.id.accX);
        acc_y = findViewById(R.id.accY);
        acc_z = findViewById(R.id.accZ);
        acc_power = findViewById(R.id.accPower);
        gyr_x = findViewById(R.id.gyroX);
        gyr_y = findViewById(R.id.gyroY);
        gyr_z = findViewById(R.id.gyroZ);
        gry_power = findViewById(R.id.gyroPower);
        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);
        light = findViewById(R.id.light);
        light_power = findViewById(R.id.lightPower);
        pressure = findViewById(R.id.altitude);
        pressure_power = findViewById(R.id.altitudePower);
        mMotionSensorManager = new MotionSensorManager(this);
        mMotionSensorManager.setOnMotionSensorManagerListener(this);
        mMotionSensorManager.registerMotionSensors();
    }

    protected final Handler mHandler = new Handler();
    Chart chartXY;
    Chart chartZ;
    //private long startTime, endTime;
    //Step counter thread
    private boolean isRunnableRunning = false;
    protected Runnable display = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            acc_x.setText(messageSensor.accX + CurrAcc.getCurrNoGDeviceAccX());
            acc_y.setText(messageSensor.accY + CurrAcc.getCurrNoGDeviceAccY());
            acc_z.setText(messageSensor.accZ + CurrAcc.getCurrNoGDeviceAccZ());
            acc_power.setText(messageSensor.accPower + CurrAcc.getAccPower() + "mA");
            gyr_x.setText(messageSensor.gyroX + CurrGyro.getCurrGyroX());
            gyr_y.setText(messageSensor.gyroY + CurrGyro.getCurrGyroY());
            gyr_z.setText(messageSensor.gyroZ + CurrGyro.getCurrGyroZ());
            gry_power.setText(messageSensor.gyroPower + CurrGyro.getGyroPower() + "mA");
            mag_x.setText(messageSensor.magX + CurrMag.getCurrMagX());
            mag_y.setText(messageSensor.magY + CurrMag.getCurrMagY());
            mag_z.setText(messageSensor.magZ + CurrMag.getCurrMagZ());
            mag_h.setText(messageSensor.magH + CurrMag.getCurrMagH());
            mag_power.setText(messageSensor.magPower + CurrMag.getMagPower() + "mA");
            latitude.setText(messageSensor.lat + UserPosition.getLatitude());
            longitude.setText(messageSensor.lon + UserPosition.getLongitude());
            light.setText(messageSensor.light + CurrLight.getCurrLight());
            light_power.setText(messageSensor.lightPower + CurrLight.getLightPower() + "mA");
            pressure.setText(messageSensor.pressure + CurrPressure.getCurrMillibarsOfPressure());
            pressure_power.setText(messageSensor.pressurePower + CurrPressure.getPressurePower() + "mA");
            mHandler.postDelayed(display,10);
        }
    };

    @Override
    protected void onResume() {
        mMotionSensorManager.registerMotionSensors();
        super.onResume();
        mHandler.postDelayed(display,10);

    }

    @Override
    protected void onPause() {
        mMotionSensorManager.unregisterMotionSensors();
        super.onPause();
    }

    @Override
    public void onAccValueUpdated(float[] acceleration) {

    }

    @Override
    public void onAccValueUpdated(float[] acceleration, long timestamp) {

    }

    @Override
    public void onGyoValueUpdated(float[] gyroscope) {

    }

    @Override
    public void onGyoValueUpdated(float[] gyroscope, long timestamp) {

    }

    @Override
    public void onMagValueUpdated(float[] magneticField) {

    }

    @Override
    public void onMagValueUpdated(float[] magneticField, long timestamp) {

    }

    @Override
    public void onGravityUpdated(float[] gravity) {

    }

    @Override
    public void onGravityUpdated(float[] gravity, long timestamp) {

    }

    @Override
    public void onEarthAccValueUpdated(PDRDataOut dataOut) {

    }

    @Override
    public void onLocationUpdated(float[] locations) {

    }

    @Override
    public void onOriValueUpdated(float[] ori) {

    }

    @Override
    public void onPressureUpdated(float pressure, long timestamp) {

    }
}