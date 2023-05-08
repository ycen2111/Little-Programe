package com.edinburgh.ewireless.Class.Trajectory;

import android.os.Handler;
import android.widget.TextView;

import java.util.Random;

/**
 A class that manages a timer for recording.
 */
public class RecordTimer {
    private TextView recordTimer;
    private int timeCounter = 0;
    protected final Handler mHandler = new Handler();
    private Random random = new Random();

    // The Runnable that updates the time display every 100 milliseconds
    protected Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            timeCounter += 1;
            recordTimer.setText(String.format("%02d", timeCounter / 600) + " : " + String.format("%02d", (timeCounter / 10) % 60) + " : " + String.valueOf(timeCounter % 10)+String.format("%02d",random.nextInt(100)));
            mHandler.postDelayed(mRunnable, 100);
        }
    };

    /**
     * Starts the timer and updates the specified TextView with the current time.
     *
     * @param textView The TextView used to display the time.
     */
    public void startTimer(TextView textView){
        this.recordTimer = textView;
        this.mHandler.postDelayed(this.mRunnable,100);
    }

    /**
     * Stops the timer and resets the time counter.
     */
    public void stopTimer(){
        this.mHandler.removeCallbacks(this.mRunnable);
        this.timeCounter = 0;
    }
}