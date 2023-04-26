package com.edinburgh.ewireless.Class.FileStorage;

import android.content.Context;
import android.util.Log;

import com.edinburgh.ewireless.Class.Trajectory.TrajectoryOuterClass;
import com.edinburgh.ewireless.Class.PDR.TrajectoryOut;
import com.edinburgh.ewireless.event.ButtonEvent;
import com.edinburgh.ewireless.method.System.ToastShow;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 A class to open a saved file and read the data.
 It uses the protocol buffer format to parse the data and extract the required information.
 */
public class OpenFile {
    File file;
    TrajectoryOuterClass.Trajectory trajectory;
    TrajectoryOut trajectoryOut;
    Context context;

    /**
     * Constructor for the OpenFile class.
     * @param trajectoryOut The trajectory output object.
     * @param context The context of the application.
     */
    public OpenFile(TrajectoryOut trajectoryOut, Context context){
        this.trajectoryOut = trajectoryOut;
        this.context = context;
    }

    /**
     * Sets the file to be opened and read.
     * @param file The file to be opened.
     * @throws InvalidProtocolBufferException
     */
    public void setFile(File file) throws InvalidProtocolBufferException {
        this.file = file;
        Log.d("File select", file.getAbsolutePath());
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        trajectory = TrajectoryOuterClass.Trajectory.parseFrom(bytes);
        readPdr();
    }

    /**
     * Reads the PDR data from the file.
     */
    public void readPdr(){
        ArrayList<float[]> array = new ArrayList<>();
        List<TrajectoryOuterClass.Pdr_Sample> pdrSamplesList = trajectory.getPdrDataList();
        List<TrajectoryOuterClass.Pressure_Sample> pressureSamplesList = trajectory.getPressureDataList();
        for(int i=0; i<pdrSamplesList.size(); i++){
            array.add(new float[]{pdrSamplesList.get(i).getX(), pdrSamplesList.get(i).getY(), pdrSamplesList.get(i).getZ(), i+1});
        }

        /*try {
            selfTest(trajectory.toBuilder());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }*/

        for (int i=0; i<array.size(); i++) {
            Log.d(String.valueOf(i), Arrays.toString(array.get(i)));
            trajectoryOut.appendCoordinates(array.get(i));
        }

        ButtonEvent buttonEvent = new ButtonEvent(this.context);
        ToastShow toastShow = new ToastShow(context);
        buttonEvent.trajectoryDraw(trajectoryOut,toastShow);
        trajectoryOut.reset();
    }

    /**
     * A method to test the protocol buffer format.
     * @param builder The protocol buffer builder.
     * @throws InvalidProtocolBufferException
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
}
