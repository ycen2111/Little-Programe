package com.edinburgh.ewireless.method.HTTP;
/////////////////////////////////////////
//
// Module : Request
// Time created: 2023.3.22
// Author: Yijian Zheng
// This module is to send get or post data in android
//
/////////////////////////////////////////

import android.os.AsyncTask;


import com.edinburgh.ewireless.Class.HTTP.ResponseMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request extends AsyncTask<String, String, ResponseMessage> {

    public final String methodGET = "GET";
    public final String methodPOST = "POST";

    public interface AsyncResponse{
        void processFinish(ResponseMessage output);
    }

    public AsyncResponse delegate;

    @Override
    protected void onPostExecute(ResponseMessage result) {
        delegate.processFinish(result);
    }

    public Request(AsyncResponse delegate){
        this.delegate = delegate;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponseMessage doInBackground(String... params) {
        String urlString = params[0]; //URL to call
        String data = params[1];// data to post
        String method = params[2];
        OutputStream out = null;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (method.equals(methodPOST)) {
                urlConnection.setRequestProperty("Content-Length", Integer.toString(data.length()));
                urlConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(data.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

            }
            if (method.equals(methodGET)) {
//                Log.d("GET", "doInBackground: ");
            }
            int responseCode = urlConnection.getResponseCode();
            InputStream inputStream;
            if (responseCode >=200 && responseCode <=299){
                inputStream = urlConnection.getInputStream();

            }
            else{
                inputStream = urlConnection.getErrorStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            urlConnection.disconnect();
            String message = response.toString();
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setCode(responseCode);
            responseMessage.setMessage(message);
            return responseMessage;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
