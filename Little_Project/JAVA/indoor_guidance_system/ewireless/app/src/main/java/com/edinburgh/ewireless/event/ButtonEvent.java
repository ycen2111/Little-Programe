package com.edinburgh.ewireless.event;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.edinburgh.ewireless.Class.HTTP.CreateUser;
import com.edinburgh.ewireless.Class.HTTP.ResponseMessage;
import com.edinburgh.ewireless.Class.FileStorage.DownloadFile;
import com.edinburgh.ewireless.Class.PDR.TrajectoryLocationOut;
import com.edinburgh.ewireless.Class.PDR.TrajectoryOut;
import com.edinburgh.ewireless.activity.MapActivity;
import com.edinburgh.ewireless.activity.ProgrammerActivity;
import com.edinburgh.ewireless.activity.WebViewActivity;
import com.edinburgh.ewireless.constant.HttpInformation;
import com.edinburgh.ewireless.method.HTTP.DownloadFileTask;
import com.edinburgh.ewireless.method.HTTP.Request;
import com.edinburgh.ewireless.method.HTTP.SendBinaryFileTask;
import com.edinburgh.ewireless.method.System.ToastShow;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: yijianzheng
 * Date: 23/03/2023 17:32
 *
 * Notes: ButtonEvent execute all button event
 */
public class ButtonEvent {
    Context context;

    public ButtonEvent(Context context){
        this.context = context;
    }

    HttpInformation httpInformation = new HttpInformation();

    /**
     * Method for creating a new user
     * @param toastShow the instance of ToastShow used to show Toast messages
     */
    public void createUser(ToastShow toastShow){
        CreateUser createUser = new CreateUser();
        createUser.setUserName("Bing_Chilling");
        Gson gson = new Gson();
        String jsonString = gson.toJson(createUser);
        //request.execute(url, jsonString);
        Request request = new Request(new Request.AsyncResponse() {
            @Override
            public void processFinish(ResponseMessage output) {
                if (output.getCode()>=200 && output.getCode() <=299){
                    toastShow.toastShow("User created");
                }
                else{
                    toastShow.toastShow("Creating new user failed");
                    Log.e("Create new user failed", String.valueOf(output.getMessage()));
                }
            }
        });
        request.execute(httpInformation.CREATE_USER, jsonString, request.methodPOST);
    }

    /**
     * Method for sending a trajectory
     * @param filePath the path of the file to be sent
     * @param toastShow the instance of ToastShow used to show Toast messages
     */
    public void sendTrajectory(String filePath, ToastShow toastShow){
        SendBinaryFileTask asyncTask = new SendBinaryFileTask(toastShow);
        asyncTask.execute(httpInformation.UPLOAD_SCV_FILE, filePath);
        toastShow.toastShow("New file uploading.......");
        Log.d("File upload", "sendTrajectory: "+filePath);
        //new SendBinaryFileTask().execute(httpInformation.CREATE_UPLOAD_FILE, filePath);
    }

    /**
     * Method for getting a trajectory
     * @param filePath the path of the file to be downloaded
     * @param toastShow the instance of ToastShow used to show Toast messages
     * @param downloadFile the instance of DownloadFile used to download the file
     */
    public void getTrajectory(String filePath, ToastShow toastShow, DownloadFile downloadFile){
        DownloadFileTask downloadFileTast = new DownloadFileTask(toastShow, downloadFile);
        downloadFileTast.execute(httpInformation.DOWNLOAD_ZIP_FILE, filePath);
        toastShow.toastShow("Downloading the trajectory.....");
    }

    /**

     This method sends a request to the server to get a list of trajectory information for a particular user.
     If the response code is in the success range, it parses the response message and displays the trajectory information
     to the user through a custom dialog.
     Otherwise, it displays a toast message to indicate that the request failed.
     @param toastShow an object that provides the ability to display toast messages to the user
     */
    public void getAllUserTrajectoryInfo(ToastShow toastShow){
        CreateUser createUser = new CreateUser();
        createUser.setUserName("Bing_Chilling");
        Gson gson = new Gson();
        String jsonString = gson.toJson(createUser);
        Request request = new Request(new Request.AsyncResponse() {
            @Override
            public void processFinish(ResponseMessage output) {
                if (output.getCode()>=200 && output.getCode() <=299){
                    toastShow.toastShow("Read trajectory list success");
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(output.getMessage()).getAsJsonArray();
                    Log.d("Read trajectory list success", String.valueOf(jsonArray));

                    ArrayList<String> item = new ArrayList<>();
                    for (JsonElement jsonElement : jsonArray){
                        String info = "Id: " + jsonElement.getAsJsonObject().get("id").getAsString() +
                                "\nTime: " + jsonElement.getAsJsonObject().get("date_submitted").getAsString()+
                                "\n------------------------------------------------------------";
                        item.add(info);
                    }

                    UserEnterEvent userEnterEvent = new UserEnterEvent();
                    userEnterEvent.cloudTrajectorySelect(context, item.toArray(new String[0]), jsonArray);
                }
                else{
                    toastShow.toastShow("Read trajectory list failed");
                    Log.e("Read trajectory list failed", String.valueOf(output.getMessage()));
                }
            }
        });
        request.execute(httpInformation.READ_USER_TRAJECTORIES,jsonString, request.methodGET);
        toastShow.toastShow("Reading Trajectory......");
    }

