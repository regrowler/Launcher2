package com.example.custom2.widget.weatherWidget.retrofit;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("icon")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
