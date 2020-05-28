package com.bignerdranch.android.musicpro;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VenueFetchr {
    private static final String TAG = "VenueFetchr";

    // Method to perform HTTP connection to the API server
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    // Method to return json string from response received
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    // This method will download the top rated venue from API
    public Venue downloadTopRatedVenue() {
        Venue item = new Venue();
        try {
            String url = Uri
                    .parse("http://jellymud.com/venues/best_venue.json")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(item, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return item;
    }

    // This method will set the values returned from API to Venue object
    private void parseItems(Venue item, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject venuesJsonObject = jsonBody;
        item.setName(venuesJsonObject.getString("name"));
        item.setAddress(venuesJsonObject.getString("address"));
        item.setLat(venuesJsonObject.getDouble("lat"));
        item.setLon(venuesJsonObject.getDouble("lon"));
    }
}

