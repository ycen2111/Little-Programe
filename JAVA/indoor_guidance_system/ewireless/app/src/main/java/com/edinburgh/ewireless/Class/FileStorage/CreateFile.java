package com.edinburgh.ewireless.Class.FileStorage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.edinburgh.ewireless.Class.Trajectory.Trajectory;
import com.edinburgh.ewireless.Class.Trajectory.TrajectoryOuterClass;
import com.google.protobuf.CodedOutputStream;
import com.opencsv.CSVWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class to create a new file for storing trajectory data and its CSV representation.
 */
public class CreateFile {
    private String fileName;
    private Trajectory trajectory;
    private File file,CSVFile;
    private byte[] fileByte;
    private boolean needCSV;

    /**
     * Creates a new file for storing the given trajectory data and, optionally, its CSV representation.
     *
     * @param fileName the name of the file to be created.
     * @param trajectory the trajectory data to be stored in the file.
     * @param context the application context.
     * @param needCSV specifies whether a CSV file should also be created for the trajectory data.
     * @return true if the file(s) were created successfully, false otherwise.
     */
    public boolean createNewFile(String fileName, Trajectory trajectory, Context context, boolean needCSV){
        this.fileName = fileName;
        this.trajectory = trajectory;
        this.needCSV = needCSV;
        try{
            createTrajectoryPath();
            Log.d("createFile", "createFile in: "+String.valueOf(this.file.getAbsolutePath()));
            this.fileByte = trajectory.getTrajectoryBuilder().build().toByteArray();
            writeContent(this.fileByte);
            if(needCSV)
                writeCSVContent();

            //galleryAddFile(context);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Creates the path for the trajectory file and, optionally, the CSV file.
     *
     * @throws IOException if an I/O error occurs while creating the file(s).
     */
    private void createTrajectoryPath() throws IOException{
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        //this.file = File.createTempFile(this.fileName,".pkt",storageDir);
        this.file = new File(storageDir,this.fileName+".pkt");
        Log.d("!!!", file.getAbsolutePath());
        if(this.file.exists()){
            this.file.delete();
        }

        this.file.createNewFile();

        if(needCSV) {
            this.CSVFile = new File(storageDir, this.fileName + ".csv");
            if (this.CSVFile.exists()) {
                this.CSVFile.delete();
            }

            this.CSVFile.createNewFile();
        }

        //this.trajectoryFile = new TrajectoryFile(file.getAbsolutePath(),this.fileName+".pkt",System.currentTimeMillis(),System.currentTimeMillis()-trajectory.getTrajectoryBuilder().getStartTimestamp(), trajectory.getTrajectoryBuilder().getCountNumber());
    }

    /**
     * Scans the file and adds it to the device's media gallery.
     *
     * @param context the application context.
     */
    private void galleryAddFile(Context context){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Writes the trajectory data to the trajectory file.
     *
     * @param text the trajectory data to be written.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    private void writeContent(byte[] text) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text);
        fos.flush();
        fos.close();
    }

    /**
     * Writes the trajectory data to the CSV file.
     */
    private void writeCSVContent(){
        FileWriter fileWriter = null;
        TrajectoryOuterClass.Trajectory.Builder trajectoryBuilder = trajectory.getTrajectoryBuilder();
        try {
            int wifi_gap_cell = 5*trajectory.getMaxWifiNum()+1;

            fileWriter = new FileWriter(CSVFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            List<String> title = new ArrayList<>();
            Collections.addAll(title, new String[]{"timestamp_global", "timestampImu", "acc_uncal_x", "acc_uncal_y", "acc_uncal_z", "ang_vel_uncal_x", "ang_vel_uncal_y", "ang_vel_uncal_z", "rotation_vector_x", "rotation_vector_y", "rotation_vector_z", "rotation_vector_w", "step_count", "magneticTimestamp", "mfield_uncal_x", "mfield_uncal_y", "mfield_uncal_z", "pdrTimeStamp", "x", "y", "z", "pdrStep", "pressureTimestamp", "pressure", "lightTimestamp", "ambient_brightness", "locateTimestamp", "lat", "long", "altitude", "accuracy", "speed", "provider", "wifiTimestamp", "wifis[boottime[ms]-bssid-rssi[dbm]-ssid-frequency[mhz]]"});
            for (int temp = 0; temp < wifi_gap_cell-1; temp++)
                title.add(null);

            csvWriter.writeNext(title.toArray(new String[0]));

            List<TrajectoryOuterClass.Pdr_Sample> pdrSamplesList = trajectoryBuilder.getPdrDataList();
            List<TrajectoryOuterClass.Motion_Sample> motionSampleList = trajectoryBuilder.getImuDataList();
            List<TrajectoryOuterClass.Position_Sample> positionSampleList = trajectoryBuilder.getPositionDataList();
            List<TrajectoryOuterClass.Pressure_Sample> pressureSampleList = trajectoryBuilder.getPressureDataList();
            List<TrajectoryOuterClass.Light_Sample> lightSampleList = trajectoryBuilder.getLightDataList();
            List<TrajectoryOuterClass.GNSS_Sample> gnssSampleList = trajectoryBuilder.getGnssDataList();
            List<TrajectoryOuterClass.WiFi_Sample> wiFiSampleList = trajectoryBuilder.getWifiDataList();

            int i = 0;
            while(i < motionSampleList.size()) {
                List<String> data = new ArrayList<>();
                data.add(String.valueOf(trajectoryBuilder.getStartTimestamp()));
                if(i < motionSampleList.size()){
                    data.add(String.valueOf(motionSampleList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(motionSampleList.get(i).getAccX()));
                    data.add(String.valueOf(motionSampleList.get(i).getAccY()));
                    data.add(String.valueOf(motionSampleList.get(i).getAccZ()));
                    data.add(String.valueOf(motionSampleList.get(i).getGyrX()));
                    data.add(String.valueOf(motionSampleList.get(i).getGyrY()));
                    data.add(String.valueOf(motionSampleList.get(i).getGyrZ()));
                    data.add(String.valueOf(motionSampleList.get(i).getRotationVectorX()));
                    data.add(String.valueOf(motionSampleList.get(i).getRotationVectorY()));
                    data.add(String.valueOf(motionSampleList.get(i).getRotationVectorZ()));
                    data.add(String.valueOf(motionSampleList.get(i).getRotationVectorW()));
                    data.add(String.valueOf(motionSampleList.get(i).getStepCount()));
                }

                if(i < positionSampleList.size()) {
                    data.add(String.valueOf(positionSampleList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(positionSampleList.get(i).getMagX()));
                    data.add(String.valueOf(positionSampleList.get(i).getMagY()));
                    data.add(String.valueOf(positionSampleList.get(i).getMagZ()));
                }
                else{
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                }

                if(i < pdrSamplesList.size()) {
                    data.add(String.valueOf(pdrSamplesList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(pdrSamplesList.get(i).getX()));
                    data.add(String.valueOf(pdrSamplesList.get(i).getY()));
                    data.add(String.valueOf(pdrSamplesList.get(i).getZ()));
                    data.add(String.valueOf(pdrSamplesList.get(i).getStep()));
                }
                else{
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                }

                if(i < pressureSampleList.size()) {
                    data.add(String.valueOf(pressureSampleList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(pressureSampleList.get(i).getPressure()));
                }
                else{
                    data.add(null);
                    data.add(null);
                }

                if(i < lightSampleList.size()) {
                    data.add(String.valueOf(lightSampleList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(lightSampleList.get(i).getLight()));
                }
                else{
                    data.add(null);
                    data.add(null);
                }

                if(i < gnssSampleList.size()){
                    data.add(String.valueOf(gnssSampleList.get(i).getRelativeTimestamp()));
                    data.add(String.valueOf(gnssSampleList.get(i).getLatitude()));
                    data.add(String.valueOf(gnssSampleList.get(i).getLongitude()));
                    data.add(String.valueOf(gnssSampleList.get(i).getAltitude()));
                    data.add(String.valueOf(gnssSampleList.get(i).getAccuracy()));
                    data.add(String.valueOf(gnssSampleList.get(i).getSpeed()));
                    data.add(String.valueOf(gnssSampleList.get(i).getProvider()));
                }
                else{
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                    data.add(null);
                }

                if(i < wiFiSampleList.size()){
                    int j = 0;
                    List<TrajectoryOuterClass.Mac_Scan> macScanList = trajectoryBuilder.getWifiData(i).getMacScansList();
                    data.add(String.valueOf(wiFiSampleList.get(i).getRelativeTimestamp()));
                    while (j < macScanList.size()) {
                        data.add(String.valueOf(macScanList.get(j).getRelativeTimestamp()));
                        data.add(String.valueOf(macScanList.get(j).getMac()));
                        data.add(String.valueOf(macScanList.get(j).getRssi()));
                        data.add(String.valueOf(macScanList.get(j).getSsid()));
                        data.add(String.valueOf(macScanList.get(j).getFrequency()));
                        j++;
                    }

                    if(trajectory.getMaxWifiNum() - macScanList.size() != 0)
                        for (int temp = 0; temp < 5*(trajectory.getMaxWifiNum() - macScanList.size())+1; temp++)
                            data.add(null);
                }
                else{
                    for (int temp = 0; temp < wifi_gap_cell+1; temp++)
                        data.add(null);
                }
                csvWriter.writeNext(data.toArray(new String[0]));
                i++;
            }


            csvWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Returns the file object associated with this class.
     @return the file object
     */
    public File getFile(){
        return this.file;
    }
    /**
     Returns the absolute path of the CSV file associated with this class.
     @return the absolute path of the CSV file
     */
    public String getSCVFileAddress(){
        return this.CSVFile.getAbsolutePath();
    }

    /**
     Returns the byte array of the file associated with this class.
     @return the byte array of the file
     */
    public byte[] getFileByte(){
        return this.fileByte;
    }

    /**
     Returns the absolute path of the file associated with this class.
     @return the absolute path of the file
     */
    public String getAddress(){
        return this.file.getAbsolutePath();
    }
}
