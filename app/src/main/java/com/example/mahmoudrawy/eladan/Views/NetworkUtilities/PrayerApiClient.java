package com.example.mahmoudrawy.eladan.Views.NetworkUtilities;


import com.example.mahmoudrawy.eladan.Views.PrayerInstances.Prayer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PH-Data™ 01221240053 on 16/02/2018.
 */

public interface PrayerApiClient {
    @GET(PrayerServiceGenerator.BASE_URL + "{date}")
    Call<Prayer> getTimes(@Path(PrayerServiceGenerator.DATE_PATH) String date,
                          @Query(PrayerServiceGenerator.LATITUDE_PARAMETER) double latitude,
                          @Query(PrayerServiceGenerator.LONGITUDE_PARAMETER) double longitude);
}
