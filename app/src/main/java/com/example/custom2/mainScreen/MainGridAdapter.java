package com.example.custom2.mainScreen;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.dialogs.ChangeApp;
import com.example.custom2.room.AppInfo;

import java.util.List;

public class MainGridAdapter extends ArrayAdapter<AppInfo> {
    public List<AppInfo> mas;
    Context context;
    MainGridSize size;
    LayoutInflater inflater;
    PackageManager manager;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    int num = 5;
    private boolean isPopUpShown = false;

    public MainGridAdapter(int size, List<AppInfo> mainGridMas) {
        super(Repository.getInstance().getContext(), 0);
        this.context = Repository.getInstance().getContext();
        inflater = LayoutInflater.from(context);
        switch (size) {
            case 1:
                this.size = MainGridSize.SMALL;
                setNum(6);
                break;
            case 2:
                this.size = MainGridSize.MEDIUM;
                setNum(5);
                break;
            case 3:
                this.size = MainGridSize.LARGE;
                setNum(4);
                break;
        }
        mas = mainGridMas;
        manager = Repository.getInstance().getPackageManager();


    }

    @Override
    public int getCount() {
        if (Repository.getInstance().getSettings().getSetParams().widgets == 1) {
            switch (size) {
                case SMALL:
                    if (mas.size() < 36) {
                        return mas.size();
                    } else return 36;
                case MEDIUM:
                    if (mas.size() < 25) {
                        return mas.size();
                    } else return 25;
                case LARGE:
                    if (mas.size() < 2) {
                        return mas.size();
                    } else return 20;
            }
        } else {
            switch (size) {
                case SMALL:
                    if (mas.size() < 48) {
                        return mas.size();
                    } else return 48;
                case MEDIUM:
                    if (mas.size() < 35) {
                        return mas.size();
                    } else return 35;
                case LARGE:
                    if (mas.size() < 28) {
                        return mas.size();
                    } else return 28;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setSize(int size) {
        switch (size) {
            case 3:
                this.size = MainGridSize.SMALL;
                setNum(6);
                break;
            case 2:
                this.size = MainGridSize.MEDIUM;
                setNum(6);
                break;
            case 1:
                this.size = MainGridSize.LARGE;
                setNum(6);
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (size) {
            case MEDIUM:
                convertView = inflater.inflate(R.layout.main_grid_item_medium, null);
                break;
            case SMALL:
                convertView = inflater.inflate(R.layout.main_grid_item_small, null);
                break;
            case LARGE:
                convertView = inflater.inflate(R.layout.main_grid_item_large, null);
                break;
        }
        Log.d("grid", convertView.getWidth() + "");
        Log.d("grid", convertView.getHeight() + "");
        //convertView.setBackgroundColor(position%2==0? Color.GREEN:Color.RED);
        ImageView imageView = convertView.findViewById(R.id.icon);
        imageView.setImageDrawable(mas.get(position).icon);
        TextView label = convertView.findViewById(R.id.label);
        label.setText(mas.get(position).label);
        LinearLayout gr = convertView.findViewById(R.id.general);
        gr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mas.get(position).name != null) {
                    if (!mas.get(position).name.equals("")) {
                        final Intent intent = manager.getLaunchIntentForPackage(mas.get(position).name);
                        context.startActivity(intent);
                    }
                }
            }
        });
        gr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenu(view, mas.get(position).id);
                Log.d("long", position + "");
                return false;
            }
        });
        return convertView;
    }

    private void showPopupMenu(View v, int pos) {
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
                                    args.putInt("comma", 1);
                                    args.putInt("pos", pos);
                                    dialogFragment.setArguments(args);
                                    dialogFragment.show(Repository.getInstance().getFragmentManager(), "");
                                    isPopUpShown = false;
                                    return true;
                                case R.id.bottomPopupClear:
                                    AppInfo info = new AppInfo();
                                    info.id = pos;
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
