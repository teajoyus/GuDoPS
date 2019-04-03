package com.example.launcher;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Random;

public class IsportActivity extends AppCompatActivity {
    TextView gongli_tv, avg_tv, time_tv, daka_tv, date;
    ImageView anchor_iv;
    RelativeLayout rl_control;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_isport2);
        rl_control = findViewById(R.id.rl_control);
        anchor_iv = findViewById(R.id.anchor_iv);
        gongli_tv = findViewById(R.id.gongli_tv);
        avg_tv = findViewById(R.id.avg);
        time_tv = findViewById(R.id.time);
        daka_tv = findViewById(R.id.daka);
        date = findViewById(R.id.date);
//        avg_tv.setText("26'18\"");
        initUseFont();
        handleData();
        initControlView();
    }

    private void initControlView() {
//        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void handleData() {
        String gongli = getIntent().getStringExtra("gongli");
        if (!TextUtils.isEmpty(gongli)) {
            gongli_tv.setText(gongli);
            boolean isWalk = getIntent().getBooleanExtra("isWalk", true);
            if (isWalk) {
                calcWalkTime(gongli);
            } else {
                calcRunTime(gongli);
            }
        }
        String timeString = getIntent().getStringExtra("time");
        if (!TextUtils.isEmpty(timeString)) {
            date.setText(timeString);
        }
        int lineWidth = getIntent().getIntExtra("lineWidth", 16);
        MyMapDraw draw = findViewById(R.id.rootView);
        draw.setStrokeWidth(lineWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && MakeActivity.sportBitmap != null) {
            findViewById(R.id.rootView).setBackground(new BitmapDrawable(getResources(), MakeActivity.sportBitmap));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gongli_tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                shotScreen();
            }
        }, 1000);


    }

    private void initUseFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BrandonGrotesqueForCodoon-BlackItalic.otf");
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/PFDinDisplayProBlackItalic.ttf");
        gongli_tv.setTypeface(typeface);
        gongli_tv.getPaint().setAntiAlias(true);//抗锯齿
        daka_tv.setTypeface(typeface);
        daka_tv.getPaint().setAntiAlias(true);//抗锯齿
        time_tv.setTypeface(typeface);
        time_tv.getPaint().setAntiAlias(true);//抗锯齿
        avg_tv.setTypeface(typeface);
        avg_tv.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void shotScreen() {

    }


    /**
     * 徒步的计算方式
     *
     * @param gongli
     */
    private void calcWalkTime(String gongli) {
        float f = Float.parseFloat(gongli);
        String time = "";
        float alltime = 0f;
        Random random = new Random();
        if (f >= 7f) {
            int minute = random.nextInt(20) + 10;
            int sec = random.nextInt(50) + 10;
            time = "01:" + minute + ":" + sec;
            //总时间
            alltime = 60 + minute + sec / 100.0f;
        } else {
            int minute = random.nextInt(20) + 40;
            int sec = random.nextInt(50) + 10;
            time = "00" + ":" + minute + ":" + sec;
            //总时间
            alltime = 60 + minute + sec / 100.0f;
        }
        time_tv.setText(time);
        float avg = alltime / f;
        DecimalFormat df = new DecimalFormat("#.00");
        String avgStr = df.format(avg).replace(".", "'") + "\"";
        avg_tv.setText(avgStr);
//已知体重、距离 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        float daka = 55 * f * 1.036f;
        df = new DecimalFormat("#.0");
        daka_tv.setText(df.format(daka));
    }

    /**
     * 跑步的计算方式
     *
     * @param gongli
     */
    private void calcRunTime(String gongli) {
        float f = Float.parseFloat(gongli);
        String time = "";
        float alltime = 0f;
        Random random = new Random();
        if (f >= 7f) {
            int hour = random.nextBoolean() ? 1 : 0;
            int minute = random.nextInt(15) + 1;
            if (hour == 0) {
                minute = random.nextInt(10) + 50;
            }
            int sec = random.nextInt(50) + 10;
            time = "0" + hour + ":" + minute + ":" + sec;
            //总时间
            alltime = hour * 60 + minute + sec / 100.0f;
        } else {
            int minute = random.nextInt(20) + 30;
            int sec = random.nextInt(50) + 10;
            time = "00" + ":" + minute + ":" + sec;
            //总时间
            alltime = 60 + minute + sec / 100.0f;
        }
        time_tv.setText(time);
        float avg = alltime / f;
        DecimalFormat df = new DecimalFormat("#.00");
        String avgStr = df.format(avg).replace(".", "'") + "\"";
        avg_tv.setText(avgStr);
//已知体重、距离 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        float daka = 55 * f * 1.036f;
        df = new DecimalFormat("#.0");
        daka_tv.setText(df.format(daka));
    }

    public void onClickAnchor(View view) {
        isAnchoring = true;
        anchor_iv.setVisibility(View.VISIBLE);
    }

    public void oncClickBack(View view) {
//        Toast.makeText(this,"oncClickBack",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onClickDraw(View view) {


        isAnchoring = false;
        float y = anchor_iv.getTranslationY();
        float x = anchor_iv.getTranslationX();
        int height = anchor_iv.getMeasuredHeight();
        int width = anchor_iv.getMeasuredWidth();
        float startX = x + width/2;
        float startY = y + height/2;
        anchor_iv.setVisibility(View.INVISIBLE);
        rl_control.setVisibility(View.INVISIBLE);
        MyMapDraw draw = findViewById(R.id.rootView);
        draw.setTranslationDraw(startX,startY);
    }

    @Override
    public void onBackPressed() {
        if (rl_control.getVisibility() == View.GONE) {
            rl_control.setVisibility(View.VISIBLE);
            return;
        } else {
            rl_control.setVisibility(View.GONE);
            return;
        }
//        super.onBackPressed();
    }

    boolean isAnchoring = false;
    float startMoveX,startMoveY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        MyMapDraw draw = findViewById(R.id.rootView);
        if (rl_control.getVisibility() == View.VISIBLE) {
            if(rl_control.dispatchTouchEvent(ev)){
                return true;
            }
            if (isAnchoring) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startMoveX = ev.getX()-anchor_iv.getTranslationX();
                        startMoveY = ev.getY()-anchor_iv.getTranslationY();
//                        startMoveX = ev.getX();
//                        startMoveY = ev.getY();
                        Log.i("dispatchTouchEvent","startMoveX:"+startMoveX);
                        Log.i("dispatchTouchEvent","startMoveY:"+startMoveY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float distanceY = ev.getY() - startMoveY;
                        float distanceX = ev.getX() - startMoveX;
//                        distanceY*=1.5f;
//                        distanceX*=1.5f;
                        Log.i("dispatchTouchEvent","distanceY:"+distanceY);
                        Log.i("dispatchTouchEvent","distanceX:"+distanceX);
//                        Log.i("dispatchTouchEvent","ev.getY():"+ev.getY());
//                        Log.i("dispatchTouchEvent","ev.getRawY():"+ev.getRawY());
                        anchor_iv.setTranslationX(distanceX);
                        anchor_iv.setTranslationY(distanceY);
                        break;
                    case MotionEvent.ACTION_UP:
//                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) anchor_iv.getLayoutParams();
//                        params.topMargin = (int) (anchor_iv.getTranslationY()+0.5f);
//                        params.leftMargin = (int) (anchor_iv.getTranslationX()+0.5f);
//                        anchor_iv.requestLayout();

                        break;
                }
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
