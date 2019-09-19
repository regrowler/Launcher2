package com.example.custom2.gridView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

//import android

public class ExpandedGridView extends GridView {

    boolean expanded = false;
    private static final int SWIPE_THRESHOLD = 20;

    public ExpandedGridView(Context context) {
        super(context);
        setExpanded(true);
    }

    public ExpandedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setExpanded(true);
    }

    public ExpandedGridView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        setExpanded(true);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.
            int expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
