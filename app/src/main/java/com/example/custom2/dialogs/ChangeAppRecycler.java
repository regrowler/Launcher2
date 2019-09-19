package com.example.custom2.dialogs;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.room.AppInfo;


public class ChangeAppRecycler extends RecyclerView.Adapter<ChangeAppRecycler.ViewH> {
    LayoutInflater inflater;
    int pos,comma;
    Dialog dialog;
    public ChangeAppRecycler(int pos, int comma, Dialog dialog){
        inflater= LayoutInflater.from(Repository.getInstance().getContext());
        this.pos=pos;this.comma=comma;this.dialog=dialog;
    }
    @NonNull
    @Override
    public ViewH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.change_app_list_item,viewGroup,false);
        return new ViewH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewH viewH, int i) {
        AppInfo appInfo=Repository.getInstance().getAppsModel().getMas().get(i);
        viewH.imageView.setImageDrawable(appInfo.icon);
        viewH.textView.setText(appInfo.label);
        viewH.g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comma==0){
                    AppInfo appInfo1=new AppInfo();
                    appInfo1.name=Repository.getInstance().getAppsModel().getMas().get(i).name;
                    appInfo1.label=Repository.getInstance().getAppsModel().getMas().get(i).label;
                    appInfo1.icon=Repository.getInstance().getAppsModel().getMas().get(i).icon;
                    appInfo1.type=1;
                    appInfo1.id=pos;
                    updateRecord(appInfo1);
//                    Repository.bottomAdapter.apps.set(pos,appInfo1);
//                    Repository.bottomAdapter.notifyDataSetChanged();
                }else if(comma==1){
                    AppInfo appInfo1=new AppInfo();
                    appInfo1.name=Repository.getInstance().getAppsModel().getMas().get(i).name;
                    appInfo1.label=Repository.getInstance().getAppsModel().getMas().get(i).label;
                    appInfo1.icon=Repository.getInstance().getAppsModel().getMas().get(i).icon;
                    appInfo1.type=2;
                    appInfo1.id=pos;
                    updateRecord(appInfo1);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Repository.getInstance().getAppsModel().getMas().size();
    }

    public class ViewH extends RecyclerView.ViewHolder{
        LinearLayout g;
        TextView textView;
        ImageView imageView;
        public ViewH(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.changeAppListItemLabel);
            imageView=itemView.findViewById(R.id.changeAppListItemIcon);
            g=itemView.findViewById(R.id.general);
        }
    }
    public void updateRecord(AppInfo appInfo){
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    Repository.getInstance().getDatabase().getAppInfoDao().updateApp(appInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();

    }

}
