package com.example.custom2.mainScreen;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.custom2.BR;
import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.dialogs.ChangeApp;
import com.example.custom2.room.AppDatabase;
import com.example.custom2.room.AppInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainScreenParameters extends BaseObservable {
    private boolean isPopUpShown = false;
    @Bindable
    private boolean isWidgetsActive=true;
    @Bindable
    private float alpha;
    @Bindable
    float blackAlpha;
    @Bindable
    AppInfo bottom1;
    @Bindable
    AppInfo bottom2;
    @Bindable
    AppInfo bottom3;
    @Bindable
    AppInfo bottom4;
    @Bindable
    AppInfo bottom5;
    @Bindable
    MainGridAdapter adapter;

    List<AppInfo> mainScreen;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        notifyPropertyChanged(BR.alpha);
    }

    public float getBlackAlpha() {
        return blackAlpha;
    }

    public void setBlackAlpha(float blackAlpha) {
        this.blackAlpha = blackAlpha;
        notifyPropertyChanged(BR.blackAlpha);
    }

    public AppInfo getBottom1() {
        return bottom1;
    }

    public void setBottom1(AppInfo bottom1) {
        this.bottom1 = bottom1;
        notifyPropertyChanged(BR.bottom1);
    }

    public AppInfo getBottom2() {
        return bottom2;
    }

    public void setBottom2(AppInfo bottom2) {
        this.bottom2 = bottom2;
        notifyPropertyChanged(BR.bottom2);
    }

    public AppInfo getBottom3() {
        return bottom3;
    }

    public void setBottom3(AppInfo bottom3) {
        this.bottom3 = bottom3;
        notifyPropertyChanged(BR.bottom3);
    }

    public AppInfo getBottom4() {
        return bottom4;
    }

    public void setBottom4(AppInfo bottom4) {
        this.bottom4 = bottom4;
        notifyPropertyChanged(BR.bottom4);
    }

    public AppInfo getBottom5() {
        return bottom5;
    }

    public void setBottom5(AppInfo bottom5) {
        this.bottom5 = bottom5;
        notifyPropertyChanged(BR.bottom5);
    }

    public MainGridAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(int i) {
        this.adapter = new MainGridAdapter(i,mainScreen);
        notifyPropertyChanged(BR.adapter);
    }

    public boolean isWidgetsActive() {
        return isWidgetsActive;
    }

    public void setWidgetsActive(boolean widgetsActive) {
        isWidgetsActive = widgetsActive;
        notifyPropertyChanged(BR.isWidgetsActive);
    }

    @SuppressLint("CheckResult")
    public MainScreenParameters() {
        mainScreen=new ArrayList<>();
        Repository.getInstance().getDatabase().getAppInfoDao()
                .getMainScreenApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    if(s.size()>0){
                        mainScreen.clear();
                        for(AppInfo info:s){
                          mainScreen.add(new AppInfo(info));
                        }
                        //mainScreen.addAll(s);
                        adapter.notifyDataSetChanged();
                    }else {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 60; i++) {
                                    AppInfo appInfo = new AppInfo();
                                    appInfo.type = 2;
                                    Repository.getInstance().getDatabase().getAppInfoDao().insertApp(appInfo);
                                }
                            }
                        };
                        thread.start();
                    }
                });
                //setAdapter(new MainGridAdapter(Repository.getInstance().getSettings().getSetParams().gridsize,mainScreen));
        Repository.getInstance().getDatabase().getAppInfoDao()
                .getBottomApps()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.size() > 0) {
                        for(int i=0;i<s.size();i++){
                            if(i==0){
                                setBottom1(new AppInfo(s.get(i)));
                            }else if(i==1){
                                setBottom2(new AppInfo(s.get(i)));
                            }else if(i==2){
                                setBottom3(new AppInfo(s.get(i)));
                            }else if(i==3){
                                setBottom4(new AppInfo(s.get(i)));
                            }else if(i==4){
                                setBottom5(new AppInfo(s.get(i)));
                            }
                        }
                    } else {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 5; i++) {
                                    AppInfo appInfo = new AppInfo();
                                    appInfo.type = 1;
                                    Repository.getInstance().getDatabase().getAppInfoDao().insertApp(appInfo);
                                }
                            }
                        };
                        thread.start();
                    }


                });

        alpha = (float) 1.0;
        blackAlpha = (float) 0.0;
    }

    public boolean app1LongClick(View v) {
        showPopupMenu(v, bottom1);

        Log.d("app1", "longclick");
        return false;
    }

    public boolean app2LongClick(View v) {
        showPopupMenu(v, bottom2);

        Log.d("app2", "longclick");
        return false;
    }

    public boolean app3LongClick(View v) {
        showPopupMenu(v, bottom3);

        Log.d("app3", "longclick");
        return false;
    }

    public boolean app4LongClick(View v) {
        showPopupMenu(v, bottom4);

        Log.d("app4", "longclick");

        return false;
    }

    public boolean app5LongClick(View v) {
        showPopupMenu(v, bottom5);

        Log.d("app5", "longclick");
        return false;
    }

    public void app1Click(View v) {
        final Intent intent = Repository.getInstance().getPackageManager().getLaunchIntentForPackage(bottom1.name);
        Repository.getInstance().getContext().startActivity(intent);
        Log.d("app1", "click");
    }

    public void app2Click(View v) {
        final Intent intent = Repository.getInstance().getPackageManager().getLaunchIntentForPackage(bottom2.name);
        Repository.getInstance().getContext().startActivity(intent);
        Log.d("app2", "click");
    }

    public void app3Click(View v) {
        final Intent intent = Repository.getInstance().getPackageManager().getLaunchIntentForPackage(bottom3.name);
        Repository.getInstance().getContext().startActivity(intent);
        Log.d("app3", "click");
    }

    public void app4Click(View v) {
        final Intent intent = Repository.getInstance().getPackageManager().getLaunchIntentForPackage(bottom4.name);
        Repository.getInstance().getContext().startActivity(intent);
        Log.d("app4", "click");
    }

    public void app5Click(View v) {
        final Intent intent = Repository.getInstance().getPackageManager().getLaunchIntentForPackage(bottom5.name);
        Repository.getInstance().getContext().startActivity(intent);
        Log.d("app5", "click");
    }

    private void showPopupMenu(View v, AppInfo pos) {
        if (!isPopUpShown) {
//            Log.d("popup")
            isPopUpShown = true;
            PopupMenu popupMenu = new PopupMenu(Repository.getInstance().getContext(), v, Gravity.TOP);
            popupMenu.inflate(R.menu.popupmenu);

            popupMenu
                    .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.bottomPopupChange:
                                    DialogFragment dialogFragment = new ChangeApp();
                                    Bundle args = new Bundle();
                                    args.putInt("comma", 0);
                                    args.putInt("pos", pos.id);
                                    dialogFragment.setArguments(args);
                                    dialogFragment.show(Repository.getInstance().getFragmentManager(), "");
                                    isPopUpShown = false;
                                    return true;
                                case R.id.bottomPopupClear:
                                    AppInfo info = new AppInfo();
                                    info.id = pos.id;
                                    info.type = 1;
                                    updateRecord(info);
                                    isPopUpShown = false;
                                    return true;
                                default:
                                    isPopUpShown = false;
                                    return false;
                            }
                        }
                    });

            popupMenu.show();
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    isPopUpShown = false;
                }
            });

        }


    }

    public void updateRecord(AppInfo appInfo) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Repository.getInstance().getDatabase().getAppInfoDao().updateApp(appInfo);
            }
        };
        thread.start();
    }


}
