package com.example.custom2.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface AppInfoDao {
    @Query("select * from appinfo where type=1")
    Flowable<List<AppInfo>> getBottomApps();
    @Query("select * from appinfo where type=2")
    Flowable<List<AppInfo>> getMainScreenApps();
    @Insert
    long insertApp(AppInfo appInfo);
    @Update
    void updateApp(AppInfo appInfo);
}
