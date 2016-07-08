package edu.phoenix.mbl402.moneysaverapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

//import android.location.Location;

public class MapsActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PROXIMITY_RADIUS = 20000;
    private static final float MAX_ZOOM_FACTOR = 22;
    private static final String[] searchTypes = {"atm","bank"};
    private static final String UOP_WA_LOCATION = "47.468563,-122.246219";
    private static final String DEBUG_TAG = "DEBUG_MapsActivity";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;


    private static GoogleMap mMap;
    private static Location myLocation;
    private static float currentZoomFactor;
    private static Marker myLocationMarker;
    private static GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override // Activity
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DEBUG_TAG, "enter MapsActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myLocation = new Location(UOP_WA_LOCATION);
        Log.i(DEBUG_TAG, "myLocation (in onCreate) = " + myLocation.toString());
        currentZoomFactor = 12;
        Log.i(DEBUG_TAG, "exit MapsActivity.onCreate");
    }

    @Override // OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        Log.i(DEBUG_TAG, "enter OnMapReadyCallback.onMapReady");
        Log.i(DEBUG_TAG, "myLocation (in onMapReady) = " + myLocation.toString());
        mMap = googleMap;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(
                        this /* FragmentActivity */ ,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();

        Location testLocation =getLastKnownLocation();
        if (testLocation != null) myLocation = testLocation;
        Log.i(DEBUG_TAG, "myLocation (in onMapReady after getLastKnownLocation) = " + ((myLocation==null)?"null": myLocation.toString()));

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        moveMap();
        findPlaces();

        Log.i(DEBUG_TAG, "exit OnMapReadyCallback.onMapReady");
    }

    protected void createLocationRequest() {
        boolean permission_fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permission_coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!(permission_fine || permission_coarse)) {
            // Missing required permissions
            // TODO: Ask that permissions be turned on and restart app
            // There is a way to use the system to offer to turn on the required permissions.
            Log.i(DEBUG_TAG, "abort OnMapReadyCallback.onMapReady, no fine or coarse permission");
            return;
        }
        mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        // TODO: Location updates will not work until we get this fixed.
    }



    private Location getLastKnownLocation() {
        boolean permission_fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permission_coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!(permission_fine || permission_coarse)) {
            // Missing required permissions
            // TODO: Ask that permissions be turned on and restart app
            // There is a way to use the system to offer to turn on the required permissions.
            Log.i(DEBUG_TAG, "abort OnMapReadyCallback.onMapReady, no fine or coarse permission");
            return null;
        }

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        createLocationRequest();
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = location;
            }
        }
        mMap.setMyLocationEnabled(true);

        return bestLocation;
    }


    private void moveMap() {
        Log.i(DEBUG_TAG, "enter MapsActivity.moveMap");
        // Add a marker for myLocation and move/zoom the camera
        Log.i(DEBUG_TAG, "myLocation (in moveMap) = " + myLocation.toString());
        LatLng moveTarget = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        float currentZoomFactor = 12;
        String locationTitle = "University of Phoenix Western Washington";
        if (myLocationMarker != null) myLocationMarker.remove();
        myLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(moveTarget)
                .title(locationTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moveTarget, currentZoomFactor));

        Log.i(DEBUG_TAG, "exit MapsActivity.moveMap");
    }

    private void findPlaces() {
        Log.i(DEBUG_TAG, "enter MapsActivity.findPlaces");

        for(String sType:searchTypes) {
            String googlePlacesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + myLocation.getLatitude() + "," + myLocation.getLongitude() +
                    "&radius=" + PROXIMITY_RADIUS +
                    "&types=" + sType +
                    "&sensor=true" +
                    "&key=" + getResources().getString(R.string.google_maps_key);
            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            Object[] toPass = new Object[2];
            toPass[0] = mMap;
            toPass[1] = googlePlacesUrl;
            googlePlacesReadTask.execute(toPass);
        }

        Log.i(DEBUG_TAG, "exit MapsActivity.findPlaces");

    }

    @Override // com.google.android.gms.location.LocationListener
    public void onLocationChanged(Location location) {
        Log.i(DEBUG_TAG, "enter com.google.android.gms.location.LocationListener.onLocationChanged");
        myLocation = location;
        moveMap();
        Log.i(DEBUG_TAG, "exit com.google.android.gms.location.LocationListener.onLocationChanged");
    }

    @Override //GoogleApiClient.ConnectionCallbacks
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(DEBUG_TAG, "GoogleApiClient.ConnectionCallbacks.onConnected called");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(DEBUG_TAG, "GoogleApiClient.ConnectionCallbacks.onConnectionSuspended called, i=" + Integer.toString(i));

    }

    @Override // GoogleApiClient.OnConnectionFailedListener
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(DEBUG_TAG, "GoogleApiClient.OnConnectionFailedListener.onConnectionFailed called, connectionResult=" + connectionResult.toString());
    }




//    private final class MyLocationListener implements LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.i(DEBUG_TAG, "enter MyLocationListener.onLocationChanged");
//            myLocation = location;
//            moveMap();
//            Log.i(DEBUG_TAG, "exit MyLocationListener.onLocationChanged");
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.i(DEBUG_TAG, "enter MyLocationListener.onProviderDisabled - no code");
//            // called when the GPS provider is turned off (user turning off the GPS on the phone)
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.i(DEBUG_TAG, "enter MyLocationListener.onProviderEnabled - no code");
//            // called when the GPS provider is turned on (user turning on the GPS on the phone)
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.i(DEBUG_TAG, "enter MyLocationListener.onStatusChanged - no code");
//            // called when the status of the GPS provider changes
//            // status may be: OUT_OF_SERVICE, TEMPORARILY_UNAVAILABLE, or AVAILABLE
//        }
//    }

}
