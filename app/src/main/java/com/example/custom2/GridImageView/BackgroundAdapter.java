package com.example.custom2.GridImageView;

import android.app.WallpaperManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.custom2.R;
import com.example.custom2.Repository;

import static android.content.Context.WALLPAPER_SERVICE;


public class BackgroundAdapter extends BaseAdapter {

    private Context mContext;
    ChooseBackgroundActivity activity;
    public BackgroundAdapter(Context c, ChooseBackgroundActivity activity) {
        mContext = c;this.activity=activity;
    }

    public int getCount() {
        return Repository.backs.length;
    }

    public Object getItem(int position) {
        return Repository.backs[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(360, 360));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 0, 8, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(Repository.backs[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WallpaperManager manager=(WallpaperManager) Repository.getInstance().getContext().getApplicationContext().getSystemService(WALLPAPER_SERVICE);

                    manager.setResource(Repository.fullbacks[position]);
//                    Repository.mainActivity.showFragment(1);
                    activity.finish();
                }catch (Exception e){
                    Log.e("waa",e.toString());
                }

            }
        });
        return imageView;
    }

}

