package com.example.custom2.GridImageView;

import android.app.WallpaperManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.custom2.R;
import com.example.custom2.Repository;

public class ChooseBackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choode_background);
        ImageView view = findViewById(R.id.background2);
        WallpaperManager manager=(WallpaperManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WALLPAPER_SERVICE);

        view.setImageDrawable(manager.getDrawable());
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setAdapter(new BackgroundAdapter(this,this));

        gridview.setOnItemClickListener(gridviewOnItemClickListener);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            try {
                WallpaperManager manager=(WallpaperManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WALLPAPER_SERVICE);
                manager.setResource(Repository.backs[position]);
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}
