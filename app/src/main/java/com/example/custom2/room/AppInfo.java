package com.example.custom2.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;

import com.example.custom2.Repository;

@Entity
public class AppInfo {
    public String label;
    public String name="";
    @Ignore
    public Drawable icon;
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int type;//0-non/1-bottom/2-mainscreen
    @Ignore
    public AppInfo() {
    }

    public AppInfo(String label, String name, int id, int type) {
        this.label = label;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public AppInfo(String label, String name, Drawable icon, int id, int type) {
        this.label = label;
        this.name = name;
        this.icon = icon;
        this.id = id;
        this.type = type;
    }

    public AppInfo(AppInfo info){
        label=info.label;
        name=info.name;
        id=info.id;
        type=info.type;
        try {
            icon=Repository.getInstance().getPackageManager().getApplicationIcon(name);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
