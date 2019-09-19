package com.example.custom2.appsScreen;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.custom2.room.AppInfo;
import com.example.custom2.BR;

import java.util.ArrayList;
import java.util.List;

public class AppsModel extends BaseObservable {
    public AppsModel(Context context) {
        setMas(new ArrayList<>());
        setAppsAdapter(new AppsAdapter(context,context.getPackageManager(),mas));
    }

    @Bindable
    private float alpha=1;

    @Bindable
    private AppsAdapter appsAdapter;
    @Bindable
    private List<AppInfo> mas;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        notifyPropertyChanged(BR.alpha);
    }

    public AppsAdapter getAppsAdapter() {
        return appsAdapter;
    }

    public void setAppsAdapter(AppsAdapter appsAdapter) {
        this.appsAdapter = appsAdapter;
        notifyPropertyChanged(BR.appsAdapter);
    }

    public List<AppInfo> getMas() {
        return mas;
    }

    public void setMas(List<AppInfo> mas) {
        this.mas = mas;
        notifyPropertyChanged(BR.mas);
    }
}
