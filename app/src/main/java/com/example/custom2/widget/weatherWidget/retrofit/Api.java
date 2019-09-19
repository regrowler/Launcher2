package com.example.custom2.widget.weatherWidget.retrofit;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface Api {

    @GET("weather")
    Observable<CurrentWeather> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,

            @Query("appid") String apiKey,
            @Query("units") String units
    );
}