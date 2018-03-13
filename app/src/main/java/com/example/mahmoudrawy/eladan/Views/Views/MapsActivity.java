package com.example.mahmoudrawy.eladan.Views.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mahmoudrawy.eladan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import com.example.mahmoudrawy.eladan.Views.Lisenters.MapLocationListener;
import com.example.mahmoudrawy.eladan.Views.Lisenters.MapsLisenter;
import com.example.mahmoudrawy.eladan.Views.Utilities.DataUtilities;
import com.example.mahmoudrawy.eladan.Views.Utilities.MapsUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, MapsLisenter {

    @BindView(R.id.IMG_location_icon)
    ImageView myLocation;
    @BindView(R.id.FBT_goToDetails)
    FloatingActionButton goToDetails;
    private GoogleMap mMap;
    private Context context;
    private LocationManager locationManager;
    private MapLocationListener mapLocationListener;
    private Location currentLocation;
    private Location userSelectedLocation;
    private ProgressDialog progressDialog;
    private MapsLisenter mapsLisenter;

    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        context = getApplicationContext();
        mapsLisenter = this;
        mapLocationListener = new MapLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        userSelectedLocation = MapsUtilities.createLocation(MapsUtilities.defaultLatitude,
                MapsUtilities.defaultLongitude);
        MapsUtilities.requestAccessLocationPermission(MapsActivity.this, context);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         if(MapsUtilities.isNetworkConnection(context))
             showMessageToUser(getString(R.string.message_when_map_and_no_connection));


    }
    ///////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        double locationLatitude = 0;
        double locationLongitude = 0;
        if (savedInstanceState.containsKey(MapsUtilities.MARKER_LOCATION_LATITUDE))
            locationLatitude = savedInstanceState.getDouble(MapsUtilities.MARKER_LOCATION_LATITUDE);
        if (savedInstanceState.containsKey(MapsUtilities.MARKER_LOCATION_LONGITUDE))
            locationLongitude = savedInstanceState.getDouble(MapsUtilities.MARKER_LOCATION_LONGITUDE);
        Location storedLocation = MapsUtilities.createLocation(locationLatitude, locationLongitude);
        userSelectedLocation = storedLocation;
        MapsUtilities.setUserSelectedLocation(storedLocation);

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(MapsUtilities.MARKER_LOCATION_LATITUDE, MapsUtilities
                .getUserSelectedLocation().getLatitude());
        outState.putDouble(MapsUtilities.MARKER_LOCATION_LONGITUDE, MapsUtilities
                .getUserSelectedLocation().getLongitude());
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        MapsUtilities.customizeMapStyle(mMap, context);
        MapsUtilities.setUserSelectedLocation(userSelectedLocation);
        MapsUtilities.drawMarker(userSelectedLocation, googleMap);
        LatLng latLng = new LatLng(userSelectedLocation.getLatitude(), userSelectedLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapsUtilities.CAMERA_ZOOM));
        if (!MapsUtilities.enableMapLocation(context, mMap)) {
            Toast.makeText(context, getString(R.string.permission_message),
                    Toast.LENGTH_LONG).show();
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapsUtilities.cameraMovingState == MapsUtilities.MOVING)
                    MapsUtilities.cameraMovingState = MapsUtilities.LOCATED;
                MapsUtilities.requestCurrentLocation(locationManager, mapLocationListener, MapsActivity.this);
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (MapsUtilities.cameraMovingState == MapsUtilities.MOVING) {
                    LatLng centerOfMap = mMap.getCameraPosition().target;
                    Location targetLocation = MapsUtilities.createLocation(centerOfMap.latitude, centerOfMap.longitude);
                    MapsUtilities.setUserSelectedLocation(targetLocation);
                    MapsUtilities.drawMarker(targetLocation, mMap);
                }
            }


        });
        ///////////////////////////////////////////////////////////////////////////////////////
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double clickedLatitude = latLng.latitude;
                double clickedLongitude = latLng.longitude;
                Location clickedLocation = MapsUtilities.createLocation(clickedLatitude, clickedLongitude);
                userSelectedLocation = clickedLocation;
                MapsUtilities.setUserSelectedLocation(clickedLocation);
                MapsUtilities.drawMarker(clickedLocation, googleMap);
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////
        goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapsUtilities.isNetworkConnection(context)) {
                    Date date = DataUtilities.getCurrentNoneFormatedDate();
                    String formatDate = DataUtilities.formatDate(date);
                    double latitude = MapsUtilities.getUserSelectedLocation().getLatitude();
                    double longitude = MapsUtilities.getUserSelectedLocation().getLongitude();
                    MapsUtilities.setCountryName(MapsActivity.this, latitude, longitude);
                    DataUtilities.getPrayerData(formatDate, latitude, longitude, mapsLisenter, context);
                } else
                    Toast.makeText(context, getString(R.string.connection_error),
                            Toast.LENGTH_LONG).show();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MapsUtilities.MY_PERMISSIONS_Access_Fine_Location: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!MapsUtilities.enableMapLocation(context, mMap)) {
                        Toast.makeText(context, getString(R.string.request_permission_message),
                                Toast.LENGTH_LONG).show();
                    }
                }

                return;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void playProgress() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();

    }
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void launchDetailsActivityWithData(Bundle bundle) {
        Intent intent = new Intent(MapsActivity.this, PrayerDetails.class);
        intent.putExtra(DataUtilities.BUNDLE_TAG, bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showMessageToUser(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG);
    }
    ///////////////////////////////////////////////////////////////////////////////////////
}
