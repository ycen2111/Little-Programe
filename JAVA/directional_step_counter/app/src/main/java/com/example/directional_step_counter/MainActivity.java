package com.example.directional_step_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/*
This is a directional step counter with compass, step counter, and direction display
this project uses:
    magnitude and accelerator meter for direction measurement,
    gyroscope meter for measured value double checking,
    stepCounter meter for step counting.

this program contains:
    the dynamic-changed low-pass filter,
    4ms rotate animation,
    a score board to determine measurement quality by multiple situation,
    directional step counter display board,
    auto/hand-controlled count direction select,
    and debug mode to display all detail messages or data for debugging.

note: the stepDetector sensor is not been used, as insensitive measurement. But it is still been
kept in this program.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //global variable for listener's level. Is best set to 2
    private static final int LISTENERTYPE = 2;
    //low pass filter coefficient
    float alpha=0;
    //record past degree value, used to start a rotate animation
    float pastDegree=0;
    //save all 8 direction information into an array, by the Direction class
    Direction[] directions = new Direction[8];

    //layout component objects
    private TextView tv_direction;
    private TextView tv_degree;
    private TextView tv_counter;
    private TextView tv_extraMagnitudeError;
    private TextView tv_degreeDoubleCheckError;
    private TextView tv_freezeText;
    private ImageView iv_compass;
    private ImageView iv_signal;
    private ImageView iv_selectMethod;
    //measured value display
    private TextView tv_accValue;
    private TextView tv_magValue;
    private TextView tv_gyrValue;

    //Object sensor manager and sensors
    private SensorManager sensormanager;
    private Sensor countMeter;
    private Sensor magneticMeter;
    private Sensor acceleroMeter;
    private Sensor gyroscopeMeter;
    private Sensor stepDetector;

    private boolean isCounterSensorPresent;
    //whether shows measured data and error box
    private boolean isInDetailMode=true;
    //whether the measurement quality is good
    private boolean isReleased=true;
    //whether the direction counter is selected automatically
    private boolean isDegreeAutoControlled=true;

    //step numbers in counter
    int StepCount = 0;

    //FIFO object. record the measured direction in the time gap between stepDetector and stepCounter
    //FIFO fifo = new FIFO();

    //the order and ID of directions saved in directionCount
    int directionID;
    //score board for quality counting.
    int magnitudeQualityCount = 0;
    //dynamic pass line for score board, can be seen as add score to score board with time pass
    float magnitudeQualityAlign = 0;
    //measured degree by gyroscope value. can be used to check whether measured degree is correct
    float degreeBoubleCheck = 0;

    //values of calculated direction
    float degree;
    float degreeAbsValue=0;

    //measured values
    float[] accelerometervalues = new float[3];
    float[] magneticFieldValues = new float[3];
    float[] gyroscopeValues = new float[3];

    /*
    initialize components by initialization(),
    check permitted permission,
    and calculate first direction degree
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[] {Manifest.permission.ACTIVITY_RECOGNITION},Sensor.TYPE_STEP_COUNTER);
        }

        //keep screen display all time
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        calculateOrientation();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //change accelero and magnetic
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometervalues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magneticFieldValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyroscopeValues = event.values;
            //add all changed degree (unsigned value) together, for degree double check
            degreeBoubleCheck += Math.sqrt(Math.pow(gyroscopeValues[2],2));
        }

        //change counter
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            //add steps
            StepCount += 1;
            tv_counter.setText("Total Steps:"+String.valueOf(StepCount));
            //decide with detail direction will be added on this step
            countDirectionalStep();
        }
        /*if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            Log.d("", String.valueOf(event.values[0]));
            fifo.writeData(directionID);
        }*/

        //calculate direction
        calculateOrientation();
        //for test only
        displayData();
    }

    //init for test view and call sensors
    private void initialization() {
        //link textView
        tv_direction=findViewById(R.id.tv_direction);
        tv_degree=findViewById(R.id.tv_degree);
        tv_counter=findViewById(R.id.tv_counter);
        tv_extraMagnitudeError=findViewById(R.id.extraMagnitudeError);
        tv_degreeDoubleCheckError=findViewById(R.id.degreeDoubleCheckError);
        tv_freezeText=findViewById(R.id.freezeText);
        tv_freezeText.setVisibility(View.INVISIBLE);
        iv_compass=findViewById(R.id.compass);
        iv_signal=findViewById(R.id.signalQuality);
        iv_selectMethod=findViewById(R.id.selectMethod);

        /*
        turn on/off extra message box (debug mode) by clicking signal image
        in debug mode, extra message will appeared
         */
        iv_signal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isInDetailMode=!isInDetailMode;

                if (isInDetailMode){
                    tv_accValue.setVisibility(View.VISIBLE);
                    tv_gyrValue.setVisibility(View.VISIBLE);
                    tv_magValue.setVisibility(View.VISIBLE);
                    tv_degreeDoubleCheckError.setVisibility(View.VISIBLE);
                    tv_extraMagnitudeError.setVisibility(View.VISIBLE);
                }
                else{
                    tv_accValue.setVisibility(View.INVISIBLE);
                    tv_gyrValue.setVisibility(View.INVISIBLE);
                    tv_magValue.setVisibility(View.INVISIBLE);
                    tv_degreeDoubleCheckError.setVisibility(View.INVISIBLE);
                    tv_extraMagnitudeError.setVisibility(View.INVISIBLE);
                }
            }
        });

        /*
        turn on/off auto direction selection
        in auto selection method, the counted direction will same as current measured direction
         */
        iv_selectMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDegreeAutoControlled=!isDegreeAutoControlled;

                if (isDegreeAutoControlled)
                    iv_selectMethod.setImageResource(R.drawable.auto);
                else
                    iv_selectMethod.setImageResource((R.drawable.hand));
            }
        });

        //display measured value
        tv_accValue=findViewById(R.id.accelerator);
        tv_magValue=findViewById(R.id.magnitude);
        tv_gyrValue=findViewById(R.id.gyroscope);

        //object and create 8 direction class in directions[]
        for (int i=0;i<directions.length;i++){
            directions[i]=new Direction();
        }

        //initialize 8 directions and link them with textView box
        directions[0].init(0,findViewById(R.id.eastName),findViewById(R.id.east));
        directions[1].init(1,findViewById(R.id.eastSouthName),findViewById(R.id.eastSouth));
        directions[2].init(2,findViewById(R.id.southName),findViewById(R.id.south));
        directions[3].init(3,findViewById(R.id.southWestName),findViewById(R.id.southWest));
        directions[4].init(4,findViewById(R.id.westName),findViewById(R.id.west));
        directions[5].init(5,findViewById(R.id.westNorthName),findViewById(R.id.westNorth));
        directions[6].init(6,findViewById(R.id.northName),findViewById(R.id.north));
        directions[7].init(7,findViewById(R.id.northEastName),findViewById(R.id.northEast));

        //initialize sensorManage and 4 sensor's default value
        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magneticMeter = sensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleroMeter = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeMeter = sensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        stepDetector = sensormanager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //check if stepCounter's permission is allowed
        if (sensormanager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            countMeter = sensormanager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        }
        // counter read is not permitted
        else {
            tv_counter.setText("Counter Sensor is not present");
            isCounterSensorPresent = false;
        }

        //set sensor listener
        sensormanager.registerListener(this,countMeter,LISTENERTYPE);
        sensormanager.registerListener(this,magneticMeter,LISTENERTYPE);
        sensormanager.registerListener(this,acceleroMeter,LISTENERTYPE);
        sensormanager.registerListener(this,gyroscopeMeter,LISTENERTYPE);
        sensormanager.registerListener(this,stepDetector,LISTENERTYPE);

        //init total counter text
        tv_counter.setText("0");

        //start 2s timer in loop, determine score board quality
        countDownTimer.start();
    }

    /*
    calculate direction degree by measured magnitude and acceleration value.
    the origin calculated value will pass a low-pass filter, and rotate compass image by
    rotateAnimation behavior

    input: Void
    output: Void
    Changes: float degree,      filtered degree
            int directionID,    the direction, which will be used in directional step counting
            textView tv_direction,      change displayed direction
     */
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];

        SensorManager.getRotationMatrix(R,null,accelerometervalues,magneticFieldValues);
        SensorManager.getOrientation(R,values);
        //Log.d("Direction", "["+(float) Math.toDegrees(values[0])+","+(float) Math.toDegrees(values[1])+","+(float) Math.toDegrees(values[2])+"]");

        /*
        degree is in range of -180 to 180.
        north [-45 to 45)
        east [45 to 135)
        south [135 to 180] [-180 to -135)
        west [-135 to -45)
         */
        //degree = (float) Math.toDegrees(values[0]);
        degree = weightLowPassFilter(degree+180,(float) Math.toDegrees(values[0])+180)-180;

        if (degree >= -22.5 && degree < 22.5){
            tv_direction.setText("North");
            if (determineCurrentQuality())
                directionID=6;
        }
        else if (degree >= 22.5 && degree < 67.5){
            tv_direction.setText("NE");
            if (determineCurrentQuality())
                directionID=7;
        }
        else if (degree >=67.5 && degree < 112.5){
            tv_direction.setText("East");
            if (determineCurrentQuality())
                directionID=0;
        }
        else if (degree >= 112.5 && degree < 157.5){
            tv_direction.setText("SE");
            if (determineCurrentQuality())
                directionID=1;
        }
        else if (degree >=157.5 || degree < -157.5){
            tv_direction.setText("South");
            if (determineCurrentQuality())
                directionID=2;
        }
        else if (degree >= -157.5 && degree < -112.5){
            tv_direction.setText("SW");
            if (determineCurrentQuality())
                directionID=3;
        }
        else if (degree >= -112.5 && degree < -67.5){
            tv_direction.setText("West");
            if (determineCurrentQuality())
                directionID=4;
        }
        else /*(degree >= -67.5 && degree < -22.5)*/{
            tv_direction.setText("NW");
            if (determineCurrentQuality())
                directionID=5;
        }
        tv_degree.setText(String.valueOf((int) degree));

        //update measured degree into compass figure's angle
        //iv_compass.setRotation(-degree);
        if (isReleased) {
            createAnimation(pastDegree, degree);

            if (accelerometervalues[2]<7){
                tv_freezeText.setText("Please Horizonte your phone");
                tv_freezeText.setBackgroundColor(Color.YELLOW);
                tv_freezeText.setTextColor(Color.BLACK);
                tv_freezeText.setVisibility(View.VISIBLE);
            }
            else
                tv_freezeText.setVisibility(View.INVISIBLE);
        }
        else {
            tv_freezeText.setText("Please Check Nearby Environment For Best Measurement");
            tv_freezeText.setBackgroundColor(Color.RED);
            tv_freezeText.setTextColor(Color.WHITE);
            tv_freezeText.setVisibility(View.VISIBLE);
        }
        pastDegree=degree;
        //change selected direction's color
        changeTextBackgroundColor();
    }

    //a 2-second count down timer. will run timerFinished() in every 2 second for quality counting
    CountDownTimer countDownTimer = new CountDownTimer(500, 500) {

        public void onTick(long millisUntilFinished) {
            // do something after 1s
        }

        public void onFinish() {
            // do something end after 1s
            timerFinished();
            start();
        }
    };

    /*
    count scores of how good the magnitude sensor is. the score is  magnitudeQualityMargin-magnitudeQualityCount,
    which magnitudeQualityMargin will generally close to magnitudeQualityCount for reset the state.

    the counted result is depended by abs magnitudeQualityAlign-magnitudeQualityCount

    Here are the details for scoring:
    measured data mismatches with second method measures degree:    -6 in 2s
    detect some extra magnitude suddenly appeared:                  -3 in 2s
    detect extremely extra magnitude suddenly appeared:             -10 in 2s
    find the magnitude sensor stop works in a long time:            -1 in each loop

    and there have another align level which will gradually fit with current score, so the final score will
    gradually back to good case with time passing

    this function will pass Math.abs(magnitudeQualityAlign-magnitudeQualityCount) to determineQualityColor()
    for changing layout display.

    input: Void
    output: Void
    Changes: float magnitudeQualityAlign,   changed toward magnitudeQualityCount, with a fixed weight
            float magnitudeQualityCount,    count current score
    */
    private void timerFinished(){
        if (5*degreeBoubleCheck<degreeAbsValue && (degreeBoubleCheck>1)){
            magnitudeQualityCount -= 6;
            tv_degreeDoubleCheckError.setText("degree measurement issue");
        }
        else
            tv_degreeDoubleCheckError.setText("degree measurement works");

        //set quality to ok/bad if there has strong magnetic field (with high values) nearby
        float absmagnitudeField=Math.abs(magneticFieldValues[0])+Math.abs(magneticFieldValues[1])+Math.abs(magneticFieldValues[2]);
        if (absmagnitudeField>50) {
            if (absmagnitudeField>500) {
                magnitudeQualityCount -= 10;
                tv_extraMagnitudeError.setText("!!Extremely Extra Magnitude Field Nearby!!");
                tv_degreeDoubleCheckError.setText("degree measurement issue");
            }
            else if (absmagnitudeField>100) {
                magnitudeQualityCount -= 3;
                tv_extraMagnitudeError.setText("Strong Extra Magnitude Field Nearby!!");
            }
            else
                tv_extraMagnitudeError.setText("Some Extra Magnitude Field Nearby");
        }
        else
            tv_extraMagnitudeError.setText("No Extra Magnitude Field Nearby");

        //set dynamic margin, which will gradually fit to magnitudeQualityCount, in order to let signal back to green when magnitude works again
        magnitudeQualityAlign=magnitudeQualityCount*(float)0.1+magnitudeQualityAlign*(float)0.9;
        //reset variables
        degreeBoubleCheck=0;
        degreeAbsValue=0;

        //change layout components
        determineQualityColor(Math.abs(magnitudeQualityAlign-magnitudeQualityCount));
    }

    public Boolean determineCurrentQuality(){
        if(Math.abs(magnitudeQualityAlign-magnitudeQualityCount)>40 || accelerometervalues[2]<5)
            isReleased=false;
        else
            isReleased=true;
        return (isReleased && isDegreeAutoControlled);
    }

    //temp testing function, display all data values into view
    public void displayData(){
        tv_accValue.setText("acc:"+String.valueOf((int)accelerometervalues[0])+","+String.valueOf((int)accelerometervalues[1])+","+String.valueOf((int)accelerometervalues[2]));
        tv_magValue.setText("mag:"+String.valueOf((int)magneticFieldValues[0])+","+String.valueOf((int)magneticFieldValues[1])+","+String.valueOf((int)magneticFieldValues[2]));
        tv_gyrValue.setText("gyr:"+String.valueOf((int)gyroscopeValues[0])+","+String.valueOf((int)gyroscopeValues[1])+","+String.valueOf((int)gyroscopeValues[2]));
    }

    //a low pass filter. new data=old data*alpha+input data*(1-alpha)
    //value of alpha will be changed based on old/new values' difference
    public float weightLowPassFilter(float oldDegree, float newDegree){
        //one in [0,90] and another in [270,360]
         /*
        when program found there has both 0-90 degree angles and 270-360 degree angles in last two inputs, following code will be enabled:
        the following step can "link" 0 degree and 360 degree together, and smooth the changing,

        eg. if there has two angles 8 and 359, 8 will add 360 first, which become 368,
            and then (359+368)/2=363.5,
            because 363.5>360, so 363.5-360=3.5,
            which is the new output degree
        */
        if ((oldDegree > 270 && newDegree < 90)||(oldDegree < 90 && newDegree > 270)){
            if (newDegree < 90)
                newDegree += 360;
            else
                oldDegree += 360;
        }

        //dynamically define filter bound
        if (Math.abs(oldDegree-newDegree)>1)
            //avoid special swift ceases. eg. difference > 45
            if (Math.abs(oldDegree - newDegree) > 45)
                alpha = (float) 0.5;
            else
                alpha = 1 - (Math.abs(oldDegree - newDegree) / (90));
        else
            alpha=(float)0.9;

        newDegree = oldDegree*alpha + newDegree*(1-alpha);

        degreeAbsValue += Math.abs(newDegree-oldDegree);

        return ((newDegree > 360)? (newDegree-360) : (newDegree));
    }

    public void createAnimation(float oldDegree, float newDegree){
        RotateAnimation animation = new RotateAnimation(-oldDegree, -newDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(5);
        animation.setFillAfter(true);
        iv_compass.startAnimation(animation);
    }

    //if difference between magnitudeQualityCount and magnitudeQualityMargin<-40, turn amber; if magnitudeQualityCount<-100, turns red
    public void determineQualityColor(float difference){
        if (difference<20)
            iv_signal.setImageResource(R.drawable.green);
        else if(difference<40)
            iv_signal.setImageResource(R.drawable.amber);
        else
            iv_signal.setImageResource(R.drawable.red);
    }

    //reset all counters into 0 and refresh output layout
    public void resetCounter(View view){
        StepCount=0;
        tv_counter.setText("Total Steps:"+String.valueOf(StepCount));

        for (int i=0;i<directions.length;i++){
            directions[i].setSteps(0);
        }
    }

    //add a specific direction's step number with 1
    public void countDirectionalStep(){
        directions[directionID].setSteps(directions[directionID].steps+1);
    }

    //change directionNameBox's background color. if this direction is selected and not freeze,set yellow
    //if is freeze, set blue. if not been selected, return to white
    public void changeTextBackgroundColor(){
        for (int i=0;i<directions.length;i++){
            if (isDegreeAutoControlled) {
                if (directions[i].directionID == directionID)
                    if (isReleased)
                        directions[i].directionNameView.setBackgroundColor(Color.YELLOW);
                    else
                        directions[i].directionNameView.setBackgroundColor(Color.BLUE);
                else
                    directions[i].directionNameView.setBackgroundColor(Color.WHITE);
            }
            else{
                directions[directionID].directionNameView.setBackgroundColor(Color.BLUE);
                for (int j=0;j<directions.length;j++){
                    if (directions[j].isSelected) {
                        directionID = j;
                        directions[j].isSelected=false;
                        for (int z=0;z<directions.length;z++)
                            directions[z].directionNameView.setBackgroundColor(Color.WHITE);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensormanager.unregisterListener(this);
        countDownTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensormanager.registerListener(this,countMeter,LISTENERTYPE);
        sensormanager.registerListener(this,magneticMeter,LISTENERTYPE);
        sensormanager.registerListener(this,acceleroMeter,LISTENERTYPE);
        sensormanager.registerListener(this,gyroscopeMeter,LISTENERTYPE);
        sensormanager.registerListener(this,stepDetector,LISTENERTYPE);
        countDownTimer.start();
    }
}

