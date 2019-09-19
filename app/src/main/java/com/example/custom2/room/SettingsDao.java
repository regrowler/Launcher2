package com.example.custom2.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SettingsDao {
    @Query("SELECT * FROM setparams")
    Flowable<List<SetParams>> getparams();
    @Insert
    void initSettings(SetParams params);
    @Update
    void updateSettings(SetParams params);
}
