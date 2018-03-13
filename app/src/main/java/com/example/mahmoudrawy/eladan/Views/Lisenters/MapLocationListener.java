package com.example.mahmoudrawy.eladan.Views.Lisenters;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.mahmoudrawy.eladan.Views.Utilities.MapsUtilities;


public class MapLocationListener implements LocationListener {
    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onLocationChanged(Location location) {
        if (MapsUtilities.cameraMovingState == MapsUtilities.LOCATED) {
            if (location != null) {
                MapsUtilities.setCurrentLocation(location);
                MapsUtilities.setUserSelectedLocation(location);
                MapsUtilities.moveToCurrentLocation();
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    ///////////////////////////////////////////////////////////////////////////////////
}
