package com.edinburgh.ewireless.method.HTTP;

/**
 * Author: yijianzheng
 * Date: 18/04/2023 20:44
 * <p>
 * Notes:
 */
import android.os.AsyncTask;
import android.util.Log;

import com.edinburgh.ewireless.Class.FileStorage.DeleteFile;
import com.edinburgh.ewireless.constant.Constant;
import com.edinburgh.ewireless.method.System.ToastShow;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SendBinaryFileTask extends AsyncTask<String, Void, String> {


    private static final String TAG = "SendBinaryFileTask";

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Constant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS)
            .build();

    ToastShow toastShow;

    public SendBinaryFileTask(ToastShow toastShow) {
        this.toastShow = toastShow;
    }


    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String filePath = params[1];

        //Log.d(TAG, "doInBackground: " + filePath);
        try {
            // Read the binary file from the app-specific storage directory
            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(filePath);
            if (file == null){
                Log.d(TAG, "doInBackground: file is null");
            }
            // Create a request body from the file
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            // Create a multipart POST request
            Request request = new Request.Builder()
                    .url(url)
                    .post(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addPart(filePart)
                            .build())
                    .build();

            // Send the request and get the response
            Response response = client.newCall(request).execute();
            new DeleteFile(filePath);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result of the POST request (e.g., update the UI)
        if (result != null) {
            // The request was successful
            Log.d(TAG, "onPostExecute Sending success: " + result);
            if (result.contains("b'{\\\"detail\\\":\\\"imu_data: Field time is too short")){
                toastShow.toastShow("Failed! Field time at least 30s!");
            }
            else {
                //toastShow.toastShow(result.replace("b'{\\\"detail\\\":\\\"","").replace("\\\"}'\\n",""));
                toastShow.toastShow("Upload successful");
            }

        } else {
            // An error occurred
            Log.e(TAG, "onPostExecute: " + "Failed to send the binary file");
            toastShow.toastShow("Time out");
        }
    }
}

