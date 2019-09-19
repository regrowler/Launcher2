package com.example.custom2.settings;

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

import java.util.ArrayList;
import java.util.List;


public class SettingsLayout extends ConstraintLayout {
    NestedScrollView scrollView;
    View scrollViewChild;
    int layout = R.layout.settings;




    public SettingsLayout(Context context) {
        super(context);
    }

    public SettingsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setTranslationX(getWidth());
        List<View> mas = getAllChildren(this);
        for (int i = 0; i < mas.size(); i++) {
            if (mas.get(i) instanceof NestedScrollView) {
                scrollView = (NestedScrollView) mas.get(i);
                int x = 0;
                int y = 0;
                List<View> ma = getAllChildren(scrollView);
                for (View v : ma) {
                    int xx = v.getMeasuredWidth();
                    int yy = v.getMeasuredHeight();
                    if (v.getMeasuredWidth() >= x & v.getMeasuredHeight() >= y) {
                        scrollViewChild = v;
                        x = v.getMeasuredWidth();
                        y = v.getMeasuredHeight();
                    }
                }
            }
        }

    }


    public SettingsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void scroll(float k) {
        int sY = scrollView.getScrollY();
        sY += k * scrollViewChild.getHeight();
        scrollView.scrollTo(scrollView.getScrollX(), sY);
    }

    public void horizontalScroll(float k) {
        setTranslationX(getTranslationX() + getWidth() * (-k));
        float res = 0;
        res = 1 - (getTranslationX() / getWidth() * ((float) 0.75));
        if(res>=(float)0.75){
            res=(float)0.75;
        }
        if (Repository.getInstance().getSettings() != null) {
            Repository.getInstance().getSettings().setWhiteAlpha(res);
        }

    }

    public void swipeToLeft() {
        Log.d("swipe","left");
        ValueAnimator animator = ValueAnimator.ofFloat(getTranslationX(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        int t=(int)Repository.getInstance().getSettings().getWhiteAlpha()*100;
        animator.start();
        for(int i=t;i<76;i++){
            float r=(float)i/100;
            Repository.getInstance().getSettings().setWhiteAlpha(r);
        }

    }

    public void swipeToRight() {
        Log.d("swipe","right");
        ValueAnimator animator = ValueAnimator.ofFloat(getTranslationX(), getWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        int t=(int)Repository.getInstance().getSettings().getWhiteAlpha()*100;
        animator.start();
        for(int i=t;i>=0;i--){
            float r=(float)i/100;
            Repository.getInstance().getSettings().setWhiteAlpha(r);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("c2", "touch");
        List<View> mas = getAllChildren(this);
        for (int i = 0; i < mas.size(); i++) {
            if (ifInBounds(mas.get(i), event)) {
                if(mas.get(i)instanceof SwitchHolder){
                    mas.get(i).onTouchEvent(event);
                }else {
                    mas.get(i).performClick();
                }

            }
        }
        int i = 0;
        return super.onTouchEvent(event);
    }
    public void longClick(MotionEvent event){
        Log.d("c2", "touch");
        List<View> mas = getAllChildren(this);
        for (int i = 0; i < mas.size(); i++) {
            if (ifInBounds(mas.get(i), event)) {
                if (mas.get(i) instanceof Switch) {
                    int y = 0;
                }

                mas.get(i).performLongClick();
            }
        }
        int i = 0;
    }
    private boolean ifInBounds(View view, MotionEvent event) {
        double x = event.getX();
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
