package com.example.launcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Author:mihon
 * Time: 2019\2\26 0026.16:26
 * Description:This is MyMapDraw
 */
public class MyMapDraw extends RelativeLayout {
    //创建画笔对象和路径
    private Paint p = new Paint();
    private Path path = new Path();
    private float startX, startY;
    private Random random = new Random();

    public MyMapDraw(Context context) {
        super(context);
        initView();
    }

    public void setTranslationDraw(float translationX, float translationY) {
        startX = translationX;
        startY = translationY;
    }

    public MyMapDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyMapDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //画笔初始化，设置画笔的颜色
        p.setColor(getResources().getColor(R.color.map_line));
//        p.setColor(0xff10c694);
        //初始化画笔的大小
        p.setTextSize(10);
        p.setStrokeWidth(16);
        //给画笔清理锯齿
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
    }

    public void setStrokeWidth(int width) {
        p.setStrokeWidth(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //用drawPath进行绘制
        canvas.drawPath(path, p);
        Log.i("2222222", "onDraw");
        //绘制结束后要解锁画布
    }

    float startMoveX, startMoveY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("2222222", "event:" + event);
        switch (event.getAction()) {
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                startMoveX = event.getX();
                startMoveY = event.getY();
                //按下的时候通过moveTo()绘制按下的这个点,获取按下点的X和Y坐标
//                path.moveTo(event.getX(), event.getY());
                path.moveTo(startX, startY);
                //获取之后调用draw()方法进行绘制
//                    draw();
                break;
            //在移动的时候进行绘制
            case MotionEvent.ACTION_MOVE:
                float distanceY = event.getY() - startMoveY;
                float distanceX = event.getX() - startMoveX;
//                distanceY*=1.5f;
//                distanceX*=1.5f;
//                path.lineTo(event.getX(), event.getY());
                path.lineTo(startX + distanceX, startY + distanceY);
//                    draw();
                break;
            case MotionEvent.ACTION_UP:
                float y = event.getY() - startMoveY;
                float x = event.getX() - startMoveX;
                startX += x;
                startY += y;
                break;
        }
        invalidate();
        return true;
    }

}