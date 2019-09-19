package com.example.custom2.appsScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.example.custom2.Repository;
import com.example.custom2.room.AppInfo;
import com.example.custom2.AppInfoObservable;
import com.example.custom2.R;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AppsAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    public List<AppInfo> mas;
    private PackageManager manager;
    private boolean loading = false;
    private boolean isPopUpShown = false;

    public AppsAdapter(Context context, PackageManager manager, List<AppInfo> mas) {
        inflater = LayoutInflater.from(context);
        this.manager = manager;
        this.context = context;
        this.mas = mas;
        load();

    }

    @SuppressLint("CheckResult")
    public void load() {
        int i = 0;
        if (!loading) {
            mas.clear();
            AppInfoObservable.getObservable(context)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(
                            s -> loading = true)
                    .doOnComplete(
                            () -> loading = false)
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    })
                    .subscribe(
                            s -> {
                                mas.add(s);
                                notifyDataSetChanged();

                            }
                    );
        }

    }

    public void clear() {
        mas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mas.size();
    }

    @Override
    public Object getItem(int i) {
        return mas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void changeMas(List<AppInfo> mas) {
        this.mas = mas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_app_grid, null);
        ImageView imageView = view.findViewById(R.id.icon);
        imageView.setImageDrawable(mas.get(i).icon);
        TextView textView = view.findViewById(R.id.label);
        String label = mas.get(i).label;
        if (label.length() > 8) {
            StringBuilder newl = new StringBuilder(label.substring(0, 5));
            newl.append("..");
            textView.setText(newl.toString());
        } else {
            textView.setText(label);
        }
        LinearLayout g = view.findViewById(R.id.general);
        final Intent intent = manager.getLaunchIntentForPackage(mas.get(i).name);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mas.get(i).name;
                context.startActivity(intent);
            }
        });
        g.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenu(view, i);
                return true;
            }
        });
        return view;
    }


    private void makeShortcut(AppInfo info) {
//        for (int i = 0; i < Repository.mainGridAdapter.getCount(); i++) {
//            if (Repository.mainGridAdapter.mas.get(i).icon == null) {
//                Repository.mainGridAdapter.updateRecord(info, i);
//                break;
//            }
//        }
    }

    private void makeBottomShortCut(AppInfo info) {
//        for (int i = 0; i < Repository.bottomAdapter.getCount(); i++) {
//            if (Repository.bottomAdapter.cash.get(i).icon == null) {
//                Repository.bottomAdapter.updateRecord(info, i);
//                break;
//            }
//        }
    }

    private void showInfo(AppInfo info) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + info.name));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        context.startActivity(intent);
    }

    private void addHidden(AppInfo info) {
//        SQLiteDatabase database = Repository.database;
//        database.execSQL("CREATE TABLE IF NOT EXISTS hidden (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT);");
//        ContentValues values = new ContentValues();
//        values.put("name", info.name);
//        database.insert("hidden", null, values);
    }

    private void showPopupMenu(View v, final int pos) {
        if (!isPopUpShown) {
            isPopUpShown = true;
            PopupMenu popupMenu = new PopupMenu(Repository.getInstance().getMainActivity(), v, Gravity.TOP);
            popupMenu.inflate(R.menu.popupmenu_all);

            popupMenu
                    .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.appPopupDelete:
                                    String app_pkg_name = mas.get(pos).name;
                                    int UNINSTALL_REQUEST_CODE = 1;
                                    isPopUpShown = false;
                                    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                                    intent.setData(Uri.parse("package:" + app_pkg_name));
                                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                                    Repository.getInstance().getMainActivity().startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
                                    return true;
                                case R.id.appProperties:
                                    showInfo(mas.get(pos));
                                    isPopUpShown = false;
                                    return true;
                                default:
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

}
