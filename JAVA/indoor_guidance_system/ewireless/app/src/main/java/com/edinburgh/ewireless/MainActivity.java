package com.edinburgh.ewireless;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edinburgh.ewireless.Class.PDR.AltitudeEstimateInput;
import com.edinburgh.ewireless.Class.PDR.AltitudeEstimateOutput;
import com.edinburgh.ewireless.Class.Charts.Chart;
import com.edinburgh.ewireless.Class.PDR.CurrUserPosition;
import com.edinburgh.ewireless.Class.PDR.MadgwickAHRSFilterOutput;
import com.edinburgh.ewireless.Class.SensorData.CurrAcc;
import com.edinburgh.ewireless.Class.PDR.MadgwickAHRSFilterInput;
import com.edinburgh.ewireless.Class.PDR.PDRDataOut;
import com.edinburgh.ewireless.Class.SensorData.CurrPDRPosition;
import com.edinburgh.ewireless.Class.PDR.StepDetectOut;
import com.edinburgh.ewireless.Class.FileStorage.DownloadFile;
import com.edinburgh.ewireless.Class.FileStorage.FileManager;
import com.edinburgh.ewireless.Class.FileStorage.OpenFile;
import com.edinburgh.ewireless.Class.Trajectory.TrajectoryManager;
import com.edinburgh.ewireless.Class.PDR.TrajectoryCalculation;
import com.edinburgh.ewireless.Class.PDR.TrajectoryLocationOut;
import com.edinburgh.ewireless.Class.PDR.TrajectoryOut;
import com.edinburgh.ewireless.Class.PDR.ZUPTStepLengthEstimatorInput;
import com.edinburgh.ewireless.Class.PDR.ZUPTStepLengthEstimatorOutput;
import com.edinburgh.ewireless.constant.HttpInformation;
import com.edinburgh.ewireless.event.ButtonEvent;
import com.edinburgh.ewireless.event.UserEnterEvent;
import com.edinburgh.ewireless.message.MessageSensor;
import com.edinburgh.ewireless.method.PDR.AltitudeEstimate;
import com.edinburgh.ewireless.method.PDR.MadgwickAHRSFilter;
import com.edinburgh.ewireless.method.PDR.PDRData;
import com.edinburgh.ewireless.method.System.ToastShow;
import com.edinburgh.ewireless.method.PDR.WeinbergStepLengthEstimate;
import com.edinburgh.ewireless.method.PDR.ZUPTStepLengthEstimator;
import com.google.gson.Gson;

import java.io.File;

