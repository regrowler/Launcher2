package com.example.custom2.swipes;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.example.custom2.R;
import com.example.custom2.swipes.SwipeDetector;

public class SensorLayout extends ConstraintLayout {
    public SensorLayout(Context context) {
        super(context);
        init(context);
    }

    public SensorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SensorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(final Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.main, this, true);
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SwipeDetector.getInstance(context).onTouchEvent(event);
                return true;
            }
        });
//        ButterKnife.bind(this, v);
    }



    public void addData(Object data) {
        Log.e(getClass().getSimpleName(), data.toString());
    }

}
