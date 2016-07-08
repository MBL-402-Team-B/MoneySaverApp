package edu.phoenix.mbl402.moneysaverapp;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    private static final String DEBUG_TAG = "DEBUG_PlaceDisplay";
    // borrowed class from http://javapapers.com/android/find-places-nearby-in-google-maps-using-google-places-apiandroid-app/

    JSONObject googlePlacesJson;
    GoogleMap googleMap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
        Log.i(DEBUG_TAG, "entered List");

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
            Log.i(DEBUG_TAG, "Places found: " + Integer.toString(googlePlacesList.size()));
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception in parsing return stream. " + e.toString());
        }
        Log.i(DEBUG_TAG, "exit List");
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        Log.i(DEBUG_TAG, "onPostExecute event triggered");
//        googleMap.clear();
        // Remarked out clear to keep all previously placed markers.
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            googleMap.addMarker(markerOptions);
        }
        Log.i(DEBUG_TAG, "Placed " + Integer.toString(list.size()) + " markers on map.");
    }
}