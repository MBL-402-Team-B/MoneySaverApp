package edu.phoenix.mbl402.moneysaverapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
// borrowed class from http://javapapers.com/android/find-places-nearby-in-google-maps-using-google-places-apiandroid-app/


    private static final String DEBUG_TAG  = "DEBUG_Http";
    public String read(String httpUrl) throws IOException {
        Log.i(DEBUG_TAG, "entered read");
        String httpData = "";
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(httpUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String result = httpURLConnection.getResponseMessage();
            Log.i(DEBUG_TAG, "Connection result: " + result);
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            httpData = stringBuilder.toString();
            Log.i(DEBUG_TAG, "Return data: " + httpData);
            bufferedReader.close();
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception - reading Http url " + e.toString());
        } // removed finally block due to Java 7 automatic resource management.
        return httpData;
    }
}