package com.edinburgh.ewireless.Class.FileStorage;

import android.util.Log;

import com.edinburgh.ewireless.Class.Trajectory.timeStampToStringTime;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 A FileManager class for managing files in a specified directory.
 */
public class FileManager {
    private File[] files;
    private String[] filesInfo;
    private File environmentDir;
    private boolean hasFile = false;

    /**
     * Constructor to initialize the FileManager with a specified directory.
     * @param environmentDir The directory to be managed by the FileManager.
     */
    public FileManager(File environmentDir){
        this.environmentDir = environmentDir;
        getSavedFile();
    }

    /**
     * Method to retrieve all files in the specified directory with a ".pkt" extension.
     */
    private void getSavedFile(){
        this.files = environmentDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".pkt");
            }
        });
        ArrayList<String> arrayList = new ArrayList<String>();

        if(this.files.length == 0)
            return;

        Log.d("File read", "getSavedFile" + this.files.length);
        for (File file : this.files) {
            arrayList.add("Name: " + file.getName() +
                    "\nSize: " + file.length() / 1024 + "KB" +
                    "\nCreate time: " + timeStampToStringTime.yyyymmddhhmmss(file.lastModified()) +
                    "\n-------------------------------------------");
        }
        this.filesInfo = arrayList.toArray(new String[arrayList.size()]);
        this.hasFile = true;
    }

    /**
     * Method to retrieve all files managed by the FileManager.
     * @return An array of files managed by the FileManager.
     */
    public File[] getFiles(){
        return this.files;
    }

    /**
     * Method to retrieve information about all files managed by the FileManager.
     * @return An array of strings containing information about all files managed by the FileManager.
     */
    public String[] getFilesInfo(){
        return this.filesInfo;
    }

    /**
     * Method to retrieve a specific file managed by the FileManager.
     * @param num The index of the file to be retrieved.
     * @return The file at the specified index.
     */
    public File getTargetFileByNumber(int num){
        return files[num];
    }

    /**
     * Method to check if the FileManager has any files.
     * @return true if the FileManager has files, false otherwise.
     */
    public boolean isHasFile(){
        return this.hasFile;
    }
}
