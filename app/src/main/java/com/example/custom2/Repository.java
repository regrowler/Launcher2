package com.example.custom2;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;

import com.example.custom2.appsScreen.AppsModel;
import com.example.custom2.mainScreen.MainScreenParameters;
import com.example.custom2.room.AppDatabase;
import com.example.custom2.settings.Settings;

public class Repository {
    private static Repository ourInstance = new Repository();
    private Settings settings;
    private MainScreenParameters parameters;
    private AppsModel appsModel;
    AppDatabase database;
    PackageManager packageManager;
    Context context;
    FragmentManager fragmentManager;
    MainActivity mainActivity;

    public static Repository getInstance() {
        if(ourInstance==null){
            ourInstance=new Repository();
        }
        return ourInstance;
    }
    public static int[] backs={
            R.drawable.back1_1,
            R.drawable.back2_2,
            R.drawable.back3_3,
            R.drawable.back4_4,
            R.drawable.back5_5,
            R.drawable.back6_6,
            R.drawable.back7_7
    };
    public static int[] fullbacks={
            R.drawable.back1,
            R.drawable.back2,
            R.drawable.back3,
            R.drawable.back4,
            R.drawable.back5,
            R.drawable.back6,
            R.drawable.back7
    };
    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainScreenParameters getParameters() {
        return parameters;
    }

    public void setParameters(MainScreenParameters parameters) {
        this.parameters = parameters;
    }

    public AppsModel getAppsModel() {
        return appsModel;
    }

    public void setAppsModel(AppsModel appsModel) {
        this.appsModel = appsModel;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public PackageManager getPackageManager() {
        return packageManager;
    }

    public void setPackageManager(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private Repository() {
    }
}
