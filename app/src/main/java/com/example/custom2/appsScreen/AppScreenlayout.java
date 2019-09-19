package com.example.custom2.appsScreen;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.gridView.ExpandedGridView;

import java.util.ArrayList;
import java.util.List;

public class AppScreenlayout extends ConstraintLayout {
    NestedScrollView scrollView;
    ExpandedGridView gridView;
    public AppScreenlayout(Context context) {
        super(context);
    }

    public AppScreenlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppScreenlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("app", "touch"+(event.getY()+scrollView.getScrollY()));
        float y=event.getY();
        float x=event.getX();
        List<View> m=getAllChildren(gridView);
        for(int i=0;i<m.size();i++){
            if(ifInBounds(m.get(i),event)){
                m.get(i).callOnClick();
            }
        }

        //MotionEvent event1=event;
        //event1.offsetLocation(0,findViewById(R.id.appScreen).getHeight());
        //gridView.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
    public void onLongClick(MotionEvent event){
        Log.d("app", "touch"+(event.getY()+scrollView.getScrollY()));
        float y=event.getY();
        float x=event.getX();
        List<View> m=getAllChildren(gridView);
        for(int i=0;i<m.size();i++){
            if(ifInBounds(m.get(i),event)){
                m.get(i).performLongClick();
            }
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setTranslationY(getHeight());
        scrollView=findViewById(R.id.bottom_sheet);
        gridView=findViewById(R.id.bottomGrid);
    }
    public void scroll(float k){
        int sY = scrollView.getScrollY();
        sY += k * gridView.getHeight()/2;
        scrollView.scrollTo(scrollView.getScrollX(), sY);
    }
    public boolean isScrollOnTop(){
        return scrollView.getScrollY()==0;
    }
    public void drag(float k){
        setTranslationY(getTranslationY()-getHeight()*k);
        float res = 0;
        res = 1 - (getTranslationY() / getHeight() * ((float) 0.75));
        if(res>=(float)0.75){
            res=(float)0.75;
        }
        if (Repository.getInstance().getParameters() != null) {
            Repository.getInstance().getParameters().setBlackAlpha(res);
        }    }
    public float getTranslationPercent(){
        float res=getTranslationY();
        res/=getHeight();
        return res;
    }
    public void dragToTop(){
        ValueAnimator animator = ValueAnimator.ofFloat(getTranslationY(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationY((float)animation.getAnimatedValue());
                Repository.getInstance().getParameters().setAlpha(getTranslationPercent());
            }
        });
        animator.start();
        int t=(int)Repository.getInstance().getParameters().getBlackAlpha()*100;
        for(int i=t;i<76;i++){
            float r=(float)i/100;
            Repository.getInstance().getParameters().setBlackAlpha(r);
        }
    }

    public void dragToBottom(){
        ValueAnimator animator = ValueAnimator.ofFloat(getTranslationY(), getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationY((float)animation.getAnimatedValue());
                Repository.getInstance().getParameters().setAlpha(getTranslationPercent());
            }
        });
        animator.start();
        int t=(int)Repository.getInstance().getParameters().getBlackAlpha()*100;
        for(int i=t;i>=0;i--){
            float r=(float)i/100;
            Repository.getInstance().getParameters().setBlackAlpha(r);
        }
    }
    private boolean ifInBounds(View view, MotionEvent event) {
        double x = event.getX();
//        double y = event.getY() + scrollView.getScrollY()-findViewById(R.id.appsearch).getHeight();
        double y = event.getY() + scrollView.getScrollY();
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
