package com.example.mahmoudrawy.eladan.Views.Utilities;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.mahmoudrawy.eladan.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mahmoudrawy.eladan.Views.Lisenters.MapsLisenter;
import com.example.mahmoudrawy.eladan.Views.NetworkUtilities.ApiError;
import com.example.mahmoudrawy.eladan.Views.NetworkUtilities.ErrorUtils;
import com.example.mahmoudrawy.eladan.Views.NetworkUtilities.PrayerApiClient;
import com.example.mahmoudrawy.eladan.Views.NetworkUtilities.PrayerServiceGenerator;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Data;
import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Prayer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataUtilities {
    ///////////////////////////////////////////////////////////////////////////////////////
    public static final String FORMAT_PATTERN = "dd-MM-yyyy";
    public static final String BUNDLE_TAG = "bundle";
    public static final String DATA_TAG = "data";
    public static final String COUNTRY_TAG = "country";
    public static int responseState = 0;
    public static Data prayerData;
    ///////////////////////////////////////////////////////////////////////////////////////

    public static String formatDate(Date currentDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DataUtilities.FORMAT_PATTERN);
        String formatDate = dateFormat.format(currentDate);
        return formatDate;
    }
    //////////////////////////////////////////////////////////////////////////////////////

    public static void setPrayerData(Data prayerData) {
        DataUtilities.prayerData = prayerData;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public static void getPrayerData(String currentDate, double latitude, double longitude,
                                     final MapsLisenter mapsLisenter, final Context context) {
        mapsLisenter.playProgress();
        PrayerApiClient prayerApiClient = PrayerServiceGenerator.createService(PrayerApiClient.class);
        Call<Prayer> call = prayerApiClient.getTimes(currentDate, latitude, longitude);
        call.enqueue(new Callback<Prayer>() {
            @Override
            public void onResponse(Call<Prayer> call, Response<Prayer> response) {
                mapsLisenter.hideProgress();
                if (response.isSuccessful()) {
                    DataUtilities.setPrayerData(response.body().getData());
                    DataUtilities.responseState = 1;
                    mapsLisenter.launchDetailsActivityWithData(fillPrayerDataToBundle());
                } else {
                    DataUtilities.responseState = 0;
                    ApiError error = ErrorUtils.parseError(response);

                    if (error.status() == ApiError.NOT_FOUND_STATUS_CODE) {
                        mapsLisenter.showMessageToUser(context.getString(R.string.not_found));
                    } else if (error.status() == ApiError.INTERNAL_SERVER_ERROR_STATUS_CODE) {
                        mapsLisenter.showMessageToUser(context.getString(R.string.server_error));
                    } else if (error.status() == ApiError.CONNECTION_TIME_OUT_STATUS_CODE) {
                        mapsLisenter.showMessageToUser(context.getString(R.string.connection_time_out));
                    } else if (error.status() == ApiError.SERVICE_UNAVAILABLE_STATUS_CODE) {
                        mapsLisenter.showMessageToUser(context.getString(R.string.service_unavailable));
                    }

                }
            }

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void onFailure(Call<Prayer> call, Throwable t) {
                mapsLisenter.hideProgress();
                DataUtilities.responseState = 0;
                if (t instanceof IOException)
                    mapsLisenter.showMessageToUser(context.getString(R.string.connection_error));
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Date getCurrentNoneFormatedDate() {
        Date date = new Date();
        return date;
    }
    //////////////////////////////////////////////////////////////////////////////////////


    private static Bundle fillPrayerDataToBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataUtilities.DATA_TAG, DataUtilities.prayerData);
        bundle.putString(DataUtilities.COUNTRY_TAG, MapsUtilities.countryName);
        return bundle;
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    public static void setActivityActionBar(ActionBar activityActionBar, String barTitle) {
        activityActionBar.setDisplayHomeAsUpEnabled(true);
        activityActionBar.setTitle(barTitle);
    }
    //////////////////////////////////////////////////////////////////////////////////////

}
