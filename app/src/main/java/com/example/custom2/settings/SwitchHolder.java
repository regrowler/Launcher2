package com.example.custom2.settings;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.custom2.Repository;

import java.util.ArrayList;
import java.util.List;

public class SwitchHolder extends ConstraintLayout {
    Switch aSwitch;

    public SwitchHolder(Context context) {
        super(context);
    }

    public SwitchHolder(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        List<View> mas = getAllChildren(this);
        for (View v : mas) {
            if (v instanceof Switch) {
                aSwitch = (Switch) v;
                return;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (aSwitch != null) {
            if (ifInBounds(aSwitch, event)) {

                aSwitch.toggle();

            }else if(ifInBounds2(this,event)){
                callOnClick();
            }
        }else if(ifInBounds2(this,event)){
            callOnClick();
        }

        return false;
    }

    public SwitchHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private boolean ifInBounds2(View view, MotionEvent event) {
        double x = event.getX();


        double vXE = getWidth() - Repository.getInstance().getContext().getResources().getDisplayMetrics().density*60;

        if ((x <= vXE & x >= 0)) {
            return true;
        }
        return false;
    }
    private boolean ifInBounds(View view, MotionEvent event) {
        double x = event.getX();

        double vXS = view.getX();
        double vXE = vXS + view.getWidth();

        if ((x <= vXE & x >= vXS)) {
            return true;
        }
        return false;
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
