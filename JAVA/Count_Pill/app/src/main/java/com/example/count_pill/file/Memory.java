package com.example.count_pill.file;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.LongDef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Memory {
    private static final String TAG = "Memory";
    private File file;
    private String fileName;
    private Boolean hasFile;

    public Memory(String fileName){
        this.fileName = fileName;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        this.file = new File(storageDir,this.fileName+".txt");

        if (this.file.exists()) {
            this.hasFile = true;
        }
        else{
            this.hasFile = false;
        }
    }

    public boolean hasFile(){
        return this.hasFile;
    }

    public File getFile(){
        return this.file;
    }

    public void save_file(String content) throws IOException {
        try {
            if (this.hasFile) {
                this.file.delete();
            }

            this.file.createNewFile();
            this.hasFile = true;
            Log.d(TAG, "save_file: new file created" + file.getAbsolutePath());

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "gbk");
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "save_file: new file created" + file.getAbsolutePath() + "failed");
        }
    }

    public String read_file() throws IOException{
        try {
            if (this.hasFile) {
                BufferedReader reader = new BufferedReader(new FileReader(this.file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

                return content.toString();
            } else {
                Log.d(TAG, "read_file: no file found: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "read_file: process failed");
        }
        return null;
    }

    public void delete_file() throws  IOException{
        try{
            if (this.hasFile) {
                this.file.delete();
                this.hasFile = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "delete_file: process failed");
        }
    }
}
