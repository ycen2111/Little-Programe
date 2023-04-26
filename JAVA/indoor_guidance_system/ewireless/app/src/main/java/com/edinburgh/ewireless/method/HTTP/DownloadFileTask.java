package com.edinburgh.ewireless.method.HTTP;

/**
 * Author: yijianzheng
 * Date: 18/04/2023 21:22
 * <p>
 * Notes:
 */
import android.os.AsyncTask;
import android.util.Log;

import com.edinburgh.ewireless.Class.FileStorage.DownloadFile;
import com.edinburgh.ewireless.constant.Constant;
import com.edinburgh.ewireless.method.System.ToastShow;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class DownloadFileTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "DownloadFileTask";

    ToastShow toastShow;
    DownloadFile downloadFile;

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS)
            .build();

    public DownloadFileTask(ToastShow toastShow, DownloadFile downloadFile) {
        this.toastShow = toastShow;
        this.downloadFile = downloadFile;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String url = params[0];
        String filePath = params[1];

        try {
            // Create a GET request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            // Send the request and get the response
            Response response = client.newCall(request).execute();

            // Check if the response is successful
            if (!response.isSuccessful()) {
                return false;
            }

            // Save the file to the app-specific storage directory
            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //File file = new File(storageDir, filePath);
            File file = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = response.body().byteStream();
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        // Handle the result of the download (e.g., update the UI)
        if (success) {
            // The file was successfully downloaded
            toastShow.toastShow("File downloaded successfully");
            Log.d(TAG, "onPostExecute: " + "File downloaded successfully");
            try {
                this.downloadFile.unzip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // An error occurred
            toastShow.toastShow("Failed to download the file");
            Log.e(TAG, "onPostExecute: " + "Failed to download the file" );
        }
    }
}

