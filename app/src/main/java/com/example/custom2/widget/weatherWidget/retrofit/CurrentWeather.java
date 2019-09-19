package com.example.custom2.widget.weatherWidget.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentWeather {

    @SerializedName("main")
    private ForecastMain main;
   // private ForecastWind wind;

    @SerializedName("name")
    private String cityName;
    @SerializedName("weather")
    private List<Weather> weather;

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public ForecastMain getMain() {
        return main;
    }

    public void setMain(ForecastMain main) {
        this.main = main;
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
