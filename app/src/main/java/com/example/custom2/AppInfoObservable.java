package com.example.custom2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.example.custom2.room.AppInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class AppInfoObservable {
    public static Observable<AppInfo> getObservable(Context context) {
        final PublishSubject<AppInfo> publishSubject = PublishSubject.create();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager manager = context.getPackageManager();
        final List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        int u = 0;
        Thread thread = new Thread() {
            boolean b = true;

            @Override
            public void run() {
                while (b) {
                    if (publishSubject.hasObservers()) {
                        b = false;
                        for (ResolveInfo ri : availableActivities) {
                            AppInfo app = new AppInfo();
                            app.label = ri.loadLabel(manager).toString();
                            app.name = ri.activityInfo.packageName;
                            app.icon = ri.activityInfo.loadIcon(manager);
                            publishSubject.onNext(app);


                        }
                        publishSubject.onComplete();
                    }
                }
            }
        };
        thread.start();
        return publishSubject;
    }

}
