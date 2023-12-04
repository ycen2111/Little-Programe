package com.example.count_pill.global_value;

import android.util.Log;

import com.example.count_pill.file.Memory;

import java.io.IOException;
import java.util.Date;

public class ReadPillNumber {
    private static final String TAG = "readPillNumber";
    private Memory memory;
    private String[] stringArray;
    private Integer[] intArray = new Integer[5];
    private Integer[] days = new Integer[]{0,0,0,0,0};
    private int gapDays;

    public ReadPillNumber(Memory memory){
        this.memory = memory;
    }

    public void refreshNumber(Long modifiedTime)  {
        long currentTime = new Date().getTime();
        this.gapDays = (int) ((currentTime - modifiedTime) / (60*60*24*1000));

        this.stringArray = new String[]{"0", "0", "0", "0", "0"};
        if (memory.hasFile()) {
            try {
                String text = memory.read_file();
                String[] dataArray = text.split("/");
                Log.d(TAG, "refreshNumber: "+dataArray);
                this.stringArray = dataArray;

                int i = 0;
                for (String data : dataArray){
                    Log.d(TAG, "refreshNumber:!! "+data);
                    this.intArray[i] = Integer.parseInt(data);
                    this.days[i] = Integer.parseInt(data);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getStringArray(){
        return stringArray;
    }

    public Integer[] getIntArray(){
        return intArray;
    }

    public Integer[] getRemainingDays(){
        this.days[0] -= gapDays;
        this.days[1] = (this.days[1]/2) - gapDays;
        this.days[2] -= gapDays;
        this.days[3] -= gapDays;
        this.days[4] -= gapDays;

        return days;
    }
}
