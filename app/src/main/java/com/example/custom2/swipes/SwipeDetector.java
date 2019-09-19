package com.example.custom2.swipes;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.custom2.Repository;
import com.example.custom2.appsScreen.AppScreenlayout;
import com.example.custom2.mainScreen.MainScreenLayout;
import com.example.custom2.settings.SettingsLayout;

public class SwipeDetector {
    private Context context;

    private static SwipeDetector ourInstance;
    private static GestureDetector detector;
    private final CancelDetector cancelDetector = new CancelDetector();

    SettingsLayout settings;
    MainScreenLayout mainScreenLayout;
    AppScreenlayout appScreenlayout;

    int width = 1;
    int height = 1;
    int screenState = SCREEN_MAIN;


    public static final int SCREEN_SETTINGS = 1;
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_APPS = 2;


    public static SwipeDetector getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SwipeDetector(context);
        }
        return ourInstance;
    }
    public void closeDrag(){
        if(appScreenlayout!=null){

            appScreenlayout.setTranslationY(appScreenlayout.getHeight());
            Repository.getInstance().getParameters().setBlackAlpha(0);
            screenState=SCREEN_MAIN;
            Repository.getInstance().getParameters().setAlpha(1);
            Repository.getInstance().getSettings().setWhiteAlpha(0);
            settings.setTranslationX(settings.getWidth());
        }

    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelDetector.onPreScroll();
                Log.d("sd", "down");
                //settings.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //settings.onTouchEvent(event);
                Log.d("sd", "move");
                break;
            case MotionEvent.ACTION_UP:
                cancelDetector.onFinish();
                //settings.onTouchEvent(event);
                Log.d("sd", "up");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("sd", "cancel"); //As soon as it
                break;

        }
        detector.onTouchEvent(event);
    }

    public void setSettings(SettingsLayout settings) {
        this.settings = settings;
    }

    public AppScreenlayout getAppScreenlayout() {
        return appScreenlayout;
    }

    public void setAppScreenlayout(AppScreenlayout appScreenlayout) {
        this.appScreenlayout = appScreenlayout;
    }

    public int getScreenState() {
        return screenState;
    }

    public void setScreenState(int screenState) {
        this.screenState = screenState;
    }

    public MainScreenLayout getMainScreenLayout() {
        return mainScreenLayout;
    }

    public void setMainScreenLayout(MainScreenLayout mainScreenLayout) {
        this.mainScreenLayout = mainScreenLayout;
    }

    private SwipeDetector(Context context) {
        this.context = context;
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        detector = new GestureDetector(context, new GestureListener());
    }

    private class CancelDetector {
        float distanceX = 0;
        float distanceY = 0;
        float maxX = 0;
        float maxY = 0;
        boolean stop=false;
        boolean antiFling=true;


        private static final int NOT_DRAGGING = 653;
        private static final int HORIZONTAL_SCROLL = 264;
        private static final int VERTICAL_SCROLL = 676;

        int state;

        public void onPreScroll() {
            distanceY = 0;
            distanceX = 0;
            maxX = 0;
            maxY = 0;
            stop=false;
            state = NOT_DRAGGING;
            if(screenState==SCREEN_MAIN){
                antiFling=true;
            }
        }

        public void onFinish() {
            switch (screenState) {
                case SCREEN_MAIN:
                    if (state == HORIZONTAL_SCROLL) {
                        if (distanceX <= maxX & maxX < 0) {
                            settings.swipeToRight();
                            mainScreenLayout.swipeToRight();
                            screenState = SCREEN_MAIN;
                            Log.d("screenState", SCREEN_MAIN + "");
                        } else if (distanceX >= maxX & maxX > 0) {
                            settings.swipeToLeft();
                            mainScreenLayout.swipeToLeft();
                            screenState = SCREEN_SETTINGS;
                            Repository.getInstance().getSettings().updateTempSettings();
                            Log.d("screenState", SCREEN_SETTINGS + "");
                        } else if (maxX > 0) {
                            settings.swipeToRight();
                            mainScreenLayout.swipeToRight();
                        } else {
                            settings.swipeToLeft();
                            mainScreenLayout.swipeToLeft();
                        }
                    } else if (state == VERTICAL_SCROLL) {
                        if (distanceY <= maxY & maxY <= 0) {
                            screenState = SCREEN_MAIN;
                            appScreenlayout.dragToBottom();
                        } else if (distanceY >= maxY & maxY > 0) {
                            screenState = SCREEN_APPS;
                            appScreenlayout.dragToTop();
                        } else if (maxY > 0) {
                            appScreenlayout.dragToBottom();
                        } else {
                            appScreenlayout.dragToTop();
                        }
                    }
                    break;
                case SCREEN_SETTINGS:
                    if (state == HORIZONTAL_SCROLL) {
                        if (distanceX <= maxX & maxX < 0) {
                            settings.swipeToRight();
                            mainScreenLayout.swipeToRight();
                            screenState = SCREEN_MAIN;


                            Log.d("screenState", SCREEN_MAIN + "");
                        } else if (distanceX >= maxX & maxX > 0) {
                            settings.swipeToLeft();
                            mainScreenLayout.swipeToLeft();
                            screenState = SCREEN_SETTINGS;
                            Log.d("screenState", SCREEN_SETTINGS + "");
                        } else if (maxX > 0) {
                            settings.swipeToRight();
                            mainScreenLayout.swipeToRight();
                        } else {
                            settings.swipeToLeft();
                            mainScreenLayout.swipeToLeft();
                        }
                    }
                    break;
                case SCREEN_APPS:
                    if (state == VERTICAL_SCROLL) {
                        if (appScreenlayout.isScrollOnTop()) {
                            if (distanceY <= maxY & maxY <= 0) {
                                screenState = SCREEN_MAIN;
                                appScreenlayout.dragToBottom();
                            } else if (distanceY >= maxY & maxY > 0) {
                                screenState = SCREEN_APPS;
                                appScreenlayout.dragToTop();
                            } else if (maxY > 0) {
                                appScreenlayout.dragToBottom();
                            } else {
                                appScreenlayout.dragToTop();
                            }
                        }
                    }
                    break;
            }

            distanceY = 0;
            distanceX = 0;
            maxX = 0;
            maxY = 0;
            state = NOT_DRAGGING;
        }

        public void onScroll(float distanceXN, float distanceYN) {
            switch (screenState) {
                case SCREEN_MAIN:
                    if (state == HORIZONTAL_SCROLL) {
                        this.distanceX += distanceXN;
                        if (distanceX > 0 & distanceX > maxX) {
                            maxX = distanceX;
                        } else if (distanceX < 0 && distanceX < maxX) {
                            maxX = distanceX;
                        }
                        float r = distanceXN / width;
                        settings.horizontalScroll(r);
                        mainScreenLayout.horizontalScroll(r);
                    } else if (state == VERTICAL_SCROLL) {
                        distanceY += distanceYN;
                        if (distanceY > 0 & distanceY > maxY) {
                            maxY = distanceY;
                        } else if (distanceY < 0 && distanceY < maxY) {
                            maxY = distanceY;
                        }
                        appScreenlayout.drag(distanceYN / height);
                        mainScreenLayout.hideScroll(appScreenlayout.getTranslationPercent());
                        Log.d("scrollVertical", distanceYN + "");
                    } else {
                        if (Math.abs(distanceXN) > Math.abs(distanceYN)) {
                            distanceX = 0;
                            distanceX += distanceXN;
                            distanceY = 0;
                            float r = distanceXN / width;
                            settings.horizontalScroll(r);
                            mainScreenLayout.horizontalScroll(r);
                            maxX = distanceX;
                            state = HORIZONTAL_SCROLL;
                        } else {
                            distanceX = 0;
                            distanceY = 0;
                            distanceY += distanceYN;
                            state = VERTICAL_SCROLL;


                        }
                    }
                    break;
                case SCREEN_SETTINGS:
                    if (state == HORIZONTAL_SCROLL) {
                        this.distanceX += distanceXN;
                        if (distanceX > 0 & distanceX > maxX) {
                            maxX = distanceX;
                        } else if (distanceX < 0 && distanceX < maxX) {
                            maxX = distanceX;
                        }
                        settings.horizontalScroll(distanceXN / width);
                    } else if (state == VERTICAL_SCROLL) {
                        settings.scroll(distanceYN / width);
                    } else {
                        if (Math.abs(distanceXN) > Math.abs(distanceYN)) {
                            distanceX = 0;
                            distanceX += distanceXN;
                            distanceY = 0;
                            settings.horizontalScroll(distanceXN / width);
                            maxX = distanceX;
                            state = HORIZONTAL_SCROLL;
                        } else {
                            distanceX = 0;
                            distanceY = 0;
                            distanceY += distanceYN;
                            state = VERTICAL_SCROLL;
                            settings.scroll(distanceY / width);

                        }
                    }
                    break;
                case SCREEN_APPS:
                    if (state == VERTICAL_SCROLL) {
                        distanceY += distanceYN;
                        if (distanceY > 0 & distanceY > maxY) {
                            maxY = distanceY;
                        } else if (distanceY < 0 && distanceY < maxY) {
                            maxY = distanceY;
                        }
                        if (appScreenlayout.isScrollOnTop()) {
                            if (distanceYN < 0) {
                                stop=true;
                                appScreenlayout.drag(distanceYN / height);
                                mainScreenLayout.hideScroll(appScreenlayout.getTranslationPercent());
                            } else if(!stop){
                                appScreenlayout.scroll(distanceYN/height);
                            }
                        } else {
                            appScreenlayout.scroll(distanceYN / height);
                        }
                        Log.d("scrollVertical", distanceYN + "");
                    } else {
                        if (Math.abs(distanceXN) > Math.abs(distanceYN)) {
                            state = HORIZONTAL_SCROLL;
                        } else {
                            distanceX = 0;
                            distanceY = 0;
                            distanceY += distanceYN;
                            maxY = distanceY;
                            state = VERTICAL_SCROLL;


                        }
                    }
                    break;
            }

        }
        public void onFling(float distanceY){
            if(screenState==SCREEN_APPS&!stop&!antiFling){
                appScreenlayout.scroll(-distanceY/height*2);
            }
            antiFling=false;
        }

    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 10;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("touch","single");
            switch (screenState){
                case SCREEN_APPS:
                    appScreenlayout.onTouchEvent(e);
                    break;
                case SCREEN_MAIN:
                    mainScreenLayout.click(e);
                    break;
                case SCREEN_SETTINGS:
                    settings.onTouchEvent(e);
                    break;
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            switch (screenState){
                case SCREEN_MAIN:
                    mainScreenLayout.longClick(e);
                    break;
                case SCREEN_SETTINGS:
                    settings.longClick(e);
                    break;
                case SCREEN_APPS:
                    appScreenlayout.onLongClick(e);
                    break;
            }
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("gesture", "Scroll");
            cancelDetector.onScroll(distanceX, distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            Log.d("gesture", "fling");
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                cancelDetector.onFling(diffY);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}
