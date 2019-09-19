package com.example.custom2.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.custom2.R;

public class ChangeApp extends DialogFragment {
    int position;
    int command;
    RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.change_app_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        command=getArguments().getInt("comma");
        position=getArguments().getInt("pos");
        recyclerView=view.findViewById(R.id.changeAppRecycler);
        ChangeAppRecycler changeAppAdapter=new ChangeAppRecycler(position,command,getDialog());
        recyclerView.setAdapter(changeAppAdapter);
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
