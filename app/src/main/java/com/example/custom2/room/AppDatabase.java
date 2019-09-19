package com.example.custom2.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities ={AppInfo.class,SetParams.class},version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppInfoDao getAppInfoDao();
    public abstract SettingsDao getSettingsDao();
}
