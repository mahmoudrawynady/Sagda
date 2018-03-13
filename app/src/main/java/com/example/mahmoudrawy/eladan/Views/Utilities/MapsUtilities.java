package com.example.mahmoudrawy.eladan.Views.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.example.mahmoudrawy.eladan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by PH-Data™ 01221240053 on 07/03/2018.
 */

public class MapsUtilities {
    ///////////////////////////////////////////////////////////////////////////////////////
    public static final int MY_PERMISSIONS_Access_Fine_Location = 1;
    public static final int MARCH_SDK = 23;
    public static final int MIN_TIME = 0;
    public static final int MIN_DISTANCE = 0;
    public static final int CAMERA_ZOOM = 12;
    public static final int MOVING = 1;
    public static final int LOCATED = 0;
    public static final double defaultLatitude = 30.0006299;
    public static final double defaultLongitude = 30.973489;
    public static String countryName;
    public static final String MARKER_LOCATION_LATITUDE = "marker_latitude_now";
    public static final String MARKER_LOCATION_LONGITUDE = "marker_longitude_now";
    public static int CUSTOM_MAP_STYLE_RESOURCE = R.raw.map_style;
    public static int cameraMovingState = MapsUtilities.MOVING;
    private static Location currentLocation;
    private static GoogleMap googleMap;
    private static Location userSelectedLocation;

    /////////////////////////////////////////////////////////////////////////////////////
    public static void requestAccessLocationPermission(Activity activity, Context context) {
        if (Build.VERSION.SDK_INT >= MapsUtilities.MARCH_SDK) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MapsUtilities.MY_PERMISSIONS_Access_Fine_Location
                    );
                }
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MapsUtilities.MY_PERMISSIONS_Access_Fine_Location);
            }
        } else {

        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void makeAlertMessage(final Activity activity, final String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.location_enable_message))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.enable_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(action);
                                activity.startActivity(intent);
                            }
                        }).setNegativeButton(activity.getString(R.string.cancel_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static void drawMarker(Location location, GoogleMap googleMap) {
        if (googleMap != null) {
            googleMap.clear();
            if (MapsUtilities.cameraMovingState == MapsUtilities.LOCATED)
                MapsUtilities.cameraMovingState = MapsUtilities.MOVING;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static void drawMarkerWithCameraAnimation(Location location, GoogleMap googleMap) {
        drawMarker(location, googleMap);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapsUtilities.CAMERA_ZOOM));
        MapsUtilities.googleMap = googleMap;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void requestCurrentLocation(LocationManager locationManager, LocationListener locationListener,
                                              Activity activity) {
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!(isGPSEnabled || isNetworkEnabled)) {
            makeAlertMessage(activity, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        } else {

            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MapsUtilities.MIN_TIME, MapsUtilities.MIN_DISTANCE, locationListener);
                }
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MapsUtilities.MIN_TIME, MapsUtilities.MIN_DISTANCE, locationListener);
                }
            } else {
                requestAccessLocationPermission(activity, activity.getApplicationContext());
                requestCurrentLocation(locationManager, locationListener, activity);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static boolean enableMapLocation(Context context, GoogleMap googleMap) {
        MapsUtilities.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static void setCurrentLocation(Location location) {
        MapsUtilities.currentLocation = location;
        setUserSelectedLocation(location);

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static void setUserSelectedLocation(Location userSelsectedLocation) {
        MapsUtilities.userSelectedLocation = userSelsectedLocation;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static Location getUserSelectedLocation() {
        return userSelectedLocation;
    }

    public static void moveToCurrentLocation() {
        if (MapsUtilities.cameraMovingState == MapsUtilities.LOCATED) {
            if (currentLocation != null) {
                MapsUtilities.drawMarkerWithCameraAnimation(currentLocation, MapsUtilities.googleMap);
            }
        }

    }

    public static Location createLocation(double latitude, double longitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    public static Location getCurrentLocation() {
        return currentLocation;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    public static void customizeMapStyle(GoogleMap googleMap, Context context) {
        MapStyleOptions style = MapStyleOptions.
                loadRawResourceStyle(context, MapsUtilities.CUSTOM_MAP_STYLE_RESOURCE);
        googleMap.setMapStyle(style);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static void setCountryName(Activity activity, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                MapsUtilities.countryName = addresses.get(0).getCountryName();
            }
        } catch (IOException ignored) {
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isNetworkConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}