    /**
     * Retrieves all user trajectory information and displays it in a list format.
     * @param toastShow An instance of ToastShow used to display toast messages.
     */
    public void trajectoryDraw(TrajectoryOut trajectoryOut, ToastShow toastShow){
        Gson gson = new Gson();
        String jsonString = gson.toJson(trajectoryOut);
        Request request = new Request(new Request.AsyncResponse() {
            @Override
            public void processFinish(ResponseMessage output) {
                if (output == null){
                    Log.e("Trajectory generation failed" , "Output is null");
                    toastShow.toastShow("Trajectory generation failed" + "Output is null");
                }
                else if (output.getCode()>=200 && output.getCode() <=299){
                    toastShow.toastShow("Trajectory loading.....");
                    String httpString = output.getMessage();
                    Log.d("Trajectory generation success ", String.valueOf(httpString));
                    //openWebViewActivity(httpString);
                    openInExternalBrowser(httpString);

                }
                else{
                    toastShow.toastShow("Trajectory generation failed");
                    Log.e("Trajectory generation failed", String.valueOf(output.getMessage()));
                }
            }
        });
        request.execute(httpInformation.TRAJECTORY, jsonString, request.methodPOST);
    }

    /**
     * Start ProgrammerActivity.
     */
    public void programmerActivity(){
        Intent intent = new Intent(context, ProgrammerActivity.class);
        context.startActivity(intent);
    }

    /**
     * Opens the MapActivity and passes the coordinates to it.
     * @param trajectoryLocationOut An instance of TrajectoryLocationOut that contains the locations.
     * @param toastShow An instance of ToastShow for displaying toast messages.
     */
    public void openMap(TrajectoryLocationOut trajectoryLocationOut, ToastShow toastShow){
        ArrayList<LatLng> coordinates = new ArrayList<>();
        if (trajectoryLocationOut.getLocations().isEmpty()){
            toastShow.toastShow("Trajectory is empty");
        }
        else if (Math.abs(trajectoryLocationOut.getLocations().get(0)[0]) < 1 ){
            toastShow.toastShow("Trajectory is not ready. Please try again!");
        }
        else {
            for (int i = 0; i < trajectoryLocationOut.getLocations().size(); i++) {
                coordinates.add(new LatLng(trajectoryLocationOut.getLocations().get(i)[0], trajectoryLocationOut.getLocations().get(i)[1]));
            }
            // Start MapActivity and pass the coordinates
            Intent intent = new Intent(context, MapActivity.class);
            intent.putParcelableArrayListExtra("coordinates", coordinates);
            context.startActivity(intent);
        }
    }

    /**
     * Opens a WebViewActivity with the given httpString.
     *
     * @param httpString the URL to load in the WebView
     */
    private void openWebViewActivity(String httpString) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("httpString", httpString);
        context.startActivity(intent);
    }

    /**
     * Opens the specified HTTP string in an external browser.
     * @param httpString the HTTP string to open
     */
    private void openInExternalBrowser(String httpString) {
        try {
            // Save the HTTP string to an HTML file
            File tempFile = File.createTempFile("webcontent", ".html", context.getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.write(httpString.getBytes());
            outputStream.close();

            // Create a content URI for the HTML file
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", tempFile);

            // Create an Intent to open the content URI in an external browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, contentUri);
            browserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Start the external browser activity
            context.startActivity(browserIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////
    //
    // Wifi detected
    // Usage:
    //
    //    if (isConnectedToWifi()) {
    //        // Upload the file using your preferred method (e.g., Retrofit, HttpURLConnection, etc.)
    //    } else {
    //        // Show a message to the user or retry later
    //    }
    ////////////////////////////////
    /**
     * Returns whether the device is currently connected to a Wi-Fi network.
     *
     * @return true if the device is connected to a Wi-Fi network, false otherwise
     */
    public boolean isConnectedToWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);;

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

}
