package com.example.custom2.mainScreen;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.custom2.R;
import com.example.custom2.Repository;

import java.util.ArrayList;
import java.util.List;

public class MainScreenLayout extends ConstraintLayout {
    View weather;
    View search;
    public MainScreenLayout(Context context) {
        super(context);
    }

    public MainScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MainScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void horizontalScroll(float k) {
        if (Repository.getInstance().getSettings() != null) {
            float t = Repository.getInstance().getSettings().getWhiteAlpha();
            if (Repository.getInstance().getParameters() != null) {
                Repository.getInstance().getParameters().setAlpha(1 - t);
            }
        }
    }

    public void hideScroll(float k) {
        if (Repository.getInstance().getParameters() != null) {
            Repository.getInstance().getParameters().setAlpha(k);
        }
    }

    public void swipeToRight() {
        Repository.getInstance().getParameters().setAlpha((float) 1.0);

    }

    public void swipeToLeft() {
        Repository.getInstance().getParameters().setAlpha((float) 0.0);
    }

    public void longClick(MotionEvent event) {
        if (Repository.getInstance().getParameters().isWidgetsActive()) {
            Log.d("mainlongClick", event.getAction() + "");
            float y = event.getY();
            float x = event.getX();
            List<View> m = getAllChildren(findViewById(R.id.mainGrid));
            for (int i = 0; i < m.size(); i++) {
                if (ifInBoundsWidgets(m.get(i), event)) {
                    m.get(i).performLongClick();
                }
            }
            m = getAllChildren(weather);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performLongClick();
                }
            }
            m = getAllChildren(search);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performLongClick();
                }
            }
            if(ifInBounds(findViewById(R.id.constraintLayout),event)){
                findViewById(R.id.constraintLayout).performLongClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout2),event)){
                findViewById(R.id.constraintLayout2).performLongClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout3),event)){
                findViewById(R.id.constraintLayout3).performLongClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout4),event)){
                findViewById(R.id.constraintLayout4).performLongClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout5),event)){
                findViewById(R.id.constraintLayout5).performLongClick();
            }
        } else {
            Log.d("mainlongClick", event.getAction() + "");
            float y = event.getY();
            float x = event.getX();
            List<View> m = getAllChildren(this);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performLongClick();
                }
            }
        }
    }

    public void click(MotionEvent event) {
        if (Repository.getInstance().getParameters().isWidgetsActive()) {
            float y = event.getY();
            float x = event.getX();
            List<View> m = getAllChildren(findViewById(R.id.mainGrid));
            for (int i = 0; i < m.size(); i++) {
                if (ifInBoundsWidgets(m.get(i), event)) {
                    m.get(i).performClick();
                }
            }
            m = getAllChildren(weather);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performClick();
                }
            }
            m = getAllChildren(search);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performClick();
                }
            }
            if(ifInBounds(findViewById(R.id.constraintLayout),event)){
                findViewById(R.id.constraintLayout).performClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout2),event)){
                findViewById(R.id.constraintLayout2).performClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout3),event)){
                findViewById(R.id.constraintLayout3).performClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout4),event)){
                findViewById(R.id.constraintLayout4).performClick();
            }
            if(ifInBounds(findViewById(R.id.constraintLayout5),event)){
                findViewById(R.id.constraintLayout5).performClick();
            }
        } else {
            float y = event.getY();
            float x = event.getX();
            List<View> m = getAllChildren(this);
            for (int i = 0; i < m.size(); i++) {
                if (ifInBounds(m.get(i), event)) {
                    m.get(i).performClick();
                }
            }
        }

    }

    private boolean ifInBounds(View view, MotionEvent event) {
        double x = event.getX();
        double y = event.getY();
        double vXS = view.getX();
        double vXE = vXS + view.getWidth();
        double vYS = view.getY();
        double vYE = vYS + view.getHeight();
        if ((x <= vXE & x >= vXS) & (y <= vYE & y >= vYS)) {
            return true;
        }
        return false;
    }

    private boolean ifInBoundsWidgets(View view, MotionEvent event) {
        double x = event.getX();
        weather=findViewById(R.id.weather1);
        search=findViewById(R.id.googleSearch);
        double y = event.getY()-weather.getHeight();
        double vXS = view.getX();
        double vXE = vXS + view.getWidth();
        double vYS = view.getY();
        double vYE = vYS + view.getHeight();
        if ((x <= vXE & x >= vXS) & (y <= vYE & y >= vYS)) {
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
