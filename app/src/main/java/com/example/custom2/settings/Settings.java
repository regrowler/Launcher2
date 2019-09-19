package com.example.custom2.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.custom2.BR;
import com.example.custom2.GridImageView.ChooseBackgroundActivity;
import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.gridView.MainGridSize;
import com.example.custom2.room.SetParams;

import java.lang.reflect.Method;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.BLUETOOTH_SERVICE;
import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class Settings extends BaseObservable {


    @Bindable
    public float whiteAlpha;

    @Bindable
    public tempSettings tempSettings;
    @Bindable
    public SetParams setParams;
    private boolean size=false;
    private boolean pass=false;

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public com.example.custom2.settings.tempSettings getTempSettings() {
        return tempSettings;
    }

    public void setTempSettings(com.example.custom2.settings.tempSettings tempSettings) {
        this.tempSettings = tempSettings;
        notifyPropertyChanged(BR.tempSettings);
    }

    public SetParams getSetParams() {
        return setParams;
    }

    public void setSetParams(SetParams setParams) {
        this.setParams = setParams;
        if(Repository.getInstance().getParameters().getAdapter()!=null){
            Repository.getInstance().getParameters().getAdapter().setSize(setParams.gridsize);
        }else {
            Repository.getInstance().getParameters().setAdapter(setParams.gridsize);
        }

        notifyPropertyChanged(BR.setParams);
    }

    public float getWhiteAlpha() {
        return whiteAlpha;
    }

    public void setWhiteAlpha(float whiteAlpha) {
        this.whiteAlpha = whiteAlpha;
        notifyPropertyChanged(BR.whiteAlpha);
    }


    @SuppressLint("CheckResult")
    public Settings() {
        tempSettings = new tempSettings();
        updateTempSettings();
        Repository.getInstance().getDatabase().getSettingsDao()
                .getparams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(s->{
                    Log.d("err","sets");
                })
                .subscribe(
                        s->{
                            if(s.size()==0){
                                Thread thread=new Thread(){
                                    @Override
                                    public void run() {
                                        SetParams s=new SetParams();
                                        s.gridsize= 2;
                                        s.password=0;
                                        s.weather=0;
                                        s.widgets=1;
                                        s.passwordS="";
                                        Repository.getInstance().getDatabase().getSettingsDao()
                                                .initSettings(s);
                                    }
                                };
                                thread.start();
                            }else {
                                setSetParams(s.get(0));
                                Repository.getInstance().getMainActivity().findViewById(R.id.setgen).invalidate();
                            }
                        }
                );
        this.whiteAlpha = (float) 0.0;
    }
    public void updateWidgets(boolean b){
        float scale=Repository.getInstance().getContext().getResources().getDisplayMetrics().density;
        View weather=Repository.getInstance().getMainActivity().findViewById(R.id.weather);
        View weather1=Repository.getInstance().getMainActivity().findViewById(R.id.weather1);
        View search=Repository.getInstance().getMainActivity().findViewById(R.id.googleSearch);
        View search1=Repository.getInstance().getMainActivity().findViewById(R.id.googleSearch2);
        if(b){

            ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams) weather.getLayoutParams();
            params.height=(int)scale*90;
            weather.setLayoutParams(params);
            ConstraintLayout.LayoutParams params1=(ConstraintLayout.LayoutParams) search.getLayoutParams();
            params1.height=(int)scale*48;
            search.setLayoutParams(params1);
            weather1.setVisibility(View.VISIBLE);
            search1.setVisibility(View.VISIBLE);
        }else {
            ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams) weather.getLayoutParams();
            params.height=0;
            weather.setLayoutParams(params);
            ConstraintLayout.LayoutParams params1=(ConstraintLayout.LayoutParams) search.getLayoutParams();
            params1.height=0;
            search.setLayoutParams(params1);
            weather1.setVisibility(View.GONE);
            search1.setVisibility(View.GONE );
        }
    }
    public void wifiClick(View v) {
        WifiManager manager = (WifiManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WIFI_SERVICE);

        if (manager.isWifiEnabled()) {
            tempSettings.setWifi(false);
            manager.setWifiEnabled(false);
        } else {
            tempSettings.setWifi(true);
            manager.setWifiEnabled(true);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void brightClick(View v) {
        if (!android.provider.Settings.System.canWrite(Repository.getInstance().getContext().getApplicationContext())) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + Repository.getInstance().getContext().getPackageName()));
            Repository.getInstance().getContext().startActivity(intent);
        } else {
            int bt = android.provider.Settings.System.getInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
            bt++;
            if (bt < 64) {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 63);
            } else if (bt < 128) {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 127);
            } else if (bt < 192) {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 191);
            } else if (bt < 256) {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
            } else {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
            }
            int t = bt;
            tempSettings.setBright(t / 256.0 * 100);
        }
    }

    public void airClick(View v) {
        Intent intent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        Repository.getInstance().getContext().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashClick(View v) {
        CameraManager manager = (CameraManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(CAMERA_SERVICE);
        String[] torchs = {};
        ImageView imageView = (ImageView) v;
        try {
            torchs = manager.getCameraIdList();
            if (tempSettings.isFlash()) {
                manager.setTorchMode(torchs[0], false);
            } else {
                manager.setTorchMode(torchs[0], !false);

            }
            tempSettings.setFlash(!tempSettings.isFlash());
        } catch (Exception e) {
            Log.e("torch", e.toString());
        }
    }


    public void bluetoothClick(View v) {
        BluetoothManager manager = (BluetoothManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(BLUETOOTH_SERVICE);
        if (manager.getAdapter().isEnabled()) {
            tempSettings.setBluetooth(false);
            manager.getAdapter().disable();
        } else {
            tempSettings.setBluetooth(true);
            manager.getAdapter().enable();
        }
    }

    public void setClick(View v) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        Repository.getInstance().getContext().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void rotateClick(View v) {
        if (!android.provider.Settings.System.canWrite(Repository.getInstance().getContext().getApplicationContext().getApplicationContext())) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + Repository.getInstance().getContext().getApplicationContext().getPackageName()));
            Repository.getInstance().getContext().getApplicationContext().startActivity(intent);
        } else {
            if (android.provider.Settings.System.getInt(Repository.getInstance().getContext().getApplicationContext().getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                tempSettings.setRotate(false);
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getApplicationContext().getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);
            } else {
                android.provider.Settings.System.putInt(Repository.getInstance().getContext().getApplicationContext().getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 1);
                tempSettings.setRotate(false);
            }
        }
    }

    public void locationClick(View v) {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        Repository.getInstance().getContext().startActivity(intent);
    }

    public void backClick(View v) {
        Intent intent = new Intent(Repository.getInstance().getMainActivity(), ChooseBackgroundActivity.class);
        Repository.getInstance().getMainActivity().startActivity(intent);
    }

    public void soundClick(View v) {
        AudioManager manager = (AudioManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(AUDIO_SERVICE);
        switch (manager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                NotificationManager notificationManager =
                        (NotificationManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !notificationManager.isNotificationPolicyAccessGranted()) {

                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                    Repository.getInstance().getContext().startActivity(intent);
                } else {
                    manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                NotificationManager notificationManager1 =
                        (NotificationManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !notificationManager1.isNotificationPolicyAccessGranted()) {

                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                    Repository.getInstance().getContext().startActivity(intent);
                } else {
                    manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
        }
        switch (manager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                tempSettings.setSound(1);
                tempSettings.setsImg(R.drawable.mute);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:

                tempSettings.setsImg(R.drawable.vibration);
                tempSettings.setSound(2);
                break;
            case AudioManager.RINGER_MODE_NORMAL:

                tempSettings.setsImg(R.drawable.sound);
                tempSettings.setSound(3);
                break;
        }
    }

    public void dataClick(View v) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        Repository.getInstance().getContext().getApplicationContext().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hotspotClick(View v) {
        Log.d("hotspot", "click");

        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        Repository.getInstance().getContext().getApplicationContext().startActivity(intent);

//
    }



    public boolean wifiLOngClick(View v) {
        return false;
    }

    public boolean brightLOngClick(View v) {
        return false;
    }

    public boolean airLOngClick(View v) {
        return false;
    }

    public boolean flashLOngClick(View v) {
        return false;
    }

    public boolean bluetoothLOngClick(View v) {
        return false;
    }

    public boolean setLOngClick(View v) {
        return false;
    }

    public boolean rotateLOngClick(View v) {
        return false;
    }

    public boolean locationLOngClick(View v) {
        return false;
    }

    public boolean backLOngClick(View v) {
        return false;
    }

    public boolean soundLOngClick(View v) {
        return false;
    }

    public boolean dataLOngClick(View v) {
        return false;
    }

    public boolean hotspotLOngClick(View v) {

        Log.d("hotspot", "longclick");
        return false;
    }
    public void pasSwitchClick(View v){
        Log.d("click","passwitch");
        if(!pass){
            pass=true;
            Intent intent = new Intent(Repository.getInstance().getContext().getApplicationContext(), PassActivity.class);
            Repository.getInstance().getContext().getApplicationContext().startActivity(intent);
        }

    }

    public void weatherSwitchClick(View v){
        Log.d("click","weather");
    }

    public void widgetsSwitchClick(View v){
        Log.d("click","widgets");
    }

    public void gridSizeClick(View v){
        Log.d("click","grid");
        final String[] mCatsName = {"Огромный", "Средний", "Маленький"};
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Repository.getInstance().getMainActivity());
        builder.setTitle("Выбирите размер сетки"); // заголовок для диалога

        builder.setItems(mCatsName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                switch (item) {
                    case 0:
                        Repository.getInstance().getParameters().getAdapter().setSize(3);
                        Thread thread=new Thread(){

                            @Override
                            public void run() {
                                SetParams params=getSetParams();
                                params.gridsize=1;
                                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
                            }
                        };
                        size=false;
                        thread.start();
                        break;
                    case 1:
                        Repository.getInstance().getParameters().getAdapter().setSize(2);
                        thread=new Thread(){

                            @Override
                            public void run() {
                                SetParams params=getSetParams();
                                params.gridsize=2;
                                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
                            }
                        };
                        size=false;
                        thread.start();
                        break;
                    case 2:
                        Repository.getInstance().getParameters().getAdapter().setSize(1);
                        thread=new Thread(){

                            @Override
                            public void run() {
                                SetParams params=getSetParams();
                                params.gridsize=3;
                                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
                            }
                        };
                        size=false;
                        thread.start();
                        break;
                }
            }
        });
        builder.setCancelable(false);
        if(!size){
            builder.create().show();
            size=true;
        }

    }
    public void onPasChecked(CompoundButton compoundButton, boolean b){
        SetParams params=getSetParams();
        params.password=b? 1:0;
        Thread thread=new Thread(){
            @Override
            public void run() {
                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
            }
        };
        thread.start();
        Log.d("click","pas "+b);
    }
    public void onWidgetsChecked(CompoundButton compoundButton, boolean b){
        SetParams params=getSetParams();
        params.widgets=b? 1:0;
        Thread thread=new Thread(){
            @Override
            public void run() {
                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
            }
        };
        thread.start();
        updateWidgets(b);
        Log.d("click","widgets "+b);
    }
    public void onWeatherChecked(CompoundButton compoundButton, boolean b){
        SetParams params=getSetParams();
        params.weather=b? 1:0;
        Thread thread=new Thread(){
            @Override
            public void run() {
                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(params);
            }
        };
        thread.start();
        Log.d("click","weather "+b);
    }

    public void updateTempSettings() {
        AudioManager manager = (AudioManager) Repository.getInstance().getContext().getSystemService(AUDIO_SERVICE);
        switch (manager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                tempSettings.setSound(1);
                tempSettings.setsImg(R.drawable.mute);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:

                tempSettings.setsImg(R.drawable.vibration);
                tempSettings.setSound(2);
                break;
            case AudioManager.RINGER_MODE_NORMAL:

                tempSettings.setsImg(R.drawable.sound);
                tempSettings.setSound(3);
                break;
        }
        tempSettings.setAirplane(android.provider.Settings.Global.getInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) == 1);
        WifiManager manager2 = (WifiManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        tempSettings.setWifi(manager2.isWifiEnabled());
        tempSettings.setBright(android.provider.Settings.System.getInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 0) / 256.0 * 100);
        tempSettings.setRotate(android.provider.Settings.System.getInt(Repository.getInstance().getContext().getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
        BluetoothManager manager3 = (BluetoothManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(BLUETOOTH_SERVICE);
        tempSettings.setBluetooth(manager3.getAdapter().isEnabled());
        LocationManager manager4 = (LocationManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
        tempSettings.setLocation(manager4.isProviderEnabled(LocationManager.GPS_PROVIDER));
        ConnectivityManager conMan = (ConnectivityManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] as = conMan.getAllNetworkInfo();
        boolean isOn = false;
        for (int i = 0; i < as.length; i++) {
            String s = as[i].getTypeName();
            if (s.toLowerCase().contains("mobile")) {
                if (as[i].isConnected()) {
                    isOn = true;
                }

                break;
            }
        }
        tempSettings.setData(isOn);
        ConnectivityManager conMan2 = (ConnectivityManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] as2 = conMan.getAllNetworkInfo();
        WifiManager wifiManager = (WifiManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("getWifiApState");
            method.setAccessible(true);
            int actualState = (Integer) method.invoke(wifiManager, (Object[]) null);
            int y = 0;
            tempSettings.setHotspot(actualState == 12 || actualState == 13);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
