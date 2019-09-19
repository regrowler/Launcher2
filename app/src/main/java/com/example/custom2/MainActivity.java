package com.example.custom2;

import android.app.WallpaperManager;
import android.arch.persistence.room.Room;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.custom2.appsScreen.AppScreenlayout;
import com.example.custom2.appsScreen.AppsModel;
import com.example.custom2.databinding.ActivityMainBinding;
import com.example.custom2.mainScreen.MainScreenLayout;
import com.example.custom2.mainScreen.MainScreenParameters;
import com.example.custom2.room.AppDatabase;
import com.example.custom2.room.AppInfo;
import com.example.custom2.room.SetParams;
import com.example.custom2.settings.PassDialog;
import com.example.custom2.settings.Settings;
import com.example.custom2.settings.SettingsLayout;
import com.example.custom2.swipes.SwipeDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    View mainView;
    int UNINSTALL_REQUEST_CODE = 1;
    @BindView(R.id.one)
    SettingsLayout one;
    boolean show=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        //setContentView(mainView);
        Repository.getInstance().setMainActivity(this);
        Repository.getInstance().setContext(getApplicationContext());
        Repository.getInstance().setFragmentManager(getSupportFragmentManager());
        Repository.getInstance().setPackageManager(getApplicationContext().getPackageManager());
        Repository.getInstance().setDatabase(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").fallbackToDestructiveMigration().build());
        Repository.getInstance().setSettings(new Settings());
        Repository.getInstance().setParameters(new MainScreenParameters());
        Repository.getInstance().setAppsModel(new AppsModel(this));

//        Intent i = new Intent(Intent.ACTION_MAIN, null);
//        i.addCategory(Intent.CATEGORY_LAUNCHER);
//        final List<ResolveInfo> availableActivities = getPackageManager().queryIntentActivities(i, 0);
//        AppInfo app=new AppInfo();
//        app.label = availableActivities.get(0).loadLabel(getPackageManager()).toString();
//        app.name = availableActivities.get(0).activityInfo.packageName;
//        app.icon = availableActivities.get(0).activityInfo.loadIcon(getPackageManager());
//        app.type=1;
//        Thread thread=new Thread(){
//            @Override
//            public void run() {
//                Repository.getInstance().getDatabase().getAppInfoDao().insertApp(app);
//            }
//        };
//        thread.start();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setSettings(Repository.getInstance().getSettings());
        binding.setMainScreenParams(Repository.getInstance().getParameters());
        binding.setAppModel(Repository.getInstance().getAppsModel());

        ButterKnife.bind(this);

        SwipeDetector.getInstance(this).setSettings(one);
        SwipeDetector.getInstance(this).setMainScreenLayout((MainScreenLayout) findViewById(R.id.mainScreen));
        SwipeDetector.getInstance(this).setAppScreenlayout((AppScreenlayout) findViewById(R.id.appScreen));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SwipeDetector.getInstance(this).closeDrag();
        findViewById(R.id.googleSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickSearch("");
            }
        });
        Repository.getInstance().getSettings().updateTempSettings();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
        ImageView view = findViewById(R.id.wall);
        view.setImageDrawable(manager.getDrawable());
        Repository.getInstance().getSettings().setPass(false);
        show=true;
        dialog();

    }
    public void dialog(){
        Repository.getInstance().getDatabase().getSettingsDao()
                .getparams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(s->{
                    Log.d("err","sets");
                })
                .subscribe(
                        s->{
                            int i=0;
                            if(s.size()==0){
                                Thread thread=new Thread(){
                                    @Override
                                    public void run() {
                                        SetParams s=new SetParams();
                                        s.gridsize= 2;
                                        s.password=0;
                                        s.weather=0;
                                        s.widgets=1;
                                        s.id=1;
                                        s.passwordS="";
                                        Repository.getInstance().getDatabase().getSettingsDao()
                                                .initSettings(s);
                                    }
                                };
                                thread.start();
                            }else {
                                //setSetParams(s.get(0));
                                if(s.get(0).password==1&show){
                                    show=false;
                                    DialogFragment dialogFragment = new PassDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("pas", Repository.getInstance().getSettings().getSetParams().passwordS);
                                    dialogFragment.setArguments(bundle);
                                    dialogFragment.show(getSupportFragmentManager(), "");
                                }
                            }
                        }
                );
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SwipeDetector.getInstance(this).closeDrag();
        if (requestCode == UNINSTALL_REQUEST_CODE) {

            SwipeDetector.getInstance(this).closeDrag();
            Repository.getInstance().getAppsModel().getAppsAdapter().load();
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {

            } else if (resultCode == RESULT_FIRST_USER) {

            }
        }else {
            if(data!=null){
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String voiceInput = result.get(0);
                quickSearch(voiceInput);
            }else {
                quickSearch("");
            }
            int y=0;
        }
    }
    public void quickSearch(String s){
        android.app.SearchManager searchManager = (android.app.SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName globalSearchActivity = searchManager.getGlobalSearchActivity();
        if (globalSearchActivity == null) {
            Log.w("err","No global search activity found.");
            return ;
        }
        Intent intent = new Intent(android.app.SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(globalSearchActivity);
        Bundle appSearchData = new Bundle();
        appSearchData.putString("source", getPackageName());

        intent.putExtra(android.app.SearchManager.APP_DATA, appSearchData);
        intent.putExtra(android.app.SearchManager.QUERY, s);
        intent.putExtra(android.app.SearchManager.EXTRA_SELECT_QUERY, true);
        try {
            startActivity(intent);
            return;
        } catch (ActivityNotFoundException ex) {
            Log.w("err","Global search activity not found: %s");
            return ;
        }
    }

}
