package com.example.covislot;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils extends AsyncTask {
    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    static String jsonResponse = null;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    public static ArrayList<session> checkSlotAvailability(String pinCode, String date) {
        // Create URL object
        URL url = createUrl(pinCode, date);

        // Perform HTTP request to the URL and receive a JSON response back

        new Thread(new Runnable(){
            @Override
            public void run() {
                // Do network action in this function
                try {
                    jsonResponse = makeHttpRequest(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem making the HTTP request.", e);
                }
            }
        }).start();

        return extractFeatureFromJson(jsonResponse);
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String pin, String dt) {
        URL url = null;
        try {
            String stringUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pin
                    + "&date=" + dt;
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Slot JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a {@link SessionList} object that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<session> extractFeatureFromJson(String sessionJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(sessionJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding sessions to
        ArrayList<session> S = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(sessionJSON);

            // Extract the JSONArray associated with the key called "sessions",
            // which represents a list of centres.
            JSONArray centerArray = baseJsonResponse.getJSONArray("centers");

            // For each session in the centerArray
            for (int i = 0; i < centerArray.length(); i++) {

                // Get a single center at position i within the list of sessions
                JSONObject currentCentre = centerArray.getJSONObject(i);
                JSONArray sessionJson = currentCentre.getJSONArray("sessions");

                for (int j = 0; i < sessionJson.length(); j++) {
                    //TODO: Check working of JSON parsing mechanism.
                    JSONObject currentSession = sessionJson.getJSONObject(j);

                    // Extract the value for the keys
                    String si = currentSession.getString("session_id");
                    String date = currentSession.getString("date");
                    int cap = currentSession.getInt("available_capacity");
                    int ageLim = currentSession.getInt("min_age_limit");
                    String v = currentSession.getString("vaccine");
                    //String sl[] = currentSession.accumulate("slots",);

                    if(cap > 0) {
                        // Create a new {@link sessions} object
                        session s = new session(si, date, cap, ageLim, v);

                        // Add the new {@link sessions} to the list of sessions.
                        S.add(s);
                    }
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of sessions
        return S;
    }

}
