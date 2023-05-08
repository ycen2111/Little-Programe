package com.example.directional_step_counter;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
/*
the stack structure, used to be designed for data saving between two sensors stepDetector and stepCounter,
but finally failed, because stepDetector is far insensitive then what expected
So this class is ignored in this project, but might be used in future

class name: FIFO
component: Queue<Integer> queue             read or write input data
            CountDownTimer countDownTimer   10s timer

 */
public class FIFO {

    private Queue<Integer> queue= new LinkedList<Integer>();;
    private CountDownTimer countDownTimer;

    /*
    Object and start 10s timer. New established object must been initialized,
    or count down timer will not find CountDownTimer and report error

    input: Void
    output: Void
    Changes: Create CountDownTimer and start a timer immediately
     */
    public void init() {
        startCount ();
    }

    /*
    Write an integer data into stack, and run resetCountDownTimer(),
    which will reset the timer to back to 10s

    input: Integer data
    output: Void
    Changes: add an element into stack
     */
    public void writeData(int data){
        queue.offer(data);
        resetCountDownTimer();
    }

    /*
    Read an integer data out from the stack. if there is no element, return 255.

    input: Void
    output: (if queue is not empty) Integer queue.poll()
            (if queue is empty) Integer 255
    Changes: poll the last element out from thr queue
     */
    public int readData(){
        if (queue.isEmpty()){
            return 255;
        }
        else
            return queue.poll();
    }

    /*
    A 10s count down counter. will run clearQueue() after 10s, this process will clear all element in the queue,
    because the maximum time delay between stepDetector and stepCounter is 10s.
    If there have no new data came to refresh the counter, that means those saved data are mis-measured one,
    clearQueue() will work and clear all those wrong data

    input: Void
    output: Void
    Changes: None
     */
    private void startCount () {
        countDownTimer = new CountDownTimer(10000, 10000) {

            public void onTick(long millisUntilFinished) {
                // do something after 10s
            }

            public void onFinish() {
                // do something end after 10s
                clearQueue();
            }
        };
    }

    /*
    reset the CountDownTimer back to 10s and start it.

    input: Void
    output: Void
    Changes: restart the 10s timer
     */
    private void resetCountDownTimer() {
        //cancel current timer
        countDownTimer.cancel();
        Log.d("", "resetCountDownTimer");
        //start a new timer
        startCount();
    }

    /*
    run when the timer successfully counted 10s.

    input: Void
    output: Void
    Changes: remove all elements in queue
     */
    private void clearQueue(){
        queue.clear();
        Log.d("", "Clear queue");
    }
}
