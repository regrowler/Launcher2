package com.example.custom2.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SetParams {
    @PrimaryKey
    public int id;

    public int password;
    public String passwordS;
    public int widgets;
    public int weather;
    public int gridsize;


}
