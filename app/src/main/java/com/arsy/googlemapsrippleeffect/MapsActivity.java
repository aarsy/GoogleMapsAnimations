package com.arsy.googlemapsrippleeffect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arsy.maps_library.MapRadar;
import com.arsy.maps_library.MapRipple;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng = new LatLng(28.7938709, 77.1427639);
    private Context context;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private LocationTracker locationTrackObj;
    private MapRipple mapRipple;
    private MapRadar mapRadar;
    private Button startstoprippleBtn;
    private final int ANIMATION_TYPE_RIPPLE = 0;
    private final int ANIMATION_TYPE_RADAR = 1;
    private int whichAnimationWasRunning = ANIMATION_TYPE_RIPPLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationTrackObj = new LocationTracker(context);
        if (!locationTrackObj.canGetLocation()) {
            locationTrackObj.showSettingsAlert();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
        }
        startstoprippleBtn = (Button) findViewById(R.id.startstopripple);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeMap(mMap);
                } else {
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                initializeMap(mMap);
            }
        } else {
            initializeMap(mMap);
        }


    }

    private void initializeMap(GoogleMap mMap) {
        if (mMap != null) {
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setMyLocationEnabled(true);
            Location location = mMap.getMyLocation();
            if (location == null)
                location = locationTrackObj.getLocation();
            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(location.getLatitude(),
                        location.getLongitude()), 14));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (location != null)
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            else {
                latLng = new LatLng(0.0, 0.0);
            }
            mapRipple = new MapRipple(mMap, latLng, context);

//            mapRipple.withNumberOfRipples(3);
//            mapRipple.withFillColor(Color.parseColor("#FFA3D2E4"));
//            mapRipple.withStrokeColor(Color.BLACK);
//            mapRipple.withStrokewidth(0);      // 10dp
//            mapRipple.withDistance(2000);      // 2000 metres radius
//            mapRipple.withRippleDuration(12000);    //12000ms
//            mapRipple.withTransparency(0.5f);
            mapRipple.startRippleMapAnimation();


            mapRadar = new MapRadar(mMap, latLng, context);
            //mapRadar.withClockWiseAnticlockwise(true);
            mapRadar.withDistance(2000);
            mapRadar.withClockwiseAnticlockwiseDuration(2);
            //mapRadar.withOuterCircleFillColor(Color.parseColor("#12000000"));
            mapRadar.withOuterCircleStrokeColor(Color.parseColor("#fccd29"));
            //mapRadar.withRadarColors(Color.parseColor("#00000000"), Color.parseColor("#ff000000"));  //starts from transparent to fuly black
            mapRadar.withRadarColors(Color.parseColor("#00fccd29"), Color.parseColor("#fffccd29"));  //starts from transparent to fuly black
            //mapRadar.withOuterCircleStrokewidth(7);
            //mapRadar.withRadarSpeed(5);
            mapRadar.withOuterCircleTransparency(0.5f);
            mapRadar.withRadarTransparency(0.5f);
        }
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }

            return false;
        } else {
            return true;
        }
    }

    public void startstopAnimation(View view) {
        if (mapRadar.isAnimationRunning() || mapRipple.isAnimationRunning()) {
            if (mapRadar.isAnimationRunning())
                mapRadar.stopRadarAnimation();
            if (mapRipple.isAnimationRunning())
                mapRipple.stopRippleMapAnimation();
            ((Button) view).setText("Start Animation");
        } else {
            if (whichAnimationWasRunning == ANIMATION_TYPE_RADAR)
                mapRadar.startRadarAnimation();
            else
                mapRipple.startRippleMapAnimation();
            startstoprippleBtn.setText("Stop Animation");
        }
    }

    public void advancedRipple(View view) {
        mapRadar.stopRadarAnimation();
        mapRipple.stopRippleMapAnimation();
        mapRipple.withNumberOfRipples(3);
        mapRipple.withFillColor(Color.parseColor("#FFA3D2E4"));
        mapRipple.withStrokewidth(0);      //0dp
        mapRipple.startRippleMapAnimation();
        startstoprippleBtn.setText("Stop Animation");
        whichAnimationWasRunning = ANIMATION_TYPE_RIPPLE;
    }

    public void radarAnimation(View view) {
        mapRipple.stopRippleMapAnimation();
        mapRadar.startRadarAnimation();
        startstoprippleBtn.setText("Stop Animation");
        whichAnimationWasRunning = ANIMATION_TYPE_RADAR;
    }

    public void simpleRipple(View view) {
        mapRadar.stopRadarAnimation();
        mapRipple.stopRippleMapAnimation();
        mapRipple.withNumberOfRipples(1);
        mapRipple.withFillColor(Color.parseColor("#00000000"));
        mapRipple.withStrokeColor(Color.BLACK);
        mapRipple.withStrokewidth(10);      // 10dp
        mapRipple.startRippleMapAnimation();
        startstoprippleBtn.setText("Stop Animation");
        whichAnimationWasRunning = ANIMATION_TYPE_RIPPLE;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mapRipple.isAnimationRunning()) {
                mapRipple.stopRippleMapAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class LocationTracker implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        private boolean isGPSEnabled = false;

        // flag for network status
        private boolean isNetworkEnabled = false;

        // flag for GPS status
        private boolean canGetLocation = false;

        private Location location; // location
        private double latitude; // latitude
        private double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000; // 1 sec

        private final String TAG = "LocationTracker";
        // Declaring a Location Manager
        protected LocationManager locationManager;

        public LocationTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    this.canGetLocation = false;
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         */
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(LocationTracker.this);
            }
        }

        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS Settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Click on setting to enable and get location, please start app again after turning on GPS.");
            alertDialog.setCancelable(false);

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });


            // Showing Alert Message
            alertDialog.show();
        }

        Random rand = new Random();

        @Override
        public void onLocationChanged(Location location) {
            //            mapRipple.withNumberOfRipples(3);
            this.location = location;
//            Toast.makeText(context, "  " + location.getLatitude() + ",  " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            if (mapRipple.isAnimationRunning())
                mapRipple.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            if (mapRadar.isAnimationRunning())
                mapRadar.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            location = getLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
}