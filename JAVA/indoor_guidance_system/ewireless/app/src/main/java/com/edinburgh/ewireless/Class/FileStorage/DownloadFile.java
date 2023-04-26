package com.edinburgh.ewireless.Class.FileStorage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.edinburgh.ewireless.Class.Trajectory.Trajectory;
import com.edinburgh.ewireless.event.ButtonEvent;
import com.edinburgh.ewireless.method.System.ToastShow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 This class is responsible for downloading a zip file from a server and unzipping it
 to extract JSON files that contain trajectory data. The JSON files are then parsed and
 converted into trajectory objects, which are used to create new files for each trajectory.
 The downloaded zip file is then deleted along with the extracted JSON files.
 */
public class DownloadFile {
    private static File file, storageDir;
    private ButtonEvent buttonEvent;
    private  Context context;

    /**
     * Constructor for DownloadFile class. Initializes variables and initiates file download.
     * @param context The context of the activity using this class.
     * @param toastShow An instance of ToastShow used for displaying toasts.
     */
    public DownloadFile(Context context, ToastShow toastShow){
        this.context = context;
        this.buttonEvent = new ButtonEvent(context);
        this.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        this.file = new File(storageDir,"data.zip");
        if(file.exists())
            file.delete();
        buttonEvent.getTrajectory(file.getAbsolutePath(), toastShow, this);
        toastShow.toastShow("File downloading...");
        Log.d("DownloadFile", "path: "+storageDir.getAbsolutePath());
    }

    /**
     * Unzips the downloaded zip file and extracts JSON files containing trajectory data.
     * Each JSON file is then converted into a trajectory object and used to create a new file.
     * The extracted JSON files and the downloaded zip file are then deleted.
     * @throws IOException
     */
    public void unzip() throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file.getAbsolutePath()));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            String filePath = storageDir.getAbsolutePath() + "/" + fileName;
            FileOutputStream fos = new FileOutputStream(filePath);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        Log.d("DownloadFile", "File unzipped");

        File[] JSONFiles = getJSONFile();

        for (File JSONFile : JSONFiles){
            Trajectory trajectory = new Trajectory(System.currentTimeMillis());
            List<double[]> data = new ArrayList<>();
            data = readJSONFile(JSONFile);

            for (int step = 0; step < data.size(); step++){
                double[] dataGroup = data.get(step);
                trajectory.addPdrSample((float)dataGroup[0], (float)dataGroup[1], (float)dataGroup[2], step);
            }

            CreateFile createFile = new CreateFile();
            createFile.createNewFile(JSONFile.getName(), trajectory, context, false);

            new DeleteFile(JSONFile);
        }

        new DeleteFile(file);
        Log.d("DownloadFile", "Zip file deleted");
    }

    /**
     Returns an array of File objects that represent JSON files in the storage directory.
     @return an array of File objects that represent JSON files in the storage directory
     */
    private File[] getJSONFile(){
        return storageDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
    }

    /**
     Reads a JSON file and returns a list of double arrays containing the data.
     @param file the JSON file to read
     @return a list of double arrays containing the data
     */
    private List<double[]> readJSONFile(File file){
        Gson gson = new Gson();
        List<double[]> data = null;
        try {
            data = gson.fromJson(new FileReader(file.getAbsolutePath()), new TypeToken<List<double[]>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }
}
