package com.example.mahmoudrawy.eladan.Views.NetworkUtilities;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PH-Data™ 01221240053 on 16/02/2018.
 */

public class PrayerServiceGenerator {
    ///////////////////////////////////////////////////////////////////////////////////
    public static final String BASE_URL = "http://api.aladhan.com/v1/timings/";
    public static final String DATE_PATH = "date";
    public static final String LATITUDE_PARAMETER = "latitude";
    public static final String LONGITUDE_PARAMETER = "longitude";
    ///////////////////////////////////////////////////////////////////////////////////
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
