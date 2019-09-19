package com.example.custom2.settings;

import android.arch.persistence.room.Ignore;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.example.custom2.BR;
import com.example.custom2.Repository;


public class tempSettings extends BaseObservable {
    @Bindable
    boolean wifi;
    @Bindable
    boolean airplane;
    @Bindable
    boolean flash;
    @Bindable
    boolean bluetooth;
    @Bindable
    double bright;
    @Bindable
    boolean location;
    @Bindable
    boolean rotate;
    @Bindable
    int sound;
    @Bindable
    boolean data;
    @Bindable
    boolean hotspot;
    @Bindable
    String br;

    @Bindable
    public Drawable sImg;

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
        notifyPropertyChanged(BR.wifi);
    }

    public Drawable getsImg() {
        return sImg;
    }

    public void setsImg(int sImg) {
        this.sImg = Repository.getInstance().getContext().getResources().getDrawable(sImg);
        notifyPropertyChanged(BR.sImg);
    }

    public boolean isAirplane() {
        return airplane;
    }

    public void setAirplane(boolean airplane) {
        this.airplane = airplane;
        notifyPropertyChanged(com.example.custom2.BR.airplane);
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
        notifyPropertyChanged(BR.br);
    }

    public boolean isFlash() {
        return flash;
    }

    public void setFlash(boolean flash) {
        this.flash = flash;
        notifyPropertyChanged(BR.flash);
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
        notifyPropertyChanged(BR.bluetooth);
    }

    public double isBright() {
        return bright;
    }

    public void setBright(double bright) {
        this.bright = bright;
        setBr(Math.round(bright)+"%");
        notifyPropertyChanged(BR.bright);
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
        notifyPropertyChanged(BR.rotate);
    }

    public int isSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
        notifyPropertyChanged(BR.sound);
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public boolean isHotspot() {
        return hotspot;
    }

    public void setHotspot(boolean hotspot) {
        this.hotspot = hotspot;
        notifyPropertyChanged(BR.hotspot);
    }
}
