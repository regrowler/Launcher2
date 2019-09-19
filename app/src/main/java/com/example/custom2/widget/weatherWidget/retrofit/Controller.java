package com.example.custom2.widget.weatherWidget.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    public static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "19f4dc2345c545881b416e6ef125b7cc";
    public static final String DEFAULT_UNITS = "metric";

    public static Api start(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Api.class);
//        Call<CurrentWeather> call = api.getCurrentWeather(latitude, longitude, apiKey, units);

    }

}