import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity implements MotionSensorManager.OnMotionSensorManagerListener{

    private static final String TAG = "Main";

    private MotionSensorManager mMotionSensorManager;

    private  MainActivity mainActivity;

    /////////////////////////////////
    //
    // Data processing             //
    //
    /////////////////////////////////

    private ZUPTStepLengthEstimator zuptStepLengthEstimator = new ZUPTStepLengthEstimator();
    private ZUPTStepLengthEstimatorInput zuptStepLengthEstimatorInput = new ZUPTStepLengthEstimatorInput();
    private ZUPTStepLengthEstimatorOutput zuptStepLengthEstimatorOutput = new ZUPTStepLengthEstimatorOutput();
    private PDRData pdrData = new PDRData();


    private MadgwickAHRSFilterInput madgwickAHRSFilterInput = new MadgwickAHRSFilterInput();
    private MadgwickAHRSFilterOutput madgwickAHRSFilterOutput = new MadgwickAHRSFilterOutput();
    private MadgwickAHRSFilter madgwickAHRSFilter = new MadgwickAHRSFilter(madgwickAHRSFilterInput, madgwickAHRSFilterOutput);

    private AltitudeEstimateInput altitudeEstimateInput = new AltitudeEstimateInput();
    private AltitudeEstimateOutput altitudeEstimateOutput = new AltitudeEstimateOutput();
    private AltitudeEstimate altitudeEstimate = new AltitudeEstimate(altitudeEstimateInput, altitudeEstimateOutput);

    /////////////////////////////////
    //
    // Text View
    //
    /////////////////////////////////

    private TextView mag_x;
    private TextView mag_y;
    private TextView mag_z;
    private TextView mag_h;

    private TextView acc_x;
    private TextView acc_y;
    private TextView acc_z;

    private TextView gyr_x;
    private TextView gyr_y;
    private TextView gyr_z;

    private TextView latitude;
    private TextView longitude;

    private TextView stepCount;
    private TextView total_distance;
    private TextView Yaw;
    private TextView Pitch;
    private TextView Roll;
    private TextView test2;
    private TextView test3;

    private TextView recordTimer;
    private Button tra_button;
    private WebView webView;

    private ImageView loadFile_image;
    private ImageView switch_image;
    private ImageView wifi_image;
    private ImageView download_image;
    private LineChartView chartViewXY;
    private LineChartView chartViewZ;

    File externalFilesDir;

    HttpInformation httpInformation = new HttpInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        externalFilesDir = getExternalFilesDir(null);

        mMotionSensorManager = new MotionSensorManager(this);
        mMotionSensorManager.setOnMotionSensorManagerListener(this);
        initialization();
    }

    private final MessageSensor messageSensor = new MessageSensor();
    private WeinbergStepLengthEstimate weinbergStepLengthEstimate = new WeinbergStepLengthEstimate();
    private StepDetectOut stepDetectOut = new StepDetectOut();
    @SuppressLint("SetTextI18n")
    @Override
    public void onAccValueUpdated(float[] acceleration) {
        //Estimate step length and step detected
//        zuptStepLengthEstimatorInput.setValue(acceleration);
//        zuptStepLengthEstimatorOutput = zuptStepLengthEstimator.calculateStepLength(zuptStepLengthEstimatorInput);
//        Log.d(TAG, "onAccValueUpdated: " + zuptStepLengthEstimatorOutput.isStepDetected()
//                + "\n" + zuptStepLengthEstimatorOutput.getStepCount()
//                + "\n" + zuptStepLengthEstimatorOutput.getStepLength());
//        test.setText(String.valueOf(zuptStepLengthEstimatorOutput.getStepLength()));
        // Text view

//        UserPosition userPosition = (UserPosition) getIntent().getSerializableExtra("pos_data");
//        ClientPos clientPos = (ClientPos) getIntent().getSerializableExtra("pos_data");
//        CurrUserPosition.setPosPara(userPosition);
        //WeinbergStepLengthEstimate weinbergStepLengthEstimate = new WeinbergStepLengthEstimate();
        //stepDetectOut = weinbergStepLengthEstimate.onStartCommand(UserPosition.getLatitude(), UserPosition.getLongitude(), CurrAcc.getCurrDeviceAcc(), CurrOri.getCurrOriX());
        //test.setText("StepCount" + stepDetectOut.getStepCount() + " Step" + stepDetectOut.getStep());
        /*acc_x.setText(messageSensor.accX + acceleration[0]);
        acc_y.setText(messageSensor.accY + acceleration[1]);
        acc_z.setText(messageSensor.accZ + acceleration[2]);
        latitude.setText("Latitude:" + UserPosition.getLatitude());
        longitude.setText("Longitude:" + UserPosition.getLongitude());*/

    }

    ///////////////////////////////////////////
    //
    // Sensor Interface method achievement
    //
    ///////////////////////////////////////////
    @Override
    public void onAccValueUpdated(float[] acceleration, long timestamp) {
        // Orientation estimation using Madgwick AHRS filter
        madgwickAHRSFilterInput.setAcceleration(new float[]{acceleration[0], acceleration[1], acceleration[2]});

//        if (temp!=0) {
//            float dt = (timestamp-temp)*1.0f / 1000000000.0f;
//            Log.d(TAG, "Acc period" + dt);
//            temp = timestamp;
//        }else{
//            temp = timestamp;
//        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGyoValueUpdated(float[] gyroscope) {
        /*gyr_x.setText(messageSensor.gyroX + gyroscope[0]);
        gyr_y.setText(messageSensor.gyroY + gyroscope[1]);
        gyr_z.setText(messageSensor.gyroZ + gyroscope[2]);*/

    }

    float[] orientation = new float[3];
    @Override
    public void onGyoValueUpdated(float[] gyroscope, long timestamp) {
        // Orientation estimation using Madgwick AHRS filter
        madgwickAHRSFilterInput.setGyroscope(new float[]{gyroscope[0], gyroscope[1], gyroscope[2]});
        madgwickAHRSFilterInput.setTimestamp(timestamp);
        orientation = madgwickAHRSFilter.getOrientation();
        Yaw.setText("Yaw: "+String.format("%.2f", orientation[0]));
        Pitch.setText("Pitch: "+String.format("%.2f", orientation[1]));
        Roll.setText("Roll: "+String.format("%.2f", orientation[2]));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMagValueUpdated(float[] magneticField) {
        /*mag_x.setText(messageSensor.magX + magneticField[0]);
        mag_y.setText(messageSensor.magY + magneticField[1]);
        mag_z.setText(messageSensor.magZ + magneticField[2]);
        mag_h.setText(messageSensor.magH + magneticField[3]);*/


    }

    @Override
    public void onMagValueUpdated(float[] magneticField, long timestamp) {
        // Orientation estimation using Madgwick AHRS filter
        madgwickAHRSFilterInput.setMagnetic(new float[]{magneticField[0], magneticField[1], magneticField[2]});
    }

    @Override
    public void onGravityUpdated(float[] gravity) {
        //test.setText(String.valueOf(gravity[0]));
    }

    @Override
    public void onGravityUpdated(float[] gravity, long timestamp) {
        madgwickAHRSFilterInput.setGravity(new float[]{gravity[0], gravity[1], gravity[2]});
    }

    @Override
    public void onEarthAccValueUpdated(PDRDataOut dataOut) {
        //////////////////////////////////////////////
        //
        // ZUPT are execute here, but it has been dropped.
        //
        ///////////////////////////////////////////////
        /*
        float[][] pdrFiltered = pdrData.filter(dataOut);
        float[] pdrInput = {pdrFiltered[0][0], pdrFiltered[1][0], pdrFiltered[2][0]};
        zuptStepLengthEstimatorInput.setValue(pdrInput);
        zuptStepLengthEstimatorInput.setTimestamp(dataOut.getTimestamp());
        zuptStepLengthEstimatorOutput = zuptStepLengthEstimator.calculateStepLength(zuptStepLengthEstimatorInput);
        */
//        Log.d(TAG, "onAccValueUpdated: " + zuptStepLengthEstimatorOutput.isStepDetected()
//                + "\n" + zuptStepLengthEstimatorOutput.getStepCount()
//                + "\n" + zuptStepLengthEstimatorOutput.getStepLength());
//        test.setText("Step Count" + zuptStepLengthEstimatorOutput.getStepCount()
//                + " Step Length" + zuptStepLengthEstimatorOutput.getStepLength()
//                + " Step vel" + Math.sqrt(zuptStepLengthEstimator.getVelocity()[0]*zuptStepLengthEstimator.getVelocity()[0] + zuptStepLengthEstimator.getVelocity()[1] * zuptStepLengthEstimator.getVelocity()[1]));
    }

    @Override
    public void onLocationUpdated(float[] locations) {

    }

    @Override
    public void onOriValueUpdated(float[] ori) {

    }

    @Override
    public void onPressureUpdated(float pressure, long timestamp) {
        altitudeEstimateInput.setPressure(pressure);
        altitudeEstimateInput.setTimeStamp(timestamp);
        altitudeEstimate.estimateAltitude();
        /*test3.setText("Altitude: " + altitudeEstimateOutput.getRelativeAltitude());*/
    }


    ///////////////////////////////////////////
    //
    // AppCompatActivity method              //
    //
    ///////////////////////////////////////////
    @Override
    protected void onResume() {

        super.onResume();
        mMotionSensorManager.registerMotionSensors();
//        mHandler.postDelayed(orientationUpdate,1);
        //mHandler.postDelayed(stepCounter, 20);
        startStepCounter();

    }

    @Override
    protected void onPause() {

        super.onPause();
        mMotionSensorManager.unregisterMotionSensors();
        stopStepCounter();
    }


    /**
     * Starts the step counter if it's not already running.
     * Sets the isRunnableRunning flag to true and schedules
     * a stepCounter Runnable to be executed after a delay
     * of 20 milliseconds using a Handler.
     *
     * @return void
     */
    private void startStepCounter() {
        if (!isRunnableRunning) {
            isRunnableRunning = true;
            mHandler.postDelayed(stepCounter, 20);
        }
    }

    /**
     * Stops the step counter if it's currently running.
     * Removes any pending post of the stepCounter Runnable
     * from the Handler and sets the isRunnableRunning flag to false.
     *
     * @return void
     */
    private void stopStepCounter() {
        if (isRunnableRunning) {
            mHandler.removeCallbacks(stepCounter);
            isRunnableRunning = false;
        }
    }

    /**
     * Initializes the UI components and sets up the various charts
     * used for displaying sensor data. It also asks for location and
     * Wi-Fi permissions, and sets the click listeners for various buttons.
     *
     * @return void
     */
    private void initialization(){
        /*mag_x = findViewById(R.id.magX);
        mag_y = findViewById(R.id.magY);
        mag_z = findViewById(R.id.magZ);
        mag_h = findViewById(R.id.magField);
        acc_x = findViewById(R.id.accX);
        acc_y = findViewById(R.id.accY);
        acc_z = findViewById(R.id.accZ);
        gyr_x = findViewById(R.id.gyroX);
        gyr_y = findViewById(R.id.gyroY);
        gyr_z = findViewById(R.id.gyroZ);
        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);*/
        stepCount = findViewById(R.id.StepCount);
        total_distance = findViewById(R.id.total_distance);
        Yaw = findViewById(R.id.Yaw);
        Pitch = findViewById(R.id.Pitch);
        Roll = findViewById(R.id.Roll);
        /*test2 = findViewById(R.id.test2);
        test3 = findViewById(R.id.test3);*/
        recordTimer = findViewById(R.id.recordTimer);
        tra_button = findViewById(R.id.TRAJECTORY);
        webView = findViewById(R.id.webView);
        loadFile_image = findViewById(R.id.loadFile_image);
        switch_image = findViewById(R.id.change_image);
        wifi_image = findViewById(R.id.wifiImage);
        download_image = findViewById(R.id.downloadImage);

        chartViewXY = findViewById(R.id.XYchart);
        chartViewZ = findViewById(R.id.Zchart);
        chartXY = new Chart(chartViewXY, Color.BLACK);
        chartXY.setXAxisName("PDR X (m)");
        chartXY.setYAxisName("PDR Y (m)");
        chartXY.draw();
        chartZ = new Chart(chartViewZ, Color.GRAY);
        chartZ.getLine().setFilled(true);
        chartZ.setXAxisName("Step Count");
        chartZ.setYAxisName("Height (m)");
        chartZ.draw();

        askLocationPermissions();
        askWiFiPermissions();

        setOnClickListener();
    }

    private static final int REQUEST_ID_LOCATION_PERMISSION = 99;
    /**
     * Asks for location and file read/write permissions, and requests
     * permission from the user if they haven't been granted.
     *
     * @return void
     */
    private void askLocationPermissions(){
        if (Build.VERSION.SDK_INT >= 23){
            int CoarseLocationPermission = ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int FineLocationPermission = ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            int internetPermission = ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.INTERNET);
            int WriteFilePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int ReadFilePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (CoarseLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    internetPermission != PackageManager.PERMISSION_GRANTED ||
                    FineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    WriteFilePermission != PackageManager.PERMISSION_GRANTED ||
                    ReadFilePermission != PackageManager.PERMISSION_GRANTED){
                //If don't have permission so prompt the user
                this.requestPermissions(
                        new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INTERNET,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        REQUEST_ID_LOCATION_PERMISSION
                );
                return;
            }
        }
    }

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    /**
     * Asks for Wi-Fi and location permissions, and requests permission
     * from the user if they haven't been granted.
     *
     * @return void
     */
    private void askWiFiPermissions(){
        if(Build.VERSION.SDK_INT >= 23) {
            int wifiAccessPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
            int wifiChangePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);
            int coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int fineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(wifiAccessPermission != PackageManager.PERMISSION_GRANTED ||
                    wifiChangePermission != PackageManager.PERMISSION_GRANTED ||
                    coarseLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    fineLocationPermission != PackageManager.PERMISSION_GRANTED)
            {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ID_READ_WRITE_PERMISSION

                );
                return;
            }
        }
    }

    /**
     * Handles the response from the user when asked for permissions.
     * Displays a Toast message to indicate whether the user granted
     * or denied the requested permissions.
     *
     * @param requestCode the code used to request permissions
     * @param permissions the requested permissions
     * @param grantResults the results of the permission request
     * @return void
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_ID_LOCATION_PERMISSION:{
                if (grantResults.length >1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Location Permission granted!", Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(this, "Location Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation();
    private TrajectoryOut trajectoryOut = new TrajectoryOut();
    private TrajectoryLocationOut trajectoryLocationOut = new TrajectoryLocationOut();
    protected final Handler mHandler = new Handler();
    Chart chartXY;
    Chart chartZ;
    //private long startTime, endTime;
    //Step counter thread
    private boolean isRunnableRunning = false;
    private float distance = 0;
    /**
     Sets the OnClickListener for various UI elements in the activity.
     @return void
     */
    protected Runnable stepCounter = new Runnable() {
        @Override
        public void run() {
            if (buttonEvent.isConnectedToWifi())
                wifi_image.setImageResource(R.drawable.green_wifi_label);
            else
                wifi_image.setImageResource(R.drawable.red_wifi_label);
            //startTime = System.currentTimeMillis();
            //Log.d(TAG, "run: Time" + (endTime - startTime));
            stepDetectOut = weinbergStepLengthEstimate.onStartCommand(CurrAcc.getCurrDeviceAcc(), madgwickAHRSFilter.getOrientation()[0]);
            float[] coordinate = trajectoryCalculation.calculate(madgwickAHRSFilter.getOrientation()[0],stepDetectOut, altitudeEstimateOutput.getRelativeAltitude());
            if (trajectoryCalculation.isStep()){
                stepCount.setText("StepCount" + stepDetectOut.getStepCount());
                total_distance.setText("Distance: " + String.format("%.2f", distance += stepDetectOut.getStep()));
                //trajectoryOut.appendCoordinates(new float[]{coordinate[0], coordinate[1], coordinate[2], stepDetectOut.getStepCount()});
                //Log.d(TAG, "run: Coordinate" + coordinate[0]+ " " + coordinate[1] + " " + coordinate[2]);
                trajectoryLocationOut.appendLocation(new double[]{CurrUserPosition.getCurrLatitude(), CurrUserPosition.getCurrLongitude()});
                //Log.d(TAG, "run: Location" + CurrUserPosition.getCurrLatitude()+ " " + CurrUserPosition.getCurrLongitude());

                //record to trejactory file
                //trajectoryManager.saveOneStep(coordinate[0], coordinate[1], coordinate[2]);
                CurrPDRPosition.setCurrDevicePDR(coordinate[0], coordinate[1], coordinate[2]);
                chartXY.addValue(coordinate[0], coordinate[1]);
                chartZ.addValue(coordinate[2]);
            }
            //endTime = System.currentTimeMillis();

            isRunnableRunning = false;

            // Schedule the next run
            startStepCounter();
        }
    };

    OpenFile openFile = new OpenFile(trajectoryOut,this);
    /**

     Sets the OnClickListener for various image views and buttons.
     When clicked, these views perform certain actions such as opening a file, switching chart views, downloading files,
     and displaying user trajectory information.
     */
    public void setOnClickListener(){
        loadFile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager fileManager = new FileManager(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));

                if (fileManager.isHasFile())
                    userEnterEvent.listSelect(MainActivity.this,fileManager.getFilesInfo(),fileManager,openFile);
                else
                    toastShow.toastShow("Please Save a File First");
            }
        });

        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chartViewXY.getVisibility() == View.VISIBLE){
                    chartViewXY.setVisibility(View.INVISIBLE);
                    chartViewZ.setVisibility(View.VISIBLE);
                }
                else{
                    chartViewXY.setVisibility(View.VISIBLE);
                    chartViewZ.setVisibility(View.INVISIBLE);
                }
            }
        });

        download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile = new DownloadFile(mainActivity, toastShow);
            }
        });

        wifi_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEvent.getAllUserTrajectoryInfo(toastShow);
            }
        });
    }

    DownloadFile downloadFile;
    ToastShow toastShow = new ToastShow(this);
    ButtonEvent buttonEvent = new ButtonEvent(this);
    public void onClick(View view) {
        if (trajectoryRecording){
            toastShow.toastShow("Trajectory is recording...");
        }
        else
            buttonEvent.programmerActivity();
    }

    public void onClickMap(View view){
        if (trajectoryRecording){
            toastShow.toastShow("Trajectory is recording...");
        }
        else
            buttonEvent.openMap(trajectoryLocationOut, toastShow);
    }

    boolean trajectoryRecording = false;
    TrajectoryManager trajectoryManager = new TrajectoryManager(buttonEvent,toastShow);
    UserEnterEvent userEnterEvent = new UserEnterEvent();
    public void onTrajectoryClick(View view) throws InterruptedException {
        if (!trajectoryRecording){
            if(mMotionSensorManager != null) {
                chartXY.clear();
                chartZ.clear();
                tra_button.setText("STOP RECORD");
                toastShow.toastShow("Start Record Trajectory");
                this.trajectoryManager.startRecording(mMotionSensorManager,recordTimer);
                sensorStart();
                total_distance.setText("Distance: " + String.format("%.2f", distance = 0));
                trajectoryRecording = true;
            }
            else
                toastShow.toastShow("ERROR: Recording Failed");
        }
        else{
            trajectoryManager.stopRecording(this,userEnterEvent);
            tra_button.setText("START RECORD");
            sensorStop();
            trajectoryRecording = false;
        }
    }

    /**
     Starts the sensors used for step counting and orientation calculations.
     Resets the relevant data structures and sets the sensors to enabled.
     */
    private void sensorStart(){
        weinbergStepLengthEstimate.reset();
        trajectoryCalculation.reset();
        trajectoryOut.reset();
        trajectoryLocationOut.reset();
        trajectoryCalculation.setEnable(true);
        weinbergStepLengthEstimate.setEnable(true);
        madgwickAHRSFilter.setEnable(true);
        altitudeEstimate.setEnable(true);
    }
    Gson gson = new Gson();
    /**
     Stops the sensors used for step counting and orientation calculations.
     Disables the relevant sensors and logs the trajectory output as a JSON string.
     */
    private void sensorStop(){
        weinbergStepLengthEstimate.setEnable(false);
        trajectoryCalculation.setEnable(false);
        madgwickAHRSFilter.setEnable(false);
        altitudeEstimate.setEnable(false);
        //Log.d(TAG, "sensorStop: Json" + gson.toJson(trajectoryOut));
    }

}